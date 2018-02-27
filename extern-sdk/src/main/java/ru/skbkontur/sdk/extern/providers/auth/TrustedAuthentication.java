/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ru.argosgrp.cryptoservice.Key;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthBaseUriProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.providers.ServiceError.ErrorCode;
import ru.skbkontur.sdk.extern.providers.SignatureKeyProvider;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
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
	private static final String IDENTITY = "identity";

	private final ApiClient apiClient;
	private ApiKeyProvider apiKeyProvider;
	private AuthBaseUriProvider authBaseUriProvider;
	private SignatureKeyProvider signatureKeyProvider;
	private String serviceUserId;
	private String authPrefix;
	private String timestamp;
	private Credential credential;
	private Key signKey;

	public TrustedAuthentication(String serviceUserId, String authPrefix) {
		this.apiClient = new ApiClient();
		this.serviceUserId = serviceUserId;
		this.authPrefix = authPrefix;
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

	public SignatureKeyProvider getSignatureKeyProvider() {
		return signatureKeyProvider;
	}
	
	public void setSignatureKeyProvider(SignatureKeyProvider signatureKeyProvider) {
		this.signatureKeyProvider = signatureKeyProvider;
		this.signKey = null;
	}
	
	public TrustedAuthentication signatureKeyProvider(SignatureKeyProvider signatureKeyProvider) {
		setSignatureKeyProvider(signatureKeyProvider);
		return this;
	}
	
	public String getServiceUserId() {
		return serviceUserId;
	}

	public void setServiceUserId(String serviceUserId) {
		this.serviceUserId = serviceUserId;
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
	
	public void getCredential(Credential credential) {
		this.credential = credential;
	}
	
	public TrustedAuthentication credential(Credential credential) {
		this.credential = credential;
		return this;
	}
	
	@Override
	public QueryContext<String> sessionId() {
		apiClient.setBasePath(authBaseUriProvider.getAuthBaseUri());
		timestamp = new SimpleDateFormat("dd:MM:yyyy HH:mm:ss").format(new Date());
		QueryContext<Object> identityQuery = authenticateWithHttpInfo();
		if (identityQuery.isFail())
			return new QueryContext(identityQuery, IDENTITY);
		else
			return new QueryContext(IDENTITY).setResult("test",IDENTITY);
	}

	@Override
	public String authPrefix() {
		return authPrefix;
	}

	private QueryContext<Object> authenticateWithHttpInfo() {
		try {
			com.squareup.okhttp.Call call = authenticateValidateBeforeCall(null, null);
			Type localVarReturnType = new TypeToken<TrustedAuthentication.ResponesIdentity>() {}.getType();
			ApiResponse<TrustedAuthentication.ResponesIdentity> result = apiClient.execute(call, localVarReturnType);
			return new QueryContext<>().setResult(result.getData(), "Identity");
		}
		catch (ApiException x) {
			return new QueryContext().setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.auth, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	@SuppressWarnings("rawtypes")
	private com.squareup.okhttp.Call authenticateValidateBeforeCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {

		com.squareup.okhttp.Call call = authenticateCall(progressListener, progressRequestListener);

		return call;

	}

	public com.squareup.okhttp.Call authenticateCall(final ProgressResponseBody.ProgressListener progressListener, final ProgressRequestBody.ProgressRequestListener progressRequestListener) throws ApiException {
		Object localVarPostBody = "signature";
		// create path and map variables
		String localVarPath = "v5.9/authenticate-by-truster";

		List<Pair> localVarQueryParams = new ArrayList<>();

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "apiKey", apiKeyProvider.getApiKey()));

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "timestamp", timestamp));

		localVarQueryParams.addAll(apiClient.parameterToPairs("", "serviceUserId", serviceUserId));
		
		Map<String, String> localVarHeaderParams = new HashMap<>();

		Map<String, Object> localVarFormParams = new HashMap<>();
		/*
		final String[] localVarAccepts = {
			"application/json", "text/json"
		};
		final String localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
		if (localVarAccept != null) {
			localVarHeaderParams.put("Accept", localVarAccept);
		}
		 */
		final String[] localVarContentTypes = {"text/plain"};
		final String localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);
		localVarHeaderParams.put("Content-Type", localVarContentType);

		if (progressListener != null) {
			apiClient.getHttpClient().networkInterceptors().add(new com.squareup.okhttp.Interceptor() {
				@Override
				public com.squareup.okhttp.Response intercept(com.squareup.okhttp.Interceptor.Chain chain) throws IOException {
					com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
					return originalResponse.newBuilder()
						.body(new ProgressResponseBody(originalResponse.body(), progressListener))
						.build();
				}
			});
		}

		String[] localVarAuthNames = new String[]{};

		return apiClient.buildCall(localVarPath, "POST", localVarQueryParams, localVarPostBody, localVarHeaderParams, localVarFormParams, localVarAuthNames, progressRequestListener);
	}
	
	public static class ResponesIdentity {
		@SerializedName("Link")
		private Link link;
		@SerializedName("Key")
		private String key;
		
		public ResponesIdentity() {
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
		private String  rel;
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
	
	private String getSignData() {
		StringBuilder signData = new StringBuilder("apikey=");
		signData.append("apikey=").append(apiKeyProvider.getApiKey()).append(EOL);
		signData.append("id=").append(serviceUserId).append(EOL);
		signData.append("timestamp=").append(timestamp).append(EOL);
		return signData.toString();
	}
	/*
	private QueryContext<Key> getSignKey() {
		if (signatureKeyProvider == null) {
			return new QueryContext<Key>()
				.setServiceError(
					new ServiceErrorImpl(
						ErrorCode.business,
						"Unknown signature key provider.",
						0,
						null,
						null
					)
				);
		}
		
		
	}
*/
}
