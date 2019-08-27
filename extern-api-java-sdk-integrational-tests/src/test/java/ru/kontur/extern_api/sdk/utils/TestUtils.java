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
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.argosgrp.cryptoservice.utils.XMLUtil;
import ru.kontur.extern_api.sdk.model.ClientInfo;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.FssRecipient;
import ru.kontur.extern_api.sdk.model.OrganizationRequest;
import ru.kontur.extern_api.sdk.model.PfrRecipient;
import ru.kontur.extern_api.sdk.model.SenderRequest;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.model.TogsRecipient;
import ru.kontur.extern_api.sdk.service.SDKException;

public class TestUtils {

    private final static String ORG_NAME = "Ромашка";
    private static final String WINDOWS_1251 = "windows-1251";

    public static TestData[] getTestData(String x509b64) {
        TestData[] data = Resources.loadFromJson("/client-infos.json", TestData[].class);
        for (TestData td : data) {
            td.getClientInfo().getSender().setCertificate(x509b64);
        }
        return data;
    }

    public static DraftMetaRequest toDraftMetaRequest(TestData td) {
        String senderIp = td.getClientInfo().getSender().getIpAddress();
        DraftMetaRequest draftMetaRequest = new DraftMetaRequest();
        ClientInfo clientInfo = Objects.requireNonNull(td.getClientInfo());

        ClientInfo.Organization org = clientInfo.getOrganization();
        OrganizationRequest payer = new OrganizationRequest(org.getInn(), org.getKpp(), ORG_NAME);
        payer.setRegistrationNumberPfr(org.getRegistrationNumberPfr());
        draftMetaRequest.setPayer(payer);

        ClientInfo.Recipient recipient = clientInfo.getRecipient();

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getTogsCode)
                .filter(s -> !s.isEmpty())
                .map(TogsRecipient::new)
                .ifPresent(draftMetaRequest::setRecipient);

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getIfnsCode)
                .filter(s -> !s.isEmpty())
                .map(FnsRecipient::new)
                .ifPresent(draftMetaRequest::setRecipient);

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getFssCode)
                .filter(s -> !s.isEmpty())
                .map(FssRecipient::new)
                .ifPresent(draftMetaRequest::setRecipient);

        Optional.of(recipient)
                .map(ClientInfo.Recipient::getUpfrCode)
                .filter(s -> !s.isEmpty())
                .map(PfrRecipient::new)
                .ifPresent(draftMetaRequest::setRecipient);

        ClientInfo.Sender sender = clientInfo.getSender();
        draftMetaRequest.setSender(new SenderRequest(
                sender.getInn(),
                sender.getKpp(),
                sender.getCertificate(),
                senderIp
        ));
        return draftMetaRequest;
    }

    public static DocumentContents loadDocumentContents(String path, DocType docType){
        if (docType == DocType.PFR && path.contains("SomePfrAttachment.txt")){
            return getPfrAttachmentContents(path);
        }

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
            documentDescription.setFilename(newFileId + ".xml");
        }
        if (docType == DocType.PFR && path.contains("SomePfrReportDescription.xml")) {
            documentDescription.setContentType("application/xml");

            String currentDateTimeStr = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            dom.getDocumentElement().getFirstChild().getNextSibling().getChildNodes().item(0).setNodeValue(currentDateTimeStr);
        }

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        UncheckedRunnable.run(() -> XMLUtil.serialize(dom, os, "Windows-1251"));

        DocumentContents documentContents = new DocumentContents();
        documentContents.setBase64Content(Base64.getEncoder().encodeToString(os.toByteArray()));
        documentContents.setDescription(documentDescription);

        return documentContents;

    }

    @NotNull
    private static DocumentContents getPfrAttachmentContents(String path) {
        DocumentContents attachmentContents = new DocumentContents();
        DocumentDescription description = new DocumentDescription();
        description.setFilename("SomePfrAttachment.txt");
        attachmentContents.setDescription(description);
        InputStream resourceAsStream = TestUtils.class.getResourceAsStream(path);
        byte[] attachment = new byte[0];
        try {
            attachment = IOUtils.toByteArray(resourceAsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        attachmentContents.setBase64Content(Base64.getEncoder().encodeToString(attachment));
        return attachmentContents;
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

    public static String fromWin1251Bytes(byte[] content) {
        try {
            return new String(content, WINDOWS_1251);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Win-1251 no supported");
        }
    }

    public static String toBase64(byte[] docContent) {
        return new String(Base64.getEncoder().encode(docContent));
    }
}
