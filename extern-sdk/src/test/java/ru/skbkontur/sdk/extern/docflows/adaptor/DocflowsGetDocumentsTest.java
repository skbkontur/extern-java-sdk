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

import java.util.List;
import java.util.UUID;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjects;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.docflows.DocflowsValidator;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

public class DocflowsGetDocumentsTest {
    private static final String LOCALHOST_DOCFLOWS = "http://localhost:8080/docflows";
    private static Server server;

    private QueryContext<List<Document>> queryContext;

    private final static String DOCUMENT_DESCRIPTION = "{\"type\": \"urn:nss:nid\"," +
            "\"filename\": \"string\"," +
            "\"content-type\": \"string\"}";

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
        queryContext.setDocflowId(UUID.randomUUID());
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
    public void testGetDocuments_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[]");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        assertNotNull("Documents must not be null!", queryContext.get());
    }

    @Test
    public void testGetDocuments_Document() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("[{\"id\": \"%s\"}]", StandardValues.ID));
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        List<Document> documents = queryContext.get();
        StandardObjectsValidator.validateNotEmptyList(documents, "Documents");
        DocflowsValidator.validateDocument(documents.get(0), false, false, false, false);
    }

    @Test
    public void testGetDocuments_Document_Description() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"description\": " + DOCUMENT_DESCRIPTION +
                "}]");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        DocflowsValidator.validateDocument(queryContext.get().get(0), true, false, false, false);
    }

    @Test
    public void testGetDocuments_Document_WithContent() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"description\": " + DOCUMENT_DESCRIPTION + "," +
                "\"content\": {\n" +
                "  \"decrypted\": " + StandardObjects.LINK + "," +
                "  \"encrypted\": " + StandardObjects.LINK +
                "}" +
                "}]");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        DocflowsValidator.validateDocument(queryContext.get().get(0), true, true, false, false);
    }

    @Test
    public void testGetDocuments_Document_Signature() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"description\": " + DOCUMENT_DESCRIPTION + "," +
                "\"content\": {\n" +
                "  \"decrypted\": " + StandardObjects.LINK + "," +
                "  \"encrypted\": " + StandardObjects.LINK +
                "}," +
                "\"signatures\": [{\"id\": \"" + StandardValues.ID + "\"}]" +
                "}]");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        DocflowsValidator.validateDocument(queryContext.get().get(0), true, true, true, false);
    }

    @Test
    public void testGetDocuments_Document_Links() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("[{" +
                "\"id\": \"" + StandardValues.ID + "\"," +
                "\"description\": " + DOCUMENT_DESCRIPTION + "," +
                "\"content\": {\n" +
                "  \"decrypted\": " + StandardObjects.LINK + "," +
                "  \"encrypted\": " + StandardObjects.LINK +
                "}," +
                "\"signatures\": [{\"id\": \"" + StandardValues.ID + "\"}]," +
                "\"links\": [" + StandardObjects.LINK + "]" +
                "}]");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.getDocuments(queryContext);
        DocflowsValidator.validateDocument(queryContext.get().get(0), true, true, true, true);
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
        docflowsAdaptor.getDocuments(queryContext);
        List<Document> documents = queryContext.get();
        assertNull("documents must be null!", documents);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}
