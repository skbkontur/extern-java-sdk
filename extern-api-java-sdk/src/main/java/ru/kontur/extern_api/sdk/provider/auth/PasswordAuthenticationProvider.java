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
package ru.kontur.extern_api.sdk.provider.auth;

import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.portal.AuthApi;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;


public class PasswordAuthenticationProvider implements AuthenticationProvider {

    private String apiKey;
    private LoginAndPasswordProvider creds;
    private final AuthApi authApi;

    public PasswordAuthenticationProvider(
            AuthApi authApi,
            LoginAndPasswordProvider creds,
            String apiKey
    ) {
        this.authApi = authApi;

        this.creds = creds;
        this.apiKey = apiKey;
    }

    @Override
    public QueryContext<String> sessionId() {
        return QueryContextUtils.join(
                authApi.passwordAuthentication(creds.getLogin(), apiKey, null, creds.getPass())
                        .thenApply(SessionResponse::getSid)
                        .thenApply(QueryContext.constructor(QueryContext.SESSION_ID))
                        .exceptionally(QueryContextUtils::completeCareful)
        );
    }

    @Override
    public PasswordAuthenticationProvider httpClient(HttpClient httpClient) {
        return this;
    }

}
