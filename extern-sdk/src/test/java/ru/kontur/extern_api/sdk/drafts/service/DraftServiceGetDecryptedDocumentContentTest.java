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
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.common.ResponseData;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.common.TestServlet;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */
public class DraftServiceGetDecryptedDocumentContentTest {

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
        engine = new ExternEngine();
        engine.setServiceBaseUriProvider(() -> "http://localhost:8080/drafts");
        engine.setAccountProvider(UUID::randomUUID);
        engine.setApiKeyProvider(() -> UUID.randomUUID().toString());
        engine.setAuthenticationProvider(new AuthenticationProviderAdaptor());

    }

    @Test
    public void testGetDecryptedDocumentContent_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        assertEquals("Response string is wrong!", "{}", getString());
        assertEquals("Response string is wrong!", "{}", getStringAsync());
    }

    @Test
    public void testGetDecryptedDocumentContent() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"id\": \"" + StandardValues.ID + "\"}");
        assertEquals("Response string is wrong!", "{\"id\": \"" + StandardValues.ID + "\"}",
            getString());
        assertEquals("Response string is wrong!", "{\"id\": \"" + StandardValues.ID + "\"}",
            getStringAsync());
    }

    @Test
    public void testGetDecryptedDocumentContent_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testGetDecryptedDocumentContent_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testGetDecryptedDocumentContent_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testGetDecryptedDocumentContent_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testGetDecryptedDocumentContent_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<String> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        queryContext.setDocumentId(StandardValues.ID);
        QueryContext<String> stringQueryContext = engine.getDraftService()
            .getDecryptedDocumentContent(queryContext);
        assertNull(stringQueryContext.get());
        ServiceError serviceError = stringQueryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }

    private String getString() {
        QueryContext<String> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        queryContext.setDocumentId(StandardValues.ID);
        return engine.getDraftService().getDecryptedDocumentContent(queryContext).get();
    }

    private String getStringAsync() {
        try {
            return engine.getDraftService()
                .getDecryptedDocumentContentAsync(StandardValues.ID, StandardValues.ID).get().get();
        }
        catch (InterruptedException | ExecutionException e) {
            fail();
            return null;
        }
    }
}
