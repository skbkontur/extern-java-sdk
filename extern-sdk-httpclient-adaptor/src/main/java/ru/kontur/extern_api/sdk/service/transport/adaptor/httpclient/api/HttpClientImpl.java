/*
 * The MIT License
 *
 * Copyright 2018 alexs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.Map;
import ru.kontur.extern_api.sdk.ServiceLogger;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.httpclient.invoker.HttpClientException;

/**
 * @author alexs
 */
public class HttpClientImpl implements HttpClient {

    private final ru.kontur.extern_api.sdk.service.transport.httpclient.invoker.HttpClientImpl httpClient;

    public HttpClientImpl() {
        this.httpClient = new ru.kontur.extern_api.sdk.service.transport.httpclient.invoker.HttpClientImpl();
        this.httpClient.setJson(GsonProvider.getGson());
    }

    @Override
    public HttpClientImpl setGson(Gson json) {
        httpClient.setJson(json);
        return this;
    }

    @Override
    public HttpClient setServiceBaseUri(String uri) {
        httpClient.setServiceBaseUri(uri);
        return this;
    }

    @Override
    public HttpClient acceptAccessToken(String authPrefix, String sessionId) {
        httpClient.acceptAccessToken(sessionId);
        return this;
    }

    @Override
    public HttpClient acceptApiKey(String apiKey) {
        httpClient.acceptApiKey(apiKey);
        return this;
    }

    @Override
    public <T> ApiResponse<T> submitHttpRequest(String path, String httpMetod,
        Map<String, Object> queryParams, Object body, Map<String, String> headerParams,
        Map<String, Object> formParams, Type type) throws ApiException {
        try {
            ru.kontur.extern_api.sdk.service.transport.httpclient.invoker.ApiResponse<T> resp
                = httpClient
                .sendHttpRequest(path, httpMetod, queryParams, body, headerParams, type);
            Object logHttpRequest = System.getProperties().get("ru.extern_api.logHttpRequest");
            if (logHttpRequest != null && logHttpRequest.equals("true")) {
                ServiceLogger.getInstance()
                    .logHttpRequest(path, httpMetod, queryParams, body, headerParams, formParams,
                        type,
                        resp.getStatusCode(), resp.getHeaders(), resp.getData());
            }
            return new ApiResponse<>(resp.getStatusCode(), resp.getHeaders(), resp.getData());
        } catch (HttpClientException x) {
            throw new ApiException(x.getMessage(), x, x.getCode(), x.getResponseHeaders(),
                x.getResponseBody());
        }
    }

    @Override
    public HttpClient userAgentProvider(UserAgentProvider userAgentProvider) {
        httpClient.setUserAgent(userAgentProvider.getVersion());
        return this;
    }

    @Override
    public void setConnectWaiting(int millisocond) {
        httpClient.setConnectTimeout(millisocond);
    }

    @Override
    public void setReadTimeout(int millisocond) {
        httpClient.setReadTimeout(millisocond);
    }
}
