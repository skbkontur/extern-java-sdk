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
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.EngineBuilder.AccountSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeyOrAuth;
import ru.kontur.extern_api.sdk.EngineBuilder.ApiKeySyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.AuthProviderSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.MaybeCryptoProviderSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.OverrideDefaultsSyntax;
import ru.kontur.extern_api.sdk.EngineBuilder.Syntax;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.ProviderSuite;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.provider.UserIPProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.useragent.DefaultUserAgentProvider;
import ru.kontur.extern_api.sdk.service.impl.DefaultServicesFactory;

public final class ExternEngineBuilder implements Syntax {

    private Function<AuthenticationProviderBuilder, AuthenticationProvider> providerCtor;

    @NotNull
    public static ApiKeySyntax createExternEngine() {
        return new ExternEngineBuilder();
    }

    @NotNull
    public static ApiKeyOrAuth createExternEngine(Configuration defaults) {
        return new ExternEngineBuilder().setConfiguration(defaults);
    }

    @NotNull
    public static MaybeCryptoProviderSyntax authFromConfiguration(@NotNull Configuration configuration) {
        Objects.requireNonNull(configuration);

        AuthenticationProvider authProvider = ConfigurationUtils
                .guessAuthProvider(configuration)
                .orElseThrow(() -> new NullPointerException(
                        "can not guess authentication type from given configuration. " +
                                "provide either login+pass or rsa thumbprint+credential. "
                ));

        return new ExternEngineBuilder()
                .setConfiguration(configuration)
                .apiKey(configuration.getApiKey())
                .authProvider(authProvider);
    }

    private Configuration configuration;
    private AuthenticationProvider authenticationProvider;
    private CryptoProvider cryptoProvider;
    private UserAgentProvider userAgentProvider;
    private UserIPProvider userIPProvider;

    private int readTimeout = 60_000;
    private int connectTimeout = 1_000;

    private ExternEngineBuilder() {
        configuration = new Configuration();
        configuration.setAuthBaseUri(DefaultExtern.BASE_AUTH);
        configuration.setServiceBaseUri(DefaultExtern.BASE_URL);
        userAgentProvider = new DefaultUserAgentProvider();
        userIPProvider = () -> "80.247.184.194";
    }

    private ApiKeyOrAuth setConfiguration(Configuration configuration) {
        this.configuration = configuration;
        return this;
    }

    @NotNull
    @Override
    public ExternEngine build(Level logLevel) {

        ProviderSuite providerSuite = new ProviderSuite();

        providerSuite.setAccountProvider(configuration::getAccountId);
        providerSuite.setApiKeyProvider(configuration::getApiKey);
        providerSuite.setServiceBaseUriProvider(configuration::getServiceBaseUri);

        if (authenticationProvider == null) {
            authenticationProvider = providerCtor.apply(AuthenticationProviderBuilder
                    .createFor(configuration.getAuthBaseUri())
                    .withApiKey(configuration.getApiKey())
            );
        }

        providerSuite.setAuthenticationProvider(authenticationProvider);
        providerSuite.setCryptoProvider(cryptoProvider);

        providerSuite.setUserAgentProvider(userAgentProvider);
        providerSuite.setUserIPProvider(userIPProvider);

        KonturConfiguredClient konturClient = new KonturConfiguredClient(logLevel)
                .setConnectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .setReadTimeout(readTimeout, TimeUnit.MILLISECONDS);

        DefaultServicesFactory serviceFactory = new DefaultServicesFactory(konturClient, providerSuite);

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
    public MaybeCryptoProviderSyntax authProvider(@NotNull AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = Objects.requireNonNull(authenticationProvider);
        return this;
    }

    @NotNull
    @Override
    public MaybeCryptoProviderSyntax buildAuthentication(
            @NotNull String authBaseUrl,
            @NotNull Function<AuthenticationProviderBuilder, AuthenticationProvider> providerCtor
    ) {
        configuration.setAuthBaseUri(authBaseUrl);
        this.providerCtor = providerCtor;
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
    public AuthProviderSyntax apiKey(@NotNull String apiKey) {
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
    public OverrideDefaultsSyntax readTimeout(int milliseconds) {
        readTimeout = milliseconds;
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax connectTimeout(int milliseconds) {
        connectTimeout = milliseconds;
        return this;
    }

    @NotNull
    @Override
    public OverrideDefaultsSyntax doNotSetupAccount() {
        return this;
    }

}
