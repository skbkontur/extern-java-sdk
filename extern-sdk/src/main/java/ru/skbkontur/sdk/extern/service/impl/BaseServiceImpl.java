/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public class BaseServiceImpl {
	
	protected ServiceBaseUriProvider serviceBaseUriProvider;
	
	protected AuthenticationProvider authenticationProvider;
	
	protected AccountProvider accountProvider;

	protected ApiKeyProvider apiKeyProvider;
	
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

	protected <T> QueryContext<T> createQueryContext(String entityName) {
		QueryContext<T> cxt = new QueryContext<>(entityName);
		cxt.setServiceBaseUriProvider(serviceBaseUriProvider);
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setAccountProvider(accountProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}

	protected <T> QueryContext<T> createQueryContext(QueryContext<?> parent, String entityName) {
		QueryContext<T> cxt = new QueryContext<>(parent,entityName);
		cxt.setServiceBaseUriProvider(serviceBaseUriProvider);
		cxt.setAuthenticationProvider(authenticationProvider);
		cxt.setAccountProvider(accountProvider);
		cxt.setApiKeyProvider(apiKeyProvider);
		cxt.configureApiClient();
		return cxt;
	}
}
