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
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.RequestBody;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.Pair;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.auth.ApiKeyAuth;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.auth.Authentication;

/**
 * @author AlexS
 */
public class ApiClient extends ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiClient implements HttpClient {

    public ApiClient() {
        super();
    }

    @Override
    public HttpClient setServiceBaseUri(String uri) {
        super.setBasePath(uri);
        return this;
    }

    @Override
    public HttpClient acceptAccessToken(String authPrefix, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            Authentication apiKeyAuth = getAuthentication("auth.sid");
            if (apiKeyAuth != null && apiKeyAuth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) apiKeyAuth).setApiKey(sessionId);
                ((ApiKeyAuth) apiKeyAuth).setApiKeyPrefix(authPrefix);
            }
        }
        return this;
    }
    
    @Override
    public HttpClient acceptApiKey(String apiKey) {
        if (apiKey != null && !apiKey.isEmpty()) {
            Authentication apiKeyAuth = getAuthentication("apiKey");
            if (apiKeyAuth != null && apiKeyAuth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) apiKeyAuth).setApiKey(apiKey);
            }
        }
        return this;
    }
    
    
    /**
     * Serialize the given Java object into request body according to the object's class and the request Content-Type.
     *
     * @param obj The Java object
     * @param contentType The request Content-Type
     * @return The serialized request body
     * @throws ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException a swagger Exception
     */
    @Override
    public RequestBody serialize(Object obj, String contentType) throws ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException {
        if (obj instanceof byte[]) {
            // Binary (byte array) body parameter support.
            return RequestBody.create(MediaType.parse(contentType), (byte[]) obj);
        }
        else if (obj instanceof String) {
            // File body parameter support.
            return RequestBody.create(MediaType.parse(contentType), (String) obj);
        }
        else if (obj instanceof File) {
            // File body parameter support.
            return RequestBody.create(MediaType.parse(contentType), (File) obj);
        }
        else if (isJsonMime(contentType)) {
            String content;
            if (obj != null) {
                content = getJSON().serialize(obj);
            }
            else {
                content = null;
            }
            return RequestBody.create(MediaType.parse(contentType), content);
        }
        else {
            throw new ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException("Content type \"" + contentType + "\" is not supported");
        }
    }

    @Override
    public <T> ApiResponse<T> submitHttpRequest(String httpRequestUri, String httpMetod, Map<String, String> queryParams, Object body, Map<String, String> headerParams, Map<String, Object> formParams, Class<T> dtoClass) throws ApiException {
        try {
            String[] localVarAuthNames = new String[]{"apiKey", "auth.sid"};

            List<Pair> params
                = queryParams
                    .entrySet()
                    .stream()
                    .map(e -> new Pair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());

            Call call = buildCall(httpRequestUri, httpMetod, params, body, headerParams, formParams, localVarAuthNames, null);

            ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiResponse<T> resp = this.execute(call, dtoClass);

            return new ApiResponse<>(resp.getStatusCode(), resp.getHeaders(), resp.getData());
        }
        catch (ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException x) {
            throw new ApiException(x.getMessage(), x.getCause(), x.getCode(), x.getResponseHeaders(), x.getResponseBody());
        }
    }

    public void setReadTimeout(int milliseconds) {
        getHttpClient().setReadTimeout(milliseconds, TimeUnit.MILLISECONDS);
    }
}
