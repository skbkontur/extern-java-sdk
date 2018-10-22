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
 */
package ru.kontur.extern_api.sdk.provider.auth;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAmount;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Credential;
import ru.kontur.extern_api.sdk.portal.AuthApi;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;

public class TrustedAuthenticationProvider extends CachingRefreshingAuthProvider {

    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter
            .ofPattern("dd.MM.yyyy HH:mm:ss")
            .withZone(ZoneOffset.UTC);

    private final String apiKey;
    private final CryptoProvider cryptoProvider;
    private final String signerThumbprint;
    private final UUID serviceUserId;
    private final Credential credential;
    private final AuthApi authApi;

    TrustedAuthenticationProvider(
            @NotNull AuthApi authApi,
            @NotNull String apiKey,
            @NotNull CryptoProvider cryptoProvider,
            @NotNull String signerThumbprint,
            @Nullable UUID serviceUserId,
            @Nullable Credential credential,
            @NotNull TemporalAmount cacheTime
    ) {
        super(cacheTime, authApi);
        this.authApi = authApi;
        this.apiKey = apiKey.toLowerCase();
        this.cryptoProvider = cryptoProvider;
        this.signerThumbprint = signerThumbprint;
        this.serviceUserId = serviceUserId;
        this.credential = credential;
    }

    @Override
    protected CompletableFuture<SessionResponse> authenticate() {

        String EOF = System.getProperty("line.separator", "\r\n");
        String sTimestamp = TIMESTAMP_FORMATTER.format(Instant.now());

        String iApiKey = "apikey=" + apiKey + EOF;
        String iIdentity = "id=" + credential.getValue() + EOF;
        String iTimestamp = "timestamp=" + sTimestamp + EOF;

        String signingData = iApiKey + iIdentity + iTimestamp;

        Map<String, String> identity = Objects.equals(credential.getName(), "serviceUserId")
                ? Collections.emptyMap()
                : Collections.singletonMap(credential.getKey(), credential.getValue());

        return cryptoProvider
                .signAsync(signerThumbprint, signingData.getBytes())
                .thenApply(QueryContext::getOrThrow)
                .thenCompose(signature -> authApi.trustedAuthenticationInit(
                        sTimestamp,
                        identity,
                        serviceUserId,
                        apiKey,
                        signature
                ))
                .thenCompose(quest -> authApi.trustedAuthenticationConfirm(
                        quest.getKey(),
                        credential.getValue(),
                        apiKey
                ));
    }

    public QueryContext<Void> registerExternalServiceId(UUID serviceUserId, String phone) {
        return QueryContextUtils.join(authApi
                .registerExternalServiceId(apiKey, serviceUserId, phone)
                .thenApply(QueryContext.constructor(QueryContext.NOTHING))
                .exceptionally(QueryContextUtils::completeCareful)
        );
    }
}
