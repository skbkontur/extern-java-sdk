/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthBaseUriProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.LoginAndPasswordAuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.adaptors.ServiceErrorImpl;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SESSION_ID;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiResponse;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.Pair;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ProgressRequestBody;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ProgressResponseBody;

/**
 *
 * @author AlexS
 */
public class AuthenticationProviderByPass implements AuthenticationProvider {

	private final ApiClient apiClient;
	private ApiKeyProvider apiKeyProvider;
	private LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider;
	private AuthBaseUriProvider authBaseUriProvider;
	private String authPrefix;

	public AuthenticationProviderByPass() {
		this.apiClient = new ApiClient();
	}

	public AuthenticationProviderByPass(AuthBaseUriProvider authBaseUriProvider, LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider, ApiKeyProvider apiKeyProvider, String authPrefix) {
		this();
		this.authBaseUriProvider = authBaseUriProvider;
		this.loginAndPasswordAuthenticationProvider = loginAndPasswordAuthenticationProvider;
		this.apiKeyProvider = apiKeyProvider;
		this.authPrefix = authPrefix;
	}

	public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
		this.apiKeyProvider = apiKeyProvider;
	}

	public void setLoginAndPasswordAuthenticationProvider(LoginAndPasswordAuthenticationProvider loginAndPasswordAuthenticationProvider) {
		this.loginAndPasswordAuthenticationProvider = loginAndPasswordAuthenticationProvider;
	}

	public void setAuthBaseUriProvider(AuthBaseUriProvider authBaseUriProvider) {
		this.authBaseUriProvider = authBaseUriProvider;
	}

	@Override
	public QueryContext<String> sessionId() {
		apiClient.setBasePath(authBaseUriProvider.getAuthBaseUri());
		return authenticateWithHttpInfo(loginAndPasswordAuthenticationProvider.getLogin(), loginAndPasswordAuthenticationProvider.getPass());
	}

	@Override
	public String authPrefix() {
		return authPrefix;
	}
	
	/**
	 * @param login String
	 * @param pass String
	 * @return ApiResponse&lt;Object&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	private QueryContext<String> authenticateWithHttpInfo(String login, String pass) {
		try {
			com.squareup.okhttp.Call call = authenticateValidateBeforeCall(login, pass, null, null);
			Type localVarReturnType = new TypeToken<ResponseSid>() {}.getType();
			ApiResponse<ResponseSid> result = apiClient.execute(call, localVarReturnType);
			return new QueryContext().setResult(result.getData().getSid(),SESSION_ID);
		}
		catch (ApiException x) {
			return new QueryContext().setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.auth, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	@SuppressWarnings("rawtypes")
	private com.squareup.okhttp.Call authenticateValidateBeforeCall(String login, String pass, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

		// verify the required parameter 'billingAccountId' is set
		if (login == null) {
			throw new ApiException("Missing the required parameter 'login' when calling getSid(Async)");
		}

		// verify the required parameter 'clientInfo' is set
		if (pass == null) {
			throw new ApiException("Missing the required parameter 'pass' when calling getSid(Async)");
		}

		com.squareup.okhttp.Call call = authenticateCall(login, pass, progressListener, progressRequestListener);

		return call;

	}

	public com.squareup.okhttp.Call authenticateCall(String login, String pass, final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = pass;

		// create path and map variables
		String localVarPath = "/v5.6/authenticate-by-pass";

		List<Pair> localVarQueryParams = new ArrayList<>();

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "login", login));

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "apiKey", apiKeyProvider.getApiKey()));

		Map<String, String> localVarHeaderParams = new HashMap<>();

		Map<String, Object> localVarFormParams = new HashMap<>();
		/*
		final String[] localVarAccepts = {
			"application/json", "text/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}
		 */
		final String[] localVarContentTypes = {"text/plain"};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		if (progressListener != null) {
			apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
				@Override
				public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
					com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder()
						.body(new ProgressResponseBody(originalResponse.body(), progressListener))
						.build();
				}
			});
		}

		String[] localVarAuthNames = new String[]{};

		return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
	}
}
