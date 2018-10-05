package ru.kontur.extern_api.sdk.test;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.test.utils.CertificateResource;
import ru.kontur.extern_api.sdk.test.utils.SystemProperty;
import ru.kontur.extern_api.sdk.test.utils.TestSuite;


class CertAuthenticationTest {

    private static ExternEngine engine;

    @BeforeEach
    void setUp() {
        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @BeforeAll
    static void setUpClass() {

        engine = TestSuite.LoadManually((configuration, builder) -> builder
                .certificateAuth(CertificateResource.read(configuration.getThumbprint()))
                .cryptoProvider(new CryptoProviderMSCapi())
                .doNotSetupAccount()
                .build()
        ).engine;

    }

    @Test
    void basicAuth() {
        engine.getAuthenticationProvider().sessionId().ensureSuccess();
    }
}
