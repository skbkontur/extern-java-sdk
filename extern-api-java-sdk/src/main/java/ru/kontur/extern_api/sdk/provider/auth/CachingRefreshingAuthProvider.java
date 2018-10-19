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

package ru.kontur.extern_api.sdk.provider.auth;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.TemporalAmount;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.portal.AuthApi;
import ru.kontur.extern_api.sdk.portal.model.SessionResponse;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;

public abstract class CachingRefreshingAuthProvider implements AuthenticationProvider {

    private final TemporalAmount cacheTime;
    private final AuthApi authApi;

    private SessionResponse cachedSession;
    private Instant timestamp;

    public CachingRefreshingAuthProvider(TemporalAmount cacheTime, AuthApi authApi) {
        this.cacheTime = cacheTime;
        this.authApi = authApi;
        cachedSession = null;
        timestamp = null;
    }

    private SessionResponse store(SessionResponse response) {
        cachedSession = new SessionResponse(response.getSid(), response.getRefreshToken());
        timestamp = now();
        return cachedSession;
    }


    private Optional<CompletableFuture<SessionResponse>> getOrRefresh() {

        if (cachedSession == null || timestamp == null) {
            cachedSession = null;
            timestamp = null;
            return Optional.empty();
        }

        if (now().isAfter(timestamp.plus(cacheTime))) {
            return Optional.of(refresh(cachedSession));
        }

        return Optional.of(CompletableFuture.completedFuture(cachedSession));
    }

    private CompletableFuture<SessionResponse> refresh(SessionResponse cachedSession) {
        return authApi
                .refreshSession(cachedSession.getSid(), cachedSession.getRefreshToken())
                .thenApply(this::store);
    }

    protected abstract CompletableFuture<SessionResponse> authenticate();

    @Override
    public final QueryContext<String> sessionId() {
        return QueryContextUtils.join(
                getOrRefresh()
                        .orElseGet(() -> authenticate().thenApply(this::store))
                        .thenApply(SessionResponse::getSid)
                        .thenApply(QueryContext.constructor(QueryContext.SESSION_ID))
                        .exceptionally(QueryContextUtils::completeCareful)
        );
    }

    @Override
    @Deprecated
    public final AuthenticationProvider httpClient(HttpClient httpClient) {
        return this;
    }

    private static Instant now() {
        return Instant.now(Clock.systemUTC());
    }
}
