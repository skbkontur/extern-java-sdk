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
package ru.kontur.extern_api.sdk.service.transport.httpclient.invoker;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;
import ru.kontur.extern_api.sdk.PublicDateFormat;
import ru.kontur.extern_api.sdk.httpclient.invoker.HttpClientLogger;
import ru.kontur.extern_api.sdk.httpclient.invoker.HttpClientLogger.LogLevel;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;

/**
 * @author alexs
 */
public class HttpClientImpl {

    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTH_PREFIX = "auth.sid ";
    private static final String APIKEY = "X-Kontur-Apikey";
    private static final String USER_AGENT = "User-Agent";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String DEFAULT_CONTENT_TYPE = "application/json; charset=utf-8";
    private static final String OCTET_STREAM_CONTENT_TYPE = "application/octet-stream";
    private static final Charset DEFAULT_CHARSET = Charset.forName("utf-8");

    private static final Logger log = Logger.getLogger(HttpClientImpl.class.getName());

    private final Map<String, String> defaultHeaderParams = new ConcurrentHashMap<>();

    private String serviceBaseUri;

    private int connectTimeout;
    private int readTimeout;
    private Gson json;
    private boolean keepAlive;
    private UserAgentProvider userAgentProvider;
    private HttpClientLogger clientLogger;

    public HttpClientImpl() {
        this.connectTimeout = 60_000;
        this.readTimeout = 60_000;
        this.keepAlive = Boolean.valueOf(System.getProperty("http.keepalive", "true"));
        this.clientLogger = new HttpClientLogger(LogLevel.BODY);
    }

    public HttpClientImpl setKeepAlive(boolean keepAlive) {
        this.keepAlive = keepAlive;
        return this;
    }

    public void setUserAgentProvider(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }

    public HttpClientImpl setServiceBaseUri(String serviceBaseUri) {
        this.serviceBaseUri = serviceBaseUri;
        return this;
    }

    private Map<String, String> getDefaultHeaderParams() {
        return this.defaultHeaderParams;
    }

    public HttpClientImpl acceptAccessToken(String sessionId) {
        Map<String, String> defaultHeaderParams = getDefaultHeaderParams();
        defaultHeaderParams.put(AUTHORIZATION, AUTH_PREFIX + sessionId);
        return this;
    }

    public HttpClientImpl acceptApiKey(String apiKey) {
        Map<String, String> defaultHeaderParams = getDefaultHeaderParams();
        defaultHeaderParams.put(APIKEY, apiKey);
        return this;
    }

    public int getConnectTimeout() {
        return connectTimeout;
    }

    public void setConnectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public HttpClientImpl setJson(Gson json) {
        this.json = json;
        return this;
    }

    public Gson getJson() {
        return json;
    }

    public <T> ApiResponse<T> sendHttpRequest(String path, String method,
            Map<String, Object> queryParams, Object body, Map<String, String> headerParams,
            Type type) throws HttpClientException {

        clientLogger.setLogLevel(HttpClientLogger.logLevelFromSystemProperties());

        try {
            URL url = buildUrl(path, queryParams);

            HttpURLConnection connect = (HttpURLConnection) url.openConnection();

            // setup default headers
            getDefaultHeaderParams().forEach(connect::setRequestProperty);

            Map<String, String> newHeaders = new HashMap<>(headerParams);

            newHeaders.putIfAbsent(CONTENT_TYPE, guessContentType(body));

            if (userAgentProvider != null) {
                newHeaders.putIfAbsent(USER_AGENT, userAgentProvider.getVersion());
            }

            newHeaders.forEach(connect::setRequestProperty);

            connect.setRequestProperty("Connection", (keepAlive ? "keep-alive" : "close"));
            connect.setConnectTimeout(connectTimeout);
            connect.setReadTimeout(readTimeout);
            connect.setRequestMethod(method);
            connect.setAllowUserInteraction(false);
            connect.setUseCaches(false);
            connect.setDefaultUseCaches(false);

            Map<String, List<String>> requestProperties = connect.getRequestProperties();

            if (method.equalsIgnoreCase("POST") || method.equalsIgnoreCase("PUT")) {
                connect.setDoOutput(true);
                byte[] preparedBody = acquireBodyAsByteArray(body, headerParams.get(CONTENT_TYPE));
                // todo: handle auth errors when fixed streaming enabled.
                // connect.setFixedLengthStreamingMode(preparedBody.length);
                // write request body
                try (OutputStream outputStream = connect.getOutputStream()) {
                    writeData(preparedBody, outputStream);
                }
            }

            HashMap<String, List<String>> headers = new HashMap<>(requestProperties);
            for (Entry<String, String> entry : getDefaultHeaderParams().entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                headers.put(key, Collections.singletonList(value));
            }
            clientLogger.with(log, json).logRequest(url, method, headers, body);

            // read server answer
            int responseCode = connect.getResponseCode();
            String responseMessage = connect.getResponseMessage();
            Map<String, List<String>> responseHeaders = connect.getHeaderFields();
            byte[] responseData = readResponse(connect);
            MediaType mediaType = getContentType(responseHeaders);

            Response response = new Response(
                    responseCode,
                    responseMessage,
                    responseHeaders,
                    mediaType,
                    responseData
            );
            connect.disconnect();

            clientLogger.with(log, json).logResponse(url, method, responseHeaders, responseData);

            if (responseCode >= 200 && responseCode < 300) {
                return new ApiResponse<>(responseCode, responseMessage, responseHeaders,
                        deserialize(response, type));
            } else if (responseCode >= 300 && responseCode < 400) {
                // process redirect
                String redirectToUrl = connect.getHeaderField("Location");
                if (redirectToUrl == null || redirectToUrl.isEmpty()) {
                    throw new HttpClientException("Redirect address is empty", responseCode,
                            responseHeaders, response.bodyToString());
                }
                return sendHttpRequest(redirectToUrl, method, queryParams, body, headerParams,
                        type);
            } else {
                // error 400 - 500
                throw new HttpClientException(responseMessage, null, responseCode, responseHeaders,
                        response.bodyToString());
            }
        } catch (HttpRetryException e) {
            throw new HttpClientException(e.responseCode(), e.getLocation());
        } catch (IOException x) {
            throw new HttpClientException(x);
        }
    }

    /**
     * Deserialize response body to Java object, according to the return type and the Content-Type
     * response header.
     *
     * @param <T> Type
     * @param response server response
     * @param returnType The type of the Java object
     * @return The deserialized Java object
     * @throws HttpClientException If fail to deserialize response body, i.e. cannot read
     *         response
     *         body or the Content-Type of the response is not supported.
     */
    @SuppressWarnings("unchecked")
    private <T> T deserialize(Response response, Type returnType) throws HttpClientException {
        if (response.getBody() == null || returnType == null) {
            return null;
        }

        MediaType contentType =
                response.getMediaType() == null ? MediaType.parse(DEFAULT_CONTENT_TYPE)
                        : response.getMediaType();

        assert contentType != null;
        String respBody = new String(response.getBody(), contentType.charset(DEFAULT_CHARSET));

        if (isJsonMime(contentType.toString())) {
            return deserialize(respBody, returnType);
        } else if (returnType.equals(String.class)) {
            // Expecting string, return the raw response body.
            return (T) respBody;
        } else {
            throw new HttpClientException(
                    "Content type \"" + contentType.toString() + "\" is not supported for type: "
                            + returnType,
                    response.getCode(),
                    response.getHeaders(),
                    respBody
            );
        }
    }

    /**
     * Build full URL by concatenating base path, the given sub path and query parameters.
     *
     * @param path The sub path
     * @param queryParams The query parameters
     * @return The full URL
     */
    private URL buildUrl(String path, Map<String, Object> queryParams)
            throws MalformedURLException {
        final StringBuilder request = new StringBuilder();

        request.append(serviceBaseUri).append(path);

        if (queryParams != null && !queryParams.isEmpty()) {
            // support (constant) query string in `path`, e.g. "/posts?draft=1"
            String prefix = path.contains("?") ? "&" : "?";
            for (Map.Entry<String, Object> param : queryParams.entrySet()) {
                if (param.getValue() != null) {

                    String value = parameterToString(param.getValue());

                    if (value != null && !value.isEmpty()) {
                        if (prefix != null) {
                            request.append(prefix);
                            prefix = null;
                        } else {
                            request.append("&");
                        }

                        request.append(escapeString(param.getKey())).append("=")
                                .append(escapeString(value));
                    }
                }
            }
        }

        return new URL(request.toString());
    }

    /**
     * Escape the given string to be used as URL query value.
     *
     * @param str String to be escaped
     * @return Escaped string
     */
    private String escapeString(String str) {
        try {
            return URLEncoder.encode(str, "utf8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            return str;
        }
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
        } else if (param instanceof Collection) {
            StringBuilder b = new StringBuilder();
            ((Collection<?>) param).forEach((o) -> {
                if (b.length() > 0) {
                    b.append(",");
                }
                b.append(String.valueOf(o));
            });
            return b.toString();
        } else {
            return String.valueOf(param);
        }
    }

    private byte[] acquireBodyAsByteArray(Object body, String contentType)
            throws HttpClientException {
        if (body == null) {
            return new byte[0];
        }


        MediaType mediaType = MediaType.parse(contentType);

        if (mediaType == null) {
            throw new HttpClientException("Content type \"" + contentType + "\" is not supported");
        }

        if (body instanceof byte[]) {
            return (byte[]) body;
        } else if (body instanceof String) {
            return ((String) body).getBytes(mediaType.charset(DEFAULT_CHARSET));
        } else if ("json".equalsIgnoreCase(mediaType.subtype())) {
            return json.toJson(body).getBytes(mediaType.charset(DEFAULT_CHARSET));
        } else {
            throw new HttpClientException("Content type \"" + contentType + "\" is not supported");
        }
    }

    private byte[] readResponse(HttpURLConnection connect) throws IOException {
        try (InputStream is = connect.getInputStream()) {
            return readData(is);
        } catch (IOException x) {
            try (InputStream is = connect.getErrorStream()) {
                if (is == null) {
                    throw x;
                }
                return readData(is);
            }
        }
    }

    private byte[] readData(InputStream is) throws IOException {
        final int size = 8192;
        final byte[] buffer = new byte[size];
        int length;
        BufferedInputStream bis = new BufferedInputStream(is, size);
        ByteArrayOutputStream bos = new ByteArrayOutputStream(size);
        while ((length = bis.read(buffer)) != -1) {
            bos.write(buffer, 0, length);
        }
        is.close();
        return bos.toByteArray();
    }

    private void writeData(byte[] data, OutputStream os) throws IOException {
        final int size = 8192;
        BufferedOutputStream bos = new BufferedOutputStream(os, size);
        int offset = 0;
        while (offset < data.length) {
            int length = data.length - offset < size ? data.length - offset : size;
            bos.write(data, offset, length);
            offset += length;
        }
        bos.flush();
    }

    private MediaType getContentType(Map<String, List<String>> headers) {
        MediaType mediaType = null;
        List<String> header = headers.get(CONTENT_TYPE);
        if (header != null && header.size() > 0) {
            mediaType = MediaType.parse(header.get(0));// = responseHeaders
        }
        return mediaType;
    }

    /**
     * Check if the given MIME is a JSON MIME. JSON MIME examples: application/json
     * application/json; charset=UTF8 APPLICATION/JSON application/vnd.company+json
     *
     * @param mime MIME (Multipurpose Internet Mail Extensions)
     * @return True if the given MIME is JSON, false otherwise.
     */
    private boolean isJsonMime(String mime) {
        String jsonMime = "(?i)^(application/json|[^;/ \t]+/[^;/ \t]+[+]json)[ \t]*(;.*)?$";
        return mime != null && (mime.matches(jsonMime) || mime
                .equalsIgnoreCase("application/json-patch+json"));
    }

    /**
     * Deserialize the given JSON string to Java object.
     *
     * @param <T> Type
     * @param body The JSON string
     * @param returnType The type to deserialize into
     * @return The deserialized Java object
     */
    @SuppressWarnings("unchecked")
    public <T> T deserialize(String body, Type returnType) {
        try {
            return json.fromJson(body, returnType);
        } catch (JsonParseException e) {
            // Fallback processing when failed to parse JSON form response body:
            //   return the response body string directly for the String return type;
            //   parse response body into date or datetime for the Date return type.
            if (returnType.equals(String.class)) {
                return (T) body;
            } else if (returnType.equals(Date.class)) {
                return (T) PublicDateFormat.parseDateTime(body);
            } else {
                throw (e);
            }
        }
    }

    private String guessContentType(Object body) {
        if (body instanceof byte[]) {
            return OCTET_STREAM_CONTENT_TYPE;
        }
        return DEFAULT_CONTENT_TYPE;
    }

    private static class Response {

        private int code;
        private String message;
        private Map<String, List<String>> headers;
        private MediaType mediaType;
        private byte[] body;

        Response(int code, String message, Map<String, List<String>> headers, MediaType mediaType,
                byte[] body) {
            this.code = code;
            this.message = message;
            this.headers = headers;
            this.mediaType = mediaType;
            this.body = body;
        }

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Map<String, List<String>> getHeaders() {
            return headers;
        }

        public void setHeaders(Map<String, List<String>> headers) {
            this.headers = headers;
        }

        public MediaType getMediaType() {
            return mediaType;
        }

        public void setMediaType(MediaType mediaType) {
            this.mediaType = mediaType;
        }

        public byte[] getBody() {
            return body;
        }

        public void setBody(byte[] body) {
            this.body = body;
        }

        public String bodyToString() {
            if (body == null || body.length == 0) {
                return "";
            }
            return mediaType == null ? new String(body)
                    : new String(body, mediaType.charset(DEFAULT_CHARSET));
        }
    }
}
