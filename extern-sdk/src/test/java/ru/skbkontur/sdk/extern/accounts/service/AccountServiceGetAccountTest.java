package ru.skbkontur.sdk.extern.accounts.service;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.accounts.AccountsValidator;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjects;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

/**
 * @author Mikhail Pavlenko
 */

public class AccountServiceGetAccountTest {
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
    public void testGetAccount_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        QueryContext<CertificateList> queryContext = new QueryContext<>();
        queryContext.set(QueryContext.ACCOUNT_ID, UUID.fromString(StandardValues.ID));
        assertNotNull("Account must not be null!", engine.getAccountService().getAccount(queryContext).get());
    }

    @Test
    public void testGetAccountAsync_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        try {
            assertNotNull("Account must not be null!", engine.getAccountService().getAccountAsync(StandardValues.ID).get().get());
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetAccount_Account() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" + ACCOUNT + "}");
        QueryContext<CertificateList> queryContext = new QueryContext<>();
        queryContext.set(QueryContext.ACCOUNT_ID, UUID.fromString(StandardValues.ID));
        AccountsValidator.validateAccount(engine.getAccountService().getAccount(queryContext).get(), false);
    }

    @Test
    public void testGetAccountAsync_Account() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" + ACCOUNT + "}");
        try {
            AccountsValidator.validateAccount(engine.getAccountService().getAccountAsync(StandardValues.ID).get().get(), false);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetAccount_Links() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                ACCOUNT + "," +
                "\"links\": [" + StandardObjects.LINK + "]" +
                "}");
        QueryContext<CertificateList> queryContext = new QueryContext<>();
        queryContext.set(QueryContext.ACCOUNT_ID, UUID.fromString(StandardValues.ID));
        AccountsValidator.validateAccount(engine.getAccountService().getAccount(queryContext).get(), true);
    }

    @Test
    public void testGetAccountAsync_Links() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                ACCOUNT + "," +
                "\"links\": [" + StandardObjects.LINK + "]" +
                "}");
        try {
            AccountsValidator.validateAccount(engine.getAccountService().getAccountAsync(StandardValues.ID).get().get(), true);
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetAccount_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testGetAccount_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testGetAccount_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testGetAccount_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testGetAccount_INTERNAL_SERVER_ERROR() {
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
