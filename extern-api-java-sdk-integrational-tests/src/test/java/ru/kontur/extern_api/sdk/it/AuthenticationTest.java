package ru.kontur.extern_api.sdk.it;

import java.net.URI;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.it.utils.CertificateResource;
import ru.kontur.extern_api.sdk.it.utils.TestConfig;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.auth.PasswordAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;


class AuthenticationTest {


    private static Configuration cfg;

    @BeforeAll
    static void setUp() {
        cfg = TestConfig.LoadConfigFromEnvironment();
    }

    @Test
    void certAuth() throws Exception {
        // cache installed keys before starting dialog with auth.
        new CryptoApi().getInstalledKeys(false);

        ExternEngine engine = TestSuite.LoadManually((configuration, builder) -> builder
                .certificateAuth(CertificateResource.read(configuration.getThumbprint()))
                .cryptoProvider(new CryptoProviderMSCapi())
                .doNotSetupAccount()
                .build()
        ).engine;

        engine.getAuthenticationProvider().sessionId().ensureSuccess();
    }

    @Test
    void passwordAuth() throws Exception {
        PasswordAuthenticationProvider auth = AuthenticationProviderBuilder
                .createFor(cfg.getAuthBaseUri(), Level.BODY)
                .passwordAuthentication(cfg.getLogin(), cfg.getPass());

        Assertions.assertNotNull(auth.sessionId().getOrThrow());

    }
}
