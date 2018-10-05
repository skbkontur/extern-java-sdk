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
package ru.kontur.extern_api.sdk.provider.crypt.cloud;

import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.crypt.cloud.model.ApprovedDecryptResponse;
import ru.kontur.extern_api.sdk.provider.crypt.cloud.model.ApprovedSignaturesResponse;
import ru.kontur.extern_api.sdk.provider.crypt.cloud.model.ContentRequest;
import ru.kontur.extern_api.sdk.provider.crypt.cloud.model.RequestResponse;
import ru.kontur.extern_api.sdk.adaptor.Query;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static ru.kontur.extern_api.sdk.Messages.C_NO_DECRYPT;
import static ru.kontur.extern_api.sdk.Messages.C_NO_SIGNATURE;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import static ru.kontur.extern_api.sdk.adaptor.QueryContext.CONTENT;

/**
 * @author Aleksey Sukhorukov
 * @deprecated to be removed. Use service embedded method instead
 */
@Deprecated
public class CloudCryptoProvider implements CryptoProvider {

    private static final String SIGNATURE = "подпись";
    private static final String DECRYPT = "дешифрование";

    private static final String REQUEST_RESPONSE = "requestResponse";
    private static final String APPROVED_SIGNATURE_RESPONSE = "approvedSignaturesResponse";
    private static final String APPROVED_DECRYPT_RESPONSE = "approvedDecryptResponse";
    private static final String APPROVE_CODE = "approveCode";
    private static final String RESULT_ID = "resultId";

    private static final String SIGN_REQUEST = "/sign";
    private static final String DECRYPT_REQUEST = "/decrypt";
    private static final String APPROVE_SIGNATURES_REQUEST = "/signatures/{0}";
    private static final String APPROVE_DECRYPTED_REQUEST = "/decrypted/{0}";
    private static final String SMS_CODE = "X-CloudCrypt-SmsCode";

    private final String cloudCryptoBaseUri;

    private AuthenticationProvider authenticationProvider;

    private ApiKeyProvider apiKeyProvider;

    private Function<String, String> approveCodeProvider;

    private CertificateProvider certificateProvider;

    private HttpClient httpClient;

    public CloudCryptoProvider(String cloudCryptoBaseUri) {
        this.cloudCryptoBaseUri = cloudCryptoBaseUri;
    }

    public String getCloudCryptoBaseUri() {
        return cloudCryptoBaseUri;
    }
    
    public void setApproveCodeProvider(Function<String, String> approveCodeProvider) {
        this.approveCodeProvider = approveCodeProvider;
    }

    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public CloudCryptoProvider authenticationProvider(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        return this;
    }

    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    public CloudCryptoProvider apiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
        return this;
    }

    public CloudCryptoProvider approveCodeProvider(Function<String, String> approveCodeProvider) {
        this.approveCodeProvider = approveCodeProvider;
        return this;
    }

    public void setCertificateProvider(CertificateProvider certificateProvider) {
        this.certificateProvider = certificateProvider;
    }

    public CloudCryptoProvider certificateProvider(CertificateProvider certificateProvider) {
        this.certificateProvider = certificateProvider;
        return this;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
    
    public CloudCryptoProvider httpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
        return this;
    }
    
    @Override
    public CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content) {

        QueryContext<byte[]> cxt = createQueryContext();

        return CompletableFuture.supplyAsync(() -> sign(cxt.setThumbprint(thumbprint).setContent(content)));
    }

    @Override
    public QueryContext<byte[]> sign(QueryContext<byte[]> parent) {

        QueryContext<byte[]> cxt = createQueryContext(parent, SIGNATURE);

        // получить смс-код для подтверждения подписи
        if (approveCodeProvider == null) {
            return cxt.setServiceError("SMS provider does not installed.");
        }

        // запросить облачную подпись у сервиса
        QueryContext<RequestResponse> signatureReqRespCxt = operateRequest(cxt, SIGN_REQUEST, SIGNATURE);
        if (signatureReqRespCxt.isFail()) {
            return cxt.setServiceError(signatureReqRespCxt);
        }
        // ответ сервиса содержит идентификатор запроса
        RequestResponse signatureReqResp = signatureReqRespCxt.get(REQUEST_RESPONSE);
        // запросить код подтверждения у владельца облачного сертификата
        String approveCode = approveCodeProvider.apply(signatureReqResp.getResultId());
        // устанавливаем в контекст код подтверждения
        cxt.set(APPROVE_CODE, approveCode);
        // устанавливаем в контекст код подтверждения
        cxt.set(RESULT_ID, signatureReqResp.getResultId());
        // подтвердить создание подписи смс-кодом
        QueryContext<ApprovedSignaturesResponse> approvedCxt = approveRequest(cxt, ApprovedSignaturesResponse.class, SIGNATURE, APPROVED_SIGNATURE_RESPONSE, APPROVE_SIGNATURES_REQUEST);
        if (approvedCxt.isFail()) {
            return cxt.setServiceError(approvedCxt);
        }
        // проверяем наличие подписи в ответе сервиса
        if (approvedCxt.get() == null || approvedCxt.get().getSignatures() == null || approvedCxt.get().getSignatures().isEmpty()) {
            return cxt.setServiceError(Messages.get(C_NO_SIGNATURE));
        }
        // ожидается только одна подпись в ответе
        String base64 = approvedCxt.get().getSignatures().get(0).getContent();

        return cxt.setResult(Base64.getDecoder().decode(base64), CONTENT);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint) {

        QueryContext<byte[]> cxt = createQueryContext();

        return CompletableFuture
            .supplyAsync(
                ()
                -> {
                return getSignerCertificate(
                    cxt
                        .setThumbprint(thumbprint)
                );
            }
            );
    }

    @Override
    public QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> parent) {
        QueryContext<byte[]> cxt = createQueryContext(parent, SIGNATURE);

        if (certificateProvider == null) {
            return cxt.setServiceError("Certificate provider does not installed.");
        }

        String thumbprint = cxt.getThumbprint();

        QueryContext<byte[]> certCxt = certificateProvider.getCertificate(thumbprint);
        
        if (certCxt.isFail()) {
            return cxt.setServiceError(certCxt);
        }
        
        return cxt.setResult(certCxt.get(), CONTENT);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
        return CompletableFuture.supplyAsync(() -> decrypt(new QueryContext<byte[]>().setThumbprint(thumbprint).setContent(content)));
    }

    @Override
    public QueryContext<byte[]> decrypt(QueryContext<byte[]> parent) {

        QueryContext<byte[]> cxt = createQueryContext(parent, DECRYPT);

        // провайдер смс-кода для подтверждения подписи
        if (approveCodeProvider == null) {
            return cxt.setServiceError("SMS provider does not installed.");
        }

        // запросить расшифрование контента у сервиса
        QueryContext<RequestResponse> decryptReqRespCxt = operateRequest(cxt, DECRYPT_REQUEST, DECRYPT);
        if (decryptReqRespCxt.isFail()) {
            return cxt.setServiceError(decryptReqRespCxt);
        }
        // ответ сервиса содержит идентификатор запроса
        RequestResponse decryptReqResp = decryptReqRespCxt.get(REQUEST_RESPONSE);
        // запросить код подтверждения у владельца облачного сертификата
        String approveCode = approveCodeProvider.apply(decryptReqResp.getResultId());
        // устанавливаем в контекст код подтверждения
        cxt.set(APPROVE_CODE, approveCode);
        // устанавливаем в контекст код подтверждения
        cxt.set(RESULT_ID, decryptReqResp.getResultId());
        // подтвердить полномочия смс-кодом
        QueryContext<ApprovedDecryptResponse> approvedCxt = approveRequest(cxt, ApprovedDecryptResponse.class, DECRYPT, APPROVED_DECRYPT_RESPONSE, APPROVE_DECRYPTED_REQUEST);
        if (approvedCxt.isFail()) {
            return cxt.setServiceError(approvedCxt);
        }
        // проверяем наличие контента в ответе сервиса
        if (approvedCxt.get() == null || approvedCxt.get().getData() == null || approvedCxt.get().getData().isEmpty()) {
            return cxt.setServiceError(Messages.get(C_NO_DECRYPT));
        }
        // ожидается только одна подпись в ответе
        String base64 = approvedCxt.get().getData().get(0).getContent();

        return cxt.setResult(Base64.getDecoder().decode(base64), CONTENT);
    }

    private QueryContext<RequestResponse> operateRequest(QueryContext<byte[]> cxt, String request, String operateName) {

        String thumbprint = cxt.getThumbprint();

        byte[] content = cxt.getContent();

        String base64 = Base64.getEncoder().encodeToString(content);

        ContentRequest contentRequest = new ContentRequest(thumbprint, base64);

        QueryContext<RequestResponse> respCxt = createQueryContext(cxt, operateName);

        return respCxt.apply(new RequestResponseQuery(contentRequest, request));
    }

    private <T> QueryContext<T> approveRequest(QueryContext<byte[]> cxt, Class<T> clazz, String operateName, String entityName, String request) {
        // идентификатор запроса
        String resultId = cxt.get(RESULT_ID);
        // смс-код подтверждения
        String approveCode = cxt.get(APPROVE_CODE);

        QueryContext<T> respCxt = createQueryContext(cxt, operateName);

        return respCxt.apply(new ApprovedQuery<>(resultId, approveCode, clazz, entityName, request));
    }

    private <T> QueryContext<T> createQueryContext() {
        QueryContext<T> cxt = new QueryContext<>(SIGNATURE);
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        return acquireSessionId(cxt);
    }

    private <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
        QueryContext<T> cxt = new QueryContext<>(parent, entityName);
        cxt.setAuthenticationProvider(authenticationProvider);
        cxt.setApiKeyProvider(apiKeyProvider);
        return acquireSessionId(cxt);
    }

    private Map<String, String> acceptAccessToken(QueryContext<?> context, Map<String, String> headerParams) {
        headerParams.put("Authorization", context.getAuthenticationProvider().authPrefix() + context.getSessionId());
        headerParams.put("X-Kontur-Apikey", context.getApiKeyProvider().getApiKey());
        return headerParams;
    }

    private <T> QueryContext<T> acquireSessionId(QueryContext<T> cxt) {
        String sessionId = cxt.getSessionId();
        if (sessionId == null || sessionId.isEmpty()) {
            if (authenticationProvider != null) {
                QueryContext<String> authQuery = authenticationProvider.sessionId();
                if (authQuery.isFail()) {
                    cxt.setServiceError(authQuery);
                }
                else {
                    cxt.setSessionId(authQuery.get()).getSessionId();
                    cxt.setAuthPrefix(authenticationProvider.authPrefix());
                }
            }
            else {
                cxt.setServiceError(ServiceError.ErrorCode.unknownAuth);
            }
        }
        return cxt;
    }
    
    class RequestResponseQuery implements Query<RequestResponse> {

        private final ContentRequest contentRequest;
        private final String request;

        RequestResponseQuery(ContentRequest contentRequest, String request) {
            this.contentRequest = contentRequest;
            this.request = request;
        }

        @Override
        public QueryContext<RequestResponse> apply(QueryContext<?> cxt) {
            try {
                if (cxt.isFail()) {
                    return new QueryContext<>(cxt, cxt.getEntityName());
                }

                Map<String, String> headerParams = acceptAccessToken(cxt, new HashMap<>());

                httpClient.setServiceBaseUri(cloudCryptoBaseUri);

                ApiResponse<RequestResponse> resp = httpClient.submitHttpRequest(
                    request,
                    "POST",
                    Collections.emptyMap(),
                    contentRequest,
                    headerParams,
                    Collections.emptyMap(),
                    RequestResponse.class
                );

                return new QueryContext<RequestResponse>(cxt, cxt.getEntityName()).setResult(resp.getData(), REQUEST_RESPONSE);
            }
            catch (ApiException x) {
                return new QueryContext<RequestResponse>(cxt, cxt.getEntityName()).setServiceError(x);
            }
        }

    }

    class ApprovedQuery<T> implements Query<T> {

        private final String resultId;
        private final String approveCode;
        private final Class<T> clazz;
        private final String entityName;
        private final String request;

        ApprovedQuery(String resultId, String approveCode, Class<T> clazz, String entityName, String request) {
            this.resultId = resultId;
            this.approveCode = approveCode;
            this.clazz = clazz;
            this.entityName = entityName;
            this.request = request;
        }

        @Override
        public QueryContext<T> apply(QueryContext<?> cxt) {
            
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }
            
            // запрос на отправку кода подтверждения
            final String theRequest = java.text.MessageFormat.format(request, resultId);
            // заголовок запроса
            Map<String, String> headers = new HashMap<String, String>() {
                private static final long serialVersionUID = 1L;

                {
                    put(SMS_CODE, approveCode);
                }
            };

            acceptAccessToken(cxt, headers);
            
            try {

                ApiResponse<T> resp = httpClient.submitHttpRequest(
                    theRequest,
                    "GET",
                    Collections.emptyMap(),
                    null,
                    headers,
                    Collections.emptyMap(),
                    clazz
                );
                
                return new QueryContext<T>(cxt, cxt.getEntityName()).setResult(resp.getData(), entityName);
            }
            catch (ApiException x) {
                return new QueryContext<T>(cxt, cxt.getEntityName()).setServiceError(x);
            }
        }
    }
}
