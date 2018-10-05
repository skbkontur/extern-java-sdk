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

package ru.kontur.extern_api.sdk.httpclient.invoker;

import com.google.gson.Gson;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.logging.Logger;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

public class HttpClientLogger {

    public static final String PROTOCOL = "HTTP/1.1";

    public static LogLevel logLevelFromSystemProperties() {

        if (System.getProperty("httpclient.debug") == null) {
            return LogLevel.NONE;
        }

        String logLevel = System.getProperty("httpclient.log_level");

        try {
            return LogLevel.valueOf(logLevel);
        } catch (IllegalArgumentException | NullPointerException e) {
            return LogLevel.BODY;
        }
    }

    public enum LogLevel {

        NONE,
        QUERY,
        HEADERS,
        BODY;

        public Optional<String> filterBy(LogLevel level) {
            return this.ordinal() < level.ordinal() ? Optional.of("") : Optional.empty();
        }

    }

    private LogLevel requestLogLevel;

    private LogLevel responseLogLevel;

    public HttpClientLogger(LogLevel logLevel) {
        this.setLogLevel(logLevel);
    }

    public LoggerWorker with(Logger logger) {
        return with(logger, GsonProvider.getGson());
    }

    public LoggerWorker with(Logger logger, Gson gson) {
        return this.new LoggerWorker(logger, gson);
    }

    public LogLevel getRequestLogLevel() {
        return requestLogLevel;
    }

    public void setRequestLogLevel(LogLevel requestLogLevel) {
        this.requestLogLevel = requestLogLevel;
    }

    public LogLevel getResponseLogLevel() {
        return responseLogLevel;
    }

    public void setResponseLogLevel(LogLevel responseLogLevel) {
        this.responseLogLevel = responseLogLevel;
    }

    public void setLogLevel(LogLevel commonLogLevel) {
        this.responseLogLevel = commonLogLevel;
        this.requestLogLevel = commonLogLevel;
    }

    public class LoggerWorker {

        private final Logger logger;
        private final Gson gson;

        private LoggerWorker(Logger logger, Gson gson) {
            this.logger = logger;
            this.gson = gson;
        }

        public void logRequest(
                URL url,
                String method,
                Map<String, List<String>> headers,
                Object body) {

            if (requestLogLevel == LogLevel.NONE) {
                return;
            }

            logger.info(() -> {
                String sQuery = requestLogLevel.filterBy(LogLevel.QUERY)
                        .orElseGet(() -> method + " " + url.toString() + " " + PROTOCOL);

                String sHeaders = requestLogLevel.filterBy(LogLevel.HEADERS)
                        .orElseGet(() -> headersToString(headers));

                String sBody = requestLogLevel.filterBy(LogLevel.BODY)
                        .orElseGet(() -> Optional
                                .ofNullable(body)
                                .map(LoggerWorker.this::serializeBody)
                                .map(s -> "\n" + s)
                                .orElse(""));

                return YAStringUtils.joinIfExists("\n",
                        center(" request "),
                        sQuery, sHeaders, sBody,
                        center(" request ends "));
            });

        }

        public void logResponse(
                URL requestedUrl,
                String requestedMethod,
                Map<String, List<String>> headers,
                byte[] data) {

            if (responseLogLevel == LogLevel.NONE) {
                return;
            }

            logger.info(() -> {

                String sResponseFor = responseLogLevel.filterBy(LogLevel.QUERY)
                        .orElseGet(() -> "RESPONSE FOR " + requestedMethod +
                                " " + requestedUrl.toString());

                String sStatus = responseLogLevel.filterBy(LogLevel.QUERY)
                        .orElseGet(() -> headerLine(headers.get(null)));

                String sHeaders = responseLogLevel.filterBy(LogLevel.HEADERS)
                        .orElseGet(() -> headersToString(headers));

                String sData = responseLogLevel.filterBy(LogLevel.BODY)
                        .orElseGet(() -> Optional.ofNullable(data)
                                .map(String::new)
                                .map(s -> "\n" + s)
                                .orElse(""));

                return YAStringUtils.joinIfExists("\n",
                        center(" response "),
                        sResponseFor, sStatus, sHeaders, sData,
                        center(" response ends "));
            });
        }

        private String serializeBody(Object body) {
            if (body instanceof String) {
                return (String) body;
            } else if (body instanceof byte[]) {
                return new String((byte[]) body);
            }
            return gson.toJson(body);
        }
    }

    private static String headersToString(Map<String, List<String>> headers) {
        StringBuilder sb = new StringBuilder();

        for (Entry<String, List<String>> e : headers.entrySet()) {
            if (e.getKey() == null) {
                continue;
            }

            List<String> values = Optional
                    .ofNullable(e.getValue())
                    .orElse(Collections.emptyList());

            sb.append(e.getKey())
                    .append(": ")
                    .append(headerLine(values))
                    .append('\n');
        }

        return sb.toString().trim();
    }

    private static String headerLine(List<String> headerValueList) {
        return headerValueList == null ? "" : String.join("; ", headerValueList);
    }

    private static String center(String string) {
        return YAStringUtils.center(string, 30, '=');
    }

}
