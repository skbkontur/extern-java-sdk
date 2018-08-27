/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
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
package ru.kontur.extern_api.sdk.service.transport.adaptor;

import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.util.Map;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;

/**
 * @author Aleksey Sukhorukov
 */
public interface HttpClient {
    HttpClient setServiceBaseUri(String uri);
    HttpClient acceptAccessToken(String authPrefix, String sessionId);
    HttpClient acceptApiKey(String apiKey);
    void setConnectWaiting(int millisocond);
    void setReadTimeout(int millisocond);
    HttpClient userAgentProvider(UserAgentProvider userAgentProvider);
    <T> ApiResponse<T> submitHttpRequest(
            String httpRequestUri,
            String httpMetod,
            Map<String, Object> queryParams,
            Object body,
            Map<String, String> headerParams,
            Map<String, Object> formParams,
            Type type)
            throws ApiException;

    HttpClient setGson(Gson gson);
    Gson getGson();
}
