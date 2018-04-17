package ru.skbkontur.sdk.extern.drafts;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.model.DraftMeta;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class DraftsLookupDraftMetaTest {
    private static final String LOCALHOST_DRAFTS = "http://localhost:8080/drafts";
    private static Server server;

    private QueryContext<DraftMeta> queryContext;

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/drafts/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @Before
    public void prepareQueryContext() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(LOCALHOST_DRAFTS);
        queryContext = new QueryContext<>();
        queryContext.setApiClient(apiClient);
        queryContext.setAccountProvider(UUID::randomUUID);
        queryContext.setDraftId(UUID.randomUUID());
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
    public void testDraftsLookup() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
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
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDraftMeta(queryContext);
        DraftsValidator.validateDraftMeta(queryContext.getDraftMeta());
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
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDraftMeta(queryContext);
        DraftMeta draftMeta = queryContext.get();
        assertNull("DraftMeta must be null!", draftMeta);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}