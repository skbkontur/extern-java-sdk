package ru.skbkontur.sdk.extern.accounts.adaptor;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.accounts.AccountsValidator;
import ru.skbkontur.sdk.extern.common.*;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import javax.servlet.http.HttpServletResponse;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class AccountsAcquireAccountsTest {
    private static final String LOCALHOST_ACCOUNTS = "http://localhost:8080/accounts";
    private static Server server;

    private QueryContext<AccountList> queryContext;

    private final static String ACCOUNT_LIST = "\"skip\": 0, " +
            "\"take\": 0, " +
            "\"total-count\": 0 ";
    private final static String ACCOUNT = "\"id\": \"" + StandardValues.ID + "\"," +
            "\"inn\": \"string\"," +
            "\"kpp\": \"string\"," +
            "\"organization-name\": \"string\"";

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
    public void testAcquireAccounts_Empty() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireAccounts(queryContext);
        assertNotNull("accountList must not be null!", queryContext.get());
    }

    @Test
    public void testAcquireAccounts_EmptyArray() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"accounts\": []}");
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireAccounts(queryContext);
        AccountList accountList = queryContext.get();
        assertNotNull("accountList must not be null!", accountList);
        StandardObjectsValidator.validateEmptyList(accountList.getAccounts(), "Accounts");
    }

    @Test
    public void testAcquireAccounts_AccountList() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s}", ACCOUNT_LIST));
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireAccounts(queryContext);
        AccountList accountList = queryContext.get();
        AccountsValidator.validateAccountList(accountList, false);
    }

    @Test
    public void testAcquireAccounts_Accounts() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                ACCOUNT_LIST + "," +
                "\"accounts\": [{" + ACCOUNT + "}]" +
                "}");
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireAccounts(queryContext);
        AccountList accountList = queryContext.get();
        AccountsValidator.validateAccountList(accountList, true);
    }

    @Test
    public void testAcquireAccounts_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testAcquireAccounts_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testAcquireAccounts_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testAcquireAccounts_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testAcquireAccounts_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        AccountsAdaptor accountsAdaptor = new AccountsAdaptor();
        accountsAdaptor.acquireAccounts(queryContext);
        AccountList accountList = queryContext.get();
        assertNull("accountList must be null!", accountList);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}