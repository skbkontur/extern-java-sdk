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
import java.util.function.Supplier;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TokenInterceptor implements Interceptor {

    private final TokenLocation location;
    private final String paramName;

    private Supplier<String> tokenSupplier;

    public TokenInterceptor(TokenLocation location, String paramName) {
        this.location = location;
        this.paramName = paramName;
    }

    public TokenLocation getLocation() {
        return location;
    }

    public String getParamName() {
        return paramName;
    }

    public Supplier<String> getTokenSupplier() {
        return tokenSupplier;
    }

    /**
     * @param tokenSupplier token supplier function or null to disable authorization
     */
    public void supplyTokenBy(@Nullable Supplier<String> tokenSupplier) {
        this.tokenSupplier = tokenSupplier;
    }

    public boolean isDisabled() {
        return tokenSupplier == null;
    }

    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {
        Request request = chain.request();

        if (tokenSupplier == null) {
            return chain.proceed(request);
        }

        String token = tokenSupplier.get();

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
