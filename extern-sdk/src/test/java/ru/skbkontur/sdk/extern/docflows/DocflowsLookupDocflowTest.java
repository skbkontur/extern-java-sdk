package ru.skbkontur.sdk.extern.docflows;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.*;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjects;
import ru.skbkontur.sdk.extern.common.StandardValues;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class DocflowsLookupDocflowTest {
    private static final String LOCALHOST_DOCFLOWS = "http://localhost:8080/docflows";
    private static Server server;

    private QueryContext<Docflow> queryContext;

    private final static String DOCFLOW = "\"id\": \"" + StandardValues.ID + "\"," +
            "\"type\": \"urn:nss:nid\"," +
            "\"status\": \"urn:nss:nid\"," +
            "\"send-date\": \"" + StandardValues.DATE + "\"," +
            "\"last-change-date\": \"" + StandardValues.DATE + "\"";


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
    public void testLookupDocflow_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        assertNotNull("Docflows must not be null!", queryContext.get());
    }

    @Test
    public void testLookupDocflow_Docflow() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s}", DOCFLOW));
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        Docflow docflow = queryContext.get();
        DocflowsValidator.validateDocflow(docflow, false, false, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Description() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s,\"description\": {}}", DOCFLOW));
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        Docflow docflow = queryContext.get();
        DocflowsValidator.validateDocflow(docflow, true, false, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Documents() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage(String.format("{%s," +
                "\"description\": {}," +
                "\"documents\": [{" +
                "   \"id\": \"%s\"" +
                "}]" +
                "}", DOCFLOW, StandardValues.ID));
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        DocflowsValidator.validateDocflow(queryContext.get(), true, true, false);
    }

    @Test
    public void testLookupDocflow_Docflow_Links() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                DOCFLOW + "," +
                "  \"description\": {}," +
                "  \"documents\": [{\"id\": \"" + StandardValues.ID + "\"}]," +
                "  \"links\": [" + StandardObjects.LINK + "]" +
                "}");
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        DocflowsValidator.validateDocflow(queryContext.get(), true, true, true);
    }

    @Test
    public void testLookupDocflow_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testLookupDocflow_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testLookupDocflow_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testLookupDocflow_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testLookupDocflow_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        DocflowsAdaptor docflowsAdaptor = new DocflowsAdaptor();
        docflowsAdaptor.lookupDocflow(queryContext);
        Docflow docflow = queryContext.get();
        assertNull("docflow must be null!", docflow);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}
