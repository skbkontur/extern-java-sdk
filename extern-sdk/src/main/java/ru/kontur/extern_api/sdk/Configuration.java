/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.Credential;


/**
 * @author Aleksey Sukhorukov
 */
public class Configuration implements Serializable, AccountProvider, ApiKeyProvider, LoginAndPasswordProvider {

    private static final String DEFAULT_AUTH_PREFIX = "auth.sid ";
    private static final long serialVersionUID = -1659495610747854951L;

    @SerializedName("accountId")
    private UUID accountId;
    @SerializedName("apiKey")
    private String apiKey;
    @SerializedName("authPrefix")
    private String authPrefix;
    @SerializedName("credential")
    private Credential credential;
    @SerializedName("serviceUserId")
    private String serviceUserId;
    @SerializedName("login")
    private String login;
    @SerializedName("pass")
    private String pass;
    @SerializedName("serviceBaseUri")
    private String serviceBaseUri;
    @SerializedName("authBaseUri")
    private String authBaseUri;
    @SerializedName("thumbprint")
    private String thumbprint; // a thumbprint of a signature certificate for CryptoPro
    @SerializedName("thumbprintCloud")
    private String thumbprintCloud; // a thumbprint of a signature certificate for CloudCrypto
    @SerializedName("thumbprintRsa")
    private String thumbprintRsa; // a thumbprint of a signature certificate with RSA algorithm
    @SerializedName("jksPass")
    private String jksPass; // a password of JKS
    @SerializedName("rsaKeyPass")
    private String rsaKeyPass; // a password of a RSA key

    public Configuration() {
        authPrefix = DEFAULT_AUTH_PREFIX;
    }

    @Override
    public UUID accountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = UUID.fromString(accountId);
    }

    @Override
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

    @Override
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getServiceBaseUri() {
        return serviceBaseUri;
    }

    public void setServiceBaseUri(String serviceBaseUri) {
        this.serviceBaseUri = serviceBaseUri;
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

    public String getThumbprintCloud() {
        return thumbprintCloud;
    }

    public void setThumbprintCloud(String thumbprintCloud) {
        this.thumbprintCloud = thumbprintCloud;
    }

    public String getThumbprintRsa() {
        return thumbprintRsa;
    }

    public void setThumbprintRsa(String thumbprintRsa) {
        this.thumbprintRsa = thumbprintRsa;
    }

    public String getJksPass() {
        return jksPass;
    }

    public void setJksPass(String jksPass) {
        this.jksPass = jksPass;
    }

    public String getRsaKeyPass() {
        return rsaKeyPass;
    }

    public void setRsaKeyPass(String rsaKeyPass) {
        this.rsaKeyPass = rsaKeyPass;
    }

    public Credential getCredential() {
        return credential;
    }

    public void setCredential(Credential credential) {
        this.credential = credential;
    }

    public String getServiceUserId() {
        return serviceUserId;
    }

    /**
     * Устанавливает идентификатор пользователя во внешней системе
     *
     * @param serviceUserId идетификатор пользователя во внешней системе
     */
    public void setServiceUserId(String serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public static Configuration load(URL resourceUrl) throws IOException {
        try (InputStream is = resourceUrl.openStream()) {
            if (is == null) {
                throw new IOException(resourceUrl.toExternalForm() + " is not found");
            }

            return new Gson().fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
        }
    }
}
