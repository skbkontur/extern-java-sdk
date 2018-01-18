/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.net.HttpURLConnection;
import java.util.Date;
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

/**
 *
 * @author AlexS
 */
public class ExternSDKBase {

	protected final ExternSDK externSDK;
	protected final Base64.Encoder ENCODER_BASE64 = Base64.getEncoder();

	public ExternSDKBase(ExternSDK externSDK) {
		this.externSDK = externSDK;
	}

	protected final void configureApiClient(ApiClient apiClient) {
		apiClient.getJSON().setGson(new GsonBuilder()
			.disableHtmlEscaping()
			.registerTypeAdapter(Date.class, new DateAdapter(apiClient))
			.registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
			.registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
			.create());
	}

	protected UUID getBillingAccountId() {
		return externSDK.getEnvironment().configuration.getBillingAccountId();
	}

	protected String getApiKey() {
		return externSDK.getEnvironment().configuration.getApiKey();
	}

	protected String getAccessToken() {
		return externSDK.getEnvironment().accessToken;
	}

	protected <T> T jsonToDTO(Map<?, ?> response, Class<T> t) {
		Gson gson = new Gson();
		String json = gson.toJson(response);
		return gson.fromJson(json, t);
	}

	protected CryptoService getCryptoService() {
		return externSDK.getEnvironment().cryptoService;
	}

	protected Key getSignKey() {
		return externSDK.getEnvironment().signKey;
	}

	protected void errorHandler(ApiException x, int attempt) throws ExternSDKException {
		if (x.getCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
			if (attempt == 0) 
				// update authorization token
				externSDK.acquireAccessToken();
			else 
				// throw authorization error
				throw new ExternSDKException(ExternSDKException.AUTHORIZATION_BY_LOGIN, x);
		}
		else {
			throw new ExternSDKException(ExternSDKException.SERVER_ERROR, x);
		}
	}
	
	protected void errorHandler(ApiException x, int attempt, String entityName, String id) throws ExternSDKException {
		if (x.getCode() == HttpURLConnection.HTTP_NOT_FOUND) {
			throw new ExternSDKException(ExternSDKException.ENTITY_NOT_FOUND, entityName, id);
		}
		else {
			ExternSDKBase.this.errorHandler(x,attempt);
		}
	}
}
