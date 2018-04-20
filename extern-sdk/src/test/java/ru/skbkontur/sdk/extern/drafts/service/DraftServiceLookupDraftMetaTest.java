package ru.skbkontur.sdk.extern.drafts.service;

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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.drafts.DraftsValidator;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.model.Draft;
import ru.skbkontur.sdk.extern.model.DraftMeta;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

public class DraftServiceLookupDraftMetaTest {

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUpClass() {
        engine = new ExternEngine();
        engine.setServiceBaseUriProvider(() -> "http://localhost:8080/drafts");
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
    public void testDraftsLookup() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
            "\"sender\": {" +
            "  \"inn\": \"string\"," +
            "  \"kpp\": \"string\"," +
            "  \"certificate\": {" +
            "    \"content\": \"string\"" +
            "  }," +
            "  \"ipaddress\": \"string\"" +
            "}," +
            "\"recipient\": {" +
            "  \"ifns-code\": \"string\"" +
            "}," +
            "\"payer\": {" +
            "  \"inn\": \"string\"," +
            "  \"organization\": {" +
            "    \"kpp\": \"string\"" +
            "  }" +
            "}" +
            "}");
        DraftsValidator.validateDraftMeta(getDraftMeta());
        DraftsValidator.validateDraftMeta(getDraftMetaAsync());
    }

    @Test
    public void testDraftsLookupDraftMeta_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testDraftsLookupDraftMeta_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testDraftsLookupDraftMeta_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testDraftsLookupDraftMeta_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testDraftsLookupDraftMeta_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        QueryContext<Draft> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        QueryContext<DraftMeta> draftMetaQueryContext = engine.getDraftService()
            .lookupDraftMeta(queryContext);
        assertNull("DraftMeta must be null!", draftMetaQueryContext.get());
        ServiceError serviceError = draftMetaQueryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }

    private DraftMeta getDraftMeta() {
        QueryContext<Draft> queryContext = new QueryContext<>();
        queryContext.setDraftId(StandardValues.ID);
        return engine.getDraftService().lookupDraftMeta(queryContext).get();
    }

    private DraftMeta getDraftMetaAsync() {
        try {
            return engine.getDraftService().lookupDraftMetaAsync(StandardValues.ID).get().get();
        } catch (InterruptedException | ExecutionException e) {
            fail();
            return null;
        }
    }
}
