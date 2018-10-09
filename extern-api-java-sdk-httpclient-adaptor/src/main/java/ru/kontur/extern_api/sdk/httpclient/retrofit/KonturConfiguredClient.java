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

package ru.kontur.extern_api.sdk.httpclient.retrofit;

import com.google.gson.GsonBuilder;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import ru.kontur.extern_api.sdk.GsonProvider;

public class KonturConfiguredClient {

    private final TokenAuth apiKeyAuth = new TokenAuth(TokenLocation.HEADER, "X-Kontur-Apikey");
    private final TokenAuth authSidAuth = new TokenAuth(TokenLocation.HEADER, "Authorization");
    private final TokenAuth userAgentAuth = new TokenAuth(TokenLocation.HEADER, "User-Agent");

    private final Retrofit.Builder serviceBuilder;
    private final OkHttpClient.Builder okBuilder;

    private String baseUrl;

    public KonturConfiguredClient(@NotNull Level loggingLevel, @NotNull String baseUrl) {

        this.baseUrl = Objects.requireNonNull(baseUrl, "baseUrl");

        Logger logger = Logger.getLogger(this.getClass().getName());

        this.okBuilder = new OkHttpClient.Builder()
                .addInterceptor(apiKeyAuth)
                .addInterceptor(authSidAuth)
                .addInterceptor(new HttpLoggingInterceptor(logger::info).setLevel(loggingLevel))
                .followRedirects(false)
                .followSslRedirects(false);

        this.serviceBuilder = new Retrofit.Builder()
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonCustomConverterFactory.create(getGsonBuilder().create()))
                .addCallAdapterFactory(Java8CallAdapterFactory.create());
    }

    public KonturConfiguredClient(Level body) {
        this(Level.BODY, "");
    }

    public <S> S createService(Class<S> serviceClass) {
        return serviceBuilder
                .baseUrl(baseUrl)
                .client(okBuilder.build())
                .build()
                .create(serviceClass);
    }

    /**
     * @param apiKey api key or null to disable apiKey authorization
     */
    public KonturConfiguredClient setApiKey(@Nullable String apiKey) {
        apiKeyAuth.setToken(apiKey);
        return this;
    }

    /**
     * @param authSid auth sid or null to disable auth sid authorization
     */
    public KonturConfiguredClient setAuthSid(@Nullable String authSid) {
        authSidAuth.setToken(Optional
                .ofNullable(authSid)
                .map(sid -> "auth.sid " + sid)
                .orElse(null)
        );
        return this;
    }

    /**
     * @param userAgent User agent string or null to disable user agent supply
     */
    public KonturConfiguredClient setUserAgent(@Nullable String userAgent) {
        userAgentAuth.setToken(userAgent);
        return this;
    }

    public KonturConfiguredClient setServiceBaseUrl(String serviceBaseUrl) {
        if (!serviceBaseUrl.endsWith("/")) {
            serviceBaseUrl += "/";
        }
        baseUrl = serviceBaseUrl;
        return this;
    }

    public KonturConfiguredClient setConnectTimeout(long timeout, TimeUnit unit) {
        okBuilder.connectTimeout(timeout, unit);
        return this;
    }

    public KonturConfiguredClient setReadTimeout(long timeout, TimeUnit unit) {
        okBuilder.readTimeout(timeout, unit);
        return this;
    }

    /**
     * @return okBuilder to manual request configuration.
     */
    public OkHttpClient.Builder getHttpBuilder() {
        return okBuilder;
    }

    /**
     * @return gson builder that creates gson with server-acceptable serialization politic
     */
    public GsonBuilder getGsonBuilder() {
        return GsonProvider.getPreConfiguredGsonBuilder();
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
