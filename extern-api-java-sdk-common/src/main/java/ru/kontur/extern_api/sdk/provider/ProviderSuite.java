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


public class ProviderSuite implements ProviderHolder {

    private UriProvider uriProvider;
    private AuthenticationProvider authenticationProvider;
    private AccountProvider accountProvider;
    private ApiKeyProvider apiKeyProvider;
    private CryptoProvider cryptoProvider;
    private UserIPProvider userIPProvider;
    private UserAgentProvider userAgentProvider;

    @Override
    public UriProvider getServiceBaseUriProvider() {
        return uriProvider;
    }

    @Override
    public void setServiceBaseUriProvider(UriProvider uriProvider) {
        this.uriProvider = uriProvider;
    }

    @Override
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    @Override
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    @Override
    public AccountProvider getAccountProvider() {
        return accountProvider;
    }

    @Override
    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    @Override
    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider;
    }

    @Override
    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    @Override
    public CryptoProvider getCryptoProvider() {
        return cryptoProvider;
    }

    @Override
    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    @Override
    public UserIPProvider getUserIPProvider() {
        return userIPProvider;
    }

    @Override
    public void setUserIPProvider(UserIPProvider userIPProvider) {
        this.userIPProvider = userIPProvider;
    }

    @Override
    public void setUserAgentProvider(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    @Override
    public UserAgentProvider getUserAgentProvider() {
        return userAgentProvider;
    }
}
