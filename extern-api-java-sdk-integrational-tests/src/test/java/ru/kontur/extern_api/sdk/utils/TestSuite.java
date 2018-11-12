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

import com.google.gson.Gson;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeyOrAuth;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.GsonProvider;

public class TestSuite {

    public final ExternEngine engine;

    private final Gson gson;

    private TestSuite(ExternEngine engine) {
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
                .doNotUseCryptoProvider()
                .doNotSetupAccount()
                .build();

        return new TestSuite(engine);
    }

    public static TestSuite LoadManually(
            BiFunction<Configuration, ApiKeyOrAuth, ExternEngine> buildFunc
    ) {
        Configuration config = TestConfig.LoadConfigFromEnvironment();
        return new TestSuite(buildFunc.apply(config, createExternEngine(config)));
    }

    public static TestSuite Load() {
        return Load(c -> {
        });
    }

}
