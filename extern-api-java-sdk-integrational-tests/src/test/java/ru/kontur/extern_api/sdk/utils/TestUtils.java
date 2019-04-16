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

package ru.kontur.extern_api.sdk.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.w3c.dom.Document;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.argosgrp.cryptoservice.utils.XMLUtil;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.service.SDKException;

public class TestUtils {

    private final static String ORG_NAME = "Ромашка";
    private final static String DEFAULT_KND = "1160001";//"1165013";

    public static TestData[] getTestData(String x509b64) {
        TestData[] data = Resources.loadFromJson("/client-infos.json", TestData[].class);
        for (TestData td : data) {
            td.getClientInfo().getSender().setCertificate(x509b64);
        }
        return data;
    }

    public static CompletableFuture<List<DemandTestData>> getTestDemand(TestSuite testSuite, String x509b64) {
        TestData[] data = Resources.loadFromJson("/client-infos.json", TestData[].class);
        return testSuite.GetEasyDocflowApi().thenApply(
                api -> {
                    ArrayList<CompletableFuture<DemandTestData>> list = new ArrayList<>();
                    for (TestData td : data) {
                        td.getClientInfo().getSender().setCertificate(x509b64);
                        DemandRequestDto requestDto = new DemandRequestDto(
                                testSuite.engine.getAccountProvider().accountId(), td.getClientInfo(), ORG_NAME,
                                new String[]{DEFAULT_KND});
                        list.add(api.getDemand(requestDto).thenApply(
                                response -> new DemandTestData(td, UUID.fromString(response.getDocflowId()))));
                    }
                    return list.stream().map(CompletableFuture::join).collect(Collectors.toList());
                });
    }

    public static DraftMetaRequest toDraftMetaRequest(TestData td) {
        String senderIp = td.getClientInfo().getSender().getIpAddress();
        DraftMetaRequest dm = new DraftMetaRequest();
        ClientInfo clientInfo = Objects.requireNonNull(td.getClientInfo());

        ClientInfo.Organization org = clientInfo.getOrganization();
        dm.setPayer(new OrganizationRequest(org.getInn(), org.getKpp(), ORG_NAME));

        ClientInfo.Recipient recipient = clientInfo.getRecipient();

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getTogsCode)
                .filter(s -> !s.isEmpty())
                .map(TogsRecipient::new)
                .ifPresent(dm::setRecipient);

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getIfnsCode)
                .filter(s -> !s.isEmpty())
                .map(FnsRecipient::new)
                .ifPresent(dm::setRecipient);

        ClientInfo.Sender sender = clientInfo.getSender();
        dm.setSender(new SenderRequest(
                sender.getInn(),
                sender.getKpp(),
                sender.getCertificate(),
                senderIp
        ));
        return dm;
    }

    public static DocumentContents loadDocumentContents(String path, DocType docType) {
        Document dom;
        try (InputStream is = TestUtils.class.getResourceAsStream(path)) {
            dom = XMLUtil.deserialize(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        DocumentDescription documentDescription = new DocumentDescription();

        if (docType == DocType.FNS) {
            String uuid = UUID.randomUUID().toString();

            String fileId = dom.getDocumentElement().getAttribute("ИдФайл");
            String newFileId = fileId.substring(0, fileId.lastIndexOf("_") + 1) + uuid;

            dom.getDocumentElement().setAttribute("ИдФайл", newFileId);

            documentDescription.setContentType("application/xml");
            documentDescription.setType(null);
            documentDescription.setFilename(fileId + ".xml");
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        UncheckedRunnable.run(() -> XMLUtil.serialize(dom, os, "Windows-1251"));

        DocumentContents documentContents = new DocumentContents();
        documentContents.setBase64Content(Base64.getEncoder().encodeToString(os.toByteArray()));
        documentContents.setDescription(documentDescription);

        return documentContents;

    }

    public static byte[] loadDocument(String path) {
        try (InputStream is = Objects.requireNonNull(TestUtils.class.getResourceAsStream(path))) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtil.copyStream(is, os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getUpdatedDocumentPath(String path) throws SDKException {
        File file = new File(path);
        String name = IOUtil.getFileNameWithoutExt(file.getName());

        return Resources.getPath(file.getParentFile().getPath(), name, "1.xml");
    }

    public static byte[] loadUpdateDocument(String path) {
        return loadDocument(getUpdatedDocumentPath(path));
    }


}
