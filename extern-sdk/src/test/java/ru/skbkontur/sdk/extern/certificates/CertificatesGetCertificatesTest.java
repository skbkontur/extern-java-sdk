package ru.skbkontur.sdk.extern.certificates;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.*;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.model.Certificate;
import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.CertificatesAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

import java.util.List;
import java.util.UUID;

import static javax.servlet.http.HttpServletResponse.*;
import static junit.framework.TestCase.*;

public class CertificatesGetCertificatesTest {
    private static final String LOCALHOST_CERTIFICATES = "http://localhost:8080/certificates";
    private static Server server;
    private QueryContext<CertificateList> queryContext;

    private final static String CERTIFICATE_LIST = "" +
            "\"total-count\": 0," +
            "\"page-index\": 0," +
            "\"page-size\": 0";

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/certificates/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @Before
    public void prepareQueryContext() {
        ApiClient apiClient = new ApiClient();
        apiClient.setBasePath(LOCALHOST_CERTIFICATES);
        queryContext = new QueryContext<>();
        queryContext.setApiClient(apiClient);
        queryContext.setAccountProvider(UUID::randomUUID);
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
    public void testGetCertificates_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        CertificatesAdaptor certificatesAdaptor = new CertificatesAdaptor();
        certificatesAdaptor.getCertificates(queryContext);
        assertNotNull("Certificates must not be null!", queryContext.get());
    }

    @Test
    public void testGetCertificates_CertificateList() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" + CERTIFICATE_LIST + "}");
        CertificatesAdaptor certificatesAdaptor = new CertificatesAdaptor();
        certificatesAdaptor.getCertificates(queryContext);
        CertificateList certificateList = queryContext.get();
        CertificatesValidator.validateCertificateList(certificateList);
        StandardObjectsValidator.validateEmptyList(certificateList.getCertificates(), "Certificates");
    }

    @Test
    public void testGetCertificates_Certificates() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" +
                CERTIFICATE_LIST + "," +
                    "\"certificates\": [{" +
                    "\"fio\": \"string\"," +
                    "\"inn\": \"string\"," +
                    "\"kpp\": \"string\"," +
                    "\"is-valid\": true," +
                    "\"is-cloud\": true," +
                    "\"is-qualified\": true," +
                    "\"content\": \"string\"}]" +
                "}");
        CertificatesAdaptor certificatesAdaptor = new CertificatesAdaptor();
        certificatesAdaptor.getCertificates(queryContext);
        CertificateList certificateList = queryContext.get();
        CertificatesValidator.validateCertificateList(certificateList);

        List<Certificate> certificates = certificateList.getCertificates();
        StandardObjectsValidator.validateNotEmptyList(certificates, "Certificates");
        CertificatesValidator.validateCertificate(certificates.get(0));
    }

    @Test
    public void testGetCertificates_BAD_REQUEST() {
        ResponseData.INSTANCE.setResponseCode(SC_BAD_REQUEST); // 400
        checkResponseCode(SC_BAD_REQUEST);
    }

    @Test
    public void testGetCertificates_UNAUTHORIZED() {
        ResponseData.INSTANCE.setResponseCode(SC_UNAUTHORIZED); // 401
        checkResponseCode(SC_UNAUTHORIZED);
    }

    @Test
    public void testGetCertificates_FORBIDDEN() {
        ResponseData.INSTANCE.setResponseCode(SC_FORBIDDEN); // 403
        checkResponseCode(SC_FORBIDDEN);
    }

    @Test
    public void testGetCertificates_NOT_FOUND() {
        ResponseData.INSTANCE.setResponseCode(SC_NOT_FOUND); // 404
        checkResponseCode(SC_NOT_FOUND);
    }

    @Test
    public void testGetCertificates_INTERNAL_SERVER_ERROR() {
        ResponseData.INSTANCE.setResponseCode(SC_INTERNAL_SERVER_ERROR); // 500
        checkResponseCode(SC_INTERNAL_SERVER_ERROR);
    }

    private void checkResponseCode(int code) {
        CertificatesAdaptor certificatesAdaptor = new CertificatesAdaptor();
        certificatesAdaptor.getCertificates(queryContext);
        CertificateList certificateList = queryContext.get();
        assertNull("certificateList must be null!", certificateList);
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}
