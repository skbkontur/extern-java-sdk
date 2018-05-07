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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.providers.auth;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import java.net.URL;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import ru.kontur.extern_api.sdk.model.AuthInitResponse;
import ru.kontur.extern_api.sdk.providers.ServiceError.ErrorCode;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;

public class CertificateAuthenticationTest {

    private static final Header jsonContentType = new Header("Content-Type", "application/json");
    private static final int PORT = 1080;
    private static final String HOST = "localhost";
    private static final Gson gson = new Gson();

    private static ClientAndServer mockServer;
    private CertificateAuthenticationProvider auth;

    @BeforeClass
    public static void startJetty() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
    }

    @AfterClass
    public static void stopJetty() {
        mockServer.stop();
    }

    private static String quoted(String str) {
        return str.replace('\'', '"');
    }

    private static String createInitResp(String key, String rel, String href) {
        AuthInitResponse authInitResponse = new AuthInitResponse();
        authInitResponse.setEncryptedKey(key);
        Link link = new Link();
        link.setHref(href);
        link.setRel(rel);
        authInitResponse.setLink(link);
        return gson.toJson(authInitResponse);
    }

    private void createAnswerForInitiation(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(
                        request().withMethod("POST").withPath("/v5.9/authenticate-by-cert"),
                        exactly(1))
                .respond(response()
                        .withStatusCode(code)
                        .withHeader(jsonContentType)
                        .withBody(body)
                );
    }

    private void createAnswerForApprove(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(request().withMethod("POST").withPath("/v5.9/approve-cert"), exactly(1))
                .respond(
                        response().withStatusCode(code).withHeader(jsonContentType).withBody(body)
                );
    }

    private void createAnswerForValidInitiation() {
        createAnswerForInitiation(200, createInitResp("secret", "this is a link", "go go"));
    }

    private void createAnswerForValidApprove(String sid) {
        createAnswerForApprove(200, quoted("{ 'Sid': '" + sid + "', 'RefreshToken': 'juice' }"));
    }

    @Before
    public void setupProvider() {

        URL resource = getClass().getClassLoader().getResource("certificate.cer");
        assert resource != null;

        auth = CertificateAuthenticationProvider.usingCertificate(resource)
                .setCryptoProvider(new MockCryptoProvider())
                .setApiKeyProvider(() -> "apikey")
                .setServiceBaseUriProvider(() -> String.format("http://%s:%s/", HOST, PORT))
                .setSignatureKeyProvider(() -> "signature")
                .buildAuthenticationProvider();
    }

    @Test
    public void testSuccessfulAcquiringOfSessionId() {
        createAnswerForValidInitiation();

        String expectedSid = "qwerty";
        createAnswerForValidApprove(expectedSid);

        QueryContext<String> sessionId = auth.sessionId();

        if (sessionId.isFail()) {
            throw sessionId.failure();
        }

        String actualSid = sessionId.getSessionId();
        assertThat(actualSid, equalTo(expectedSid));
    }

    @Test
    public void testForbiddenInitiationAnswerCodeAvailable() {
        createAnswerForInitiation(403, quoted("{ 'Code': 'CertNotValid' }"));
        QueryContext<String> sessionId = auth.sessionId();
        Assert.assertTrue(sessionId.isFail());
        String message = sessionId.getServiceError().getMessage();
        ErrorCode errorCode = sessionId.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.auth));
        assertThat(message, is("CertNotValid"));
    }

    @Test
    public void testForbiddenAuthConfirmationCodeAvailable() {
        createAnswerForValidInitiation();
        createAnswerForApprove(403, quoted("{ 'Code': 'WrongKey' }"));

        QueryContext<String> sessionId = auth.sessionId();
        Assert.assertTrue(sessionId.isFail());

        String message = sessionId.getServiceError().getMessage();
        ErrorCode errorCode = sessionId.getServiceError().getErrorCode();
        assertThat(errorCode, is(ErrorCode.auth));
        assertThat(message, is("WrongKey"));
    }

    @Test
    public void testIncorrectDataDuringInitialization() {
        createAnswerForInitiation(200, createInitResp(null, "this is link", "go"));
        createAnswerForValidApprove("lol");
        QueryContext<String> sessionId = auth.sessionId();
        Assert.assertTrue(sessionId.isSuccess());
    }
}
