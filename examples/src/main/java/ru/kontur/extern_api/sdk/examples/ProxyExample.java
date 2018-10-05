/*
 * The MIT License
 *
 * Copyright 2018 alexs.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.examples;

import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.model.Credential;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthentication;
import ru.kontur.extern_api.sdk.provider.crypt.cloud.CloudCryptoProvider;
import ru.kontur.extern_api.sdk.provider.crypt.rsa.CryptoProviderRSA;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

import javax.swing.*;
import java.io.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.UUID;
import java.util.function.Function;

import static ru.kontur.extern_api.sdk.Messages.C_RESOURCE_NOT_FOUND;
import static ru.kontur.extern_api.sdk.Messages.UNKNOWN;

/**
 *
 * @author alexs
 *
 * Данный пример демонстрирует как установить параметры для работы через прокси-сервер
 *
 * <code>
 * Возможные режимы прокси-сервера:
 * - https, с использование стандартного порта 3128;
 * - socks4 или socks5, с использованием стандартного порта 1080.
 *
 * В данном примере производится подпись контента с помощью облачной подписи зарегистрированным пользователем.
 * Подпись возвращается после получения СМС-кода сервером от пользователя. 
 * Для этого в диалоговое окно необходимо ввести СМС-код, которое сервер вышлет на телефон зарегистрированного пользователя.
 *
 * Для запуска примера необходимо в командной строке передать путь к файлу:
 *
 * extern.properties
 *
 * со следующим содержимым:
 *
 * # URI Экстерн сервиса
 * service.base.uri = http://extern-api.testkontur.ru
 * # идентификатор аккаунта
 * account.id = XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
 * # идентификатор внешнего сервиса
 * api.key = XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
 * # URI сервиса аутентификации
 * auth.base.uri = http://api.testkontur.ru/auth
 * # логин для аутентификации по логину и паролю
 * auth.login = *****
 * # пароль для аутентификации по логину и паролю
 * auth.pass = *****
 * # идентификатор пользователя во внешней системе
 * service.user.id=XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX
 * # отпечаток облачного сертификата
 * thumbprint.cloud = XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 * # параметры для доверительной аутентификации
 * credential.name = phone
 * credential.value = 03
 * # отпечаток сертификата RSA ключа
 * thumbprint.rsa = XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 * # пароль хранилищу сертификатов
 * jks.pass = *********
 * # пароль к личному ключу
 * rsa.key.pass = *******
 * </code>
 */
public class ProxyExample {

    private static final String CONTENT = "Asbest";

    private static final String EOL = "\r\n";

    public static void main(String[] args) throws IOException {
        // необходимо передать путь
        if (args.length == 0) {
            System.out.println("There is no path to the properter file in the command line.");
            return;
        }
        // файл с параметрами должен существовать
        File parameterFile = new File(args[0]);
        if (!parameterFile.exists() || !parameterFile.isFile()) {
            System.out.println("Parameter file not found: " + args[0]);
            return;
        }
        // создаем экземляр движка для работы с API Экстерна
        ExternEngine engine = new ExternEngine();
        // загружаем параметры для отправки документа на портал Экстерна
        Properties parameters = new Properties();
        try (InputStream is = new FileInputStream(parameterFile)) {
            parameters.load(is);
        }
        // КОНФИГУРИРОВАНИЕ ДВИЖКА
        engine.getConfiguration().setThumbprintCloud(parameters.getProperty("thumbprint.cloud"));
        // конфигурируем доверительную аутентификацию
        engine.setAuthenticationProvider(
            new TrustedAuthentication()
                .apiKeyProvider(() -> parameters.getProperty("api.key"))
                .serviceUserIdProvider(() -> parameters.getProperty("service.user.id"))
                .authBaseUriProvider(() -> parameters.getProperty("auth.base.uri"))
                .credentialProvider(() -> new Credential(parameters.getProperty("credential.name"), parameters.getProperty("credential.value")))
                .signatureKeyProvider(() -> parameters.getProperty("thumbprint.rsa"))
                .cryptoProvider(new CryptoProviderRSA(parameters.getProperty("jks.pass"), parameters.getProperty("rsa.key.pass")))
        );
        engine.setCryptoProvider(
            new CloudCryptoProvider("https://cc.testkontur.ru/")
                .certificateProvider(new CertificateProviderImpl())
                .approveCodeProvider(new ApproveCodeUserProviderImpl())
                .apiKeyProvider(() -> parameters.getProperty("api.key"))
                .authenticationProvider(engine.getAuthenticationProvider())
                .httpClient(engine.getHttpClient())
        );
        // устанавливаем URI для Экстерн API
        engine.setServiceBaseUriProvider(() -> parameters.getProperty("service.base.uri"));
        // устанавливаем идентификатор аккаунта
        engine.setAccountProvider(() -> UUID.fromString(parameters.getProperty("account.id")));
        // устанавливаем идентификатор внешнего сервиса
        engine.setApiKeyProvider(() -> parameters.getProperty("api.key"));
        // провайдер логина и пароля
        LoginAndPasswordProvider loginAndPasswordProvider = new LoginAndPasswordProvider() {
            @Override
            public String getLogin() {
                return parameters.getProperty("auth.login");
            }

            @Override
            public String getPass() {
                return parameters.getProperty("auth.pass");
            }
        };
        // устанавливаем провайдер для аутентификации по логину и паролю/*
        engine.setAuthenticationProvider(
            new AuthenticationProviderByPass(
                () -> parameters.getProperty("auth.base.uri"),
                loginAndPasswordProvider,
                engine.getApiKeyProvider()
            )
        );
        // устанвливаем криптопровайдер
        engine.setCryptoProvider(engine.getCryptoProvider());
        // посылаем запрос на подпись через http прокси-сервер
        sendViaHttpProxy(engine);
        // посылаем запрос на подпись через socks прокси-сервер
        sendViaSocksProxy(engine);
    }

    private static void sendViaHttpProxy(ExternEngine engine) {
        System.getProperties().remove("socksProxyHost");
        System.getProperties().remove("socksProxyPort");
        // устанавливаем адрес и порт прокси-сервера
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "3128");
        System.setProperty("http.keepalive", "false");
        // устанавливаем логин и пароль для аутентификации, если нужно
        java.net.Authenticator.setDefault(
            new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "asbest".toCharArray());
            }
        }
        );
        // делаем подпись на облаке
        sign(engine);

        System.out.println("Подписано через HTTP Proxy-Server");
    }

    private static void sendViaSocksProxy(ExternEngine engine) {
        System.getProperties().remove("http.proxyHost");
        System.getProperties().remove("http.proxyPort");
        // устанавливаем адрес и порт прокси-сервера
        System.setProperty("socksProxyHost", "127.0.0.1");
        System.setProperty("socksProxyPort", "1080");
        System.setProperty("http.keepalive", "false");
        // устанавливаем логин и пароль для аутентификации, если нужно
        java.net.Authenticator.setDefault(
            new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("admin", "asbest".toCharArray());
            }
        }
        );
        // делаем подпись на облаке
        sign(engine);

        System.out.println("Подписано через Socks Proxy-Server");
    }

    private static void sign(ExternEngine engine) {
        System.out.println("cloud signature");

        final byte[] signContent = CONTENT.getBytes();

        QueryContext<byte[]> signCxt
            = engine
                .getCryptoProvider()
                .sign(
                    new QueryContext<byte[]>()
                        .setThumbprint(engine.getConfiguration().getThumbprintCloud())
                        .setContent(signContent)
                );

        if (signCxt.isFail()) {
            signCxt.failure();
        }

        assert (signCxt.get() != null);
    }

    static class CertificateProviderImpl implements CertificateProvider {

        @Override
        public QueryContext<byte[]> getCertificate(String thumbprint) {
            String certPath = "/certs/" + thumbprint + ".cer";
            try (InputStream is = getClass().getResourceAsStream(certPath)) {
                if (is != null) {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    ru.argosgrp.cryptoservice.utils.IOUtil.copyStream(is, os);
                    return new QueryContext<byte[]>().setResult(os.toByteArray(),QueryContext.CONTENT);
                }
                else {
                    return new QueryContext<byte[]>().setServiceError(Messages.get(C_RESOURCE_NOT_FOUND, certPath));
                }
            }
            catch (IOException x) {
                return new QueryContext<byte[]>().setServiceError(Messages.get(UNKNOWN), x);
            }
        }

    }

    static class ApproveCodeUserProviderImpl implements Function<String, String> {

        @Override
        public String apply(String resultId) {
            String code = JOptionPane.showInputDialog(null, "Код: ", "Код подтверждения", JOptionPane.PLAIN_MESSAGE);
            if (code == null || code.isEmpty()) {
                return "";
            }
            return code;
        }

    }

    static class ApproveCodeProviderImpl implements Function<String, String> {

        private static final String APPROVE_CODE = "approvedCode";
        private static final String APPROVE_CODE_REQUEST = "";

        private final ExternEngine engine;

        public ApproveCodeProviderImpl(ExternEngine engine) {
            this.engine = engine;
        }

        @Override
        public String apply(String resultId) {
            String request = MessageFormat.format(APPROVE_CODE_REQUEST, resultId);

            QueryContext<String> cxt = new QueryContext<>("");

            CloudCryptoProvider cloudCryptoProvider = (CloudCryptoProvider) engine.getCryptoProvider();

            cxt.setServiceBaseUriProvider(() -> cloudCryptoProvider.getCloudCryptoBaseUri());
            cxt.setAuthenticationProvider(engine.getAuthenticationProvider());
            cxt.setApiKeyProvider(engine.getApiKeyProvider());

            HttpClient httpClient = cloudCryptoProvider.getHttpClient();

            QueryContext<String> approvedCxt
                = cxt.apply(
                    (QueryContext<?> c) -> {
                        try {
                            ApiResponse<String> resp
                            = httpClient
                                .setServiceBaseUri(cloudCryptoProvider.getCloudCryptoBaseUri())
                                .acceptAccessToken(engine.getAuthenticationProvider().authPrefix(), engine.getAuthenticationProvider().sessionId().get())
                                .acceptApiKey(c.getApiKeyProvider().getApiKey())
                                .submitHttpRequest(
                                    request,
                                    "GET",
                                    Collections.emptyMap(),
                                    null,
                                    new HashMap<>(),
                                    Collections.emptyMap(),
                                    String.class
                                );

                            return new QueryContext<String>(c, c.getEntityName()).setResult(resp.getData(), APPROVE_CODE);
                        }
                        catch (ApiException x) {
                            return new QueryContext<String>(c, c.getEntityName()).setServiceError(x);
                        }
                    }
                );

            if (approvedCxt.isFail()) {
                ServiceError e = approvedCxt.getServiceError();
                System.out.println(MessageFormat.format("Method: {0}" + EOL + "Details:" + EOL + "{1}" + EOL, "ApproveCodeProviderImpl", e.toString()));
                return "";
            }
            else {
                return approvedCxt.get();
            }
        }
    }
}
