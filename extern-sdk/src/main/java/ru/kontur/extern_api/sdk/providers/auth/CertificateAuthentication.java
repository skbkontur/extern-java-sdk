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

package ru.kontur.extern_api.sdk.providers.auth;

import static ru.kontur.extern_api.sdk.Messages.C_RESOURCE_NOT_FOUND;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.model.AuthInitResponse;
import ru.kontur.extern_api.sdk.model.CertSessionCredentials;
import ru.kontur.extern_api.sdk.providers.ApiKeyProvider;
import ru.kontur.extern_api.sdk.providers.CryptoProvider;
import ru.kontur.extern_api.sdk.util.TypeLookup;
import ru.kontur.extern_api.sdk.providers.ServiceBaseUriProvider;
import ru.kontur.extern_api.sdk.providers.SignatureKeyProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiException;

/**
 * Провайдер аутентификации по сертификату.
 * <a href="https://github.com/skbkontur/extern-api-docs/blob/master/Аутентификация.md#1">
 * Описание процесса.
 * </a>
 *
 * @author Ostanin Igor
 */
public class CertificateAuthentication extends AuthenticationProviderAbstract {

    private static final Decoder b64decoder = Base64.getDecoder();

    private static final String APIKEY_QUERY_PARAM = "apiKey";
    private static final String SKIP_VALIDATION_QUERY_PARAM = "free";
    private static final String THUMBPRINT_QUERY_PARAM = "thumbprint";

    private static final String AUTH_BY_CERT_PATH = "/auth/v5.9/authenticate-by-cert";
    private static final String APPROVE_CERT_PATH = "/auth/v5.9/approve-cert";

    private final URL certificateUrl;
    private final ApiClient apiClient;
    private final String skipCertValidation;
    private final ApiKeyProvider apiKeyProvider;
    private final ServiceBaseUriProvider serviceBaseUriProvider;
    private final CryptoProvider cryptoProvider;
    private final SignatureKeyProvider signatureKeyProvider;

    private CertSessionCredentials credentials;

    /**
     * @see CertificateAuthentication#CertificateAuthentication(URL, TypeLookup, boolean)
     */
    public CertificateAuthentication(
            @NotNull URL certificateUrl,
            @NotNull TypeLookup typeLookup) {
        this(certificateUrl, typeLookup, false);
    }

    /**
     * @param certificateUrl URL используемого для аутентификации сертификата
     * @param typeLookup with {@link ApiKeyProvider}, {@link CryptoProvider}, authentication
     * {@link ServiceBaseUriProvider} and {@link SignatureKeyProvider}
     * @param skipCertValidation {@code true} - не проверять валидность сертификата, по умолчанию -
     * {@code false}
     */
    public CertificateAuthentication(
            @NotNull URL certificateUrl,
            @NotNull TypeLookup typeLookup,
            boolean skipCertValidation) {

        Objects.requireNonNull(certificateUrl);
        Objects.requireNonNull(typeLookup);
        this.certificateUrl = certificateUrl;
        this.apiKeyProvider = typeLookup.require(ApiKeyProvider.class);
        this.cryptoProvider = typeLookup.require(CryptoProvider.class);
        this.serviceBaseUriProvider = typeLookup.require(ServiceBaseUriProvider.class);
        this.signatureKeyProvider = typeLookup.require(SignatureKeyProvider.class);
        this.apiClient = new ApiClient();
        this.skipCertValidation = Boolean.valueOf(skipCertValidation).toString();
    }


    private QueryContext<String> acquireSessionId() {
        apiClient.setBasePath(serviceBaseUriProvider.getServiceBaseUri());

        byte[] cert;
        try {
            cert = Files.readAllBytes(new File(certificateUrl.getFile()).toPath());
        } catch (IOException e) {
            return new QueryContext<String>()
                    .setServiceError(Messages.get(C_RESOURCE_NOT_FOUND), e);
        }

        String apiKey = apiKeyProvider.getApiKey();
        String thumbprint = signatureKeyProvider.getThumbprint();

        CertSessionCredentials credentials;
        try {
            AuthInitResponse initialResp = initAuth(cert, apiKey, skipCertValidation);
            credentials = confirmAuth(initialResp, apiKey, thumbprint);
        } catch (ApiException e) {
            return QueryContext.fromError(e);
        }

        this.credentials = credentials;

        QueryContext<String> authContext = QueryContext
                .fromResult(credentials.getSid(), QueryContext.SESSION_ID);

        fireAuthenticationEvent(authContext);
        return authContext;
    }

    @Override
    public QueryContext<String> sessionId() {

        if (credentials == null) {
            return acquireSessionId();
        }

        // TODO: we can renew old credentials if they become rotten

        return QueryContext.fromResult(credentials.getSid(), QueryContext.SESSION_ID);
    }

    private byte[] decodeSecret(String encryptedKey, String thumbprint) {
        byte[] decodedEncryptedKey = b64decoder.decode(encryptedKey);
        return cryptoProvider.decrypt(
                new QueryContext<byte[]>()
                        .setThumbprint(thumbprint)
                        .setContent(decodedEncryptedKey)
        ).get();
    }

    private AuthInitResponse initAuth(
            byte[] certBytes,
            String apiKey,
            String skipCertValidation) throws ApiException {

        Map<String, String> queryParams = new HashMap<>(2);
        queryParams.put(APIKEY_QUERY_PARAM, apiKey);
        queryParams.put(SKIP_VALIDATION_QUERY_PARAM, skipCertValidation);

        return post(AUTH_BY_CERT_PATH, queryParams, certBytes, AuthInitResponse.class);
    }

    private CertSessionCredentials confirmAuth(
            AuthInitResponse initial,
            String apiKey,
            String thumbprint) throws ApiException {

        byte[] decodedSecret = decodeSecret(initial.getEncryptedKey(), thumbprint);

        Map<String, String> queryParams = new HashMap<>(2);
        queryParams.put(APIKEY_QUERY_PARAM, apiKey);
        queryParams.put(THUMBPRINT_QUERY_PARAM, thumbprint);

        return post(APPROVE_CERT_PATH, queryParams, decodedSecret, CertSessionCredentials.class);
    }

    private <T> T post(String path, Map<String, String> queryParams, Object body, Class<T> outType)
            throws ApiException {

        Map<String, Object> forms = Collections.emptyMap();
        Map<String, String> headers = new HashMap<>(1);
        if (body instanceof byte[]) {
            headers.put("Content-Type", "application/octet-stream");
        }

        return apiClient
                .submitHttpRequest(path, "POST", queryParams, body, headers, forms, outType)
                .getData();
    }

    @Override
    public String authPrefix() {
        return DEFAULT_AUTH_PREFIX;
    }

}
