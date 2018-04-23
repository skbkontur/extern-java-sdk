/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
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
package ru.skbkontur.sdk.extern.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.FnsRecipient;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.Sender;
import ru.skbkontur.sdk.extern.providers.LoginAndPasswordProvider;
import ru.skbkontur.sdk.extern.providers.auth.AuthenticationProviderByPass;
import ru.skbkontur.sdk.extern.providers.crypt.mscapi.CryptoProviderMSCapi;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 * @author Sukhorukov A.D.
 *
 * Отправка документа, при этом отправитель и организация, за которую производится отправка документа, является одной и той-же организацией
 *
 * <pre>
 * {@code
 * Для запуска примера необходимо в командной строке передать путь к файлу:
 *
 * SendDocForYourself.properties
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
 * # ИНН отправителя
 * sender.inn = **********
 * # КПП отправителя
 * sender.kpp = *********
 * # IP отправителя
 * sender.ip = XX.XXX.XXX.XXX
 * # отпечаток сертификата отправителя
 * sender.thumbprint = XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX
 * # код ФНС, куда отправляется документ
 * ifns.code = 0087
 * # ИНН организации, для которой отправляется документ
 * company.inn = **********
 * # КПП организации, для которой отправляется документ
 * company.kpp = *********
 * # пути к документам, который необходимо отправить, разделенные через ;
 * document.path = X:\\path documents\\NO_SRCHIS_0087_0087_XXXXXXXXXXXXXXXXXXX_20180126_XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX.xml;
 * }
 * </pre>
 *
 */
class SendDocument {

    public static void main(String[] args) throws IOException, CryptoException, InterruptedException, ExecutionException {
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

        engine.configureServices();

        // отправитель
        Sender sender = new Sender();
        // ИНН отправителя
        sender.setInn(parameters.getProperty("sender.inn"));
        // КПП отправителя
        sender.setKpp(parameters.getProperty("sender.kpp"));
        // IP адресс отправителя
        sender.setIpaddress(parameters.getProperty("sender.ip"));
        // отпечаток сертификат отправителя
        sender.setThumbprint(parameters.getProperty("sender.thumbprint"));
        // получатель
        FnsRecipient recipient = new FnsRecipient();
        // ИНН отправителя
        recipient.setIfnsCode(parameters.getProperty("ifns.code"));
        // подотчетная организация
        Organization organization = new Organization(parameters.getProperty("company.inn"), parameters.getProperty("company.kpp"));

        // путь к документу, который будем отправлять
        String docPath = parameters.getProperty("document.path");
        String[] docPaths = docPath == null ? null : docPath.split(";");
        if (docPaths != null) {
            ru.skbkontur.sdk.extern.service.File[] files
                = Stream
                    .of(docPaths)
                    .map(File::new)
                    .map(f -> new ru.skbkontur.sdk.extern.service.File(f.getName(), readFileContent(f))) //
                    .collect(Collectors.toList())
                    .toArray(new ru.skbkontur.sdk.extern.service.File[docPaths.length]);

            // отправляем документы
            QueryContext<List<Docflow>> sendCxt = engine.getBusinessDriver().sendDocument(files, sender, recipient, organization);
            // проверяем результат отправки
            if (sendCxt.isFail()) {
                // ошибка отправки документа
                // регистрируем ошибку в лог файл
                System.out.println("Error sending document.\r\n" + sendCxt.getServiceError().toString());

                return;
            }

            // документ был отправлен
            // в результате получаем список документооборотов (ДО)
            // иногда один документ может вызвать несколько ДО
            System.out.println(MessageFormat.format("The documents: \r\n{0}\r\nwas sent.", Stream.of(files).map(ru.skbkontur.sdk.extern.service.File::getFileName).reduce((x, y) -> x + "\r\n" + y).orElse("")));
        }
    }

    private static byte[] readFileContent(File file) throws RuntimeException {
        try {
            return IOUtil.readFileContent(file);
        }
        catch (IOException x) {
            throw new RuntimeException(x);
        }
    }
}
