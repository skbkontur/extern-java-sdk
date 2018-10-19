/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */
package ru.kontur.extern_api.sdk.provider.auth;

import java.time.temporal.TemporalAmount;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.portal.AuthApi;
import ru.kontur.extern_api.sdk.portal.model.CertificateAuthenticationQuest;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;

/**
 * Провайдер аутентификации по сертификату.
 * <a href="https://github.com/skbkontur/extern-api-docs/blob/master/">
 * Описание процесса.
 * </a>
 */
public final class CertificateAuthenticationProvider extends CachingRefreshingAuthProvider {

    private final String certThumbprint;
    private final CryptoProvider cryptoProvider;
    private final String apiKey;
    private final byte[] cert;
    private final AuthApi authApi;

    public CertificateAuthenticationProvider(
            AuthApi authApi,
            byte[] cert,
            String certThumbprint,
            String apiKey,
            CryptoProvider cryptoProvider,
            TemporalAmount cacheTime) {
        super(cacheTime, authApi);
        this.authApi = authApi;
        this.cert = cert;
        this.certThumbprint = certThumbprint;
        this.apiKey = apiKey;
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public CompletableFuture<SessionResponse> authenticate() {
        return authApi
                .certificateAuthenticationInit(apiKey, null, null, cert)
                .thenApply(CertificateAuthenticationQuest::getEncryptedKey)
                .thenCompose(key -> cryptoProvider.decryptAsync(certThumbprint, key))
                .thenApply(QueryContext::getOrThrow)
                .thenCompose(decrypted -> authApi
                        .certificateAuthenticationConfirm(certThumbprint, apiKey, decrypted)
                );
    }

}
