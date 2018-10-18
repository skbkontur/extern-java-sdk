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

import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TokenAuth implements Interceptor {

    private final TokenLocation location;
    private final String paramName;

    private String token;

    public TokenAuth(TokenLocation location, String paramName) {
        this.location = location;
        this.paramName = paramName;
    }

    public TokenLocation getLocation() {
        return location;
    }

    public String getParamName() {
        return paramName;
    }

    public String getToken() {
        return token;
    }

    /**
     * @param token token or null to disable authorization
     */
    public void setToken(@Nullable String token) {
        this.token = token;
    }

    public boolean isDisabled() {
        return token == null;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        if (token == null) {
            return chain.proceed(request);
        }

        switch (location) {

            case QUERY:
                request = request.newBuilder()
                        .url(request.url().newBuilder()
                                .addQueryParameter(paramName, token)
                                .build()
                        )
                        .build();
                break;

            case HEADER:
                request = request.newBuilder()
                        .addHeader(paramName, token)
                        .build();
                break;
        }

        return chain.proceed(request);
    }
}
