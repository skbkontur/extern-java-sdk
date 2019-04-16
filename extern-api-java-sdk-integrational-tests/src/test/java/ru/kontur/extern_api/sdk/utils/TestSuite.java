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

package ru.kontur.extern_api.sdk.utils;

import static ru.kontur.extern_api.sdk.ExternEngineBuilder.createExternEngine;
import static ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder.createFor;

import com.google.gson.Gson;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeyOrAuth;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.api.EasyDocflowApi;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.portal.AuthApi;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.auth.PasswordAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;

public class TestSuite {

    public final ExternEngine engine;

    public Configuration getConfig() {
        return config;
    }

    private final Configuration config;
    private final Gson gson;

    private TestSuite(ExternEngine engine, Configuration config) {
        this.config = config;
        this.engine = engine;
        this.gson = GsonProvider.getPreConfiguredGsonBuilder().setPrettyPrinting().create();
    }

    public String serialize(Object obj) {
        return gson.toJson(obj);
    }

    public static TestSuite Load(Consumer<Configuration> configOverride) {

        Configuration config = TestConfig.LoadConfigFromEnvironment();
        configOverride.accept(config);

        ExternEngine engine = ExternEngineBuilder
                .createExternEngine(config)
                .apiKey(config.getApiKey())
                .buildAuthentication(
                        config.getAuthBaseUri(),
                        builder -> builder.passwordAuthentication(
                                config.getLogin(), config.getPass())
                )
                .cryptoProvider(new CryptoProviderMSCapi())
                .doNotSetupAccount()
                .build(Level.BODY);

        engine.getConfiguration().setThumbprint(config.getThumbprint());
        return new TestSuite(engine, config);
    }

    public static TestSuite LoadManually(
            BiFunction<Configuration, ApiKeyOrAuth, ExternEngine> buildFunc
    ) {
        Configuration config = TestConfig.LoadConfigFromEnvironment();
        return new TestSuite(buildFunc.apply(config, createExternEngine(config)), config);
    }

    public CompletableFuture<EasyDocflowApi> GetEasyDocflowApi() {
        return GetEasyDocflowApi(config.getServiceBaseUri(), config.getAuthBaseUri());
    }

    public CompletableFuture<EasyDocflowApi> GetEasyDocflowApi(String baseUri) {
        return GetEasyDocflowApi(baseUri, config.getAuthBaseUri());
    }

    public CompletableFuture<EasyDocflowApi> GetEasyDocflowApi(String baseUri, String authUri) {
        return AuthenticationProviderBuilder.createFor(authUri, Level.BODY)
                .withApiKey(config.getApiKey())
                .passwordAuthentication(config.getLogin(), config.getPass()).authenticate()
                .thenApply(sessionResponse -> new KonturConfiguredClient(Level.BODY, baseUri)
                        .setApiKey(config.getApiKey())
                        .setAuthSid(sessionResponse.getSid())
                        .setConnectTimeout(600, TimeUnit.SECONDS)
                        .setReadTimeout(600, TimeUnit.SECONDS)
                        .createApi(EasyDocflowApi.class));
    }

    public static TestSuite Load() {
        return Load(c -> {
        });
    }
}