/*
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
 *
 */

package ru.kontur.extern_api.sdk.httpclient;

import com.google.gson.Gson;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.HttpUrl.Builder;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.internal.http.HttpMethod;
import okio.Buffer;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.PublicDateFormat;
import ru.kontur.extern_api.sdk.SerializationProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;

public class KonturHttpClient implements HttpClient {

    private static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";

    private static final MediaType mimeJson = MediaType.parse(DEFAULT_CONTENT_TYPE);
    private static final MediaType mimeOctetStream = MediaType.parse(OCTET_STREAM_CONTENT_TYPE);
    private static final MediaType mimePlain = MediaType.parse("text/plain");


    private final KonturConfiguredClient client;
    private SerializationProvider gsonProvider;
    private final LibapiResponseConverter responseConverter;

    private UserAgentProvider userAgentProvider;
    private String serviceBaseUri;

    public KonturHttpClient(KonturConfiguredClient client, GsonProvider gsonProvider) {
        this.client = client;
        this.gsonProvider = gsonProvider;
        this.responseConverter = new LibapiResponseConverter();
        this.serviceBaseUri = "";
    }

    @Override
    public HttpClient setServiceBaseUri(String serviceBaseUrl) {
        if (!serviceBaseUrl.isEmpty() && !serviceBaseUrl.endsWith("/")) {
            serviceBaseUrl += "/";
        }
        this.serviceBaseUri = serviceBaseUrl;
        return this;
    }

    @Override
    public HttpClient acceptAccessToken(String sessionId) {
        client.setAuthSidSupplier(() -> sessionId);
        return this;
    }

    @Override
    public HttpClient acceptApiKey(String apiKey) {
        client.setApiKeySupplier(() -> apiKey);
        return this;
    }

    @Override
    public void setConnectWaiting(int millisecond) {
        client.setConnectTimeout(millisecond, TimeUnit.MILLISECONDS);
    }

    @Override
    public void setReadTimeout(int millisecond) {
        client.setReadTimeout(millisecond, TimeUnit.MILLISECONDS);
    }

    @Override
    public HttpClient setUserAgentProvider(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
        return this;
    }

    @Override
    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }

    @Override
    public <T> ApiResponse<T> submitHttpRequest(
            String httpRequestUri,
            String httpMethod,
            Map<String, Object> queryParams,
            Object body,
            Map<String, String> headerParams,
            Map<String, Object> formParams,
            Type type
    ) throws ApiException {

        if (queryParams == null) {
            queryParams = Collections.emptyMap();
        }
        if (headerParams == null) {
            headerParams = Collections.emptyMap();
        }

        client.setUserAgentSupplier(() -> Optional
                .ofNullable(userAgentProvider)
                .map(UserAgentProvider::getUserAgent)
                .orElse(null)
        );

        String urlPath = serviceBaseUri + httpRequestUri;
        Builder queryBuilder = Optional.ofNullable(HttpUrl.parse(urlPath))
                .orElseThrow(() -> incorrectUrl(urlPath))
                .newBuilder();

        for (Entry<String, Object> entry : queryParams.entrySet()) {
            queryBuilder.addQueryParameter(
                    entry.getKey(),
                    parameterToString(entry.getValue())
            );
        }

        Request.Builder request = new Request.Builder()
                .method(httpMethod, body(body, httpMethod))
                .url(queryBuilder.build())
                .headers(Headers.of(headerParams));

        OkHttpClient httpClient = client.getHttpBuilder().build();

        try {
            Call call = httpClient.newCall(request.build());
            retrofit2.Response<T> response = parseResponse(call.execute(), type);
            return responseConverter.toApiResponse(getGson(), response);
        } catch (IOException e) {
            throw new ApiException(e);
        }
    }

    @Override
    public HttpClient setGson(Gson gson) {
        gsonProvider = () -> gson;
        return this;
    }

    @Override
    public Gson getGson() {
        return gsonProvider.getGson();
    }

    private RequestBody body(Object body, String httpMethod) {
        if (body == null) {
            if (HttpMethod.requiresRequestBody(httpMethod)) {
                return RequestBody.create(null, "");
            }
            return null;
        }
        if (body instanceof byte[]) {
            return RequestBody.create(mimeOctetStream, ((byte[]) body));
        }
        if (body instanceof String) {
            return RequestBody.create(mimePlain, ((String) body));
        }
        return RequestBody.create(mimeJson, getGson().toJson(body));
    }

    private ApiException incorrectUrl(String url) {
        return new ApiException("url `" + url + "` isn't a well-formed HTTP or HTTPS URL");
    }

    /**
     * Format the given parameter object into string.
     *
     * @param param Parameter
     * @return String representation of the parameter
     */
    private String parameterToString(Object param) {
        if (param == null) {
            return "";
        } else if (param instanceof Date) {
            return PublicDateFormat.formatDatetime((Date) param);
        }
        return String.valueOf(param);
    }

    private <T> retrofit2.Response<T> parseResponse(Response rawResponse, Type returnType)
            throws IOException {
        ResponseBody rawBody = Optional
                .ofNullable(rawResponse.body())
                .orElseGet(() -> ResponseBody.create(null, ""));

        int code = rawResponse.code();
        if (code < 200 || code >= 300) {
            try (ResponseBody body = rawBody) {
                // Buffer the entire body to avoid future I/O.
                ResponseBody bufferedBody = buffer(body);
                return retrofit2.Response.error(bufferedBody, rawResponse);
            }
        }

        if (code == 204 || code == 205 || Void.class.equals(returnType)) {
            rawBody.close();
            return retrofit2.Response.success(null, rawResponse);
        }

        T deserialized;
        MediaType contentType = rawBody.contentType();
        if (Objects.equals(contentType, mimeJson)) {
            deserialized = getGson().fromJson(rawBody.string(), returnType);

        } else if (Objects.equals(contentType, mimeOctetStream) && byte[].class.equals(returnType)) {
            @SuppressWarnings("unchecked")
            T value = (T) rawBody.bytes();
            deserialized = value;

        } else if (Objects.equals(returnType, String.class)) {
            @SuppressWarnings("unchecked")
            T value = (T) rawBody.string();
            deserialized = value;

        } else {
            // fallback to mimeJson
            deserialized = getGson().fromJson(rawBody.string(), returnType);
        }

        return retrofit2.Response.success(deserialized, rawResponse);
    }

    private static ResponseBody buffer(final ResponseBody body) throws IOException {
        Buffer buffer = new Buffer();
        body.source().readAll(buffer);
        return ResponseBody.create(body.contentType(), body.contentLength(), buffer);
    }
}
