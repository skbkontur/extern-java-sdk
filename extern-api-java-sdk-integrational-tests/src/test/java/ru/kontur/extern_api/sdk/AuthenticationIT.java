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

package ru.kontur.extern_api.sdk;

import java.time.Duration;
import java.util.UUID;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.auth.CachingRefreshingAuthProvider;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.PasswordAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.TestConfig;


@Execution(ExecutionMode.SAME_THREAD)
class AuthenticationIT {

    private static AuthenticationProviderBuilder build;
    private static Configuration configuration;


    @BeforeAll
    static void setUpClass() {
        configuration = TestConfig.LoadConfigFromEnvironment();
        build = AuthenticationProviderBuilder
                .createFor(configuration.getAuthBaseUri(), Level.BODY)
                .withApiKey(configuration.getApiKey());
    }

    @Nested
    @DisplayName("caching & refreshing")
    class CacheRefreshTest {

        @Test
        void cachingTest() {

            CachingRefreshingAuthProvider auth = build
                    .passwordAuthentication(configuration.getLogin(), configuration.getPass());

            String s1 = auth.sessionId().getOrThrow();
            String s2 = auth.sessionId().getOrThrow();

            Assertions.assertEquals(s1, s2);
        }

        @Test
        void refreshTest() throws Exception {

            CachingRefreshingAuthProvider auth = AuthenticationProviderBuilder
                    .createFor(configuration.getAuthBaseUri(), Level.BODY)
                    .withApiKey(configuration.getApiKey())
                    .cacheKeyFor(Duration.ofSeconds(0))
                    .passwordAuthentication(configuration.getLogin(), configuration.getPass());

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
                    .passwordAuthentication(configuration.getLogin(), configuration.getPass());

            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }

    }

    @Nested
    @DisplayName("cert authentication")
    class CertAuthTest {

        @Test
        void certAuth() throws Exception {
            CryptoProviderMSCapi mscapi = new CryptoProviderMSCapi();
            byte[] cert = mscapi.getSignerCertificateAsync(configuration.getThumbprint())
                    .join()
                    .getOrThrow();

            CertificateAuthenticationProvider auth = build.certificateAuthentication(cert);

            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }
    }

    @Nested
    @DisplayName("trusted authentication")
    class TrustedAuthTest {

        TrustedAuthenticationProvider auth = build
                .trustedAuthentication(UUID.fromString(configuration.getServiceUserId()))
                .configureEncryption(
                        configuration.getJksPass(),
                        configuration.getRsaKeyPass(),
                        configuration.getThumbprintRsa()
                );

        @Test
        void trustedAuth() {
            Assertions.assertNotNull(auth.sessionId().getOrThrow());
        }

        @Test
        void registerExternalServiceId() {

            final UUID serviceUserId = UUID.fromString("47024bf5-8c2c-4f1a-8a28-4b41b104a030");
            final String phone = "9500308900";

            auth.registerExternalServiceId(serviceUserId, phone).getOrThrow();
        }
    }


}
