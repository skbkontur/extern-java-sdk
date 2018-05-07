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

package ru.kontur.extern_api.sdk.providers;

import com.google.gson.Gson;
import ru.kontur.extern_api.sdk.model.AuthenticationErrorCode;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiException;

final public class AuthenticationServiceError implements ServiceError {

    private static final Gson gson = new Gson();

    private final ApiException cause;
    private final String code;

    private AuthenticationServiceError(ApiException cause, String code) {
        this.cause = cause;
        this.code = code;
    }

    @Override
    public ErrorCode getErrorCode() {
        return ErrorCode.auth;
    }

    @Override
    public int getResponseCode() {
        return 403;
    }

    @Override
    public String getMessage() {
        return code;
    }

    @Override
    public Throwable getCause() {
        return cause;
    }

    /**
     * При возникновении ошибки на любом шаге аутентификации, сервер высылает ответ с кодом 403
     * (Forbidden). В теле ответа код ошибки.
     *
     * @throws com.google.gson.JsonSyntaxException – если возвращённый json невозможно распарсить в
     * {@link AuthenticationErrorCode}
     */
    public static AuthenticationServiceError fromAuthenticationException(ApiException e) {

        AuthenticationErrorCode code = gson
                .fromJson(e.getResponseBody(), AuthenticationErrorCode.class);

        return new AuthenticationServiceError(e, code.getCode());
    }
}
