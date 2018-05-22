/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.Messages;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.service.SDKException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @param <T> adaptor API
 *
 * @author alexs
 */
public class AbstractService<T> {

    protected UriProvider serviceBaseUriProvider;
    protected AuthenticationProvider authenticationProvider;
    protected AccountProvider accountProvider;
    protected ApiKeyProvider apiKeyProvider;
    protected CryptoProvider cryptoProvider;

    public CryptoProvider getCryptoProvider() throws SDKException {
        if (cryptoProvider == null) {
            throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
        }
        return cryptoProvider;
    }

    protected <T> QueryContext<T> createQueryContext(String entityName) {
        QueryContext<T> cxt = new QueryContext<>(entityName);
        if (validity(cxt).isFail()) {
            return cxt;
        }
        cxt.setServiceBaseUriProvider(serviceBaseUriProvider);
        cxt.setAccountProvider(accountProvider);
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        
        return acquireSessionId(cxt);
    }

    protected <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
        QueryContext<T> cxt = new QueryContext<>(parent, entityName);
        if (validity(cxt).isFail()) {
            return cxt;
        }
        cxt.setServiceBaseUriProvider(serviceBaseUriProvider);
        cxt.setAccountProvider(accountProvider);
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        
        return acquireSessionId(cxt);
    }

    private <T> QueryContext<T> validity(QueryContext<T> cxt) {
        if (serviceBaseUriProvider == null) {
            return cxt.setServiceError("Undefined Base URI Provider.");
        }
        if (authenticationProvider == null) {
            return cxt.setServiceError("Undefined Authentication Provider.");
        }
        if (accountProvider == null) {
            return cxt.setServiceError("Undefined Account Provider.");
        }
        if (apiKeyProvider == null) {
            return cxt.setServiceError("Undefined Api Key Provider.");
        }
        return cxt;
    }

    private <T> QueryContext<T> acquireSessionId(QueryContext<T> cxt) {
        String sessionId = cxt.getSessionId();
        if (sessionId == null || sessionId.isEmpty()) {
            if (authenticationProvider != null) {
                QueryContext<String> authQuery = authenticationProvider.sessionId();
                if (authQuery.isFail()) {
                    cxt.setServiceError(authQuery);
                }
                else {
                    cxt.setSessionId(authQuery.get()).getSessionId();
                    cxt.setAuthPrefix(authenticationProvider.authPrefix());
                }
            }
            else {
                cxt.setServiceError(ServiceError.ErrorCode.unknownAuth);
            }
        }
        return cxt;
    }
}
