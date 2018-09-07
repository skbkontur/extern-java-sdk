/*
 * MIT License
 *
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
 */
package ru.kontur.extern_api.sdk.drafts.service;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.common.ResponseData;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.common.TestServlet;
import ru.kontur.extern_api.sdk.drafts.DraftsValidator;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */
public class DraftServiceLookupDocumentTest {

    private static ExternEngine engine;
    private static Server server;

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/drafts/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @AfterClass
    public static void stopJetty() {
        try {
            server.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUpClass() {
        engine = ExternEngineBuilder.createExternEngine()
                .apiKey(UUID.randomUUID().toString())
                .authProvider(new AuthenticationProviderAdaptor())
                .doNotUseCryptoProvider()
                .accountId(UUID.randomUUID().toString())
                .serviceBaseUrl("http://localhost:8080/drafts")
                .build();
    }

    @Test
    public void testLookupDocument_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        assertNotNull("DraftDocument must not be null!", getDraftDocument());
        assertNotNull("DraftDocument must not be null!", getDraftDocumentAsync());
    }

    @Test
    public void testLookupDocument_DraftDocument() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"id\": \"" + StandardValues.ID + "\"}");
        DraftsValidator.validateDraftDocument(getDraftDocument(), false, false, false);
        DraftsValidator.validateDraftDocument(getDraftDocumentAsync(), false, false, false);
    }

    @Test
    public void testLookupDocument_DecryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + "\"id\": \"" + StandardValues.ID + "\","
            + "\"decrypted-content-link\": {"
            + "  \"href\": \"string\","
            + "  \"rel\": \"string\","
            + "  \"name\": \"string\","
            + "  \"title\": \"string\","
            + "  \"profile\": \"string\","
            + "  \"templated\": true"
            + "}}");
        DraftsValidator.validateDraftDocument(getDraftDocument(), true, false, false);
        DraftsValidator.validateDraftDocument(getDraftDocumentAsync(), true, false, false);
    }

    @Test
    public void testLookupDocument_EncryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + "\"id\": \"" + StandardValues.ID + "\","
            + "\"encrypted-content-link\": {"
            + "  \"href\": \"string\","
            + "  \"rel\": \"string\","
            + "  \"name\": \"string\","
            + "  \"title\": \"string\","
            + "  \"profile\": \"string\","
            + "  \"templated\": true"
            + "}}");
        DraftsValidator.validateDraftDocument(getDraftDocument(), false, true, false);
        DraftsValidator.validateDraftDocument(getDraftDocumentAsync(), false, true, false);
    }

    @Test
    public void testLookupDocument_SignatureContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + "\"id\": \"" + StandardValues.ID + "\","
            + "\"signature-content-link\": {"
            + "  \"href\": \"string\","
            + "  \"rel\": \"string\","
            + "  \"name\": \"string\","
            + "  \"title\": \"string\","
            + "  \"profile\": \"string\","
            + "  \"templated\": true"
            + "}}");
        DraftsValidator.validateDraftDocument(getDraftDocument(), false, false, true);
        DraftsValidator.validateDraftDocument(getDraftDocumentAsync(), false, false, true);
    }

    @Test
    public void testLookupDocument_DocumentDescription() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + "\"id\": \"" + StandardValues.ID + "\","
            + "\"description\": {"
            + "  \"type\": \"urn:nss:nid\","
            + "  \"filename\": \"string\","
            + "  \"content-type\": \"string\""
            + "}}");
        DraftsValidator.validateDocumentDescription(getDraftDocument().getDescription());
        DraftDocument draftDocumentAsync = getDraftDocumentAsync();
        if (draftDocumentAsync != null) {
            DraftsValidator
                .validateDocumentDescription(draftDocumentAsync.getDescription());
        }
    }

    @Test
    public void testDraftsLookupDocument_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testDraftsLookupDocument_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testDraftsLookupDocument_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testDraftsLookupDocument_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testDraftsLookupDocument_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<DraftDocument> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        queryContext.setDocumentId(StandardValues.ID);
        QueryContext<DraftDocument> draftDocumentQueryContext = engine.getDraftService()
            .lookupDocument(queryContext);
        assertNull("draftDocument must be null!", draftDocumentQueryContext.get());
        ServiceError serviceError = draftDocumentQueryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }

    private DraftDocument getDraftDocument() {
        QueryContext<DraftDocument> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        queryContext.setDocumentId(StandardValues.ID);
        return engine.getDraftService().lookupDocument(queryContext).get();
    }

    private DraftDocument getDraftDocumentAsync() {
        try {
            return engine.getDraftService()
                .lookupDocumentAsync(StandardValues.ID, StandardValues.ID)
                .get().get();
        }
        catch (InterruptedException | ExecutionException e) {
            fail();
            return null;
        }
    }
}
