package ru.kontur.extern_api.sdk.it.utils;

import static ru.kontur.extern_api.sdk.ExternEngineBuilder.createExternEngine;

import com.google.gson.Gson;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeyOrAuth;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.GsonProvider;

public class TestSuite {

    public final ExternEngine engine;

    public final Gson gson;

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
                .authFromConfiguration(config)
                .doNotUseCryptoProvider()
                .doNotSetupAccount()
                .userIpProvider(() -> "80.247.184.194")
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
