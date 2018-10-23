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
import static org.junit.Assert.assertThat;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Header;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.portal.model.CertificateAuthenticationQuest;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;

public class CertificateAuthenticationTest {

    private static final Header JSON_CONTENT_TYPE = new Header("Content-Type", "application/json");
    private static final int PORT = 1080;
    private static final String HOST = "localhost";
    private static final Gson GSON = GsonProvider.PORTAL.getGson();

    private static ClientAndServer mockServer;
    private AuthenticationProvider auth;

    @BeforeClass
    public static void startMock() {
        mockServer = ClientAndServer.startClientAndServer(PORT);
    }

    @AfterClass
    public static void stopMock() {
        mockServer.stop();
    }

    private static String quoted(String str) {
        return str.replace('\'', '"');
    }

    private static String createInitResp(String key, String rel, String href) {
        return GSON.toJson(new CertificateAuthenticationQuest(
                key.getBytes(),
                null,
                new Link(rel, href)
        ));
    }

    private void createAnswerForInitiation(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(request().withMethod("POST").withPath(".*/authenticate-by-cert"),
                        exactly(1))
                .respond(response()
                        .withStatusCode(code)
                        .withHeader(JSON_CONTENT_TYPE)
                        .withBody(body)
                );
    }

    private void createAnswerForApprove(int code, String body) {
        new MockServerClient(HOST, PORT)
                .when(request().withMethod("POST").withPath(".*/approve-cert"), exactly(1))
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
    public void setupProvider() throws Exception {

        String baseUri = String.format("http://%s:%s/", HOST, PORT);
        auth = AuthenticationProviderBuilder.createFor(baseUri, Level.BODY)
                .certificateAuthentication(new MockCryptoProvider(), new byte[]{1, 2, 3});
    }

    @Test
    public void testSuccessfulAcquiringOfSessionId() {
        createAnswerForValidInitiation();

        String expectedSid = "qwerty";
        createAnswerForValidApprove(expectedSid);

        String actualSid = auth.sessionId().getOrThrow();

        assertThat(actualSid, equalTo(expectedSid));
    }

    @Test
    public void testForbiddenInitiationAnswerCodeAvailable() {
        createAnswerForInitiation(403, quoted("{ 'Code': 'CertNotValid' }"));
        QueryContext<String> sessionId = auth.sessionId();
        Assert.assertTrue(sessionId.isFail());

        Assert.assertEquals(403, sessionId.getServiceError().getResponseCode());
        Assert.assertEquals("CertNotValid", sessionId.getServiceError().getMessage());
    }

    @Test
    public void testForbiddenAuthConfirmationCodeAvailable() {
        createAnswerForValidInitiation();
        createAnswerForApprove(403, quoted("{ 'Code': 'WrongKey' }"));

        QueryContext<String> sessionId = auth.sessionId();
        Assert.assertTrue(sessionId.isFail());
        Assert.assertEquals(403, sessionId.getServiceError().getResponseCode());
        Assert.assertEquals("WrongKey", sessionId.getServiceError().getMessage());
    }

}
