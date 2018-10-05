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
 *
 */

package ru.kontur.extern_api.sdk.test.utils;

import java.util.function.Function;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;

public class ApproveCodeProvider implements Function<String, String> {

    private static final String APPROVE_CODE_REQUEST = "/backdoor";
    private static final String BACKDOOR_BASE = "https://cc.dev.kontur";

    private final AuthenticationProvider authenticationProvider;
    private final ApiKeyProvider apiKeyProvider;
    private final HttpClient httpClient;

    public ApproveCodeProvider(ExternEngine engine) {
        this(engine.getAuthenticationProvider(), engine.getApiKeyProvider(), engine.getHttpClient());
    }

    public ApproveCodeProvider(
            AuthenticationProvider authenticationProvider,
            ApiKeyProvider apiKeyProvider,
            HttpClient httpClient) {
        this.authenticationProvider = authenticationProvider;
        this.apiKeyProvider = apiKeyProvider;
        this.httpClient = httpClient;
    }

    @Override
    public String apply(String requestId) {
        String sid = authenticationProvider.sessionId().get();
        String request = BACKDOOR_BASE + APPROVE_CODE_REQUEST + "?resultId=" + requestId;

        httpClient
                .acceptApiKey(apiKeyProvider.getApiKey())
                .acceptAccessToken("auth.sid ", sid);

        return httpClient.followGetLink(request, String.class);
    }

}
