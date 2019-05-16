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

import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContract;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.OrganizationRequest;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.model.ion.IonRequestContractV1;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.Resources;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestUtils;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("Draft service should")
@Execution(ExecutionMode.CONCURRENT)
class DocumentBuildIT {

    private static DraftService draftService;
    private static DraftMetaRequest draftMeta;
    private static Configuration config;
    private static ExternEngine ee;
    private static Certificate workCert;
    private Map<BuildDocumentType, String> IonTypeToTestFolder = new HashMap<BuildDocumentType, String>() {
        {
            put(BuildDocumentType.ION1, "/docs/ion1/");
            put(BuildDocumentType.ION2, "/docs/ion2/");
            put(BuildDocumentType.ION3, "/docs/ion3/");
            put(BuildDocumentType.ION4, "/docs/ion4/");
            put(BuildDocumentType.ION5, "/docs/ion5/");
        }
    };

    private static IonRequestContract loadIon(String path, BuildDocumentType type) {
        switch (type) {
            case ION1:
                return Resources.loadFromJson(path, Ion1RequestContract.class);
            case ION2:
                return Resources.loadFromJson(path, Ion2RequestContract.class);
            case ION3:
                return Resources.loadFromJson(path, Ion3RequestContract.class);
            case ION4:
                return Resources.loadFromJson(path, Ion4RequestContract.class);
            case ION5:
                return Resources.loadFromJson(path, Ion5RequestContract.class);
            default:
                return Resources.loadFromJson(path, IonRequestContract.class);
        }
    }

    private static UsnServiceContractInfo loadUsn(String path) {
        return Resources.loadFromJson(path, UsnServiceContractInfo.class);
    }

    @BeforeAll
    static void init() throws Exception {
        config = TestConfig.LoadConfigFromEnvironment();
        ee = ExternEngineBuilder
                .createExternEngine(config.getServiceBaseUri())
                .apiKey(config.getApiKey())
                .buildAuthentication(config.getAuthBaseUri(), builder -> builder.
                        passwordAuthentication(config.getLogin(), config.getPass())
                )
                .cryptoProvider(new CryptoProviderMSCapi())
                .accountId(config.getAccountId())
                .build(Level.BODY);

        draftService = ee.getDraftService();
        String cert = ee.getCryptoProvider()
                .getSignerCertificateAsync(config.getThumbprint())
                .thenApply(QueryContext::getOrThrow)
                .thenApply(Base64.getEncoder()::encodeToString)
                .join();

        draftMeta = Arrays
                .stream(TestUtils.getTestData(cert))
                .filter(td -> td.getClientInfo().getRecipient().getIfnsCode() != null)
                .findAny()
                .map(TestUtils::toDraftMetaRequest)
                .orElseThrow(() -> new RuntimeException("no suitable data for usn tests"));

        workCert = ee
                .getCertificateService()
                .getCertificates(0, 100)
                .join()
                .getOrThrow()
                .getCertificates()
                .stream()
                .filter(certificate -> Objects.equals(certificate.getContent(), cert))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Cannot find certificate"));
    }

    private static byte[] sign(byte[] bytes) {
        return CryptoUtils.with(ee.getCryptoProvider()).sign(config.getThumbprint(), bytes);
    }

    @TestFactory
    @DisplayName("allow to create a valid usn")
    Stream<DynamicTest> createUsnTests() {
        return Stream.of(
                dynamicTest("V1 from file", () -> checkUsn(1, loadUsn(
                        "/docs/USN/usnV1ForSelf.json"))),
                dynamicTest("V1 from dto NOT_SUPPORTED", System.out::println),
                dynamicTest("V2 from file", () -> checkUsn(2, loadUsn(
                        "/docs/USN/usnV2ForSelf.json"))),
                dynamicTest("V2 from dto",
                        () -> checkUsn(
                                2,
                                PreparedTestData.usnV2(workCert, new OrganizationRequest("111", "111", "111"))
                        )
                )
        );
    }

    @TestFactory
    @DisplayName("allow to create a valid ion")
    Stream<DynamicTest> createIonTests() {

        OrganizationRequest org = new OrganizationRequest("111", "111", "111");

        return Stream.of(
                dynamicTest("Good Ion1 from dto", () -> checkIon(BuildDocumentType.ION1, PreparedTestData.buildIon(BuildDocumentType.ION1, workCert, org), true)),
                dynamicTest("Good Ion2 from dto", () -> checkIon(BuildDocumentType.ION2, PreparedTestData.buildIon(BuildDocumentType.ION2, workCert, org), true)),
                dynamicTest("Good Ion3 from dto", () -> checkIon(BuildDocumentType.ION3, PreparedTestData.buildIon(BuildDocumentType.ION3, workCert, org), true)),
                dynamicTest("Good Ion4 from dto", () -> checkIon(BuildDocumentType.ION4, PreparedTestData.buildIon(BuildDocumentType.ION4, workCert, org), true)),
                dynamicTest("Good Ion5 from dto", () -> checkIon(BuildDocumentType.ION5, PreparedTestData.buildIon(BuildDocumentType.ION5, workCert, org), true))
        );
    }

    @TestFactory
    @DisplayName("allow to create a ion from file")
    Stream<DynamicTest> createIonFromFileTests() throws IOException {

        ArrayList<DynamicTest> result = new ArrayList<DynamicTest>();

        for (Map.Entry<BuildDocumentType, String> entry : IonTypeToTestFolder.entrySet()) {
            BuildDocumentType type = entry.getKey();
            String prefix = entry.getValue();

            result.addAll(
                    Resources.walk(prefix)
                            .map(file -> dynamicTest(file, () -> checkIon(type, loadIon(prefix + file, type), file.startsWith("+"))))
                            .collect(Collectors.toList()));
        }

        return result.stream();
    }

    private void checkIon(BuildDocumentType ionType, IonRequestContract ion, boolean isPositive) {
        UUID draftId = draftService
                .createAsync(draftMeta)
                .join()
                .getOrThrow()
                .getId();

        draftService.newIonRequestAsync(draftId, ionType, ion)
                .thenApply(QueryContext::getOrThrow)
                .join();

        CheckResultData result = draftService.checkAsync(draftId).join().getOrThrow();

        if (isPositive) {
            assertTrue(result.hasNoErrors());
        } else {
            assertFalse(result.hasNoErrors());
        }
    }

    private void checkUsn(int version, UsnServiceContractInfo usn) {

        UUID draftId = draftService
                .createAsync(draftMeta)
                .join()
                .getOrThrow()
                .getId();

        draftService.createAndBuildDeclarationAsync(draftId, version, usn)
                .thenApply(QueryContext::getOrThrow)
                .join();

        CheckResultData result = draftService.checkAsync(draftId).join().getOrThrow();
        assertTrue(result.hasNoErrors());
    }

    @TestFactory
    @DisplayName("to send created")
    Stream<DynamicTest> send() {
        return Stream.of(
                dynamicTest("usn", this::sendCheckedUsn),
                dynamicTest("ion", this::sendCheckedIon)
        );
    }

    private void sendCheckedUsn() {
        UsnServiceContractInfo usn = loadUsn("/docs/USN/usnV2ForSelf.json");
        sendDraftTest(draftId -> draftService
                .createAndBuildDeclarationAsync(draftId, 2, usn)
                .thenApply(QueryContext::getOrThrow)
                .join()
                .getId()
        );
    }

    private void sendCheckedIon() {
        sendDraftTest(draftId -> draftService
                .newIonRequestAsync(draftId, BuildDocumentType.ION1, loadIon("/docs/ion.json", BuildDocumentType.ION1))
                .thenApply(QueryContext::getOrThrow)
                .join()
                .getId()
        );
    }

    private void sendDraftTest(Function<UUID, UUID> fillDraft) {
        UUID draftId = draftService.createAsync(draftMeta).join().getOrThrow().getId();

        UUID documentId = fillDraft.apply(draftId);

        byte[] doc = draftService.getDecryptedDocumentContentAsync(draftId, documentId)
                .join().getOrThrow();

        draftService.updateSignatureAsync(draftId, documentId, sign(doc))
                .join().getOrThrow();

        Docflow docflow = draftService.sendAsync(draftId).join().getOrThrow();
        Assertions.assertEquals(DocflowStatus.SENT, docflow.getStatus());
    }

}
