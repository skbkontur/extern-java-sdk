/*
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk;

import static ru.kontur.extern_api.sdk.utils.YAStringUtils.isNullOrEmpty;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.DocType;
import ru.kontur.extern_api.sdk.utils.SystemProperty;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@Disabled("Cert problems")
class DraftWithCloudCertIT {

    private static Certificate cloudCert;
    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {

        engine = TestSuite.Load().engine;

        SystemProperty.push("httpclient.debug");

        List<Certificate> certs = engine
                .getCertificateService()
                .getCertificates(0, 100)
                .join()
                .getOrThrow()
                .getCertificates().stream()
                .filter(Certificate::getIsCloud)
                .filter(Certificate::getIsQualified)
                .filter(Certificate::getIsValid)
                .collect(Collectors.toList());

        Assertions.assertNotEquals(0, certs.size());

        cloudCert = certs.get(0);
    }

    private UUID draftId;

    @BeforeEach
    void setUp() throws IOException {
        draftId = createDraftWithCert(cloudCert);
    }

    @Test
    @DisplayName("cloud sign draft")
    void testCloudSign() {
        ApproveCodeProvider backdoor = new ApproveCodeProvider(engine);
        engine.getDraftService()
                .cloudSignAsync(draftId, cxt -> backdoor.apply(cxt.getRequestId()))
                .join()
                .getOrThrow();

        PrepareResult prepareResult = engine.getDraftService()
                .prepareAsync(draftId.toString())
                .join()
                .getOrThrow();

        Status status = prepareResult.getStatus();
        Assertions.assertTrue(status == Status.OK
                || status == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);
    }

    @Test
    @DisplayName("decrypt docflow document in cloud")
    void testDecryptContent(){
        ApproveCodeProvider backdoor = new ApproveCodeProvider(engine);
        engine.getDraftService()
                .cloudSignAsync(draftId, cxt -> backdoor.apply(cxt.getRequestId()))
                .join()
                .getOrThrow();

        Docflow docflow = engine.getDraftService()
                .sendAsync(draftId.toString())
                .join()
                .getOrThrow();

        for (Document d : docflow.getDocuments()) {
            if (!d.hasEncryptedContent()) {
                continue;
            }

            byte[] decrypted = engine.getDocflowService().cloudDecryptDocument(
                    docflow.getId().toString(),
                    d.getId().toString(),
                    cloudCert.getContent(),
                    init -> backdoor.apply(init.getRequestId())
            )
                    .getOrThrow();

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
        }
    }


    private static UUID createDraftWithCert(Certificate certificate) throws IOException {
        TestData[] data = TestUtils.getTestData(certificate.getContent());

        TestData testData = Arrays.stream(data)
                .filter(e -> !isNullOrEmpty(e.getClientInfo().getRecipient().getIfnsCode()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("data"));

        DraftMetaRequest dm = TestUtils.toDraftMetaRequest(testData);
        dm.getSender().setKpp(certificate.getKpp());
        dm.getSender().setInn(certificate.getInn());

        UUID draftId = engine
                .getDraftService()
                .createAsync(dm.getSender(), dm.getRecipient(), dm.getPayer())
                .join()
                .getOrThrow();

        String testDocPath = Arrays.stream(testData.getDocs())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("test doc required"));

        DocumentContents docs = TestUtils.loadDocumentContents(testDocPath, DocType.FNS);

        engine.getDraftService()
                .addDecryptedDocumentAsync(draftId, docs)
                .join().ensureSuccess();

        return draftId;
    }
}
