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

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.provider.UserIPProvider;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthCredentials;

public interface EngineBuilder {

    interface BuildSyntax {

        ExternEngine build();
    }

    interface OverrideDefaultsSyntax extends BuildSyntax {

        @NotNull
        OverrideDefaultsSyntax serviceBaseUrl(@NotNull String serviceBaseUrl);

        @NotNull
        OverrideDefaultsSyntax authServiceBaseUrl(@NotNull String authServiceBaseUrl);

        @NotNull
        OverrideDefaultsSyntax userAgentProvider(@NotNull UserAgentProvider userAgentProvider);

        @NotNull
        OverrideDefaultsSyntax userIpProvider(@NotNull UserIPProvider userIPProvider);

    }

    interface AuthProviderSyntax {

        @NotNull
        MaybeCryptoProviderSyntax authProvider(
                @NotNull AuthenticationProvider authenticationProvider);

        @NotNull
        MaybeCryptoProviderSyntax passwordAuth(@NotNull String login, @NotNull String password);

        @NotNull
        MaybeCryptoProviderSyntax trustedAuth(@NotNull TrustedAuthCredentials authCredentials);

        @NotNull
        CryptoProviderSyntax certificateAuth(@NotNull byte[] certificatePublicKey);

    }

    interface CryptoProviderSyntax {

        @NotNull
        AccountSyntax cryptoProvider(@NotNull CryptoProvider cryptoProvider);

    }

    interface MaybeCryptoProviderSyntax extends CryptoProviderSyntax {

        @NotNull
        AccountSyntax doNotUseCryptoProvider();
    }

    interface ApiKeySyntax {

        @NotNull
        AuthProviderSyntax apiKey(@NotNull String apiKey);
    }

    interface AccountSyntax {

        @NotNull
        OverrideDefaultsSyntax accountId(@NotNull String accountId);

        @NotNull
        OverrideDefaultsSyntax doNotSetupAccount();

    }

    interface ApiKeyOrAuth extends ApiKeySyntax, AuthProviderSyntax {

    }

    interface Syntax extends
            OverrideDefaultsSyntax,
            AuthProviderSyntax,
            MaybeCryptoProviderSyntax,
            ApiKeySyntax,
            AccountSyntax,
            ApiKeyOrAuth {


    }
}
