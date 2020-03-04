package ru.kontur.extern_api.sdk.scenario;

import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.utils.TestConfig;

import java.security.cert.CertificateException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static java.util.UUID.fromString;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ContentServiceTests {
    private static ExternEngine engine;
    private static Configuration configuration;
    private final UUID contentId = fromString("0be11b77-c535-4091-bba9-4a3ad089d05e");

    @BeforeAll
    static void setUpClass() throws CertificateException {

        String dssConfigPath = "/secret/extern-sdk-dss-config.json";
        configuration = TestConfig.LoadConfigFromEnvironment(dssConfigPath);

        engine = ExternEngineBuilder.createExternEngine(configuration.getServiceBaseUri())
                .apiKey(configuration.getApiKey())
                .buildAuthentication(configuration.getAuthBaseUri(), ab -> ab
                        .withApiKey(configuration.getApiKey())
                        .passwordAuthentication(configuration.getLogin(), configuration.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(configuration.getAccountId())
                .build(HttpLoggingInterceptor.Level.BODY);
    }

    @Test
    void main() throws ExecutionException, InterruptedException {
        byte[] content = engine.getContentService().downloadAllContent(contentId).get();
        assertNotNull(content);

        byte[] partialContent = engine.getContentService().downloadPartialContent(contentId, 0, 40).get();
        assertNotNull(partialContent);

        byte[] partialContentByLength = engine.getContentService().downloadPartialContentByLength(contentId, 41, 67).get();
        assertNotNull(partialContentByLength);


    }

}
