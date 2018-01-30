/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import ru.skbkontur.sdk.extern.rest.api.QueryContext;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.utils.Base64;
import ru.skbkontur.sdk.extern.rest.invoker.DateAdapter;
import ru.skbkontur.sdk.extern.rest.invoker.DateTimeTypeAdapter;
import ru.skbkontur.sdk.extern.rest.invoker.LocalDateTypeAdapter;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiClient;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.auth.ApiKeyAuth;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.auth.Authentication;
import ru.skbkontur.sdk.extern.rest.api.QueryApply;

/**
 *
 * @author AlexS
 */
public class ExternSDKBase {

	protected final ExternSDK externSDK;
	protected final Base64.Encoder ENCODER_BASE64 = Base64.getEncoder();
	protected ApiClient apiClient;

	public ExternSDKBase(ExternSDK externSDK) {
		this.externSDK = externSDK;
	}

	protected final void setApiClient(ApiClient apiClient) {
		this.apiClient = apiClient;
	}

	protected final void configureApiClient() {
		apiClient.getJSON().setGson(new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(Date.class, new DateAdapter(apiClient))
			.registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
			.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
			.create());
		// устанавливаем api-key
		acceptApiKey(externSDK.getEnvironment().configuration.getApiKey());
		// устанавливаем accessToken
		acceptAccessToken(externSDK.getEnvironment().configuration.getAuthPrefix(), externSDK.getEnvironment().accessToken);
	}

	protected UUID getAccountId() {
		return externSDK.getEnvironment().configuration.getAccountId();
	}

	protected String getApiKey() {
		return externSDK.getEnvironment().configuration.getApiKey();
	}

	protected String getAccessToken() {
		return externSDK.getEnvironment().accessToken;
	}

	protected CryptoService getCryptoService() {
		return externSDK.getEnvironment().cryptoService;
	}

	protected Key getSignKey() {
		return externSDK.getEnvironment().signKey;
	}

	protected void errorHandler(ApiException x, boolean reauthorizate, String entityName, String id) throws ExternSDKException {
		switch (x.getCode()) {
			case HttpURLConnection.HTTP_UNAUTHORIZED:
				if (reauthorizate) {
					// update authorization token
					// to accept a new authorization token
					String accessToken = externSDK.acquireAccessToken();
					// setup a new authorization token
					this.acceptAccessToken(externSDK.getEnvironment().configuration.getAuthPrefix(), accessToken);
				}
				else {
					// throw authorization error
					throw new ExternSDKException(ExternSDKException.S_AUTHORIZATION_BY_LOGIN, x);
				}
				break;
			case HttpURLConnection.HTTP_NOT_FOUND:
				throw new ExternSDKException(ExternSDKException.S_ENTITY_NOT_FOUND, entityName, id);
			default:
				throw new ExternSDKException(ExternSDKException.S_SERVER_ERROR, x);
		}
	}

	public void acceptAccessToken(String apiKeyPrefix, String accessToken) {
		if (accessToken != null && !accessToken.isEmpty()) {
			Authentication apiKeyAuth = apiClient.getAuthentication("auth.sid");
			if (apiKeyAuth != null) {
				((ApiKeyAuth) apiKeyAuth).setApiKey(accessToken);
				((ApiKeyAuth) apiKeyAuth).setApiKeyPrefix(apiKeyPrefix);
			}
		}
	}

	protected <R> QueryContext<R> invokeApply(QueryApply<R> query, QueryContext<R> context) throws ExternSDKException {
		for (boolean s : Arrays.asList(true, false)) {
			try {
				return query.apply(context);
			}
			catch (ApiException x) {
				errorHandler(x, s, context.getEntityName(), context.getEntityId());
			}
		}
		// never too be
		return null;
	}
	
	protected <R> QueryContext<R> newCxt(String entityName, Class<R> clazz) {
		return new QueryContext(getAccountId(),entityName);
	}

	protected <R> QueryContext<List<R>> newCxtForList(String entityName, Class<R> clazz) {
		return new QueryContext(getAccountId(),entityName);
	}

	protected QueryContext<Map<String, Object>> newCxtForMap(String entityName) {
		return new QueryContext(getAccountId(),entityName);
	}

	private void acceptApiKey(String apiKey) {
		if (apiKey != null && !apiKey.isEmpty()) {
			Authentication apiKeyAuth = apiClient.getAuthentication("apiKey");
			if (apiKeyAuth != null) {
				((ApiKeyAuth) apiKeyAuth).setApiKey(apiKey);
			}
		}
	}
}
