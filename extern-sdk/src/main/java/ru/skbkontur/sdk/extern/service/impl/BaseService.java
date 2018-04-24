/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import ru.skbkontur.sdk.extern.Messages;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.service.SDKException;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;

/**
 *
 * @author AlexS
 * 
 * @param <T> T transport service API
 */
@SuppressWarnings("unused")
public class BaseService<T> {
	
	protected T api;
	
	public void setApi(T api) {
		this.api = api;
	}
	
	private ServiceBaseUriProvider serviceBaseUriProvider;
	
	private AuthenticationProvider authenticationProvider;
	
	private AccountProvider accountProvider;

	private ApiKeyProvider apiKeyProvider;
	
	private CryptoProvider cryptoProvider;
	
	public void setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
		this.serviceBaseUriProvider = serviceBaseUriProvider;
	}

	public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		this.authenticationProvider = authenticationProvider;
	}

	public void setAccountProvider(AccountProvider accountProvider) {
		this.accountProvider = accountProvider;
	}

	public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
		this.apiKeyProvider = apiKeyProvider;
	}

	public CryptoProvider getCryptoProvider() throws SDKException {
		if (cryptoProvider == null)
			throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
		return cryptoProvider;
	}
	
	public void setCryptoProvider(CryptoProvider cryptoProvider) {
		this.cryptoProvider = cryptoProvider;
	}

	protected <T> QueryContext<T> createQueryContext(String entityName) {
		QueryContext<T> cxt = new QueryContext<>(entityName);
		cxt.setApiClient(new ApiClient());
		cxt.setServiceBaseUri(serviceBaseUriProvider.getServiceBaseUri());
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setAccountProvider(accountProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}

	protected <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
		QueryContext<T> cxt = new QueryContext<>(parent,entityName);
		cxt.setApiClient(new ApiClient());
		cxt.setServiceBaseUri(serviceBaseUriProvider.getServiceBaseUri());
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setAccountProvider(accountProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}
}
