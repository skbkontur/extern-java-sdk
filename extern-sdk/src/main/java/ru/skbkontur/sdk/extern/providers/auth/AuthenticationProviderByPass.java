/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SESSION_ID;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.providers.UriProvider;
import ru.skbkontur.sdk.extern.providers.LoginAndPasswordProvider;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiResponse;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class AuthenticationProviderByPass extends  AuthenticationProviderAbstract {

	private ApiKeyProvider apiKeyProvider;
	private LoginAndPasswordProvider loginAndPasswordProvider;
	private UriProvider authBaseUriProvider;
	private final String authPrefix;
	
    @SuppressWarnings("WeakerAccess")
	public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider, String authPrefix) {
		this.authBaseUriProvider = authBaseUriProvider;
		this.loginAndPasswordProvider = loginAndPasswordProvider;
		this.apiKeyProvider = apiKeyProvider;
		this.authPrefix = authPrefix == null ? DEFAULT_AUTH_PREFIX : authPrefix;
	}

	public AuthenticationProviderByPass(UriProvider authBaseUriProvider, LoginAndPasswordProvider loginAndPasswordProvider, ApiKeyProvider apiKeyProvider) {
		this(authBaseUriProvider,loginAndPasswordProvider,apiKeyProvider,DEFAULT_AUTH_PREFIX);
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
		QueryContext<T> cxt = new QueryContext<>("loginAndPasswordAuthenticationProvider");
		cxt.setApiClient(new ApiClient());
		cxt.setServiceBaseUri(authBaseUriProvider.getUri());
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}
}
