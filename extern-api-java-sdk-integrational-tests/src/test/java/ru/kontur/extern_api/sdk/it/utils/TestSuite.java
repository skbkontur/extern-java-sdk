package ru.kontur.extern_api.sdk.it.utils;

import static ru.kontur.extern_api.sdk.ExternEngineBuilder.createExternEngine;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.logging.LogManager;
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
        // because I like it ^)
        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "[%1$tF %1$tT] [%4$-7s] %5$s %n"
        );
        try {
            LogManager.getLogManager().readConfiguration();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public static class Secrets {

    }
}
