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

package ru.kontur.extern_api.sdk.providers.auth;

import ru.kontur.extern_api.sdk.providers.ApiKeyProvider;
import ru.kontur.extern_api.sdk.providers.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.providers.UriProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiResponse;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.SESSION_ID;


/**
 * @author AlexS
 */
public class AuthenticationProviderByPass extends AuthenticationProviderAbstract {

    private final String authPrefix;
    private ApiKeyProvider apiKeyProvider;
    private LoginAndPasswordProvider loginAndPasswordProvider;
    private UriProvider authBaseUriProvider;

    public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider, String authPrefix) {
        this.authBaseUriProvider = authBaseUriProvider;
        this.loginAndPasswordProvider = loginAndPasswordProvider;
        this.apiKeyProvider = apiKeyProvider;
        this.authPrefix = authPrefix == null ? DEFAULT_AUTH_PREFIX : authPrefix;
    }

    public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider) {
        this(authBaseUriProvider, loginAndPasswordProvider, apiKeyProvider, DEFAULT_AUTH_PREFIX);
    }

    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    public void setLoginAndPasswordProvider(LoginAndPasswordProvider loginAndPasswordProvider) {
        this.loginAndPasswordProvider = loginAndPasswordProvider;
    }

    public void setAuthBaseUriProvider(UriProvider authBaseUriProvider) {
        this.authBaseUriProvider = authBaseUriProvider;
    }

    @Override
    public QueryContext<String> sessionId() {
        QueryContext<String> cxt = createQueryContext();

        try {

            if (loginAndPasswordProvider == null) {
                return cxt.setServiceError("Missing the login provider");
            }

            String login = loginAndPasswordProvider.getLogin();

            String pass = loginAndPasswordProvider.getPass();

            if (login == null) {
                cxt.setServiceError("Missing the required parameter 'login'");
            }

            if (pass == null) {
                cxt.setServiceError("Missing the required parameter 'pass'");
            }

            if (apiKeyProvider == null) {
                return cxt.setServiceError("Missing the api key provider");
            }

            String apiKey = apiKeyProvider.getApiKey();

            if (apiKey == null) {
                cxt.setServiceError("Missing the required parameter 'api key'");
            }

            ApiClient apiClient = cxt.getApiClient();

            apiClient.setBasePath(authBaseUriProvider.getUri());

            Map<String, String> queryParams = new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;

                {
                    put("login", login);
                    put("apiKey", apiKey);
                }
            };

            Map<String, String> headerParams = new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;

                {
                    put("Content-Type", "text/plain");
                }
            };

            ApiResponse<ResponseSid> resp = apiClient.submitHttpRequest("/v5.6/authenticate-by-pass", "POST", queryParams, pass, headerParams, Collections.emptyMap(), ResponseSid.class);

            cxt.setResult(resp.getData().getSid(), SESSION_ID);
        } catch (ApiException x) {
            cxt.setServiceError(x);
        }

        fireAuthenticationEvent(cxt);

        return cxt;
    }

    @Override
    public String authPrefix() {
        return authPrefix;
    }

    private <T> QueryContext<T> createQueryContext() {
        QueryContext<T> cxt = new QueryContext<>("loginAndPasswordAuthenticationProvider");
        cxt.setApiClient(new ApiClient());
        cxt.setServiceBaseUri(authBaseUriProvider.getUri());
        cxt.setApiKeyProvider(apiKeyProvider);
        cxt.configureApiClient();
        return cxt;
    }
}
