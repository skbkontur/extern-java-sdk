/*
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
 *
 */

package ru.kontur.extern_api.sdk.utils;

import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.logging.Logger;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.model.Credential;

public class TestConfig {

    public static class ConfigLabel {

        static final String CONFIG_PATH = "extern.api.config.path";
        static final String ACCOUNT_ID = "extern.api.accountId";
        static final String API_KEY = "extern.api.apiKey";
        static final String LOGIN = "extern.api.login";
        static final String PASS = "extern.api.pass";
        static final String SERVICE_BASE_URI = "extern.api.serviceBaseUri";
        static final String AUTH_BASE_URI = "extern.api.authBaseUri";
        static final String THUMBPRINT = "extern.api.thumbprint";
        static final String THUMBPRINT_CLOUD = "extern.api.thumbprint.cloud";
        static final String THUMBPRINT_RSA = "extern.api.thumbprint.rsa";
        static final String JKS_PASS = "extern.api.jks.pass";
        static final String RSA_KEY_PASS = "extern.api.rsa.key.pass";
        static final String CREDENTIAL_SNILS = "extern.api.credential.snils";
        static final String CREDENTIAL_PHONE = "extern.api.credential.phone";
        static final String SERVICE_USER_ID = "extern.api.service.user.id";

    }

    private static final String DEFAULT_CONFIG_PATH = "/secret/extern-sdk-config.json";
    private static final Logger log = Logger.getLogger(TestConfig.class.getName());

    public static Configuration LoadConfigFromEnvironment() {
        String configPath = Optional
                .ofNullable(System.getProperty(ConfigLabel.CONFIG_PATH))
                .filter(String::isEmpty)
                .orElse(DEFAULT_CONFIG_PATH);

        log.info(String.format("Configuration file: %s", configPath));

        URL resource = TestConfig.class.getResource(configPath);
        final Configuration config = UncheckedSupplier
                .get(() -> Configuration.load(Objects.requireNonNull(resource)));

        tryToSet(config::setAccountId, ConfigLabel.ACCOUNT_ID);
        tryToSet(config::setApiKey, ConfigLabel.API_KEY);
        tryToSet(config::setLogin, ConfigLabel.LOGIN);
        tryToSet(config::setPass, ConfigLabel.PASS);
        tryToSet(config::setAuthBaseUri, ConfigLabel.AUTH_BASE_URI);
        tryToSet(config::setServiceBaseUri, ConfigLabel.SERVICE_BASE_URI);
        tryToSet(config::setThumbprint, ConfigLabel.THUMBPRINT);
        tryToSet(config::setThumbprintRsa, ConfigLabel.THUMBPRINT_RSA);
        tryToSet(config::setThumbprintCloud, ConfigLabel.THUMBPRINT_CLOUD);
        tryToSet(config::setJksPass, ConfigLabel.JKS_PASS);
        tryToSet(config::setRsaKeyPass, ConfigLabel.RSA_KEY_PASS);
        tryToSet(config::setServiceUserId, ConfigLabel.SERVICE_USER_ID);

        tryToSet(snils -> config.setCredential(new Credential("snils", snils)),
                ConfigLabel.CREDENTIAL_SNILS);

        tryToSet(snils -> config.setCredential(new Credential("phone", snils)),
                ConfigLabel.CREDENTIAL_PHONE);

        return config;
    }

    private static void tryToSet(Consumer<String> setter, String propertyName) {
        Optional.ofNullable(System.getProperty(propertyName)).ifPresent(setter);
    }
}
