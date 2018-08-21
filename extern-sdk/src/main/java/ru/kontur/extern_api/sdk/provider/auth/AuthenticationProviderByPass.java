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

import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SESSION_ID;

/**
 * Класс реализует аутентификацию по логину и паролю.
 * @author Aleksey Sukhorukov
 */
public class AuthenticationProviderByPass extends AuthenticationProviderAbstract {

    private final String authPrefix;
    private ApiKeyProvider apiKeyProvider;
    private LoginAndPasswordProvider loginAndPasswordProvider;
    private UriProvider authBaseUriProvider;

    private HttpClient httpClient;

    /**
     * Конструктор объекта для аутентификации по логину и паролю
     * @param authBaseUriProvider объект, реализующий {@link UriProvider} интерфейс, возвращающий адрес сервиса аутентификации. В простейшем случае можно использовать лямбда-выражение: {@code ()->"https://...”}`
     * @param loginAndPasswordProvider объект, реализующий {@link LoginAndPasswordProvider} интерфейс, возвращающий логин и пароль пользователя из внешней системы
     * @param apiKeyProvider объект, реализующий {@link ApiKeyProvider} интерфейс, возвращающий идентификатор сервиса, от которого выполняются запросы к внешнему АПИ СКБ Контур. В простейшем случае можно передать лямбда-выражение: {@code ()->"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"}
     * @param authPrefix префикс для запроса аутентификации. Является необязательным параметром. По умолчанию будет использоваться pзначение: auth.sid
     */
    public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider, String authPrefix) {
        this.authBaseUriProvider = authBaseUriProvider;
        this.loginAndPasswordProvider = loginAndPasswordProvider;
        this.apiKeyProvider = apiKeyProvider;
        this.authPrefix = authPrefix == null ? DEFAULT_AUTH_PREFIX : authPrefix;
    }

    /**
     * Конструктор объекта для аутентификации по логину и паролю
     * @param authBaseUriProvider объект, реализующий {@link UriProvider} интерфейс, возвращающий адрес сервиса аутентификации. В простейшем случае можно использовать лямбда-выражение: {@code ()->"https://...”}`
     * @param loginAndPasswordProvider объект, реализующий {@link LoginAndPasswordProvider} интерфейс, возвращающий логин и пароль пользователя из внешней системы
     * @param apiKeyProvider объект, реализующий {@link ApiKeyProvider} интерфейс, возвращающий идентификатор сервиса, от которого выполняются запросы к внешнему АПИ СКБ Контур. В простейшем случае можно передать лямбда-выражение: {@code ()->"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX"}
     */
    public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider) {
        this(authBaseUriProvider, loginAndPasswordProvider, apiKeyProvider, DEFAULT_AUTH_PREFIX);
    }

    @Override
    public AuthenticationProviderByPass httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
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

            httpClient.setServiceBaseUri(authBaseUriProvider.getUri());

            Map<String, Object> queryParams = new HashMap<String, Object>() {
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

            ApiResponse<ResponseSid> resp = httpClient.submitHttpRequest("/authenticate-by-pass", "POST", queryParams, pass, headerParams, Collections.emptyMap(), ResponseSid.class);

            cxt.setResult(resp.getData().getSid(), SESSION_ID);
        }
        catch (ApiException x) {
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
        return 
            new QueryContext<T>("loginAndPasswordAuthenticationProvider")
                .setApiKeyProvider(apiKeyProvider);
    }
}
