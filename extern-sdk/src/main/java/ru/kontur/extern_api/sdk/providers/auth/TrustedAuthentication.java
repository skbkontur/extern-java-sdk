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

package ru.kontur.extern_api.sdk.providers.auth;

import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.providers.ApiKeyProvider;
import ru.kontur.extern_api.sdk.providers.CredentialProvider;
import ru.kontur.extern_api.sdk.providers.CryptoProvider;
import ru.kontur.extern_api.sdk.providers.ServiceError;
import ru.kontur.extern_api.sdk.providers.ServiceUserIdProvider;
import ru.kontur.extern_api.sdk.providers.SignatureKeyProvider;
import ru.kontur.extern_api.sdk.providers.UriProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiResponse;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.NOTHING;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.SESSION_ID;


/**
 * @author AlexS
 */
public class TrustedAuthentication extends AuthenticationProviderAbstract {

    private static final String EOL = System.getProperty("line.separator", "\r\n");
    private static final String APIKEY = "apikey";
    private static final String SERVICE_USER_ID = "serviceUserId";
    private static final String ID = "id";
    private static final String PHONE = "phone";
    private static final String TIMESTAMP = "timestamp";

    private final ApiClient apiClient;
    private ApiKeyProvider apiKeyProvider;
    private UriProvider authBaseUriProvider;
    private CryptoProvider cryptoProvider;
    private SignatureKeyProvider signatureKeyProvider;
    private ServiceUserIdProvider serviceUserIdProvider;
    private String authPrefix;
    private String timestamp;
    private CredentialProvider credentialProvider;

    public TrustedAuthentication(String authPrefix) {
        this.apiClient = new ApiClient();
        this.authPrefix = authPrefix == null ? DEFAULT_AUTH_PREFIX : authPrefix;
    }

    public TrustedAuthentication() {
        this.apiClient = new ApiClient();
        this.authPrefix = DEFAULT_AUTH_PREFIX;
    }

    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider;
    }

    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    public TrustedAuthentication apiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
        return this;
    }

    public ServiceUserIdProvider getServiceUserIdProvider() {
        return serviceUserIdProvider;
    }

    public void setServiceUserIdProvider(ServiceUserIdProvider serviceUserIdProvider) {
        this.serviceUserIdProvider = serviceUserIdProvider;
    }

    public TrustedAuthentication serviceUserIdProvider(ServiceUserIdProvider serviceUserIdProvider) {
        this.serviceUserIdProvider = serviceUserIdProvider;
        return this;
    }

    public UriProvider getAuthBaseUriProvider() {
        return authBaseUriProvider;
    }

    public void setAuthBaseUriProvider(UriProvider authBaseUriProvider) {
        this.authBaseUriProvider = authBaseUriProvider;
    }

    public TrustedAuthentication authBaseUriProvider(UriProvider authBaseUriProvider) {
        this.authBaseUriProvider = authBaseUriProvider;
        return this;
    }

    public CryptoProvider geCryptoProvider() {
        return cryptoProvider;
    }

    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    public TrustedAuthentication cryptoProvider(CryptoProvider cryptoProvider) {
        setCryptoProvider(cryptoProvider);
        return this;
    }

    public SignatureKeyProvider getSignatureKeyProvider() {
        return signatureKeyProvider;
    }

    public void setSignatureKeyProvider(SignatureKeyProvider signatureKeyProvider) {
        this.signatureKeyProvider = signatureKeyProvider;
    }

    public TrustedAuthentication signatureKeyProvider(SignatureKeyProvider signatureKeyProvider) {
        setSignatureKeyProvider(signatureKeyProvider);
        return this;
    }

    public String getAuthPrefix() {
        return authPrefix;
    }

    public void setAuthPrefix(String authPrefix) {
        this.authPrefix = authPrefix;
    }

    public CredentialProvider getCredentialProvider() {
        return credentialProvider;
    }

    public void setCredentialProvider(CredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
    }

    public TrustedAuthentication credentialProvider(CredentialProvider credentialProvider) {
        this.credentialProvider = credentialProvider;
        return this;
    }

    @Override
    public QueryContext<String> sessionId() {
        if (cryptoProvider == null)
            return new QueryContext<String>().setServiceError(ServiceError.ErrorCode.auth, Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER), 0, null, null);

        apiClient.setBasePath(authBaseUriProvider.getUri());

        timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());

        return authenticateRequest();
    }

    @Override
    public String authPrefix() {
        return authPrefix;
    }

    public QueryContext<Void> registerExternalServiceId(String serviceUserId, String phone) {
        QueryContext<Void> registerCxt;

        try {
            apiClient.setBasePath(authBaseUriProvider.getUri());

            Map<String, String> queryParams = new HashMap<>();

            queryParams.put(APIKEY, apiKeyProvider.getApiKey());

            queryParams.put(SERVICE_USER_ID, serviceUserId);

            queryParams.put(PHONE, phone);

            Map<String, String> localHeaderParams = new HashMap<>();

            Map<String, Object> localVarFormParams = new HashMap<>();

            apiClient.submitHttpRequest("/auth/v5.9/register-external-service-id", "PUT", queryParams, null, localHeaderParams, localVarFormParams, Object.class);

            registerCxt = new QueryContext<Void>().setResult(null, NOTHING);
        } catch (ApiException x) {
            registerCxt = new QueryContext<Void>().setServiceError(x);
        }
        return registerCxt;
    }

    private QueryContext<String> authenticateRequest() {

        QueryContext<String> authCxt;

        try {
            StringBuilder identityData = new StringBuilder();

            Map<String, String> queryParams = new HashMap<>();

            String apiKey = apiKeyProvider.getApiKey().toLowerCase();
            queryParams.put(APIKEY, apiKey);
            identityData.append(APIKEY).append("=").append(apiKey).append(EOL);

            if (credentialProvider != null) {
                queryParams.put(credentialProvider.getCredential().getName(), credentialProvider.getCredential().getValue());
                identityData.append(ID).append("=").append(credentialProvider.getCredential().getValue()).append(EOL);
            }

            queryParams.put(TIMESTAMP, timestamp);
            identityData.append(TIMESTAMP).append("=").append(timestamp).append(EOL);

            queryParams.put(SERVICE_USER_ID, serviceUserIdProvider.getServiceUserIdProvider());

            Map<String, String> localHeaderParams = new HashMap<>();

            Map<String, Object> localVarFormParams = new HashMap<>();

            QueryContext<byte[]> signature = cryptoProvider.sign(
                    new QueryContext<byte[]>()
                            .setThumbprint(signatureKeyProvider.getThumbprint())
                            .setContent(identityData.toString().getBytes())
            );

            if (signature.isFail()) {
                return new QueryContext<String>().setServiceError(signature);
            }

            ApiResponse<ResponseLink> responseLink = apiClient.submitHttpRequest("/v5.9/authenticate-by-truster", "POST", queryParams, signature.get(), localHeaderParams, localVarFormParams, ResponseLink.class);

            apiClient.setBasePath("");

            ResponseLink link = responseLink.getData();

            byte[] key = IOUtil.hexToBytes(link.getKey());

            String approveRequest = link.getLink().getHref(); // + "&" + APIKEY + "=" + apiKey;

            ApiResponse<ResponseSid> sid = apiClient.submitHttpRequest(approveRequest, "POST", Collections.emptyMap(), key, localHeaderParams, localVarFormParams, ResponseSid.class);

            authCxt = new QueryContext<String>().setResult(sid.getData().getSid(), SESSION_ID);
        } catch (ApiException x) {
            authCxt = new QueryContext<String>().setServiceError(x);
        }

        fireAuthenticationEvent(authCxt);

        return authCxt;
    }
}
