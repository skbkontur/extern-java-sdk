/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.examples;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.provider.CertificateProvider;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.auth.CertificateAuthenticationProvider.CertificateAuthenticationProviderBuilder;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.impl.DefaultServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

/*
    Для аутентификации по сертификату необходимо создать провайдер типа CertificateAuthenticationProvider
    и установить его в экземпляр класса ExternEngine
 */
public class CertificateAuthenticationExample {

    public static void main(String[] args) throws IOException {
        // 1. загружаем параметры
        Properties properties = getProperties();
        if (properties == null) {
            return;
        }

        // 2. Необходимо реализовать метод getCertificate() интерфейса CertificateProvider
        // Метод принимает в качестве параметра имя сертификата и возвращает контекст с
        // сертификатом в DER-кодировке или ошибку
        CertificateProvider certificateProvider = (String thumbprint) -> {
            URL resource = CertificateAuthenticationExample.class.getResource("/" + thumbprint);
            if (resource == null) {
                return new QueryContext<byte[]>().setServiceError(Messages
                    .get(Messages.C_CRYPTO_ERROR_CERTIFICATE_NOT_FOUND, thumbprint));
            }
            try {
                return new QueryContext<byte[]>()
                    .setResult(Files.readAllBytes(Paths.get(resource.toURI())),
                        QueryContext.CONTENT);
            } catch (URISyntaxException | IOException e) {
                return new QueryContext<byte[]>().setServiceError(
                    Messages.get(Messages.C_CRYPTO_ERROR_CERTIFICATE_NOT_FOUND, thumbprint), e);
            }
        };

        // 3. вызваем статический метод usingCertificate класса CertificateAuthenticationProvider.
        // данный метод принимает в качестве параметра ранее экземпляр класса CertificateProvider
        // и возвращает билдер CertificateAuthenticationProviderBuilder
        // Дополнительно можно передать параметр skipCertValidation, задающий режим отмены валидации сертификата.
        // Если параметр не задан, то сертификат будет проходить процедуру валидации.
        CertificateAuthenticationProviderBuilder certificateAuthenticationProviderBuilder = CertificateAuthenticationProvider
            .usingCertificate(certificateProvider);

        // 4. Теперь необходимо установить следующие провайдеры:
        CertificateAuthenticationProvider certificateAuthenticationProvider = certificateAuthenticationProviderBuilder
            // ApiKeyProvider - провайдер для получения идентификатора сервиса.
            .setApiKeyProvider(() -> properties.getProperty("api.key"))
            // CryptoProvider - реализацию криптографического провайдера.
            .setCryptoProvider(new CryptoProviderMSCapi())
            // ServiceBaseUriProvider - провайдер, возвращающий адрес сервиса аутентификации в Интернет.
            .setServiceBaseUriProvider(() -> properties.getProperty("auth.base.uri"))
            // SignatureKeyProvider - провайдер, предоставляющий отпечаток сертификата личного ключа.
            .setSignatureKeyProvider(() -> "fd3e438933387026ee46c03691f20743d7d34766")
            .buildAuthenticationProvider();

        // Для того чтобы начать работу с SDK, необходимо создать и сконфигурировать объект ExternEngine.
        ExternEngine engine = new ExternEngine();
        // Для конфигурации необходимо создать и передать с помощью соответствующего сеттера следующие провайдеры:
        // ServiceBaseUriProvider – предоставляет адрес сервиса в Интернет.
        engine.setServiceBaseUriProvider(() -> properties.getProperty("service.base.uri"));
        // AccountProvider — предоставляет идентификатор аккаунта, который передается при отправки данных на сервис.
        engine.setAccountProvider(() -> UUID.fromString(properties.getProperty("account.id")));
        // ApiKeyProvider – предоставляет идентификатор, который выдается сервису, от которого отправляются запросы к API СКБ Контура.
        engine.setApiKeyProvider(() -> properties.getProperty("api.key"));
        // AuthenticationProvider — предоставляет аутентификатор.
        engine.setAuthenticationProvider(certificateAuthenticationProvider
            .httpClient(new DefaultServicesFactory().getHttpClient()));

        // теперь можно использовать engine для дальнейшей работы
        QueryContext<AccountList> cxt = new QueryContext<>();
        cxt.setAccountId(UUID.fromString(properties.getProperty("account.id")));
        QueryContext<AccountList> queryContext = engine.getAccountService()
            .acquireAccounts(cxt);
    }

    // получаем параметры из файла конфигурации
    @Nullable
    private static Properties getProperties() throws IOException {
        if (CertificateAuthenticationExample.class.getResource("/prop.properties") == null) {
            System.out.println(
                String.format("Parameter file %s not found in resources", "prop.properties"));
            return null;
        }

        // loads properties
        Properties properties = new Properties();
        try (InputStream is = CertificateAuthenticationExample.class
            .getResourceAsStream("/prop.properties")) {
            properties.load(is);
        }

        System.out.println("Properties loaded");

        return properties;
    }

}
