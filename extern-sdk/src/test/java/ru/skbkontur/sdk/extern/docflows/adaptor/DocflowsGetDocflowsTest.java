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

package ru.skbkontur.sdk.extern.docflows.adaptor;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;
import static javax.servlet.http.HttpServletResponse.SC_FORBIDDEN;
import static javax.servlet.http.HttpServletResponse.SC_INTERNAL_SERVER_ERROR;
import static javax.servlet.http.HttpServletResponse.SC_NOT_FOUND;
import static javax.servlet.http.HttpServletResponse.SC_OK;
import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;

import java.util.UUID;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.joda.time.DateTime;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.docflows.DocflowsValidator;
import ru.skbkontur.sdk.extern.model.DocflowPage;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

/**
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Mikhail Pavlenko
 */

public class DocflowsGetDocflowsTest {

    private static final String LOCALHOST_DOCFLOWS = "http://localhost:8080/docflows";
    private static Server server;

    private QueryContext<DocflowPage> queryContext;

    private final static String DOCFLOW_PAGE = "\"skip\": 0," +
        "\"take\": 0," +
        "\"total-count\": 0";

    private final static String DOCFLOW_PAGE_ITEM =
        "\"id\": \"00000000-0000-0000-0000-000000000000\"," +
            "\"type\": \"urn:nss:nid\"," +
            "\"status\": \"urn:nss:nid\"," +
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

    @Before
    public void prepareQueryContext() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(LOCALHOST_DOCFLOWS);
        queryContext = new QueryContext<>();
        queryContext.setApiClient(apiClient);
        queryContext.setAccountProvider(UUID::randomUUID);
        queryContext.setFinished(true);
        queryContext.setIncoming(true);
        queryContext.setSkip(0);
        queryContext.setTake(0);
        queryContext.setInnKpp("string");
        queryContext.setUpdatedFrom(new DateTime());
        queryContext.setUpdatedTo(new DateTime());
        queryContext.setCreatedFrom(new DateTime());
        queryContext.setCreatedTo(new DateTime());
        queryContext.setType("string");
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
    public void testGetDocflows_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        new DocflowsAdaptor().getDocflows(queryContext);
        assertNotNull("Docflows must not be null!", queryContext.get());
    }

    @Test
    public void testGetDocflows_DocflowPage() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s}", DOCFLOW_PAGE));
        new DocflowsAdaptor().getDocflows(queryContext);
        DocflowsValidator.validateDocflowPage(queryContext.get(), false);
    }

    @Test
    public void testGetDocflows_DocflowPageItem() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s," +
            "  \"docflows-page-item\": [{%s}]" +
            "}", DOCFLOW_PAGE, DOCFLOW_PAGE_ITEM));
        new DocflowsAdaptor().getDocflows(queryContext);
        DocflowsValidator.validateDocflowPage(queryContext.get(), true);
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
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocflows(queryContext);
        DocflowPage docflowPage = queryContext.get();
        assertNull("docflowPage must be null!", docflowPage);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}
