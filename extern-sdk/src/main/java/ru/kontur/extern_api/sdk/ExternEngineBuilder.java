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

package ru.kontur.extern_api.sdk;

import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.EngineBuilder.AccountSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeySyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.AuthProviderSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.CryptoProviderSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.OverrideDefaultsSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.Syntax;
import ru.kontur.extern_api.sdk.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.HttpClientBundle;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.ProviderSuite;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.provider.UserIPProvider;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthCredentials;
import ru.kontur.extern_api.sdk.provider.useragent.DefaultUserAgentProvider;
import ru.kontur.extern_api.sdk.service.impl.DefaultServicesFactory;

public class ExternEngineBuilder implements Syntax {

    @NotNull
    public static AuthProviderSyntax createExternEngine() {
        return createExternEngine(new Configuration());
    }

    @NotNull
    public static AuthProviderSyntax createExternEngine(Configuration defaults) {
        return new ExternEngineBuilder().setConfiguration(defaults);
    }

    @NotNull
    public static CryptoProviderSyntax authFromConfiguration(@NotNull Configuration configuration) {
        Objects.requireNonNull(configuration);

        AuthenticationProvider authProvider = ConfigurationUtils
                .guessAuthProvider(configuration)
                .orElseThrow(() -> new NullPointerException(
                        "can not guess authentication type from given configuration. " +
                                "provide either login+pass or rsa thumbprint+credential. "
                ));

        return new ExternEngineBuilder()
                .setConfiguration(configuration)
                .authProvider(authProvider)
                .apiKey(configuration.getApiKey());
    }


    private Configuration configuration;
    private AuthenticationProvider authenticationProvider;
    private CryptoProvider cryptoProvider;
    private UserAgentProvider userAgentProvider;
    private UserIPProvider userIPProvider;

    private AuthType authType = AuthType.UNSPECIFIED;

    private ExternEngineBuilder() {
        configuration = new Configuration();
        configuration.setAuthBaseUri(DefaultExtern.BASE_AUTH);
        configuration.setServiceBaseUri(DefaultExtern.BASE_URL);
        userAgentProvider = new DefaultUserAgentProvider();
        userIPProvider = () -> "80.247.184.194";
    }

    private AuthProviderSyntax setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    private AuthenticationProvider buildAuth(Configuration configuration) {
        switch (authType) {
            case PASSWORD:
                return ConfigurationUtils.createPasswordAuthProvider(configuration);
            case TRUSTED:
                return ConfigurationUtils.createTrustedAuth(configuration);
            case UNSPECIFIED:
                break;
        }
        return authenticationProvider;
    }

    @NotNull
    @Override
    public ExternEngine build() {

        ProviderSuite providerSuite = new ProviderSuite();

        // todo менять это в Configuration синхронно с ProviderHolder
        providerSuite.setAccountProvider(configuration::getAccountId);
        providerSuite.setApiKeyProvider(configuration::getApiKey);
        providerSuite.setServiceBaseUriProvider(configuration::getServiceBaseUri);

        providerSuite.setAuthenticationProvider(buildAuth(configuration));
        providerSuite.setCryptoProvider(cryptoProvider);

        providerSuite.setUserAgentProvider(userAgentProvider);
        providerSuite.setUserIPProvider(userIPProvider);

        AdaptorBundle adaptorBundle = new HttpClientBundle(providerSuite);

        DefaultServicesFactory serviceFactory = new DefaultServicesFactory(
                providerSuite,
                adaptorBundle
        );

        HttpClient httpClient = serviceFactory
                .getHttpClient()
                .setServiceBaseUri(configuration.getAuthBaseUri());

        authenticationProvider.httpClient(httpClient);

        return new ExternEngine(configuration, providerSuite, serviceFactory);
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax serviceBaseUrl(@NotNull String serviceBaseUrl) {
        configuration.setServiceBaseUri(Objects.requireNonNull(serviceBaseUrl));
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax authServiceBaseUrl(@NotNull String authServiceBaseUrl) {
        configuration.setAuthBaseUri(Objects.requireNonNull(authServiceBaseUrl));
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax userAgentProvider(@NotNull UserAgentProvider userAgentProvider) {
        this.userAgentProvider = Objects.requireNonNull(userAgentProvider);
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax userIpProvider(@NotNull UserIPProvider userIPProvider) {
        this.userIPProvider = Objects.requireNonNull(userIPProvider);
        return this;
    }

    @NotNull
    @Override
    public ApiKeySyntax authProvider(@NotNull AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = Objects.requireNonNull(authenticationProvider);
        return this;
    }

    @Override
    public @NotNull ApiKeySyntax passwordAuth(@NotNull String login, @NotNull String password) {
        authType = AuthType.PASSWORD;
        configuration.setLogin(login);
        configuration.setPass(password);
        return this;
    }

    @Override
    public @NotNull ApiKeySyntax trustedAuth(@NotNull TrustedAuthCredentials authCredentials) {
        authType = AuthType.TRUSTED;

        configuration.setAuthBaseUri(authCredentials.getAuthBaseUri());
        configuration.setApiKey(authCredentials.getApiKey());
        configuration.setThumbprintRsa(authCredentials.getThumbprintRsa());
        configuration.setCredential(authCredentials.getCredential());
        configuration.setServiceUserId(authCredentials.getServiceUserId());
        configuration.setJksPass(authCredentials.getJksPass());
        configuration.setRsaKeyPass(authCredentials.getRsaKeyPass());
        return this;
    }

    @NotNull
    @Override
    public AccountSyntax cryptoProvider(@NotNull CryptoProvider cryptoProvider) {
        this.cryptoProvider = Objects.requireNonNull(cryptoProvider);
        return this;
    }

    @NotNull
    @Override
    public AccountSyntax doNotUseCryptoProvider() {
        this.cryptoProvider = null;
        return this;
    }

    @NotNull
    @Override
    public CryptoProviderSyntax apiKey(@NotNull String apiKey) {
        configuration.setApiKey(Objects.requireNonNull(apiKey));
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax accountId(@NotNull String accountId) {
        configuration.setAccountId(Objects.requireNonNull(accountId));
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax doNotSetupAccount() {
        return this;
    }

    enum AuthType {
        UNSPECIFIED,
        PASSWORD,
        TRUSTED
    }
}
