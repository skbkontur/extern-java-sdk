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

import java.util.Objects;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.model.Credential;

public interface TrustedAuthCredentials {

    String getRsaKeyPass();

    String getAuthBaseUri();

    String getApiKey();

    String getThumbprintRsa();

    Credential getCredential();

    String getServiceUserId();

    String getJksPass();

    static TrustedAuthCredentials fromConfiguration(Configuration configuration) {
        Objects.requireNonNull(configuration.getRsaKeyPass(), "RsaKeyPass");
        Objects.requireNonNull(configuration.getAuthBaseUri(), "AuthBaseUri");
        Objects.requireNonNull(configuration.getApiKey(), "ApiKey");
        Objects.requireNonNull(configuration.getThumbprintRsa(), "ThumbprintRsa");
        Objects.requireNonNull(configuration.getCredential(), "Credential");
        Objects.requireNonNull(configuration.getServiceUserId(), "ServiceUserId");

        return new TrustedAuthCredentials() {
            @Override
            public String getRsaKeyPass() {
                return configuration.getRsaKeyPass();
            }

            @Override
            public String getAuthBaseUri() {
                return configuration.getAuthBaseUri();
            }

            @Override
            public String getApiKey() {
                return configuration.getApiKey();
            }

            @Override
            public String getThumbprintRsa() {
                return configuration.getThumbprintRsa();
            }

            @Override
            public Credential getCredential() {
                return configuration.getCredential();
            }

            @Override
            public String getServiceUserId() {
                return configuration.getServiceUserId();
            }

            @Override
            public String getJksPass() {
                return configuration.getJksPass();
            }
        };
    }

}
