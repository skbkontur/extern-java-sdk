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

import com.google.gson.Gson;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import okhttp3.logging.HttpLoggingInterceptor.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import retrofit2.Retrofit;

public class KonturConfiguredClient {

    private static final String PRIMARY_AUTH_SCHEME = "auth.sid ";

    // default interceptors

    private final TokenInterceptor timeoutToken = new TokenInterceptor(
            TokenLocation.HEADER,
            "X-Kontur-Request-Timeout"
    );
    private final TokenInterceptor apiKeyToken = new TokenInterceptor(
            TokenLocation.HEADER,
            "X-Kontur-Apikey"
    );

    private final TokenInterceptor authenticationToken = new TokenInterceptor(
            TokenLocation.HEADER,
            "Authorization"
    );

    private final TokenInterceptor userAgentToken = new TokenInterceptor(
            TokenLocation.HEADER,
            "User-Agent"
    );


    private final OkHttpClient.Builder okBuilder;
    private final Level loggingLevel;

    private String baseUrl;

    private long connectTimeout;
    private TimeUnit connectTimeoutUnit;

    private long readTimeout;
    private TimeUnit readTimeoutUnit;

    public KonturConfiguredClient(
            @NotNull Level loggingVerbosity,
            @NotNull String baseUrl,
            @NotNull Logger logger
    ) {
        this.loggingLevel = loggingVerbosity;
        setServiceBaseUrl(baseUrl);

        this.okBuilder = new OkHttpClient.Builder()
                .addInterceptor(userAgentToken)
                .addInterceptor(timeoutToken)
                .addInterceptor(new HttpLoggingInterceptor(logger).setLevel(loggingVerbosity))
                .addInterceptor(apiKeyToken)
                .addInterceptor(authenticationToken)
                .followRedirects(false)
                .followSslRedirects(false)
                .retryOnConnectionFailure(false);
    }

    public KonturConfiguredClient(
            @NotNull Level loggingVerbosity,
            @NotNull String baseUrl
    ) {
        this(loggingVerbosity, baseUrl, Logger.DEFAULT);
    }

    public <S> S createApi(Class<S> serviceClass) {

        ResponseConverter responseConverter = createResponseConverter(
                serviceClass.getAnnotation(ApiResponseConverter.class)
        );

        Gson gson = createGson(serviceClass.getAnnotation(JsonSerialization.class));

        return new Retrofit.Builder()
                .addConverterFactory(GsonCustomConverterFactory.create(gson))
                .addCallAdapterFactory(ApiResponseCallAdapterFactory.create(gson, responseConverter))
                .baseUrl(baseUrl)
                .client(okBuilder.build())
                .build()
                .create(serviceClass);
    }

    /**
     * @param apiKeySupplier api key supplier or null to disable apiKey authorization
     */
    public KonturConfiguredClient setApiKeySupplier(@Nullable Supplier<String> apiKeySupplier) {
        apiKeyToken.supplyTokenBy(apiKeySupplier);
        return this;
    }

    /**
     * @param authSidSupplier auth sid supplier or null to disable auth sid authorization
     */
    public KonturConfiguredClient setAuthSidSupplier(@Nullable Supplier<String> authSidSupplier) {
        if (authSidSupplier != null) {
            authenticationToken.supplyTokenBy(() -> PRIMARY_AUTH_SCHEME + authSidSupplier.get());
        }
        return this;
    }

    /**
     * @param userAgentSupplier User agent supplier string or null to disable user agent tracking
     */
    public KonturConfiguredClient setUserAgentSupplier(@Nullable Supplier<String> userAgentSupplier) {
        userAgentToken.supplyTokenBy(userAgentSupplier);
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
        connectTimeout = timeout;
        connectTimeoutUnit = unit;
        okBuilder.connectTimeout(timeout, unit);
        return this;
    }

    public KonturConfiguredClient setReadTimeout(long timeout, TimeUnit unit) {
        readTimeout = timeout;
        readTimeoutUnit = unit;
        okBuilder.readTimeout(timeout, unit);
        timeoutToken.supplyTokenBy(() -> String.valueOf(unit.toMicros(timeout) * 10));
        return this;
    }

    /**
     * @return okBuilder to manual request configuration.
     */
    public OkHttpClient.Builder getHttpBuilder() {
        return okBuilder;
    }

    public String getBaseUrl() {
        return baseUrl;
    }

    public KonturConfiguredClient copy() {
        return new KonturConfiguredClient(loggingLevel, getBaseUrl())
                .setApiKeySupplier(apiKeyToken.getTokenSupplier())
                .setAuthSidSupplier(authenticationToken.getTokenSupplier())
                .setUserAgentSupplier(userAgentToken.getTokenSupplier())
                .setConnectTimeout(connectTimeout, connectTimeoutUnit)
                .setReadTimeout(readTimeout, readTimeoutUnit);
    }


    private ResponseConverter createResponseConverter(@Nullable ApiResponseConverter annotation) {
        if (annotation == null) {
            throw new IllegalStateException(
                    "Service class must be annotated with " + ApiResponseConverter.class.getSimpleName()
                            + " to specify response conversion policy."
            );
        }

        try {
            return annotation.value().getDeclaredConstructor().newInstance();
        } catch (InstantiationException |
                IllegalAccessException |
                InvocationTargetException |
                NoSuchMethodException e) {
            throw new IllegalStateException(
                    "There is no default constructor in " + annotation.value().getSimpleName(), e
            );
        }
    }

    private Gson createGson(@Nullable JsonSerialization annotation) {
        if (annotation == null) {
            throw new IllegalStateException(
                    "Service class must be annotated with " + JsonSerialization.class.getSimpleName()
                            + " to specify json serialization policy."
            );
        }

        return annotation.value().getGson();
    }
}
