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
 *
 */

package ru.kontur.extern_api.sdk.examples;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * @author Mikhail Pavlenko
 *
 * Данный класс позволяет получить сконфигурированные на основе переданный в конструктор параметров
 * объекты классов, необходимых длz документооборота
 */

class ConfiguratorService {

    private final Properties parameters;

    ConfiguratorService(Properties parameters) {
        this.parameters = parameters;
    }

    ExternEngine getExternEngine() {
        // создаем экземляр движка для работы с API Экстерна
        ExternEngine engine = new ExternEngine();

        // КОНФИГУРИРОВАНИЕ ДВИЖКА
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
        // данную инициализацию делать необязательно,
        // если используется свой криптопровайдер
        engine.setCryptoProvider(new CryptoProviderMSCapi());

        return engine;
    }

    /**
     * Отправитель
     *
     * @return Sender
     */
    Sender getSender() {
        Sender sender = new Sender();
        // ИНН отправителя
        sender.setInn(parameters.getProperty("sender.inn"));
        // КПП отправителя
        sender.setKpp(parameters.getProperty("sender.kpp"));
        // IP адресс отправителя
        sender.setIpaddress(parameters.getProperty("sender.ip"));
        // отпечаток сертификат отправителя
        sender.setThumbprint(parameters.getProperty("sender.thumbprint"));

        QueryContext<byte[]> x509DerCxt = getExternEngine().getCryptoProvider()
            .getSignerCertificate(new QueryContext<byte[]>().setThumbprint(sender.getThumbprint()));
        sender.setCertificate(IOUtil.encodeBase64(x509DerCxt.get()));
        return sender;
    }

    /**
     * Получатель
     *
     * @return FnsRecipient
     */

    FnsRecipient getRecipient() {
        FnsRecipient recipient = new FnsRecipient();
        // ИНН отправителя
        recipient.setIfnsCode(parameters.getProperty("ifns.code"));
        return recipient;
    }

    /**
     * Подотчетная организация
     *
     * @return Organization
     */
    Organization getOrganization() {
        return new Organization(parameters.getProperty("company.inn"),
            parameters.getProperty("company.kpp"));
    }

    /**
     * Возвращает файл для отправки
     *
     * @return ru.skbkontur.sdk.extern.service.File
     */
    ru.kontur.extern_api.sdk.service.File getFile() {
        File file = new File(parameters.getProperty("document.path"));
        return new ru.kontur.extern_api.sdk.service.File(file.getName(),
            readFileContent(file));
    }

    private static byte[] readFileContent(File file) throws RuntimeException {
        try {
            return IOUtil.readFileContent(file);
        } catch (IOException x) {
            throw new RuntimeException(x);
        }
    }

}
