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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.test.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import ru.argosgrp.cryptoservice.utils.Base64;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.SDKException;
import ru.kontur.extern_api.sdk.test.model.TestData;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;


public class AbstractTest {

    private static final Base64.Decoder DECODER_BASE64 = Base64.getDecoder();
    private static final Base64.Encoder ENCODER_BASE64 = Base64.getEncoder();

    protected static ExternEngine engine;

    protected AccountService accountService;
    protected DraftService draftService;
    protected DocflowService docflowService;
    protected OrganizationService organizationService;

    protected static TestData[] getTestData(String x509b64) {
        TestData[] data = Resources.loadFromJson("/client-infos.json", TestData[].class);
        for (TestData td : data) {
            td.getClientInfo().getSender().setCertificate(x509b64);
        }
        return data;
    }

    protected static void initEngine() {
        engine = TestSuite.Load().engine;
    }

    protected String getUpdatedDocumentPath(String path) throws SDKException {
        File file = new File(path);
        String name = IOUtil.getFileNameWithoutExt(file.getName());

        return Resources.getPath(file.getParentFile().getPath(), name, "1.xml");
    }

    protected static String loadX509(ExternEngine engine) {
        String thumbprint = Objects.requireNonNull(engine.getConfiguration().getThumbprint());

        CompletableFuture<String> future = engine.getCryptoProvider()
                .getSignerCertificateAsync(thumbprint)
                .thenApply(QueryContext::ensureSuccess)
                .thenApply(QueryContext::getContent)
                .thenApply(ENCODER_BASE64::encodeToString);

        return UncheckedSupplier.get(future::get);
    }

    protected byte[] loadDocument(String path) {
        try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(path))) {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            IOUtil.copyStream(is, os);
            return os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected static byte[] sign(ExternEngine engine, byte[] content) {
        String thumbprint = engine.getConfiguration().getThumbprint();
        Objects.requireNonNull(thumbprint);
        Objects.requireNonNull(content);

        return engine
                .getCryptoProvider()
                .sign(new QueryContext<byte[]>()
                        .setThumbprint(thumbprint)
                        .setContent(content)
                )
                .ensureSuccess()
                .getContent();
    }

    protected static DocumentContents createDocumentContents(
            final ExternEngine engine,
            final String path,
            final DocType docType) {

        DocumentContents documentContents = TestUtils.loadDocumentContents(path, docType);
        byte[] signature = sign(engine, DECODER_BASE64.decode(documentContents.getBase64Content()));

        if (signature != null) {
            documentContents.setSignature(ENCODER_BASE64.encodeToString(signature));
        }

        return documentContents;
    }

    protected static QueryContext<DraftDocument> addDecryptedDocument(
            final ExternEngine engine,
            final QueryContext<?> current,
            final String path,
            DocType docType) {

        DocumentContents documentContents = createDocumentContents(engine, path, docType);
        return engine
                .getDraftService()
                .addDecryptedDocument(current.setDocumentContents(documentContents));
    }

    protected byte[] loadUpdateDocument(String path) {
        return this.loadDocument(getUpdatedDocumentPath(path));
    }

}
