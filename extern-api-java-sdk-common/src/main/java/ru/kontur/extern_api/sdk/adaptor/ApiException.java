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

package ru.kontur.extern_api.sdk.adaptor;

import java.util.Collections;
import java.util.List;
import java.util.Map;


public class ApiException extends RuntimeException {

    private final int code;
    private final String errorId;
    private final Map<String, List<String>> responseHeaders;

    public ApiException(Throwable throwable) {
        super(
                throwable instanceof ApiException ? throwable.getMessage() : null,
                throwable instanceof ApiException ? null : throwable
        );

        if (throwable instanceof ApiException) {
            ApiException parent = (ApiException) throwable;
            code = parent.getCode();
            errorId = parent.getErrorId();
            responseHeaders = parent.getResponseHeaders();
        } else {
            code = -1;
            errorId = "exception-was-thrown";
            responseHeaders = Collections.emptyMap();
        }
    }

    public ApiException(String message) {
        this(-1, null, message, null, null);
    }

    public ApiException(int code, String message) {
        this(code, null, message, null, null);
    }

    public ApiException(
            int code,
            String errorId,
            String message,
            Map<String, List<String>> responseHeaders,
            Throwable throwable
    ) {
        super(message, throwable);

        this.code = code;
        this.errorId = errorId;
        this.responseHeaders = responseHeaders == null ? Collections.emptyMap() : responseHeaders;
    }

    /**
     * @return HTTP status code
     */
    public int getCode() {
        return code;
    }

    /**
     * @return HTTP status code
     */
    public int getResponseCode() {
        return code;
    }


    /**
     * @return HTTP response headers
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * @return error identifier
     */
    public String getErrorId() {
        return errorId;
    }

    @Override
    public String toString() {
        return prettyPrint(getCode(), getErrorId(), getMessage(), getResponseHeaders());
    }

    private static String prettyPrint(
            int code,
            String errorId,
            String message,
            Map<String, List<String>> responseHeaders
    ) {
        String headers = String.join("\n", responseHeaders
                .entrySet()
                .stream()
                .map(e -> e.getKey() + ": " + String.join(" ", e.getValue().toArray(new String[0])))
                .toArray(String[]::new)
        );

        if (!headers.isEmpty()) {
            headers = "\n" + headers;
        }

        return "ApiException: " + String.valueOf(code) + " " + errorId + ": " + message + headers;
    }
}
