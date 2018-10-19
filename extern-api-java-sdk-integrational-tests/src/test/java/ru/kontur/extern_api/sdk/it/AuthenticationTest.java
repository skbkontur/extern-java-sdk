package ru.kontur.extern_api.sdk.it;

import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.it.utils.CertificateResource;
import ru.kontur.extern_api.sdk.it.utils.TestConfig;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.PasswordAuthenticationProvider;


class AuthenticationTest {

    private static Configuration cfg;
    private static AuthenticationProviderBuilder build;

    @BeforeAll
    static void setUp() {
        cfg = TestConfig.LoadConfigFromEnvironment();
        build = AuthenticationProviderBuilder
                .createFor(cfg.getAuthBaseUri(), Level.BODY)
                .withApiKey(cfg.getApiKey());
    }

    @Nested
    @DisplayName("password authentication")
    class PasswordAuthTest {

        @Test
        void passwordAuth() throws Exception {

            PasswordAuthenticationProvider auth = build
                    .passwordAuthentication(cfg.getLogin(), cfg.getPass());

            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }

    }

    @Nested
    @DisplayName("cert authentication")
    class CertAuthTest {

        CertAuthTest() throws Exception {
            // cache installed keys before start working with auth.
            new CryptoApi().getCertificatesInstalledLocally();
        }

        @Test
        void certAuth() throws Exception {
            CertificateAuthenticationProvider auth = build
                    .certificateAuthentication(CertificateResource.read(cfg.getThumbprint()));

            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }
    }

    @Nested
    @DisplayName("trusted authentication")
    class TrustedAuthTest {}



}
