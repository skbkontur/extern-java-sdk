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
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.fail;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.common.ResponseData;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.common.TestServlet;
import ru.kontur.extern_api.sdk.docflows.DocflowsValidator;
import ru.kontur.extern_api.sdk.drafts.service.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */
public class DocflowServiceGetDocflowsTest {

    private static ExternEngine engine;
    private static Server server;

    private final static String DOCFLOW_PAGE = "\"skip\": 0," +
            "\"take\": 0," +
            "\"total-count\": 0";

    private final static String DOCFLOW_PAGE_ITEM =
            "\"id\": \"00000000-0000-0000-0000-000000000000\"," +
                    "\"type\": \"urn:docflow:fns534-report\"," +
                    "\"status\": \"urn:docflow-common-status:sent\"," +
                    "\"send-date\": \"" + StandardValues.DATE + "\"," +
                    "\"last-change-date\": \"" + StandardValues.DATE + "\"";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUpClass() {
        engine = ExternEngineBuilder.createExternEngine()
                .apiKey(UUID.randomUUID().toString()).authProvider(new AuthenticationProviderAdaptor())
                .doNotUseCryptoProvider()
                .accountId(UUID.randomUUID().toString())
                .serviceBaseUrl("http://localhost:8080/docflows")
                .build();
    }

    @Test
    public void testGetDocflows_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        assertNotNull("Docflows must not be null!", getDocflowPage());
        assertNotNull("Docflows must not be null!", getDocflowPageAsync());
    }

    @Test
    public void testGetDocflows_DocflowPage() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s}", DOCFLOW_PAGE));
        DocflowsValidator.validateDocflowPage(getDocflowPage(), false);
        DocflowsValidator.validateDocflowPage(getDocflowPageAsync(), false);
    }

    @Test
    public void testGetDocflows_DocflowPageItem() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s," +
                "  \"docflows-page-item\": [{%s}]" +
                "}", DOCFLOW_PAGE, DOCFLOW_PAGE_ITEM));
        DocflowsValidator.validateDocflowPage(getDocflowPage(), true);
        DocflowsValidator.validateDocflowPage(getDocflowPageAsync(), true);
    }

    @Test
    public void testGetDocflows_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testGetDocflows_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testGetDocflows_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testGetDocflows_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testGetDocflows_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<Docflow> queryContext = new QueryContext<>();
        queryContext.setFinished(true);
        queryContext.setIncoming(true);
        queryContext.setSkip(0);
        queryContext.setTake(0);
        queryContext.setInnKpp("string");
        queryContext.setUpdatedFrom(new Date());
        queryContext.setUpdatedTo(new Date());
        queryContext.setCreatedFrom(new Date());
        queryContext.setCreatedTo(new Date());
        queryContext.setType("string");
        QueryContext<DocflowPage> docflowPageQueryContext = engine.getDocflowService()
                .getDocflows(queryContext);
        DocflowPage docflowPage = docflowPageQueryContext.get();
        assertNull("docflowPage must be null!", docflowPage);
        ApiException serviceError = docflowPageQueryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }


    private DocflowPage getDocflowPage() {
        QueryContext<Docflow> queryContext = new QueryContext<>();
        queryContext.setFinished(true);
        queryContext.setIncoming(true);
        queryContext.setSkip(0);
        queryContext.setTake(0);
        queryContext.setInnKpp("string");
        queryContext.setUpdatedFrom(new Date());
        queryContext.setUpdatedTo(new Date());
        queryContext.setCreatedFrom(new Date());
        queryContext.setCreatedTo(new Date());
        queryContext.setType("string");
        return engine.getDocflowService().getDocflows(queryContext).get();
    }

    private DocflowPage getDocflowPageAsync() {
        try {
            return engine.getDocflowService()
                    .getDocflowsAsync(true, true, 0, 0, "string", new Date(), new Date(),
                            new Date(), new Date(), "string").get().get();
        } catch (InterruptedException | ExecutionException e) {
            fail();
            return null;
        }
    }
}
