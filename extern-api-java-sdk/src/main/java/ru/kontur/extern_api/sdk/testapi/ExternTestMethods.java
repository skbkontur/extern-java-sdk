/*
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

package ru.kontur.extern_api.sdk.testapi;

import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.DefaultExtern;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.api.TestApi;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;

/**
 * Just examples.
 */
public final class ExternTestMethods {

    public static TestApi build(ExternEngine ee) {
        return build(ee.getApiKeyProvider(), ee.getAuthenticationProvider());
    }

    public static TestApi build(
            ApiKeyProvider apiKeyProvider,
            AuthenticationProvider authenticationProvider
    ) {
        return new KonturConfiguredClient(Level.BODY, DefaultExtern.BASE_URL)
                .setApiKey(apiKeyProvider.getApiKey())
                .setAuthSid(authenticationProvider.sessionId().getOrThrow())
                .createApi(TestApi.class);
    }
}
