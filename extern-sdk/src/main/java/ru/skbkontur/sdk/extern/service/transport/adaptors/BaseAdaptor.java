/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import com.google.gson.Gson;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiResponse;

import java.util.HashMap;
import java.util.Map;


/**
 * @author AlexS
 */
public abstract class BaseAdaptor implements ApiClientAware {

    protected <T> T jsonToDTO(Map<?, ?> response, Class<T> t) {
        Gson gson = new Gson();
        String json = gson.toJson(response);
        return gson.fromJson(json, t);
    }

    protected void prepareTransport(QueryContext<?> cxt) {
        setApiClient(cxt.getApiClient());
    }

    protected <T> T submitHttpRequest(String httpRequestUri, String httpMethod, Object body, Class<T> dtoClass) throws ApiException {
        ApiClient apiClient = getApiClient();

        apiClient.setBasePath("");

        Map<String, String> localQueryParams = new HashMap<>();

        Map<String, String> localHeaderParams = new HashMap<>();

        Map<String, Object> localVarFormParams = new HashMap<>();

        ApiResponse<T> response = apiClient.submitHttpRequest(httpRequestUri, httpMethod, localQueryParams, body, localHeaderParams, localVarFormParams, dtoClass);

        return response.getData();
    }
}
