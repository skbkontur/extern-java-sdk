/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud;

import java.text.MessageFormat;
import ru.skbkontur.sdk.extern.providers.crypt.cloud.model.ContentRequest;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.providers.crypt.cloud.model.ApprovedDecryptResponse;
import ru.skbkontur.sdk.extern.providers.crypt.cloud.model.RequestResponse;
import ru.skbkontur.sdk.extern.providers.crypt.cloud.model.ApprovedSignaturesResponse;
import ru.skbkontur.sdk.extern.service.SDKException;
import ru.skbkontur.sdk.extern.service.transport.adaptors.Query;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.CONTENT;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiResponse;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.ApiKeyAuth;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.auth.Authentication;

/**
 *
 * @author AlexS
 */
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

	protected AuthenticationProvider authenticationProvider;

	protected ApiKeyProvider apiKeyProvider;

	private Function<String, String> approveCodeProvider;

	private Function<String, byte[]> certificateProvider;

	public CloudCryptoProvider(String cloudCryptoBaseUri) {
		this.cloudCryptoBaseUri = cloudCryptoBaseUri;
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

	public void setCertificateProvider(Function<String, byte[]> certificateProvider) {
		this.certificateProvider = certificateProvider;
	}

	public CloudCryptoProvider certificateProvider(Function<String, byte[]> certificateProvider) {
		this.certificateProvider = certificateProvider;
		return this;
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content) {

		QueryContext<byte[]> cxt = createQueryContext(SIGNATURE);

		return CompletableFuture
			.supplyAsync(
				()
				-> {
				return sign(
					cxt
						.setThumbprint(thumbprint)
						.setContent(content)
				);
			}
			);
	}

	@Override
	public QueryContext<byte[]> sign(QueryContext<byte[]> parent) {

		QueryContext<byte[]> cxt = createQueryContext(parent, SIGNATURE);

		// получить смс-код для подтверждения подписи
		if (approveCodeProvider == null) {
			return cxt.setServiceError("SMS provider does not installed.");
		}

		try {
			// запросить облачную подпись у сервиса
			QueryContext<RequestResponse> signatureReqRespCxt = operateRequest(cxt, SIGN_REQUEST, SIGNATURE);
			if (signatureReqRespCxt.isFail()) {
				return cxt.setServiceError(signatureReqRespCxt);
			}
			// ответ сервиса содержит идентификатор запроса
			RequestResponse signatureReqResp = (RequestResponse) signatureReqRespCxt.get(REQUEST_RESPONSE);
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
				return cxt.setServiceError(SDKException.C_NO_SIGNATURE);
			}
			// ожидается только одна подпись в ответе
			String base64 = approvedCxt.get().getSignatures().get(0).getContent();

			return cxt.setResult(Base64.getDecoder().decode(base64), CONTENT);
		}
		catch (ApiException x) {
			return cxt.setServiceError(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody());
		}
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint) {

		QueryContext<byte[]> cxt = createQueryContext(SIGNATURE);

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

		return cxt.setResult(certificateProvider.apply(thumbprint), CONTENT);
	}

	@Override
	public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
		return CompletableFuture
			.supplyAsync(
				()->
					{
						return decrypt(
							new QueryContext<byte[]>()
								.setThumbprint(thumbprint)
								.setContent(content)
						);
					}
			);
	}
	
	@Override
	public QueryContext<byte[]> decrypt(QueryContext<byte[]> parent) {

		QueryContext<byte[]> cxt = createQueryContext(parent, DECRYPT);

		// провайдер смс-кода для подтверждения подписи
		if (approveCodeProvider == null) {
			return cxt.setServiceError("SMS provider does not installed.");
		}

		try {
			// запросить расшифрование контента у сервиса
			QueryContext<RequestResponse> decryptReqRespCxt = operateRequest(cxt,DECRYPT_REQUEST, DECRYPT);
			if (decryptReqRespCxt.isFail()) {
				return cxt.setServiceError(decryptReqRespCxt);
			}
			// ответ сервиса содержит идентификатор запроса
			RequestResponse decryptReqResp = (RequestResponse) decryptReqRespCxt.get(REQUEST_RESPONSE);
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
				return cxt.setServiceError(SDKException.C_NO_DECRYPT);
			}
			// ожидается только одна подпись в ответе
			String base64 = approvedCxt.get().getData().get(0).getContent();

			return cxt.setResult(Base64.getDecoder().decode(base64), CONTENT);
		}
		catch (ApiException x) {
			return cxt.setServiceError(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody());
		}
	}
	
	private QueryContext<RequestResponse> operateRequest(QueryContext<byte[]> cxt, String request, String operateName) throws ApiException {

		String thumbprint = cxt.getThumbprint();

		byte[] content = cxt.getContent();

		String base64 = Base64.getEncoder().encodeToString(content);

		ContentRequest contentRequest = new ContentRequest(thumbprint, base64);

		QueryContext<RequestResponse> respCxt = createQueryContext(cxt, operateName);

		return respCxt.apply(new RequestResponseQuery(contentRequest, request));
	}

	private <T> QueryContext<T> approveRequest(QueryContext<byte[]> cxt, Class<T> clazz, String operateName, String entityName, String request) throws ApiException {
		// идентификатор запроса
		String resultId = cxt.get(RESULT_ID);
		// смс-код подтверждения
		String approveCode = cxt.get(APPROVE_CODE);

		QueryContext<T> respCxt = createQueryContext(cxt, operateName);

		return respCxt.apply(new ApprovedQuery<>(resultId, approveCode, clazz, entityName, request));
	}

	private <T> QueryContext<T> createQueryContext(String entityName) {
		QueryContext<T> cxt = new QueryContext<>(entityName);
		cxt.setApiClient(new ApiClient());
		cxt.setServiceBaseUri(cloudCryptoBaseUri);
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}

	private <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
		QueryContext<T> cxt = new QueryContext<>(parent, entityName);
		cxt.setApiClient(new ApiClient());
		cxt.setServiceBaseUri(cloudCryptoBaseUri);
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}

	private static void acceptAccessToken(QueryContext<?> cxt) { //String apiKeyPrefix, String accessToken) {
		if (cxt.getSessionId() != null && !cxt.getSessionId().isEmpty()) {
			Authentication apiKeyAuth = cxt.getApiClient().getAuthentication("auth.sid");
			if (apiKeyAuth != null) {
				((ApiKeyAuth) apiKeyAuth).setApiKey(cxt.getSessionId());
				((ApiKeyAuth) apiKeyAuth).setApiKeyPrefix(cxt.getAuthenticationProvider().authPrefix());
			}
		}
	}
	
	static class RequestResponseQuery implements Query<RequestResponse> {

		private final ContentRequest contentRequest;
		private final String request;

		RequestResponseQuery(ContentRequest contentRequest, String request) {
			this.contentRequest = contentRequest;
			this.request = request;
		}

		@Override
		public QueryContext<RequestResponse> apply(QueryContext<RequestResponse> context) {
			try {
				acceptAccessToken(context);
				
				ApiResponse<RequestResponse> resp 
					= context
						.getApiClient()
						.submitHttpRequest(request,
							"POST",
							Collections.emptyMap(),
							contentRequest,
							new HashMap<>(),
							Collections.emptyMap(),
							RequestResponse.class
						);

				return context.setResult(resp.getData(), REQUEST_RESPONSE);
			}
			catch (ApiException x) {
				return context.setServiceError(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody());
			}
		}
	}

	static class ApprovedQuery<T> implements Query<T> {

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
		public QueryContext<T> apply(QueryContext<T> context) {
			// запрос на отправку кода подтверждения
			final String theRequest = MessageFormat.format(request, resultId);
			// заголовок запроса
			Map<String, String> headers = new HashMap<String, String>() {
				{
					put(SMS_CODE, approveCode);
				}
			};

			try {
				acceptAccessToken(context);
				
				ApiResponse<T> resp 
					= context
						.getApiClient()
						.submitHttpRequest(
							theRequest,
							"GET",
							Collections.emptyMap(),
							null,
							headers,
							Collections.emptyMap(),
							clazz
					);

				return context.setResult(resp.getData(), entityName);
			}
			catch (ApiException x) {
				return context.setServiceError(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody());
			}
		}
	}
}