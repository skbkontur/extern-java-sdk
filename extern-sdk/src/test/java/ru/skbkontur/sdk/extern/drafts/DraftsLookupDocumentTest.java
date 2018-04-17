package ru.skbkontur.sdk.extern.drafts;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.docflows.DocflowsValidator;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class DraftsLookupDocumentTest {
    private static final String LOCALHOST_DRAFTS = "http://localhost:8080/drafts";
    private static Server server;

    private QueryContext<DraftDocument> queryContext;

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
        queryContext.setDocumentId(UUID.randomUUID());
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
    public void testDraftsLookupDocument_Empty() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        assertNotNull("DraftDocument must not be null!", queryContext.get());
    }

    @Test
    public void testDraftsLookupDocument_DraftDocument() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"id\": \"" + StandardValues.ID + "\"}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, false, false);
    }

    @Test
    public void testDraftsLookupDocument_DecryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"decrypted-content-link\": {" +
                "  \"href\": \"string\"," +
                "  \"rel\": \"string\"," +
                "  \"name\": \"string\"," +
                "  \"title\": \"string\"," +
                "  \"profile\": \"string\"," +
                "  \"templated\": true" +
                "}}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), true, false, false);
    }

    @Test
    public void testDraftsLookupDocument_EncryptedContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"encrypted-content-link\": {" +
                "  \"href\": \"string\"," +
                "  \"rel\": \"string\"," +
                "  \"name\": \"string\"," +
                "  \"title\": \"string\"," +
                "  \"profile\": \"string\"," +
                "  \"templated\": true" +
                "}}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, true, false);
    }

    @Test
    public void testDraftsLookupDocument_SignatureContentLink() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"signature-content-link\": {" +
                "  \"href\": \"string\"," +
                "  \"rel\": \"string\"," +
                "  \"name\": \"string\"," +
                "  \"title\": \"string\"," +
                "  \"profile\": \"string\"," +
                "  \"templated\": true" +
                "}}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DraftsValidator.validateDraftDocument(queryContext.getDraftDocument(), false, false, true);
    }

    @Test
    public void testDraftsLookupDocument_DocumentDescription() {
        ResponseData.INSTANCE.setResponseCode(HttpServletResponse.SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"description\": {" +
                "  \"type\": \"urn:nss:nid\"," +
                "  \"filename\": \"string\"," +
                "  \"content-type\": \"string\"" +
                "}}");
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DocflowsValidator.validateDocumentDescription(queryContext.getDraftDocument().getDocumentDescription());
    }

    @Test
    public void testDraftsLookupDocument_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testDraftsLookupDocument_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testDraftsLookupDocument_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testDraftsLookupDocument_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testDraftsLookupDocument_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        DraftsAdaptor draftsAdaptor = new DraftsAdaptor();
        draftsAdaptor.lookupDocument(queryContext);
        DraftDocument draftDocument = queryContext.get();
        assertNull("draftDocument must be null!", draftDocument);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}