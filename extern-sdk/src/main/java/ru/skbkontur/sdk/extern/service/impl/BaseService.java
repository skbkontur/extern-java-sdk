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

package ru.skbkontur.sdk.extern.service.impl;

import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;


/**
 * @param <T> T transport service API
 * @author AlexS
 */
public class BaseService<T> {

    protected T api;
    protected ServiceBaseUriProvider serviceBaseUriProvider;
    protected AuthenticationProvider authenticationProvider;
    protected AccountProvider accountProvider;
    protected ApiKeyProvider apiKeyProvider;
    protected CryptoProvider cryptoProvider;

    public void setApi(T api) {
        this.api = api;
    }

    public void setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
        this.serviceBaseUriProvider = serviceBaseUriProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }
/*
	public CryptoProvider getCryptoProvider() throws SDKException {
		if (cryptoProvider == null)
			throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
		return cryptoProvider;
	}
*/

    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    protected <T> QueryContext<T> createQueryContext(String entityName) {
        QueryContext<T> cxt = new QueryContext<>(entityName);
        cxt.setApiClient(new ApiClient());
        cxt.setServiceBaseUri(serviceBaseUriProvider.getServiceBaseUri());
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setAccountProvider(accountProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        cxt.configureApiClient();
        return cxt;
    }

    protected <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
        QueryContext<T> cxt = new QueryContext<>(parent, entityName);
        cxt.setApiClient(new ApiClient());
        cxt.setServiceBaseUri(serviceBaseUriProvider.getServiceBaseUri());
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setAccountProvider(accountProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        cxt.configureApiClient();
        return cxt;
    }
}
