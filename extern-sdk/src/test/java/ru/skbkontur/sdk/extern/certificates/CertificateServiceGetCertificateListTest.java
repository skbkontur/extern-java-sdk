/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.skbkontur.sdk.extern.certificates;

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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.common.ResponseData;
import ru.skbkontur.sdk.extern.common.StandardObjectsValidator;
import ru.skbkontur.sdk.extern.common.TestServlet;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.model.Certificate;
import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

public class CertificateServiceGetCertificateListTest {

    private static ExternEngine engine;
    private static Server server;

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
        engine.setServiceBaseUriProvider(() -> "http://localhost:8080/certificates");
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
    public void testGetCertificateList_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        assertNotNull("Certificates must not be null!",
            engine.getCertificateService().getCertificateList(new QueryContext<CertificateList>())
                .get());
    }

    @Test
    public void testGetCertificateListAsync_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");
        CompletableFuture<QueryContext<CertificateList>> queryContextAsync = engine
            .getCertificateService().getCertificateListAsync();
        try {
            assertNotNull("Certificates must not be null!", queryContextAsync.get().get());
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
    }

    @Test
    public void testGetCertificateList() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" + CERTIFICATE_LIST + "}");
        CertificateList certificateList = engine.getCertificateService()
            .getCertificateList(new QueryContext<CertificateList>()).get();
        CertificatesValidator.validateCertificateList(certificateList);
        StandardObjectsValidator
            .validateEmptyList(certificateList.getCertificates(), "Certificates");
    }

    @Test
    public void testGetCertificateListAsync() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{" + CERTIFICATE_LIST + "}");
        try {
            CertificateList certificateList = engine.getCertificateService()
                .getCertificateListAsync().get().get();
            CertificatesValidator.validateCertificateList(certificateList);
            StandardObjectsValidator
                .validateEmptyList(certificateList.getCertificates(), "Certificates");
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
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
        //
        CertificateList certificateList = engine.getCertificateService()
            .getCertificateList(new QueryContext<CertificateList>()).get();
        CertificatesValidator.validateCertificateList(certificateList);
        List<Certificate> certificates = certificateList.getCertificates();
        StandardObjectsValidator.validateNotEmptyList(certificates, "Certificates");
        CertificatesValidator.validateCertificate(certificates.get(0));
    }

    @Test
    public void testGetCertificatesAsync_Certificates() {
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
        try {
            CertificateList certificateList = engine.getCertificateService()
                .getCertificateListAsync().get().get();
            CertificatesValidator.validateCertificateList(certificateList);
            List<Certificate> certificates = certificateList.getCertificates();
            StandardObjectsValidator.validateNotEmptyList(certificates, "Certificates");
            CertificatesValidator.validateCertificate(certificates.get(0));
        } catch (InterruptedException | ExecutionException e) {
            fail();
        }
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
        QueryContext<CertificateList> queryContext = engine.getCertificateService()
            .getCertificateList(new QueryContext<CertificateList>());
        assertNull("certificateList must be null!", queryContext.get());
        ServiceError serviceError = queryContext.getServiceError();
        assertNotNull("ServiceError must not be null!", serviceError);
        assertEquals("Response code is wrong!", code, serviceError.getResponseCode());
    }
}
