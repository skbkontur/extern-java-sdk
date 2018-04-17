package ru.skbkontur.sdk.extern.docflows;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.*;
import ru.skbkontur.sdk.extern.common.*;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

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
    @Ignore // bug reported: KA-1213
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
    @Ignore // bug reported: KA-1213
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
    @Ignore // bug reported: KA-1213
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
    @Ignore // bug reported: KA-1213
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
    @Ignore // bug reported: KA-1237
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
