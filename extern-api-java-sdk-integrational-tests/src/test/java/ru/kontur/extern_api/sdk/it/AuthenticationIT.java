package ru.kontur.extern_api.sdk.it;

import java.time.Duration;
import java.util.UUID;
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
import ru.kontur.extern_api.sdk.provider.auth.CachingRefreshingAuthProvider;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.PasswordAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthenticationProvider;


class AuthenticationIT {

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
    @DisplayName("caching & refreshing")
    class CacheRefreshTest {

        @Test
        void cachingTest() {
            CachingRefreshingAuthProvider auth = build
                    .passwordAuthentication(cfg.getLogin(), cfg.getPass());

            String s1 = auth.sessionId().getOrThrow();
            String s2 = auth.sessionId().getOrThrow();

            Assertions.assertEquals(s1, s2);
        }

        @Test
        void refreshTest() throws Exception {

            CachingRefreshingAuthProvider auth = AuthenticationProviderBuilder
                    .createFor(cfg.getAuthBaseUri(), Level.BODY)
                    .withApiKey(cfg.getApiKey())
                    .cacheKeyFor(Duration.ofSeconds(0))
                    .passwordAuthentication(cfg.getLogin(), cfg.getPass());

            String s1 = auth.sessionId().getOrThrow();
            Thread.sleep(1);
            String s2 = auth.sessionId().getOrThrow();

            Assertions.assertNotEquals(s1, s2);
        }

    }

    @Nested
    @DisplayName("password authentication")
    class PasswordAuthTest {

        @Test
        void passwordAuth() {
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
    class TrustedAuthTest {

        @Test
        void trustedAuth() {
            TrustedAuthenticationProvider auth = build
                    .trustedAuthentication(UUID.fromString(cfg.getServiceUserId()))
                    .configureEncryption(
                            cfg.getJksPass(),
                            cfg.getRsaKeyPass(),
                            cfg.getThumbprintRsa()
                    );

            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }

        @Test
        void registerExternalServiceId() {
            TrustedAuthenticationProvider auth = build
                    .trustedAuthentication(UUID.fromString(cfg.getServiceUserId()))
                    .configureEncryption(
                            cfg.getJksPass(),
                            cfg.getRsaKeyPass(),
                            cfg.getThumbprintRsa()
                    );

            final UUID serviceUserId = UUID.fromString("47024bf5-8c2c-4f1a-8a28-4b41b104a030");
            final String phone = "9500308900";

            auth.registerExternalServiceId(serviceUserId, phone).getOrThrow();
        }
    }


}
