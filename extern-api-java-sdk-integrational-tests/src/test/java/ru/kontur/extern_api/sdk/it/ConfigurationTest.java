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

package ru.kontur.extern_api.sdk.it;


import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.it.utils.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.it.utils.TestConfig;
import ru.kontur.extern_api.sdk.service.AccountService;

@DisplayName("Configuration tokens should")
class ConfigurationTest {

    private Configuration C = TestConfig.LoadConfigFromEnvironment();

    private ExternEngine newEngine() {
        return ExternEngineBuilder.createExternEngine()
                .apiKey(C.getApiKey())
                .buildAuthentication(C.getAuthBaseUri(), builder -> builder.
                        passwordAuthentication(C.getLogin(), C.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(C.getAccountId())
                .build(Level.BODY);
    }

    @Test
    @DisplayName("share same sid across all services")
    void shareSameSid() throws Exception {

        ExternEngine engine = newEngine();

        AccountService accountService = engine.getAccountService();
        accountService.acquireAccountsAsync().get().getOrThrow();

        engine.setAuthenticationProvider(new AuthenticationProviderAdaptor());
        accountService.acquireAccountsAsync().get().getOrThrow();

        ApiException apiException = Assertions.assertThrows(ApiException.class,
                () -> engine.getAccountService().acquireAccountsAsync().get().getOrThrow()
        );

        Assertions.assertEquals(401, apiException.getCode());
    }
}
