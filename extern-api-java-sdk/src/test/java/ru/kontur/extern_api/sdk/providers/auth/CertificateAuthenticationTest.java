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
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.ServiceError.ErrorCode;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.HttpClientImpl;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthInitResponse;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.Link;

public class CertificateAuthenticationTest {

    private static final Header JSON_CONTENT_TYPE = new Header("Content-Type", "application/json");
    private static final int PORT = 1080;
    private static final String HOST = "localhost";
    private static final Gson GSON = GsonProvider.getGson();

    private static ClientAndServer mockServer;
    private AuthenticationProvider auth;

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
        return GSON.toJson(authInitResponse);
    }

    private void createAnswerForInitiation(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(
                        request().withMethod("POST").withPath("**/authenticate-by-cert"),
                        exactly(1))
                .respond(response()
                        .withStatusCode(code)
                        .withHeader(JSON_CONTENT_TYPE)
                        .withBody(body)
                );
    }

    private void createAnswerForApprove(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(request().withMethod("POST").withPath("**/approve-cert"), exactly(1))
                .respond(
                        response().withStatusCode(code).withHeader(JSON_CONTENT_TYPE).withBody(body)
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

        CertificateProvider certificateProvider = (String thumbprint) -> {
            URL resource1 = CertificateAuthenticationTest.this.getClass().getClassLoader()
                    .getResource(thumbprint + ".cer");
            if (resource1 == null) {
                return new QueryContext<byte[]>().setServiceError(
                        Messages.get(Messages.C_CRYPTO_ERROR_CERTIFICATE_NOT_FOUND, thumbprint));
            }

            try {
                return new QueryContext<byte[]>()
                        .setResult(Files.readAllBytes(Paths.get(resource1.toURI())),
                                QueryContext.CONTENT);
            } catch (URISyntaxException | IOException x) {
                return new QueryContext<byte[]>().setServiceError(
                        Messages.get(Messages.C_CRYPTO_ERROR_CERTIFICATE_NOT_FOUND, thumbprint), x);
            }
        };

        String baseUri = String.format("http://%s:%s/", HOST, PORT);
        auth = CertificateAuthenticationProvider
                .usingCertificate(certificateProvider)
                .setCryptoProvider(new MockCryptoProvider())
                .setApiKeyProvider(() -> "apikey")
                .setServiceBaseUriProvider(() -> baseUri)
                .setSignatureKeyProvider(() -> "certificate")
                .buildAuthenticationProvider();

        auth.httpClient(new HttpClientImpl().setServiceBaseUri(baseUri));
    }

    @Test
    public void testSuccessfulAcquiringOfSessionId() {
        createAnswerForValidInitiation();

        String expectedSid = "qwerty";
        createAnswerForValidApprove(expectedSid);

        QueryContext<String> sessionId = auth.sessionId();

        sessionId.ensureSuccess();

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
