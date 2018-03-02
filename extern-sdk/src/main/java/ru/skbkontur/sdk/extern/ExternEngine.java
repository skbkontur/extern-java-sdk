/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.service.DocflowService;
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.impl.BaseServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DocflowServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DraftServiceImpl;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;

/**
 *
 * @author AlexS
 */
public class ExternEngine {

	private static final Gson GSON = new Gson();

	private final Environment env;

	private DraftService draftService;
	
	private DocflowService docflowService;
	
	private ServiceBaseUriProvider serviceBaseUriProvider;
	
	private AuthenticationProvider authenticationProvider;
	
	private AccountProvider accountProvider;
	
	private ApiKeyProvider apiKeyProvider;
	
	public ExternEngine(Configuration configuration) throws SDKException {
		env = new Environment();
		env.configuration = configuration;
	}

	public Environment getEnvironment() {
		return env;
	}
	
	public Configuration getConfiguration() {
		return env.configuration;
	}
	
	public ExternEngine(String configPath) throws SDKException {
		// loads config data from the resourse file: extern-sdk-config.json
		this(loadConfiguration(configPath));
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
	
	private static Configuration loadConfiguration(String path) throws SDKException {
		try (InputStream is = ExternEngine.class.getResourceAsStream(path)) {
			if (is == null) {
				throw new SDKException(SDKException.C_CONFIG_NOT_FOUND);
			}

			return GSON.fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
		}
		catch (IOException x) {
			throw new SDKException(SDKException.C_CONFIG_LOAD, x);
		}
		catch (SDKException x) {
			throw x;
		}
		catch (Exception x) {
			throw new SDKException(SDKException.UNKNOWN, x);
		}
	}
	
	private void initCryptoService() throws SDKException {
		if (env.configuration.getThumbprint() != null && !env.configuration.getThumbprint().isEmpty()) {
			try {
				// a byte array of the thumbprint
				byte[] tp = IOUtil.hexToBytes(env.configuration.getThumbprint());
				env.cryptoService = new MSCapi(true);
				// searches a signature key by thumbprint
				Key[] keys = env.cryptoService.getKeys();
				for (int i = 0; i < keys.length && env.signKey == null; i++) {
					if (Arrays.equals(keys[i].getThumbprint(), tp)) {
						env.signKey = keys[i];
					}
				}
				if (env.signKey == null) {
					System.out.println("A Signature key not found.");
				}
			}
			catch (CryptoException x) {
				throw new SDKException(SDKException.C_CRYPTO_ERROR, x);
			}
		}
	}
	
	public void configureServices() throws SDKException {
		initCryptoService();
		
		DraftServiceImpl docSrv = new DraftServiceImpl();
		configureService(docSrv);
		docSrv.setDraftsApi(new DraftsAdaptor());
		this.draftService = docSrv;
		
		DocflowServiceImpl docflowSrv = new DocflowServiceImpl();
		configureService(docflowSrv);
		docflowSrv.setDraftsApi(new DocflowsAdaptor());
		this.docflowService = docflowSrv;
		
	}
	
	private void configureService(BaseServiceImpl baseService) {
		baseService.setServiceBaseUriProvider(this.serviceBaseUriProvider);
		baseService.setAuthenticationProvider(this.authenticationProvider);
		baseService.setAccountProvider(this.accountProvider);
		baseService.setApiKeyProvider(this.apiKeyProvider);
	}
}
