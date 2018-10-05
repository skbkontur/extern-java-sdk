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
 *
 */

package ru.kontur.extern_api.sdk.provider;


public interface ProviderHolderParent<TChild extends ProviderHolder> extends ProviderHolder {

    TChild getChildProviderHolder();

    @Override
    default void setServiceBaseUriProvider(UriProvider serviceBaseUriProvider) {
        getChildProviderHolder().setServiceBaseUriProvider(serviceBaseUriProvider);
    }

    @Override
    default void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        getChildProviderHolder().setAuthenticationProvider(authenticationProvider);
    }

    @Override
    default void setAccountProvider(AccountProvider accountProvider) {
        getChildProviderHolder().setAccountProvider(accountProvider);
    }

    @Override
    default void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        getChildProviderHolder().setApiKeyProvider(apiKeyProvider);
    }

    @Override
    default void setCryptoProvider(CryptoProvider cryptoProvider) {
        getChildProviderHolder().setCryptoProvider(cryptoProvider);
    }

    @Override
    default void setUserIPProvider(UserIPProvider userIPProvider) {
        getChildProviderHolder().setUserIPProvider(userIPProvider);
    }

    @Override
    default UriProvider getServiceBaseUriProvider() {
        return getChildProviderHolder().getServiceBaseUriProvider();
    }

    @Override
    default AuthenticationProvider getAuthenticationProvider() {
        return getChildProviderHolder().getAuthenticationProvider();
    }

    @Override
    default AccountProvider getAccountProvider() {
        return getChildProviderHolder().getAccountProvider();
    }

    @Override
    default ApiKeyProvider getApiKeyProvider() {
        return getChildProviderHolder().getApiKeyProvider();
    }

    @Override
    default CryptoProvider getCryptoProvider() {
        return getChildProviderHolder().getCryptoProvider();
    }

    @Override
    default UserIPProvider getUserIPProvider() {
        return getChildProviderHolder().getUserIPProvider();
    }

    @Override
    default void setUserAgentProvider(UserAgentProvider userAgentProvider) {
        getChildProviderHolder().setUserAgentProvider(userAgentProvider);
    }

    @Override
    default UserAgentProvider getUserAgentProvider() {
        return getChildProviderHolder().getUserAgentProvider();
    }
}
