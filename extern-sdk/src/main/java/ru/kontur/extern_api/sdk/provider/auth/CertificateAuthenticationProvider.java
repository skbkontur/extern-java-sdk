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
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
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

import static java.net.HttpURLConnection.HTTP_FORBIDDEN;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.SignatureKeyProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;

/**
 * Провайдер аутентификации по сертификату.
 * <a href="https://github.com/skbkontur/extern-api-docs/blob/master/">
 * Описание процесса.
 * </a>
 */
public final class CertificateAuthenticationProvider implements AuthenticationProvider {

    private static final Decoder B64DECODER = Base64.getDecoder();

    private static final String APIKEY_QUERY_PARAM = "apiKey";
    private static final String SKIP_VALIDATION_QUERY_PARAM = "free";
    private static final String THUMBPRINT_QUERY_PARAM = "thumbprint";

    private static final String AUTH_BY_CERT_PATH = "/authenticate-by-cert";
    private static final String APPROVE_CERT_PATH = "/approve-cert";

    private final CertificateProvider certificateProvider;
    private HttpClient httpClient;
    private final String skipCertValidation;

    private final ApiKeyProvider apiKeyProvider;
    private final UriProvider serviceBaseUriProvider;
    private final CryptoProvider cryptoProvider;
    private final SignatureKeyProvider signatureKeyProvider;

    private CertSessionCredentials credentials;

    private CertificateAuthenticationProvider(
            CertificateProvider certificateProvider,
            boolean skipCertValidation,
            ApiKeyProvider apiKeyProvider,
            UriProvider serviceBaseUriProvider,
            CryptoProvider cryptoProvider,
            SignatureKeyProvider signatureKeyProvider) {

        this.apiKeyProvider = apiKeyProvider;
        this.serviceBaseUriProvider = serviceBaseUriProvider;
        this.cryptoProvider = cryptoProvider;
        this.signatureKeyProvider = signatureKeyProvider;

        this.certificateProvider = certificateProvider;
        this.skipCertValidation = Boolean.toString(skipCertValidation);
    }

    private QueryContext<String> acquireSessionId() {
        httpClient.setServiceBaseUri(serviceBaseUriProvider.getUri());

        String apiKey = apiKeyProvider.getApiKey();
        String thumbprint = signatureKeyProvider.getThumbprint();
        QueryContext<byte[]> certCxt = certificateProvider.getCertificate(thumbprint);
        if (certCxt.isFail()) {
            return new QueryContext<String>().setServiceError(certCxt);
        }

        CertSessionCredentials localCredentials;
        AuthInitResponse initialResp;
        try {
            initialResp = initAuth(certCxt.get(), apiKey, skipCertValidation);
            localCredentials = confirmAuth(initialResp, apiKey, thumbprint);
        } catch (ApiException e) {
            switch (e.getCode()) {
                case HTTP_FORBIDDEN:
                    return new QueryContext<String>().setServiceError(
                            AuthenticationServiceError.fromAuthenticationException(e));
                default:
                    return new QueryContext<String>().setServiceError(e);
            }
        }

        this.credentials = localCredentials;

        return new QueryContext<String>()
                .setResult(localCredentials.getSid(), QueryContext.SESSION_ID);
    }

    @Override
    public QueryContext<String> sessionId() {

        if (credentials == null) {
            return acquireSessionId();
        }

        // TODO: we can renew old credentials if they become rotten
        return new QueryContext<String>().setResult(credentials.getSid(), QueryContext.SESSION_ID);
    }

    @Override
    public AuthenticationProvider httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }

    private byte[] decodeSecret(String encryptedKey, String thumbprint) {
        byte[] decodedEncryptedKey = B64DECODER.decode(encryptedKey);
        return cryptoProvider.decrypt(
                new QueryContext<byte[]>()
                        .setThumbprint(thumbprint)
                        .setContent(decodedEncryptedKey)
        ).ensureSuccess().get();
    }

    private AuthInitResponse initAuth(
            byte[] certBytes,
            String apiKey,
            String skipCertValidation) throws ApiException {

        Map<String, Object> queryParams = new HashMap<>(2);
        queryParams.put(SKIP_VALIDATION_QUERY_PARAM, skipCertValidation);
        queryParams.put(APIKEY_QUERY_PARAM, apiKey);

        return post(AUTH_BY_CERT_PATH, queryParams, certBytes, AuthInitResponse.class);
    }

    private CertSessionCredentials confirmAuth(
            AuthInitResponse initial,
            String apiKey,
            String thumbprint) throws ApiException {

        byte[] decodedSecret = decodeSecret(initial.getEncryptedKey(), thumbprint);

        Map<String, Object> queryParams = new HashMap<>(2);
        queryParams.put(APIKEY_QUERY_PARAM, apiKey);
        queryParams.put(THUMBPRINT_QUERY_PARAM, thumbprint);

        return post(APPROVE_CERT_PATH, queryParams, decodedSecret, CertSessionCredentials.class);
    }

    private <T> T post(String path, Map<String, Object> queryParams, Object body, Class<T> outType)
            throws ApiException {

        Map<String, Object> forms = Collections.emptyMap();
        Map<String, String> headers = new HashMap<>(1);
        if (body instanceof byte[]) {
            headers.put("Content-Type", "application/octet-stream");
        }

        ApiResponse<T> response = httpClient
                .submitHttpRequest(path, "POST", queryParams, body, headers, forms, outType);

        return response.getData();
    }

    @Override
    public String authPrefix() {
        return DEFAULT_AUTH_PREFIX;
    }

    /**
     * @param certificateProvider Supplier&lt;byte[]&gt; поставляет сертификат, которым производится
     * аутентификация
     * @param skipCertValidation {@code true} - не проверять валидность сертификата, по умолчанию -
     * {@code false}
     * @return CertificateAuthenticationProviderBuilder
     */
    public static CertificateAuthenticationProviderBuilder usingCertificate(
            @NotNull CertificateProvider certificateProvider,
            boolean skipCertValidation) {
        return new CertificateAuthenticationProviderBuilder(certificateProvider,
                skipCertValidation);
    }

    /**
     * @param certificateProvider Supplier&lt;byte[]&gt; поставляет сертификат, которым производится
     * аутентификация
     * @return CertificateAuthenticationProviderBuilder
     * @see CertificateAuthenticationProvider#usingCertificate(CertificateProvider)
     */
    public static CertificateAuthenticationProviderBuilder usingCertificate(
            @NotNull CertificateProvider certificateProvider) {
        return usingCertificate(certificateProvider, false);
    }

    public static final class CertificateAuthenticationProviderBuilder {

        private final CertificateProvider certificateProvider;
        private final boolean skipValidation;

        private ApiKeyProvider apiKeyProvider;
        private UriProvider serviceBaseUriProvider;
        private CryptoProvider cryptoProvider;
        private SignatureKeyProvider signatureKeyProvider;

        CertificateAuthenticationProviderBuilder(
                @NotNull CertificateProvider certificateProvider,
                boolean skipValidation) {
            this.certificateProvider = certificateProvider;
            this.skipValidation = skipValidation;
        }

        public CertificateAuthenticationProviderBuilder setApiKeyProvider(
                @NotNull ApiKeyProvider apiKeyProvider) {
            this.apiKeyProvider = apiKeyProvider;
            return this;
        }

        public CertificateAuthenticationProviderBuilder setServiceBaseUriProvider(
                @NotNull UriProvider serviceBaseUriProvider) {
            this.serviceBaseUriProvider = serviceBaseUriProvider;
            return this;
        }

        public CertificateAuthenticationProviderBuilder setCryptoProvider(
                @NotNull CryptoProvider cryptoProvider) {
            this.cryptoProvider = cryptoProvider;
            return this;
        }

        public CertificateAuthenticationProviderBuilder setSignatureKeyProvider(
                @NotNull SignatureKeyProvider signatureKeyProvider) {
            this.signatureKeyProvider = signatureKeyProvider;
            return this;
        }

        public CertificateAuthenticationProvider buildAuthenticationProvider() {
            return new CertificateAuthenticationProvider(
                    Objects.requireNonNull(certificateProvider),
                    skipValidation,
                    Objects.requireNonNull(apiKeyProvider),
                    Objects.requireNonNull(serviceBaseUriProvider),
                    Objects.requireNonNull(cryptoProvider),
                    Objects.requireNonNull(signatureKeyProvider)
            );
        }
    }
}
