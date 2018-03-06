/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import com.google.gson.Gson;
import java.util.HashMap;
import java.util.Map;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.NOTHING;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiResponse;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.ApiKeyAuth;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.Authentication;

/**
 *
 * @author AlexS
 */
public abstract class BaseAdaptor implements ApiClientAware {

	protected <T> T jsonToDTO(Map<?, ?> response, Class<T> t) {
		Gson gson = new Gson();
		String json = gson.toJson(response);
		return gson.fromJson(json, t);
	}

	protected void acceptAccessToken(ApiClient apiClient, QueryContext<?> cxt) { //String apiKeyPrefix, String accessToken) {
		if (cxt.getSessionId() != null && !cxt.getSessionId().isEmpty()) {
			Authentication apiKeyAuth = apiClient.getAuthentication("auth.sid");
			if (apiKeyAuth != null) {
				((ApiKeyAuth) apiKeyAuth).setApiKey(cxt.getSessionId());
				((ApiKeyAuth) apiKeyAuth).setApiKeyPrefix(cxt.getAuthenticationProvider().authPrefix());
			}
		}
	}

	protected void prepareTransport(QueryContext<?> cxt) {
		ApiClient apiClient = cxt.getApiClient();
		acceptAccessToken(apiClient, cxt);
		setApiClient(apiClient);
	}

	protected <T> T submitHttpRequest(String httpRequestUri, String httpMethod, Object body, Class<T> dtoClass) throws ApiException {
		Map<String, String> localQueryParams = new HashMap<>();

		Map<String, String> localHeaderParams = new HashMap<>();

		Map<String, Object> localVarFormParams = new HashMap<>();

		ApiResponse<T> response = getApiClient().submitHttpRequest(httpRequestUri, httpMethod, localQueryParams, body, localHeaderParams, localVarFormParams, dtoClass);

		return response.getData();
	}
}
