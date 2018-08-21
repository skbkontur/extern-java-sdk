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
 * @author Aleksey Sukhorukov
 */

package ru.kontur.extern_api.sdk.examples;

import org.jetbrains.annotations.NotNull;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.*;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.BusinessDriver;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.text.MessageFormat;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

/**
 * <pre>
 * Пример отправки декларации в ФНС и отпрвки всех регламентных документов
 *
 * На тестовом компьютере должна быть установлена программа КриптоПро.
 *
 * Для запуска примера необходимо сформировать файл, с ниже описанными свойствами,
 * и передать путь к файлу первым аргументом.
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
 * </pre>
 */
public class DocflowExample {

    public static void main(String[] args) {
        // first argument is a path to property file
        if (args.length == 0) {
            System.out.println("There is no path to the property file in the command line.");
            return;
        }

        File propFile = new File(args[0]);
        if (!propFile.exists() || !propFile.isFile()) {
            System.out.println(MessageFormat.format("The configuration file {0} is not exist.", args[0]));
            return;
        }

        Configure configure = Configure.build(propFile);
        EngineBuilder builder = new EngineBuilder(configure);
        ExternEngine engine = builder.build();
        Postman postman = new Postman(
                new SendToDepartment(configure, engine),
                new SendReplies(
                        configure.getSender(),
                        engine.getDocflowService(),
                        new Signer(engine.getCryptoProvider())
                )
        );
        postman.send();
    }

    private static class Postman {
        private SendToDepartment sendToDepartment;
        private SendReplies sendReplies;

        Postman(SendToDepartment sendToDepartment, SendReplies sendReplies) {
            this.sendToDepartment = sendToDepartment;
            this.sendReplies = sendReplies;
        }

        void send() {
            QueryContext<List<Docflow>> docflowsCxt = sendToDepartment.send();
            if (docflowsCxt.isFail()) {
                throw new ServiceException(docflowsCxt.getServiceError());
            }
            sendReplies.send(docflowsCxt.get());
        }
    }

    private static class SendToDepartment {
        private Configure configure;
        private ExternEngine engine;

        SendToDepartment(@NotNull Configure configure, @NotNull ExternEngine engine) {
            this.configure = configure;
            this.engine = engine;
        }

        QueryContext<List<Docflow>> send() {
            BusinessDriver businessDriver = engine.getBusinessDriver();
            ru.kontur.extern_api.sdk.service.File file = configure.getFile();
            try {
                return businessDriver.sendDocument(
                        file,
                        configure.getSender(),
                        configure.getRecipient(),
                        configure.getOrganization()
                );
            } catch (Throwable x) {
                throw new RuntimeException("Error sending the document to the department. File: " + file.getFileName(), x);
            }
        }
    }

    private static class SendReplies {
        private static final String STATUS_RESPONSE_ARRIVED = "urn:docflow-common-status:response-arrived";
        private static final String STATUS_RESPONSE_FINISHED = "urn:docflow-common-status:finished";

        private Sender sender;
        private DocflowService docflowService;
        private Signer signer;

        SendReplies(@NotNull Sender sender, @NotNull DocflowService docflowService, @NotNull Signer signer) {
            this.sender = sender;
            this.docflowService = docflowService;
            this.signer = signer;
        }

        void send(@NotNull List<Docflow> docflows) {
            // периодически запрашиваем статус документооборота (или нескольких).
            // статус "Ответ обработан" (response-arrived) означает, что пришли результаты проверки
            // отправленного документа и можно продолжать работать с данным документооборотом
            docflows.forEach(d -> waitStatus(d, STATUS_RESPONSE_ARRIVED, docflowService));
            //3. необходимо отправить в налоговую извещения о получении
            // для этого сначала получаем список ответных документов, подписываем каждый из них
            // и отправляем в налоговую
            for (Docflow d : docflows) {
                System.out.println("Start working with docflow " + d.getId() + "\n");
                boolean repeat;
                do {
                    // получам спосок документов для отправки
                    QueryContext<List<ReplyDocument>> replyDocumentsCxt
                            = new QueryContext<List<ReplyDocument>>()
                            .setDocflow(d)
                            .setCertificate(sender.getCertificate());
                    // метод был несогласованно удален. необходимо выяснить причину
                    // replyDocumentsCxt = docflowService.generateReplies(replyDocumentsCxt);
                    if (replyDocumentsCxt.isFail()) {
                        throw new RuntimeException("Error getting reply documents.", new ServiceException(replyDocumentsCxt.getServiceError()));
                    }
                    System.out.println("List of ReplyDocument received");
                    for (ReplyDocument replyDocument : replyDocumentsCxt.get()) {
                        System.out.println("Start sending DocumentToSend: id = " + replyDocument.getId() + ", filename = " + replyDocument.getFilename());
                        QueryContext<?> sendReplyDocflowCtx = new QueryContext<>();
                        // подписываем документ
                        byte[] signature = signer.sign(replyDocument.getContent(), sender.getThumbprint());
                        replyDocument.setSignature(signature);
                        sendReplyDocflowCtx.setReplyDocument(replyDocument);
                        // и отправляем его
                        sendReplyDocflowCtx = docflowService.sendReply(sendReplyDocflowCtx);
                        if (sendReplyDocflowCtx.isFail()) {
                            throw new RuntimeException("Error sending reply documents.", new ServiceException(sendReplyDocflowCtx.getServiceError()));
                        }
                        System.out.println("ReplyDocument sent \n");
                    }
                    System.out.println("All documents sent");

                    // после отправки последнего извещения документооборот должен завершится
                    String currentStatus = waitStatus(d, STATUS_RESPONSE_FINISHED, docflowService);
                    repeat = !STATUS_RESPONSE_FINISHED.equals(currentStatus);
                    if (repeat) {
                        // если статус ДО STATUS_RESPONSE_ARRIVED,
                        // то необходимо дослать еще ответные документы
                        QueryContext<Docflow> docflowCxt = docflowService.lookupDocflow(
                                new QueryContext<Docflow>()
                                        .setDocflowId(d.getId())
                        );
                        if (docflowCxt.isFail()) {
                            throw new ServiceException(docflowCxt.getServiceError());
                        }
                        d = docflowCxt.get();
                    }
                } while (repeat);
            }
            System.out.println("The docflow was finished.");
        }

        private String waitStatus(@NotNull Docflow docflow, @NotNull String docflowStatus, @NotNull DocflowService docflowService) {
            if (docflowStatus.equals(docflow.getStatus())) {
                return docflowStatus;
            }
            System.out.println("Start waiting: docflow = " + docflow.getId() + ", status = " + docflowStatus);
            try {
                QueryContext<Docflow> docflowCxt
                        = new QueryContext<Docflow>()
                        .setDocflowId(docflow.getId());
                while (true) {
                    docflowCxt = docflowService.lookupDocflow(docflowCxt);
                    if (docflowCxt.isFail()) {
                        System.out.println("Error lookuping docflow by id: " + docflow.getId() + "\n Details: " + docflowCxt.getServiceError());
                        Thread.sleep(5000);
                        continue;
                    }
                    String currentStatus = docflowCxt.get().getStatus();
                    // для того чтобы завершить документооборот нужно отослать все требующие документы
                    // наличие статуса STATUS_RESPONSE_ARRIVED означает наличие документов,
                    // которые пользователь должен подписать и отправить в КО
                    if (docflowStatus.equals(STATUS_RESPONSE_FINISHED) && currentStatus.equals(STATUS_RESPONSE_ARRIVED)) {
                        System.out.println("Stop waiting: need to send an answer, current status" + currentStatus);
                        return currentStatus;
                    } else if (!currentStatus.equals(docflowStatus)) {
                        System.out.println("\tStill waiting: current status = " + currentStatus);
                        Thread.sleep(1000);
                    } else {
                        System.out.println("Stop waiting: current status = " + currentStatus);
                        return currentStatus;
                    }
                }
            } catch (InterruptedException x) {
                System.out.println("Status waiting was interrupted.");
                return STATUS_RESPONSE_ARRIVED;
            }
        }
    }

    private static class Signer {
        private CryptoProvider cryptoProvider;

        Signer(@NotNull CryptoProvider cryptoProvider) {
            this.cryptoProvider = cryptoProvider;
        }

        byte[] sign(byte[] content, @NotNull String thumbprint) {
            QueryContext<byte[]> signCxt
                    = cryptoProvider.sign(
                    new QueryContext<byte[]>()
                            .setThumbprint(thumbprint)
                            .setContent(content)
            ).ensureSuccess();
            return signCxt.getContent();
        }
    }

    private static class Configure {
        private Properties props;

        private Configure(Properties props) {
            this.props = props;
        }

        static Configure build(File file) {
            try (InputStream is = new FileInputStream(file)) {
                Properties properties = new Properties();
                properties.load(is);
                return new Configure(properties);
            } catch (IOException x) {
                throw new IllegalArgumentException("A parameter file is bad.", x);
            }
        }

        UriProvider getUriProvider() {
            return () -> props.getProperty("service.base.uri");
        }

        AccountProvider getAccountProvider() {
            return () -> UUID.fromString(props.getProperty("account.id"));
        }

        ApiKeyProvider getApiKeyProvider() {
            return () -> props.getProperty("api.key");
        }

        AuthenticationProvider getAuthenticationProvider() {
            return new AuthenticationProviderByPass(
                    () -> props.getProperty("auth.base.uri"),
                    new LoginAndPasswordProvider() {
                        @Override
                        public String getLogin() {
                            return props.getProperty("auth.login");
                        }

                        @Override
                        public String getPass() {
                            return props.getProperty("auth.pass");
                        }
                    },
                    getApiKeyProvider()
            );
        }

        CryptoProvider getCryptoProvider() {
            return new CryptoProviderMSCapi();
        }

        UserIPProvider getUserIPProvider() {
            return () -> new QueryContext<String>().setResult(props.getProperty("sender.ip"), QueryContext.USER_IP);
        }

        Sender getSender() {
            Sender sender = new Sender();
            // ИНН отправителя
            sender.setInn(props.getProperty("sender.inn"));
            // КПП отправителя
            sender.setKpp(props.getProperty("sender.kpp"));
            // IP адресс отправителя
            sender.setIpaddress(props.getProperty("sender.ip"));
            // отпечаток сертификат отправителя
            sender.setThumbprint(props.getProperty("sender.thumbprint"));

            QueryContext<byte[]> x509DerCxt =
                    getCryptoProvider()
                            .getSignerCertificate(
                                    new QueryContext<byte[]>()
                                            .setThumbprint(sender.getThumbprint())
                            );
            if (x509DerCxt.isFail()) {
                throw new IllegalArgumentException("there is not found a sender certificate.");
            }
            sender.setCertificate(IOUtil.encodeBase64(x509DerCxt.get()));

            return sender;
        }

        Recipient getRecipient() {
            FnsRecipient recipient = new FnsRecipient();
            recipient.setIfnsCode(props.getProperty("ifns.code"));
            return recipient;
        }

        Organization getOrganization() {
            return new Organization(props.getProperty("company.inn"), props.getProperty("company.kpp"));
        }

        ru.kontur.extern_api.sdk.service.File getFile() {
            File file = new File(props.getProperty("document.path"));
            if (!file.isFile() || !file.exists()) {
                throw new IllegalArgumentException("The document file does not exist: " + file.getAbsolutePath());
            }
            try {
                return new ru.kontur.extern_api.sdk.service.File(
                        file.getName(),
                        Files.readAllBytes(file.toPath())
                );
            } catch (IOException x) {
                throw new IllegalArgumentException("Error loading document. File: " + file.getAbsolutePath(), x);
            }
        }
    }

    private static class EngineBuilder {
        private Configure configure;

        EngineBuilder(@NotNull Configure configure) {
            this.configure = configure;
        }

        ExternEngine build() {
            ExternEngine engine = new ExternEngine();
            // КОНФИГУРИРОВАНИЕ ДВИЖКА
            // устанавливаем URI для Экстерн API
            engine.setServiceBaseUriProvider(configure.getUriProvider());
            // устанавливаем идентификатор аккаунта
            engine.setAccountProvider(configure.getAccountProvider());
            // устанавливаем идентификатор внешнего сервиса
            engine.setApiKeyProvider(configure.getApiKeyProvider());
            // устанавливаем провайдер для аутентификации по логину и паролю/*
            engine.setAuthenticationProvider(configure.getAuthenticationProvider());
            // данную инициализацию делать необязательно,
            // если используется свой криптопровайдер
            engine.setCryptoProvider(configure.getCryptoProvider());
            // устанавливает провайдер для IP адреса
            engine.setUserIPProvider(configure.getUserIPProvider());

            return engine;
        }
    }
}
