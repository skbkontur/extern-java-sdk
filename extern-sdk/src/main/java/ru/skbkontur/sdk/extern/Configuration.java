/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class Configuration {
	public static final String DEFAULT_AUTH_PREFIX = "auth.sid ";
	
  @SerializedName("accountId")	private UUID accountId;
	@SerializedName("apiKey") private String apiKey;
	@SerializedName("authPrefix") private String authPrefix;
	@SerializedName("login") private String login;
	@SerializedName("pass") private String pass;
	@SerializedName("authBaseUri") private String authBaseUri;
	@SerializedName("thumbprint") private String thumbprint; // a thumbprint of a signature certificate

	public Configuration() {
		authPrefix = DEFAULT_AUTH_PREFIX;
	}
	
	public UUID getAccountId() {
		return accountId;
	}

	public void setAccountId(UUID accountId) {
		this.accountId = accountId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getAuthPrefix() {
		return authPrefix;
	}

	public void setAuthPrefix(String authPrefix) {
		this.authPrefix = authPrefix;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getAuthBaseUri() {
		return authBaseUri;
	}

	public void setAuthBaseUri(String authBaseUri) {
		this.authBaseUri = authBaseUri;
	}

	public String getThumbprint() {
		return thumbprint;
	}

	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}
}
