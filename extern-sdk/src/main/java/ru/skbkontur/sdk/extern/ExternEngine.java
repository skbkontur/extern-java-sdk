/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import ru.skbkontur.sdk.extern.service.SDKException;
import java.io.IOException;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import ru.skbkontur.sdk.extern.event.AuthenticationEvent;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.providers.auth.AuthenticationProviderByPass;
import ru.skbkontur.sdk.extern.service.AccountService;
import ru.skbkontur.sdk.extern.service.BusinessDriver;
import ru.skbkontur.sdk.extern.service.CertificateService;
import ru.skbkontur.sdk.extern.service.DocflowService;
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.impl.AccountServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.BaseService;
import ru.skbkontur.sdk.extern.service.impl.CertificateServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DocflowServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DraftServiceImpl;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.CertificatesAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SESSION_ID;

/**
 *
 * @author AlexS
 */
public class ExternEngine implements AuthenticationListener {

	private final Environment env;

	private AccountService accountService;

	private DraftService draftService;

	private DocflowService docflowService;

	private CertificateService certificateService;

	private ServiceBaseUriProvider serviceBaseUriProvider;

	private EngineAuthenticationProvider authenticationProvider;

	private AccountProvider accountProvider;

	private ApiKeyProvider apiKeyProvider;

	private CryptoProvider cryptoProvider;

	private String sessionId;
	
	private BusinessDriver businessDriver;

	public ExternEngine() throws SDKException {
		this(new Configuration());
	}

	public ExternEngine(Configuration configuration) throws SDKException {
		env = new Environment();
		env.configuration = configuration;
		this.sessionId = null;
		this.businessDriver = new BusinessDriver(this);
		configureProviders();
	}

	/**
	 * loads config data from the resource file
	 * @param  configUrl url to configuration
	 * @throws IOException see {@link Configuration#load(URL)}
	 */
	public ExternEngine(URL configUrl) throws IOException {
		this(Configuration.load(configUrl));
	}

	public Configuration getConfiguration() {
		return env.configuration;
	}

	public Environment getEnvironment() {
		return env;
	}

	public AccountService getAccountService() {
		return accountService;
	}

	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	public DraftService getDraftService() {
		return draftService;
	}

	public void setDraftService(DraftService draftService) {
		this.draftService = draftService;
	}

	public DocflowService getDocflowService() {
		return docflowService;
	}

	public void setDocflowService(DocflowService docflowService) {
		this.docflowService = docflowService;
	}

	public CertificateService getCertificateService() {
		return certificateService;
	}

	public void setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
		this.serviceBaseUriProvider = serviceBaseUriProvider;
	}

	public AuthenticationProvider getAuthenticationProvider() {
		return ((EngineAuthenticationProvider) authenticationProvider).getOriginAuthenticationProvider();
	}

	public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		if (authenticationProvider != null) {
			authenticationProvider.addAuthenticationListener(this);
			this.authenticationProvider = new EngineAuthenticationProvider(authenticationProvider);
		}
		else if (this.authenticationProvider != null) {
			AuthenticationProvider originAuthenticationProvider = this.authenticationProvider.getOriginAuthenticationProvider();
			originAuthenticationProvider.removeAuthenticationListener(this);
		}
	}

	public AccountProvider getAccountProvider() {
		return accountProvider;
	}

	public void setAccountProvider(AccountProvider accountProvider) {
		this.accountProvider = accountProvider;
	}

	public ApiKeyProvider getApiKeyProvider() {
		return apiKeyProvider;
	}

	public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
		this.apiKeyProvider = apiKeyProvider;
	}

	public CryptoProvider getCryptoProvider() throws SDKException {
		if (cryptoProvider == null) {
			throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
		}
		return cryptoProvider;
	}

	public CryptoProvider setCryptoProvider(CryptoProvider cryptoProvider) {
		CryptoProvider current = this.cryptoProvider;
		this.cryptoProvider = cryptoProvider;
		return current;
	}

	public void configureServices() throws SDKException {
		AccountServiceImpl accSrv = new AccountServiceImpl();
		configureService(accSrv);
		accSrv.setApi(new AccountsAdaptor());
		this.accountService = accSrv;

		DraftServiceImpl draftSrv = new DraftServiceImpl();
		configureService(draftSrv);
		draftSrv.setApi(new DraftsAdaptor());
		this.draftService = draftSrv;

		DocflowServiceImpl docflowSrv = new DocflowServiceImpl();
		configureService(docflowSrv);
		docflowSrv.setApi(new DocflowsAdaptor());
		this.docflowService = docflowSrv;

		CertificateServiceImpl certificateSrvice = new CertificateServiceImpl();
		configureService(certificateSrvice);
		certificateSrvice.setApi(new CertificatesAdaptor());
		this.certificateService = certificateSrvice;
	}

	private void configureService(BaseService baseService) {
		baseService.setServiceBaseUriProvider(this.serviceBaseUriProvider);
		baseService.setAuthenticationProvider(this.authenticationProvider);
		baseService.setAccountProvider(this.accountProvider);
		baseService.setApiKeyProvider(this.apiKeyProvider);
		baseService.setCryptoProvider(this.cryptoProvider);
	}

	@Override
	public synchronized void authenticate(AuthenticationEvent authEvent) {
		sessionId = authEvent.getAuthCxt().isSuccess() ? authEvent.getAuthCxt().get() : null;
	}

	class EngineAuthenticationProvider implements AuthenticationProvider {

		private final AuthenticationProvider authenticationProvider;

		private EngineAuthenticationProvider(AuthenticationProvider authenticationProvider) {
			this.authenticationProvider = authenticationProvider;
		}

		public AuthenticationProvider getOriginAuthenticationProvider() {
			return authenticationProvider;
		}

		@Override
		public QueryContext<String> sessionId() {
			if (sessionId == null) {
				return authenticationProvider.sessionId();
			}
			else {
				return new QueryContext<String>().setResult(sessionId, SESSION_ID);
			}
		}

		@Override
		public String authPrefix() {
			return authenticationProvider.authPrefix();
		}

		@Override
		public void addAuthenticationListener(AuthenticationListener authListener) {
			authenticationProvider.addAuthenticationListener(authListener);
		}

		@Override
		public void removeAuthenticationListener(AuthenticationListener authListener) {
			authenticationProvider.removeAuthenticationListener(authListener);
		}

		@Override
		public void raiseUnauthenticated(ServiceError x) {
			authenticationProvider.raiseUnauthenticated(x);
		}
	}

	private void configureProviders() {
		Configuration c = env.configuration;
		if (c != null) {

			Optional.ofNullable(c.accountId()).ifPresent(value -> setAccountProvider(c));
			Optional.ofNullable(c.getApiKey()).ifPresent(value -> setApiKeyProvider(c));
			Optional.ofNullable(c.getServiceBaseUri()).ifPresent(value -> setServiceBaseUriProvider(c));

			if (c.getUri() != null && c.getLogin() != null && c.getPass() != null && c.getApiKey() != null) {
				setAuthenticationProvider(new AuthenticationProviderByPass(c, c, c));
			}
		}
	}
	
	public BusinessDriver getBusinessDriver() {
		return businessDriver;
	}
}
