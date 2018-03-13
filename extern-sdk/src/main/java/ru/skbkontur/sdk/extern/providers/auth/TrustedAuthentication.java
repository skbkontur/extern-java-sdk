/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthBaseUriProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.providers.ServiceUserIdProvider;
import ru.skbkontur.sdk.extern.providers.SignatureKeyProvider;
import ru.skbkontur.sdk.extern.service.SDKException;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SESSION_ID;
import ru.skbkontur.sdk.extern.service.transport.adaptors.ServiceErrorImpl;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiResponse;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.Pair;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ProgressRequestBody;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ProgressResponseBody;

/**
 *
 * @author AlexS
 */
public class TrustedAuthentication implements AuthenticationProvider {

	private static final String EOL = System.getProperty("line.separator", "\r\n");
	private static final String APIKEY = "apikey";
	private static final String ID = "id";
	private static final String TIMESTAMP = "timestamp";
	private static final String DEFAULT_AUTH_PREFIX = "auth.sid ";

	private final ApiClient apiClient;
	private ApiKeyProvider apiKeyProvider;
	private AuthBaseUriProvider authBaseUriProvider;
	private CryptoProvider cryptoProvider;
	private SignatureKeyProvider signatureKeyProvider;
	private ServiceUserIdProvider serviceUserIdProvider;
	private String authPrefix;
	private String timestamp;
	private Credential credential;

	public TrustedAuthentication(String authPrefix) {
		this.apiClient = new ApiClient();
		this.authPrefix = authPrefix;
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

	public AuthBaseUriProvider getAuthBaseUriProvider() {
		return authBaseUriProvider;
	}

	public void setAuthBaseUriProvider(AuthBaseUriProvider authBaseUriProvider) {
		this.authBaseUriProvider = authBaseUriProvider;
	}

	public TrustedAuthentication authBaseUriProvider(AuthBaseUriProvider authBaseUriProvider) {
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

	public Credential getCredential() {
		return credential;
	}

	public void setCredential(Credential credential) {
		this.credential = credential;
	}

	public TrustedAuthentication credential(Credential credential) {
		this.credential = credential;
		return this;
	}

	@Override
	public QueryContext<String> sessionId() {
		if (cryptoProvider == null)
			return new QueryContext<String>().setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.auth, SDKException.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER, 0, null, null));
		
		apiClient.setBasePath(authBaseUriProvider.getAuthBaseUri());

		timestamp = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss").format(new Date());

//		QueryContext<Object> identityQuery = authenticateWithHttpInfo();
		return authenticateRequest();
	}

	@Override
	public String authPrefix() {
		return authPrefix;
	}

	private QueryContext<String> authenticateRequest() {
		try {
			StringBuilder identityData = new StringBuilder();

			Map<String, String> queryParams = new HashMap<>();

			String apiKey = apiKeyProvider.getApiKey().toLowerCase();
			queryParams.put(APIKEY, apiKey);
			identityData.append(APIKEY).append("=").append(apiKey).append(EOL);

			if (credential != null) {
				queryParams.put(credential.getName(), credential.getValue());
				identityData.append(ID).append("=").append(credential.getValue()).append(EOL);
			}

			queryParams.put(TIMESTAMP, timestamp);
			identityData.append(TIMESTAMP).append("=").append(timestamp).append(EOL);

			queryParams.put("serviceUserId", serviceUserIdProvider.getServiceUserIdProvider());

			Map<String, String> localHeaderParams = new HashMap<>();

			Map<String, Object> localVarFormParams = new HashMap<>();

			byte[] signature = cryptoProvider.sign(signatureKeyProvider.getThumbprint(), identityData.toString().getBytes());

			ApiResponse<TrustedAuthentication.ResponseLink> responseLink = apiClient.submitHttpRequest("/v5.9/authenticate-by-truster", "POST", queryParams, signature, localHeaderParams, localVarFormParams, TrustedAuthentication.ResponseLink.class);

			apiClient.setBasePath("");
			
			ResponseLink link = responseLink.getData();
			byte[] key = IOUtil.hexToBytes(link.getKey());
			String approveRequest = link.getLink().getHref(); // + "&" + APIKEY + "=" + apiKey;
			
			ApiResponse<ResponseSid> sid = apiClient.submitHttpRequest(approveRequest, "POST", Collections.emptyMap(), key, localHeaderParams, localVarFormParams, ResponseSid.class);
			
			return new QueryContext<String>().setResult(sid.getData().getSid(), SESSION_ID);
		}
		catch (ApiException x) {
			return new QueryContext<String>().setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.auth, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
		catch (SDKException x) {
			return new QueryContext<String>().setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.auth, x.getMessage(), 0, null, null));
		}
	}

	public com.squareup.okhttp.Call authenticateCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = "signature";
		// create path and map variables
		String localVarPath = "v5.9/authenticate-by-truster";

		List<Pair> localVarQueryParams = new ArrayList<>();

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "apiKey", apiKeyProvider.getApiKey()));

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "timestamp", timestamp));

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "serviceUserId", serviceUserIdProvider.getServiceUserIdProvider()));

		Map<String, String> localVarHeaderParams = new HashMap<>();

		Map<String, Object> localVarFormParams = new HashMap<>();
		
		final String[] localVarContentTypes = {"text/plain"};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		if (progressListener != null) {
			com.squareup.okhttp.Interceptor interceptor = (com.squareup.okhttp.Interceptor.Chain chain) -> {
				com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
				return originalResponse.newBuilder()
					.body(new ProgressResponseBody(originalResponse.body(), progressListener))
					.build();
			};
			
			apiClient.getHttpClient().networkInterceptors().add(interceptor);
		}

		String[] localVarAuthNames = new String[]{};

		return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
	}

	public static class ResponseLink {

		@SerializedName("Link")
		private Link link;
		@SerializedName("Key")
		private String key;

		public ResponseLink() {
		}

		public Link getLink() {
			return link;
		}

		public void setLink(Link link) {
			this.link = link;
		}

		public String getKey() {
			return key;
		}

		public void setKey(String key) {
			this.key = key;
		}
	}

	public static class Link {

		@SerializedName("Rel")
		private String rel;
		@SerializedName("Href")
		private String href;

		public Link() {

		}

		public Link(String rel, String href) {
			this.rel = rel;
			this.href = href;
		}

		public String getRel() {
			return rel;
		}

		public void setRel(String rel) {
			this.rel = rel;
		}

		public String getHref() {
			return href;
		}

		public void setHref(String href) {
			this.href = href;
		}
	}
}
