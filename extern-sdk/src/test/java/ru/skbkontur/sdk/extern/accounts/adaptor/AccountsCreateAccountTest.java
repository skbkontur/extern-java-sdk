package ru.skbkontur.sdk.extern.accounts.adaptor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import javax.servlet.http.HttpServletResponse;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

public class AccountsCreateAccountTest {
    private static final String LOCALHOST_ACCOUNTS = "http://localhost:8080/accounts";
    private static Server server;

    private QueryContext<Object> queryContext;

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
    public void testCreateAccount() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("OK");
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setInn("string");
        accountRequest.setKpp("string");
        accountRequest.setOrganizationName("string");
        queryContext.setCreateAccountRequest(accountRequest);
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.createAccount(queryContext);
        Object object = queryContext.get();
        assertNotNull("Account must not be null!", object);
        assertEquals("Object is wrong!", "OK", object);
    }

}