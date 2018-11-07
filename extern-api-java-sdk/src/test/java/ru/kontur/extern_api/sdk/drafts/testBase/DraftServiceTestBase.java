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

package ru.kontur.extern_api.sdk.drafts.testBase;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.ServerSocket;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.drafts.service.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.service.DraftService;


public class DraftServiceTestBase {

    private static final String HOST = "localhost";
    private static final int PORT = getFreePort();

    private static ClientAndServer mockServer;

    protected static DraftService draftService;
    protected static final Gson GSON = GsonProvider.LIBAPI.getGson();

    @BeforeEach
    protected void startMock() {
        mockServer = ClientAndServer.startClientAndServer(PORT);

        ExternEngine engine = ExternEngineBuilder
                .createExternEngine("http://" + HOST + ":" + PORT)
                .apiKey(StandardValues.ID)
                .authProvider(new AuthenticationProviderAdaptor())
                .doNotUseCryptoProvider()
                .accountId(StandardValues.ID)
                .build();

        draftService = engine.getDraftService();
    }

    @AfterEach
    protected void stopMock() {
        mockServer.stop();
    }

    protected static void serverPleaseGetSuccessful(String body) {
        serverPlease()
                .when(request().withMethod("GET"))
                .respond(response()
                        .withBody(body)
                        .withStatusCode(200));
    }

    protected static void serverPleaseGetError(int code) {
        serverPleaseError("GET", code);
    }

    protected static void serverPleasePostError(int code) {
        serverPleaseError("POST", code);
    }

    protected static MockServerClient serverPlease() {
        return new MockServerClient(HOST, PORT);
    }


    private static void serverPleaseError(String command, int code) {
        serverPlease()
                .when(request().withMethod(command))
                .respond(response()
                        .withBody(String.format("{\n"
                                + "\"id\": \"urn:error:externapi:error\",\n"
                                + "\"status-code\": %s,\n"
                                + "\"track-id\": %s,\n"
                                + "\"message\": \"Message\",\n"
                                + "\"trace-id\": %s \n"
                                + "}", code, StandardValues.TRACK_ID, StandardValues.TRACE_ID))
                        .withStatusCode(code));
    }

    private static int getFreePort() {
        try (ServerSocket socket = new ServerSocket(0)) {
            return socket.getLocalPort();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
