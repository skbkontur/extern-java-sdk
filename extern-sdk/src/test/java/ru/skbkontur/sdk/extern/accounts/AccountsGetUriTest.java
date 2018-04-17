package ru.skbkontur.sdk.extern.accounts;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.StandardObjects;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class AccountsGetUriTest {
    private static final String LOCALHOST_ACCOUNTS = "http://localhost:8080/accounts";
    private static Server server;

    private QueryContext<List<Link>> queryContext;

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/accounts/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @Before
    public void prepareQueryContext() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(LOCALHOST_ACCOUNTS);
        queryContext = new QueryContext<>();
        queryContext.setApiClient(apiClient);
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
    public void testAcquireBaseUri_Empty() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[]");
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireBaseUri(queryContext);
        assertNotNull("List of links must not be null!", queryContext.get());
    }

    @Test
    public void testAcquireBaseUri_OneLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("[%s]", StandardObjects.LINK));
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireBaseUri(queryContext);
        List<Link> links = queryContext.get();
        StandardObjectsValidator.validateNotEmptyList(links, "Links");
        assertEquals("List of links must contains only one element!", 1, links.size());
        StandardObjectsValidator.validateLink(links.get(0));
    }

    @Test
    public void testAcquireBaseUri_TwoLinks() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("[%s,%s]", StandardObjects.LINK, StandardObjects.LINK));
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireBaseUri(queryContext);
        List<Link> links = queryContext.get();
        StandardObjectsValidator.validateNotEmptyList(links, "Links");
        assertEquals("List of links must contains two elements!", 2, links.size());
        StandardObjectsValidator.validateLink(links.get(0));
        StandardObjectsValidator.validateLink(links.get(1));
    }

    @Test
    public void testAcquireBaseUri_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testAcquireBaseUri_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testAcquireBaseUri_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testAcquireBaseUri_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testAcquireBaseUri_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireBaseUri(queryContext);
        List<Link> links = queryContext.get();
        assertNull("List of links must be null!", links);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}