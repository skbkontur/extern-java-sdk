package ru.kontur.extern_api.sdk.scenario;

import okhttp3.logging.HttpLoggingInterceptor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;

import java.security.cert.CertificateException;
import java.util.UUID;

import static java.util.UUID.fromString;

public class ContentServiceTests {
    private static ExternEngine engine;
    private static TestSuite test;
    private static Configuration configuration;
    private static CryptoApi cryptoApi;

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

        cryptoApi = new CryptoApi();

        test = new TestSuite(engine, configuration);
    }

    @Test
    void main() throws Exception {
        try {
            UUID contentId = fromString("0be11b77-c535-4091-bba9-4a3ad089d05e");
            byte[] content = engine.getContentService().downloadAllContent(contentId).get();

            byte[] content2 = engine.getContentService().downloadPartContent(contentId, 0, 40).get();
            byte[] content1 = engine.getContentService().downloadPartContentByLength(contentId, 41, 67).get();
        } catch (ApiException e) {
            System.err.println(e);
        }
    }
}
