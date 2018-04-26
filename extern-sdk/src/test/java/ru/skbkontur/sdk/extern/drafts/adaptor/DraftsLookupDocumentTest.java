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

package ru.skbkontur.sdk.extern.drafts.adaptor;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import java.util.UUID;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.docflows.DocflowsValidator;
import ru.skbkontur.sdk.extern.drafts.DraftsValidator;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

/**
 * @author Mikhail Pavlenko
 */

public class DraftsLookupDocumentTest {

    private static final String LOCALHOST_DRAFTS = "http://localhost:8080/drafts";
    private static Server server;

    private QueryContext<DraftDocument> queryContext;

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/drafts/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @Before
    public void prepareQueryContext() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(LOCALHOST_DRAFTS);
        queryContext = new QueryContext<>();
        queryContext.setApiClient(apiClient);
        queryContext.setAccountProvider(UUID::randomUUID);
        queryContext.setDraftId(UUID.randomUUID());
        queryContext.setDocumentId(UUID.randomUUID());
    }

    @AfterClass
    public static void stopJetty() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDraftsLookupDocument_Empty() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        new DraftsAdaptor().lookupDocument(queryContext);
        assertNotNull("DraftDocument must not be null!", queryContext.get());
    }

    @Test
    public void testDraftsLookupDocument_DraftDocument() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"id\": \"" + StandardValues.ID + "\"}");
        new DraftsAdaptor().lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, false, false);
    }

    @Test
    public void testDraftsLookupDocument_DecryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
            "\"id\": \"" + StandardValues.ID + "\"," +
            "\"decrypted-content-link\": {" +
            "  \"href\": \"string\"," +
            "  \"rel\": \"string\"," +
            "  \"name\": \"string\"," +
            "  \"title\": \"string\"," +
            "  \"profile\": \"string\"," +
            "  \"templated\": true" +
            "}}");
        new DraftsAdaptor().lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), true, false, false);
    }

    @Test
    public void testDraftsLookupDocument_EncryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
            "\"id\": \"" + StandardValues.ID + "\"," +
            "\"encrypted-content-link\": {" +
            "  \"href\": \"string\"," +
            "  \"rel\": \"string\"," +
            "  \"name\": \"string\"," +
            "  \"title\": \"string\"," +
            "  \"profile\": \"string\"," +
            "  \"templated\": true" +
            "}}");
        new DraftsAdaptor().lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, true, false);
    }

    @Test
    public void testDraftsLookupDocument_SignatureContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
            "\"id\": \"" + StandardValues.ID + "\"," +
            "\"signature-content-link\": {" +
            "  \"href\": \"string\"," +
            "  \"rel\": \"string\"," +
            "  \"name\": \"string\"," +
            "  \"title\": \"string\"," +
            "  \"profile\": \"string\"," +
            "  \"templated\": true" +
            "}}");
        new DraftsAdaptor().lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, false, true);
    }

    @Test
    public void testDraftsLookupDocument_DocumentDescription() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
            "\"id\": \"" + StandardValues.ID + "\"," +
            "\"description\": {" +
            "  \"type\": \"urn:nss:nid\"," +
            "  \"filename\": \"string\"," +
            "  \"content-type\": \"string\"" +
            "}}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DocflowsValidator
            .validateDocumentDescription(queryContext.getDraftDocument().getDocumentDescription());
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
        new DraftsAdaptor().lookupDocument(queryContext);
        assertNull("draftDocument must be null!", queryContext.get());
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}