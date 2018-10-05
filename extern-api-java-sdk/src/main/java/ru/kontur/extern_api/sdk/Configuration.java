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
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.Credential;


/**
 * @author Aleksey Sukhorukov
 */
public class Configuration {

    private UUID accountId;
    private String apiKey;
    private Credential credential;
    private String serviceUserId;
    private String login;
    private String pass;
    private String serviceBaseUri;
    private String authBaseUri;
    private String thumbprint; // a thumbprint of a signature certificate for CryptoPro
    private String thumbprintCloud; // a thumbprint of a signature certificate for CloudCrypto
    private String thumbprintRsa; // a thumbprint of a signature certificate with RSA algorithm
    private String jksPass; // a password of JKS
    private String rsaKeyPass; // a password of a RSA key

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = UUID.fromString(accountId);
    }

    public UUID getAccountId() {
        return accountId;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
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

    public void setServiceUserId(String serviceUserId) {
        this.serviceUserId = serviceUserId;
    }

    public static Configuration load(URL resourceUrl) throws IOException {
        try (InputStream is = resourceUrl.openStream()) {
            if (is == null) {
                throw new IOException(resourceUrl.toExternalForm() + " is not found");
            }

            return new Gson()
                    .fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
        }
    }
}
