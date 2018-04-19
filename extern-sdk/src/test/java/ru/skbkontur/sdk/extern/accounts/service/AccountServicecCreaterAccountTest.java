package ru.skbkontur.sdk.extern.accounts.service;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

/**
 * @author Mikhail Pavlenko
 */

public class AccountServicecCreaterAccountTest {
    private static ExternEngine engine;
    private static Server server;

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
        engine = new ExternEngine();
        engine.setServiceBaseUriProvider(() -> "http://localhost:8080/accounts");
        engine.setAccountProvider(UUID::randomUUID);
        engine.setApiKeyProvider(() -> UUID.randomUUID().toString());
        engine.setAuthenticationProvider(
                new AuthenticationProvider() {
                    @Override
                    public QueryContext<String> sessionId() {
                        return new QueryContext<String>().setResult("1", QueryContext.SESSION_ID);
                    }

                    @Override
                    public String authPrefix() {
                        return "auth.sid ";
                    }

                    @Override
                    public void addAuthenticationListener(AuthenticationListener authListener) {
                    }

                    @Override
                    public void removeAuthenticationListener(AuthenticationListener authListener) {
                    }

                    @Override
                    public void raiseUnauthenticated(ServiceError x) {
                    }
                });

        engine.configureServices();
    }

    @Test
    public void testCreateAccount() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("OK");
        QueryContext<CertificateList> queryContext = new QueryContext<>();
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setInn("string");
        accountRequest.setKpp("string");
        accountRequest.setOrganizationName("string");
        queryContext.setCreateAccountRequest(accountRequest);
        Object object = engine.getAccountService().createAccount(queryContext).get();
        assertNotNull("Account must not be null!", object);
        assertEquals("Object is wrong!", "OK", object);
    }

    @Test
    public void testCreateAccountAsync() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("OK");
        CreateAccountRequest accountRequest = new CreateAccountRequest();
        accountRequest.setInn("string");
        accountRequest.setKpp("string");
        accountRequest.setOrganizationName("string");
        try {
            Object object = engine.getAccountService().createAccountAsync(accountRequest).get().get();
            assertNotNull("Account must not be null!", object);
            assertEquals("Object is wrong!", "OK", object);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testCreateAccount_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testCreateAccount_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testCreateAccount_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testCreateAccount_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testCreateAccount_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<AccountList> queryContext = engine.getAccountService().acquireAccounts(new QueryContext<CertificateList>());
        assertNull("List of links must be null!", queryContext.get());
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }

}
