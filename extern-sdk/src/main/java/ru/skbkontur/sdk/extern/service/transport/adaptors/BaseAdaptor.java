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
