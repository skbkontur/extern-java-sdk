/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthentication;
import ru.kontur.extern_api.sdk.provider.crypt.rsa.CryptoProviderRSA;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;


class TrustedAuthenticationTest {

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        TestSuite test = TestSuite.Load(cfg -> {
            cfg.setLogin(null);
        });

        engine = test.engine;
    }

    @Test
    void acquireSessionId() {
        System.out.println("trusted authentication");
        QueryContext<String> sessionIdCxt = engine.getAuthenticationProvider().sessionId();
        sessionIdCxt.ensureSuccess();
        assertNotNull(sessionIdCxt.get());
    }

    /**
     * Тестирует запрос связывания партального пользователя с идентификаторм внешней системы
     */
    @Test
    void registerExternalServiceUserIdTest() {
        /* Идентификатор внешней системы */
        final String serviceUserId = "47024bf5-8c2c-4f1a-8a28-4b41b104a030";

        /* Номер телефона конечного пользователя */
        final String phone = "9500308900";

        TrustedAuthentication trustedAuth = (TrustedAuthentication) engine
                .getAuthenticationProvider();

        QueryContext<Void> registerCxt = trustedAuth
                .registerExternalServiceId(serviceUserId, phone)
                .ensureSuccess();

        assertNull(registerCxt.get());
    }

    @Test
    void signRSA() {
        final String CONTENT = "Asbest";
        CryptoProviderRSA rsaCryptoProvider = new CryptoProviderRSA(
                engine.getConfiguration().getJksPass(), engine.getConfiguration().getRsaKeyPass());
        QueryContext<byte[]> signCxt
                = rsaCryptoProvider
                .sign(
                        new QueryContext<byte[]>()
                                .setThumbprint(engine.getConfiguration().getThumbprintRsa())
                                .setContent(CONTENT.getBytes())
                );

        signCxt.ensureSuccess();

        assertNotNull(signCxt.get());
    }

}
