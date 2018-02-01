/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.skbkontur.sdk.extern.rest.invoker.ApiClient;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiResponse;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.Pair;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ProgressRequestBody;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ProgressResponseBody;
import ru.skbkontur.sdk.extern.rest.model.ResponseSid;

/**
 *
 * @author AlexS
 */
public class AuthenticationApi {

	private final ApiClient apiClient;
	private final String apiKey;

	public AuthenticationApi(String apiKey) {
		this.apiClient = new ApiClient();
		this.apiKey = apiKey;
	}
	
	

	public String getSid(String login, String pass, String authUri) throws ApiException {
		apiClient.setBasePath(authUri);
		ApiResponse<ResponseSid> resp = authenticateWithHttpInfo(login, pass);
		return resp.getData().getSid();
	}

	/**
	 * @param login String 
	 * @param pass String 
	 * @return ApiResponse&lt;Object&gt;
	 * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the response body
	 */
	public ApiResponse<ResponseSid> authenticateWithHttpInfo(String login, String pass) throws ApiException {
		com.squareup.okhttp.Call call = authenticateValidateBeforeCall(login, pass, null, null);
		Type localVarReturnType = new TypeToken<ResponseSid>() {
		}.getType();
		return apiClient.execute(call, localVarReturnType);
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

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "apiKey", apiKey));
		
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
		final String[] localVarContentTypes = {
			"text/plain"
		};
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
