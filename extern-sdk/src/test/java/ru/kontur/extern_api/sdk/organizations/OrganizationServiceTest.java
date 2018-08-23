/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.organizations;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ServiceError.ErrorCode;
import ru.kontur.extern_api.sdk.drafts.service.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.GsonProvider;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationServiceTest {

    private static final Header JSON_CONTENT_TYPE = new Header("Content-Type", "application/json");
    private static final int PORT = 1080;
    private static final String HOST = "localhost";
    private static final String PATH = "/organizations";
    private static final Gson GSON = GsonProvider.getGson();

    private static ClientAndServer mockServer;
    private OrganizationService organizationService;
    private UUID accountId;
    private Company company;

    @BeforeClass
    public static void startJetty() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
    }

    @AfterClass
    public static void stopJetty() {
        if (mockServer != null)
            mockServer.stop();
    }

    @Before
    public void setupCompany() {
        company = new Company();
        company.setId(UUID.randomUUID());
        CompanyGeneral g = new CompanyGeneral();
        g.setInn("7810123456");
        g.setKpp("781001001");
        g.setName("Pajero");
        company.setGeneral(g);
    }

    @Before
    public void setupService() {
        accountId = UUID.randomUUID();
        ExternEngine engine = new ExternEngine();
        engine.setServiceBaseUriProvider(() -> "http://" + HOST + ":" + PORT + PATH);
        engine.setAccountProvider(() -> accountId);
        engine.setApiKeyProvider(() -> UUID.randomUUID().toString());
        engine.setAuthenticationProvider(new AuthenticationProviderAdaptor());

        organizationService = engine.getOrganizationService();
    }

    @Test
    public void testSuccessLookup() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "GET", 200, GSON.toJson(company));
        QueryContext<Company> cxt = organizationService.lookupAsync(company.getId().toString()).get();
        assertFalse(cxt.isFail());
        validateCompany(cxt.getCompany(), company);
    }

    @Test
    public void testUncorrectLookup() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "GET", 400, createError());
        QueryContext<Company> cxt = organizationService.lookupAsync(company.getId().toString()).get();
        assertTrue(cxt.isFail());
        String message = cxt.getServiceError().getMessage();
        ErrorCode errorCode = cxt.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.server));
        assertThat(message, is("Bad Request"));
    }

    @Test
    public void testSuccessCreate() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations", "POST", 201, GSON.toJson(company));
        QueryContext<Company> cxt = organizationService.createAsync(company.getGeneral()).get();
        assertFalse(cxt.isFail());
        validateCompany(cxt.getCompany(), company);
    }

    @Test
    public void testUncorrectCreate() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations", "POST", 400, createError());
        QueryContext<Company> cxt = organizationService.createAsync(company.getGeneral()).get();
        assertTrue(cxt.isFail());
        String message = cxt.getServiceError().getMessage();
        ErrorCode errorCode = cxt.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.server));
        assertThat(message, is("Bad Request"));
    }

    @Test
    public void testSuccessUpdate() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "PUT", 201, GSON.toJson(company));
        QueryContext<Company> cxt = organizationService.updateAsync(company.getId().toString(),"Pajero 2").get();
        assertFalse(cxt.isFail());
        validateCompany(cxt.getCompany(), company);
    }

    @Test
    public void testUncorrectUpdate() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "PUT", 400, createError());
        QueryContext<Company> cxt = organizationService.updateAsync(company.getId().toString(),"Pajero 2").get();
        assertTrue(cxt.isFail());
        String message = cxt.getServiceError().getMessage();
        ErrorCode errorCode = cxt.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.server));
        assertThat(message, is("Bad Request"));
    }

    @Test
    public void testSuccessDelete() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "DELETE", 201, GSON.toJson(company));
        QueryContext<Void> cxt = organizationService.deleteAsync(company.getId().toString()).get();
        assertFalse(cxt.isFail());
    }

    @Test
    public void testUncorrectDelete() throws ExecutionException, InterruptedException {
        createAnswerFor("v1/" + accountId + "/organizations/" + company.getId().toString(), "DELETE", 400, GSON.toJson(company));
        QueryContext<Void> cxt = organizationService.deleteAsync(company.getId().toString()).get();
        assertTrue(cxt.isFail());
        String message = cxt.getServiceError().getMessage();
        ErrorCode errorCode = cxt.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.server));
        assertThat(message, is("Bad Request"));
    }

    private void createAnswerFor(String restPath, String restMethod, int code, String body) {
        new MockServerClient(HOST, PORT)
            .when(request().withMethod(restMethod).withPath(PATH + "/" + restPath), exactly(1))
            .respond(
                response().withStatusCode(code).withHeader(JSON_CONTENT_TYPE).withBody(body)
            );
    }

    private String createError() {
        StringBuilder error = new StringBuilder();
        error.append("{")
            .append("\"id\": \"urn:nss:nid\",")
            .append("\"status-code\": \"continue\",")
            .append("\"message\": \"string\",")
            .append("\"track-id\": \"string\",")
            .append("\"properties\": {}")
            .append("}");
        return error.toString();
    }

    private void validateCompany(Company source, Company response) {
        assertNotNull(response);
        assertEquals(response.getId().toString(),source.getId().toString());
        assertEquals(response.getGeneral().getInn(), source.getGeneral().getInn());
        assertEquals(response.getGeneral().getKpp(), source.getGeneral().getKpp());
        assertEquals(response.getGeneral().getName(), source.getGeneral().getName());
    }
}
