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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.provider.auth;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import java.util.List;
import java.util.Map;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;

final public class AuthenticationServiceError implements ServiceError {

    private static final Gson GSON = new Gson();

    private final ApiException cause;
    private final int code;
    private final String message;
    private final Map<String, List<String>> responseHeaders;
    private final String responseBody;

    private AuthenticationServiceError(int code, String message, String responseBody, Map<String, List<String>> responseHeaders, ApiException cause) {
        this.code = code;
        this.message = message;
        this.responseBody = responseBody;
        this.responseHeaders = responseHeaders;
        this.cause = cause;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.auth;
    }

    @Override
    public int getResponseCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * При возникновении ошибки на любом шаге аутентификации, сервер высылает ответ с кодом 403
     * (Forbidden). В теле ответа код ошибки.
     *
     * @param e ApiException исключение транспортного уровня
     * @return AuthenticationServiceError объект, реализующий интерфейс ServiceError
     * @throws com.google.gson.JsonSyntaxException – если возвращённый json невозможно распарсить в
     * {@link AuthenticationErrorCode}
     */
    public static AuthenticationServiceError fromAuthenticationException(ApiException e) {

        try {
            GSON.fromJson(e.getResponseBody(), Map.class);
        
            String code = (String)GSON.fromJson(e.getResponseBody(), Map.class).get("Code");

        
            return new AuthenticationServiceError(e.getCode(), code, e.getResponseBody(), e.getResponseHeaders(), e);
        }
        catch (JsonSyntaxException x) {
            return new AuthenticationServiceError(e.getCode(), e.getResponseBody(), e.getResponseBody(), e.getResponseHeaders(), e);
        }
    }

    @Override
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    @Override
    public String getResponseBody() {
        return responseBody;
    }
}
