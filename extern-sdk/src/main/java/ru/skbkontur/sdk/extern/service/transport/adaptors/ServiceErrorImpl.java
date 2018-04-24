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

import ru.skbkontur.sdk.extern.providers.ServiceError;

import java.util.List;
import java.util.Map;


/**
 * @author AlexS
 */
public class ServiceErrorImpl implements ServiceError {

    private final ErrorCode errorCode;

    private final String message;

    private final int responseCode;

    private final Map<String, List<String>> responseHeaders;

    private final String responseBody;

    private final Throwable cause;

    public ServiceErrorImpl(ErrorCode errorCode, String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody, Throwable cause) {
        this.errorCode = errorCode;

        this.message = message;

        this.responseCode = responseCode;

        this.responseHeaders = responseHeaders;

        this.responseBody = responseBody;

        this.cause = cause;
    }

    public ServiceErrorImpl(ErrorCode errorCode, String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody) {
        this(errorCode, message, responseCode, responseHeaders, responseBody, null);
    }

    public ServiceErrorImpl(ErrorCode errorCode) {
        this(errorCode, errorCode.message(), 0, null, null);
    }

    public ServiceErrorImpl(ErrorCode errorCode, String message) {
        this(errorCode, message, 0, null, null);
    }

    public ServiceErrorImpl(String message, int responseCode, Map<String, List<String>> responseHeaders, String responseBody) {
        this(ErrorCode.server, message, responseCode, responseHeaders, responseBody);
    }

    @Override
    public int getResponseCode() {
        return responseCode;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    @Override
    public String toString() {
        final String EOL = "\r\n";
        StringBuilder errorMsg = new StringBuilder("Message error: ").append(message == null ? errorCode.message() : message).append(EOL);
        if (responseCode != 0) {
            errorMsg.append("  Response code: ").append(responseCode).append(EOL);
            if (responseHeaders != null) {
                errorMsg.append("  Headers:").append(EOL);
                responseHeaders.keySet().forEach((k) -> {
                    List<String> values = responseHeaders.get(k);
                    if (values != null) {
                        StringBuilder headerLine = new StringBuilder("    ").append(k).append(": ");
                        values.forEach((v) -> {
                            headerLine.append(v).append("; ");
                        });
                        errorMsg.append(headerLine).append(EOL);
                    }
                });
            }
            if (responseBody != null) {
                String cleanText = responseBody.replaceAll("\n", " ").replaceAll("\r", "");
                errorMsg.append("  Response body: ").append(cleanText).append(EOL);
            }
        }
        return errorMsg.toString();
    }

    @Override
    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
