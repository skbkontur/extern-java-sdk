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
import java.util.UUID;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.rest.api.AuthenticationApi;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;

/**
 *
 * @author AlexS
 */
public class ExternSDK {

	private static final String EXTERN_SDK_CONFIG = "/extern-sdk-config.json";

	private final Gson gson = new Gson();

	private final Environment env;

	public ExternSDKDraft draft;

	public ExternSDKDocflow docflow;

	public ExternSDK() throws ExternSDKException {
		env = new Environment();
		// loads config data from the resourse file: extern-sdk-config.json
		env.configuration = loadConfiguration();
	}

	public void setAccountId(String accountId) {
		env.configuration.setAccountId(UUID.fromString(accountId));
	}

	public void setApiKey(String apiKey) {
		env.configuration.setApiKey(apiKey);
	}

	public void setAuthPrefix(String authPrefix) {
		env.configuration.setAuthPrefix(authPrefix);
	}

	public void setLogin(String login) {
		env.configuration.setLogin(login);
	}

	public void setPass(String pass) {
		env.configuration.setPass(pass);
	}

	public void setAuthBaseUri(String authBaseUri) {
		env.configuration.setAuthBaseUri(authBaseUri);
	}

	public void setThumbprint(String thumbprint) {
		env.configuration.setThumbprint(thumbprint);
	}

	public Environment getEnvironment() {
		return env;
	}

	public String acquireAccessToken() throws ExternSDKException {
		try {
			AuthenticationApi authenticationApi = new AuthenticationApi(env.configuration.getApiKey());
			env.accessToken = authenticationApi.getSid(env.configuration.getLogin(), env.configuration.getPass(), env.configuration.getAuthBaseUri());
			return env.accessToken;
		}
		catch (ApiException x) {
			throw new ExternSDKException(ExternSDKException.S_AUTHORIZATION_BY_LOGIN, x);
		}
		catch (Exception x) {
			throw new ExternSDKException(ExternSDKException.UNKNOWN, x);
		}
	}

	public void initialize() throws ExternSDKException {
		// aquires access token (session id)
		env.accessToken = acquireAccessToken();
		// configurates a crypto service
		initCryptoService();
		// initialize draft api
		draft = new ExternSDKDraft(this);
		// initialize docflow api
		docflow = new ExternSDKDocflow(this);
	}

	private Configuration loadConfiguration() throws ExternSDKException {
		try (InputStream is = ExternSDK.class.getResourceAsStream(EXTERN_SDK_CONFIG)) {
			if (is != null) {
				return gson.fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
			}
		}
		catch (IOException x) {
			throw new ExternSDKException(ExternSDKException.C_CONFIG_LOAD, x);
		}
		catch (Exception x) {
			throw new ExternSDKException(ExternSDKException.UNKNOWN, x);
		}
		return new Configuration();
	}

	private void initCryptoService() {
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
				System.out.println("Ошибка инициализации криптосервиса. Подробности: " + x.getMessage());
			}
		}
	}
}
