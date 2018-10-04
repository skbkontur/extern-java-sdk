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
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.ErrorInfo;


public class ApiResponse<T> {

    private final int statusCode;
    private final Map<String, List<String>> headers;
    private final T data;
    private final ErrorInfo errorInfo;

    private ApiResponse(int code, Map<String, List<String>> headers, T data, ErrorInfo error) {
        this.statusCode = code;
        this.headers = headers;
        this.data = data;
        this.errorInfo = error;
    }

    /**
     * @param statusCode The status code of HTTP response
     * @param headers The headers of HTTP response
     * @param data The object deserialized from response bod
     */
    public ApiResponse(int statusCode, Map<String, List<String>> headers, T data) {
        this(statusCode, headers, data, null);
    }

    /**
     * @param statusCode The status code of HTTP response
     * @param headers The headers of HTTP response
     * @param error Deserialized ErrorInfo
     */
    public ApiResponse(int statusCode, Map<String, List<String>> headers, ErrorInfo error) {
        this(statusCode, headers, null, error);
    }

    public ApiResponse(int statusCode, Map<String, List<String>> headers) {
        this(statusCode, headers, null, null);
    }

    public int getStatusCode() {
        return statusCode;
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }

    @Nullable
    public T getData() {
        return data;
    }

    public boolean isSuccessful() {
        return statusCode >= 200 && statusCode < 300;
    }

    @Nullable
    public ErrorInfo getErrorInfo() {
        return errorInfo;
    }

    /**
     * @return ApiException with info from {@link ApiResponse#getErrorInfo()} or null if {@link
     * ApiResponse#isSuccessful()}
     */
    public ApiException asApiException() {

        if (isSuccessful()) {
            return null;
        }

        ErrorInfo e = errorInfo;

        if (e == null) {
            return new ApiException(getStatusCode(), "unknown error info");
        }

        return new ApiException(
                e.getMessage(),
                e.getThrowable(),
                e.getStatusCode(),
                getHeaders(),
                e.getMessage()
        );
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(200, Collections.emptyMap());
    }

    public static <T> ApiResponse<T> error(int code, ErrorInfo errorInfo) {
        return new ApiResponse<>(code, Collections.emptyMap(), errorInfo);
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setStatusCode(code);
        errorInfo.setId("urn:sdk:clientSideProblem");
        errorInfo.setMessage(message);
        return new ApiResponse<>(code, Collections.emptyMap(), errorInfo);
    }

    public static <T> ApiResponse<T> error(Throwable throwable) {
        ErrorInfo errorInfo = new ErrorInfo();
        errorInfo.setStatusCode(0);
        errorInfo.setId("urn:sdk:clientSideProblem");
        errorInfo.setMessage(throwable.getMessage());
        errorInfo.setThrowable(new RuntimeException(throwable));
        return new ApiResponse<>(0, Collections.emptyMap(), errorInfo);
    }
}
