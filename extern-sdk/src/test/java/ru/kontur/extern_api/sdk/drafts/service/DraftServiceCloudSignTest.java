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

package ru.kontur.extern_api.sdk.drafts.service;

import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.UUID;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

public class DraftServiceCloudSignTest {

    private static final String HOST = "localhost";
    private static final int PORT = getFreePort();

    private static ClientAndServer mockServer;
    private static DraftService draftService;

    @BeforeClass
    public static void startMock() {
        mockServer = ClientAndServer.startClientAndServer(PORT);

        ExternEngine engine = new ExternEngine();
        engine.setServiceBaseUriProvider(() -> "http://" + HOST + ":" + PORT);
        engine.setAccountProvider(UUID::randomUUID);
        engine.setApiKeyProvider(() -> UUID.randomUUID().toString());
        engine.setAuthenticationProvider(new AuthenticationProviderAdaptor());
        draftService = engine.getDraftService();
    }

    @AfterClass
    public static void stopMock() {
        mockServer.stop();
    }

    @Test
    public void signInitiationShouldParseAnswer() {

        serverPlease()
                .when(request().withMethod("POST"), exactly(1))
                .respond(response().withBody("{ "
                        + "'links': [] , "
                        + "'documents-to-sign': [], "
                        + "'request-id': '123' }"
                ));

        QueryContext<Object> context = magicContext()
                .setDraftId(UUID.randomUUID());

        QueryContext<SignInitiation> queryContext = draftService
                .cloudSignInit(context)
                .ensureSuccess();

        SignInitiation signInitiation = queryContext.get();

        Assert.assertTrue(signInitiation.getLinks().isEmpty());
        Assert.assertTrue(signInitiation.getDocumentsToSign().isEmpty());
        Assert.assertEquals("123", signInitiation.getRequestId());
    }

    @Test
    public void signConfirmShouldParseAnswer() {

        serverPlease()
                .when(request()
                                .withMethod("POST")
                                .withQueryStringParameter("code", "123")
                                .withQueryStringParameter("requestId", "321"),
                        exactly(1))
                .respond(response().withBody("{ "
                        + "'signed-documents': [] }"
                ));

        QueryContext<Object> context = magicContext()
                .setDraftId(UUID.randomUUID())
                .set("code", "123")
                .set("requestId", "321");

        SignedDraft signedDraft = draftService
                .cloudSignConfirm(context)
                .ensureSuccess()
                .get();

        Assert.assertTrue(signedDraft.getSignedDocuments().isEmpty());
    }

    //    @Test
    public void cloudSignMethodWithSupplierShouldSignDraft() throws Exception {

        String draftId = UUID.randomUUID().toString();

        serverPlease()
                .when(request().withPath(".*/cloud-sign$"), exactly(1))
                .respond(response().withBody("{ "
                        + "'links': [] , "
                        + "'documents-to-sign': [], "
                        + "'request-id': '123' }"
                ));

        serverPlease()
                .when(
                        request()
                                .withPath(".*/cloud-sign-confirm$")
                                .withQueryStringParameter("code", "1234"),
                        exactly(1))
                .respond(response().withBody("{ 'signed-documents': [] }"));

        SignedDraft signedDraft = draftService
                .cloudSignAsync(draftId, cxt -> "1234")
                .get().ensureSuccess().get();

        Assert.assertTrue(signedDraft.getSignedDocuments().isEmpty());
    }

    private static MockServerClient serverPlease() {
        return new MockServerClient(HOST, PORT);
    }

    private static <T> QueryContext<T> magicContext() {
        return new QueryContext<T>()
                .setServiceBaseUriProvider(() -> "http://" + HOST + ":" + PORT)
                .setAccountProvider(UUID::randomUUID)
                .setApiKeyProvider(() -> UUID.randomUUID().toString())
                .setAuthenticationProvider(new AuthenticationProviderAdaptor());
    }

    private static int getFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
