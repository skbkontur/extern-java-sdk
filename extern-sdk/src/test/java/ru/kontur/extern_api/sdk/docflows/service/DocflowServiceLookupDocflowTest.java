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
package ru.kontur.extern_api.sdk.docflows.service;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.fail;
import static org.junit.Assert.assertNull;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.common.ResponseData;
import ru.kontur.extern_api.sdk.common.StandardObjects;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.common.TestServlet;
import ru.kontur.extern_api.sdk.docflows.DocflowsValidator;
import ru.kontur.extern_api.sdk.drafts.service.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */
public class DocflowServiceLookupDocflowTest {

    private static ExternEngine engine;
    private static Server server;

    private final static String DOCFLOW = "\"id\": \"" + StandardValues.ID + "\","
        + "\"type\": \"urn:docflow:stat-letter\","
        + "\"status\": \"urn:docflow-common-status:arrived\","
        + "\"send-date\": \"" + StandardValues.DATE + "\","
        + "\"last-change-date\": \"" + StandardValues.DATE + "\"";

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/docflows/*");
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
        engine = ExternEngineBuilder
                .createExternEngine()
                .apiKey(UUID.randomUUID().toString()).authProvider(new AuthenticationProviderAdaptor())
                .doNotUseCryptoProvider()
                .accountId(UUID.randomUUID().toString())
                .serviceBaseUrl("http://localhost:8080/docflows")
                .build();
    }

    @Test
    public void testLookupDocflow_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        assertNotNull("Docflow must not be null!", getDocflow());
        assertNotNull("Docflow must not be null!", getDocflowAsync());
    }

    @Test
    public void testLookupDocflow_Docflow() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s}", DOCFLOW));
        DocflowsValidator.validateDocflow(getDocflow(), false, false, false);
        DocflowsValidator.validateDocflow(getDocflowAsync(), false, false, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Description() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE
            .setResponseMessage(String.format("{%s,\"description\": {}}", DOCFLOW));
        DocflowsValidator.validateDocflow(getDocflow(), true, false, false);
        DocflowsValidator.validateDocflow(getDocflowAsync(), true, false, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Documents() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s,"
            + "\"description\": {},"
            + "\"documents\": [{"
            + "   \"id\": \"%s\""
            + "}]"
            + "}", DOCFLOW, StandardValues.ID));
        DocflowsValidator.validateDocflow(getDocflow(), true, true, false);
        DocflowsValidator.validateDocflow(getDocflowAsync(), true, true, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Links() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + DOCFLOW + ","
            + "  \"description\": {},"
            + "  \"documents\": [{\"id\": \"" + StandardValues.ID + "\"}],"
            + "  \"links\": [" + StandardObjects.LINK + "]"
            + "}");
        DocflowsValidator.validateDocflow(getDocflow(), true, true, true);
        DocflowsValidator.validateDocflow(getDocflowAsync(), true, true, true);
    }

    @Test
    public void testLookupDocflow_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testLookupDocflow_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testLookupDocflow_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testLookupDocflow_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testLookupDocflow_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<Object> queryContext = new QueryContext<>();
        queryContext.setDocflowId(StandardValues.ID);
        QueryContext<Docflow> docflowQueryContext = engine.getDocflowService()
            .lookupDocflow(queryContext);
        assertNull("docflow must be null!", docflowQueryContext.get());
        assertNotNull("ServiceError must not be null!", docflowQueryContext.getServiceError());
        assertEquals("Response code is wrong!", code,
            docflowQueryContext.getServiceError().getResponseCode());
    }

    private Docflow getDocflow() {
        QueryContext<Docflow> queryContext = new QueryContext<>();
        queryContext.setDocflowId(StandardValues.ID);
        return engine.getDocflowService().lookupDocflow(queryContext).get();
    }

    private Docflow getDocflowAsync() {
        try {
            return engine.getDocflowService().lookupDocflowAsync(StandardValues.ID).get().get();
        }
        catch (InterruptedException | ExecutionException e) {
            fail();
            return null;
        }
    }
}
