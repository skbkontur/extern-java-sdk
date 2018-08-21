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
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.*;

/**
 * <pre>
 * Пример для получения списка не завершенных ДО,
 * и отправки регламентных документ в ФНС.
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
 * </pre>
 */
public class DocflowListExample {
    private static final String STATUS_RESPONSE_ARRIVED_VAL = "urn:docflow-common-status:response-arrived";
    private static final String STATUS_RESPONSE_FINISHED_VAL = "urn:docflow-common-status:finished";

    private enum Status {
        STATUS_UNKNOWN("unknown"), STATUS_RESPONSE_ARRIVED(STATUS_RESPONSE_ARRIVED_VAL), STATUS_RESPONSE_FINISHED(STATUS_RESPONSE_FINISHED_VAL);

        private String val;

        Status(String val) {
            this.val = val;
        }

        static Status findByVal(String val) {
            return Arrays.stream(Status.values()).filter(s -> s.val.equals(val)).findAny().orElse(null);
        }
    }

    private Set<Status> stopWaiting = new HashSet<Status>() {
        {
            add(Status.STATUS_RESPONSE_ARRIVED);
            add(Status.STATUS_RESPONSE_FINISHED);
        }
    };

    private Properties engineProperties;

    private DocflowListExample(Properties props) {
        this.engineProperties = props;
    }

    private Sender getSender(@NotNull CryptoProvider cryptoProvider) {
        Sender sender = new Sender();
        // ИНН отправителя
        sender.setInn(engineProperties.getProperty("sender.inn"));
        // КПП отправителя
        sender.setKpp(engineProperties.getProperty("sender.kpp"));
        // IP адресс отправителя
        sender.setIpaddress(engineProperties.getProperty("sender.ip"));
        // отпечаток сертификат отправителя
        sender.setThumbprint(engineProperties.getProperty("sender.thumbprint"));

        QueryContext<byte[]> x509DerCxt =
                cryptoProvider
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

    public static void main(String[] args) {
        // first argument is a path to property file
        if (args.length == 0) {
            System.out.println("There is no path to the property file in the command line.");
            return;
        }

        File propFile = new File(args[0]);
        if (!propFile.exists() || !propFile.isFile()) {
            System.out.println(MessageFormat.format("The file {0} is not exist.", args[0]));
            return;
        }

        Properties engineProperties = new Properties();
        try {
            engineProperties.load(new FileInputStream(propFile));
        } catch (IOException e) {
            System.out.println("There is engine properties loading error.");
            e.printStackTrace();
        }

        DocflowListExample example = new DocflowListExample(engineProperties);

        example.start();
    }

    private void start() {
        ExternEngine engine = buildEngine();
        DocflowService docflowService = engine.getDocflowService();
        // 1. Получаем список первых 2 (или меньше) незавершенных документооборотов
        // и запоминаем для дальнейшей обработки
        QueryContext<DocflowPage> docflowPageCxt = docflowService.getDocflows(
                new QueryContext<>()
                        .setFinished(false)
                        .setIncoming(false)
                        .setSkip(0L)
                        .setTake(3)
                        .setType("fns534-report")
        );
        // проверяем результат запроса
        if (docflowPageCxt.isFail()) {
            System.out.println("There is an error of  DocflowPage aquiring");
            System.out.println("Details: \n" + docflowPageCxt.getServiceError().toString());
            return;
        }
        System.out.println("DocflowPage received");
        // проверяем наличие ДО в контейнере DocflowPage
        if (docflowPageCxt.get().getDocflowsPageItem().isEmpty()) {
            System.out.println("There is no one unfinished docflow!");
            return;
        }
        List<DocflowPageItem> items = docflowPageCxt.get().getDocflowsPageItem();
        // список идентификаторов ДО
        // сохраняем в список
        System.out.println("Id collected:");
        items.stream().map(i -> i.getId().toString()).forEach(System.out::println);
        // 2. проверяем, что все документообороты имеют статус "Ответ обработан", если нет,
        // то ждем когда ДО приймет необходимый статус
        // статус "Ответ обработан" (response-arrived) означает, что пришли результаты проверки
        // отправленного документа и можно продолжать работать с данным документооборотом
        for (DocflowPageItem item : items) {
            waitStatus(item, STATUS_RESPONSE_ARRIVED_VAL, docflowService);
        }
        sendReplies(items, docflowService, engine);
    }

    private ExternEngine buildEngine() {
        // создаем экземляр движка для работы с API Экстерна
        ExternEngine engine = new ExternEngine();

        // КОНФИГУРИРОВАНИЕ ДВИЖКА
        // устанавливаем URI для Экстерн API
        engine.setServiceBaseUriProvider(() -> engineProperties.getProperty("service.base.uri"));
        // устанавливаем идентификатор аккаунта
        engine.setAccountProvider(() -> UUID.fromString(engineProperties.getProperty("account.id")));
        // устанавливаем идентификатор внешнего сервиса
        engine.setApiKeyProvider(() -> engineProperties.getProperty("api.key"));
        // провайдер логина и пароля
        LoginAndPasswordProvider loginAndPasswordProvider = new LoginAndPasswordProvider() {
            @Override
            public String getLogin() {
                return engineProperties.getProperty("auth.login");
            }

            @Override
            public String getPass() {
                return engineProperties.getProperty("auth.pass");
            }
        };
        // устанавливаем провайдер для аутентификации по логину и паролю/*
        engine.setAuthenticationProvider(
                new AuthenticationProviderByPass(
                        () -> engineProperties.getProperty("auth.base.uri"),
                        loginAndPasswordProvider,
                        engine.getApiKeyProvider()
                )
        );
        // данную инициализацию делать необязательно,
        // если используется свой криптопровайдер
        engine.setCryptoProvider(new CryptoProviderMSCapi());

        // устанавливает провайдер для IP адреса
        engine.setUserIPProvider(() -> new QueryContext<String>().setResult(engineProperties.getProperty("sender.ip"), QueryContext.USER_IP));
        return engine;
    }

    // ждем пока документооборот не изменит статус на указанный
    private Status waitStatus(@NotNull DocflowPageItem item, @NotNull String status, @NotNull DocflowService docflowService) {
        final long timeout = 1000L;

        try {

            if (item.getStatus().equals(status)) {
                System.out.println("Stop waiting: current status = " + status);
                return Status.findByVal(status);
            }
            System.out.println("Start waiting: docflow = " + item.getId() + ", status = " + item.getStatus());
            Thread.sleep(timeout);

            while (true) {
                QueryContext<Docflow> docflowCtx = new QueryContext<>();
                docflowCtx.setDocflowId(item.getId());
                docflowCtx = docflowService.lookupDocflow(docflowCtx);
                if (docflowCtx.isFail()) {
                    System.out.println("There is a docflow lookup error.");
                    System.out.println("Details: \n" + docflowCtx.getServiceError().toString());
                    Thread.sleep(timeout);
                }
                Status waitingStatus = Status.findByVal(status);
                String currentStatus = docflowCtx.get().getStatus();
                Status currentStatusEnum = Status.findByVal(currentStatus);
                if (waitingStatus == Status.STATUS_RESPONSE_FINISHED && stopWaiting.contains(currentStatusEnum)) {
                    System.out.println("Stop waiting: current status = " + currentStatus);
                    return currentStatusEnum;
                } else if (!currentStatus.equals(status)) {
                    System.out.println("\tStill waiting: current status = " + currentStatus);
                    Thread.sleep(1000);
                } else {
                    System.out.println("Stop waiting: current status = " + currentStatus);
                    return Status.findByVal(status);
                }
            }
        } catch (InterruptedException x) {
            System.out.println("Stop waiting: InterruptedException");
        }
        return Status.STATUS_UNKNOWN;
    }

    private void sendReplies(List<DocflowPageItem> items, DocflowService docflowService, ExternEngine engine) {
        // получаем отправителя
        Sender sender = getSender(engine.getCryptoProvider());
        //3. необходимо отправить в налоговую извещения о получении
        // для этого сначала получаем список ответных документов, подписываем каждый из них
        // и отправляем в налоговую
        for (DocflowPageItem item : items) {
            QueryContext<Docflow> docflowCxt = new QueryContext<>();
            docflowCxt.setDocflowId(item.getId());
            // получаем документооборот
            docflowCxt = docflowService.lookupDocflow(docflowCxt);
            if (docflowCxt.isFail()) {
                System.out.println("There is a docflow lookup error with id: " + item.getId());
                System.out.println("Details: \n" + docflowCxt.getServiceError().toString());
                continue;
            }
            Docflow docflow = docflowCxt.get();
            System.out.println("Start working with docflow " + docflow.getId()); // + ", send date: " + new SimpleDateFormat("dd.MM.YYYY").format(docflow.getSendDate()));
            // получам спосок документов для отправки
            QueryContext<List<ReplyDocument>> replyDocumentsCxt = new QueryContext<>(docflowCxt, "");
            replyDocumentsCxt.setCertificate(getSender(engine.getCryptoProvider()).getCertificate());
            replyDocumentsCxt = docflowService.generateReplies(replyDocumentsCxt);
            if (replyDocumentsCxt.isFail()) {
                System.out.println("Error getting reply documents of the docflow with id: " + item.getId());
                System.out.println("Details: \n" + replyDocumentsCxt.getServiceError());
                continue;
            }
            List<ReplyDocument> replyDocuments = replyDocumentsCxt.get();
            System.out.println("List of ReplyDocument received of a docflow with id: " + item.getId());
            sendDocflowReplies(item, replyDocuments, docflowService, engine.getCryptoProvider(), sender.getThumbprint());
        }
    }

    private void sendDocflowReplies(DocflowPageItem item, List<ReplyDocument> replyDocuments, DocflowService docflowService, CryptoProvider cryptoProvider, String thumbprint) {
        boolean resend = false;
        Status statusEnum;
        do {
            if (resend) {
                System.out.println("Resending ReplyDocument of the docflow with id = " + item.getId());
            }
            boolean isError = false;
            for (ReplyDocument replyDocument : replyDocuments) {
                System.out.println("Start sending ReplyDocument: id = " + replyDocument.getId() + ", filename = " + replyDocument.getFilename());
                QueryContext<?> sendDocflowCtx = new QueryContext<>();
                // подписываем каждый документ
                byte[] signature = sign(cryptoProvider, replyDocument.getContent(), thumbprint);
                replyDocument.setSignature(signature);
                sendDocflowCtx.setReplyDocument(replyDocument);
                // и отправляем его
                sendDocflowCtx = docflowService.sendReply(sendDocflowCtx);
                if (sendDocflowCtx.isFail()) {
                    System.out.println("Error sending the reply document with id: " + replyDocument.getId());
                    System.out.println("Details: \n" + sendDocflowCtx.getServiceError());
                    isError = true;
                    continue;
                }
                System.out.println("ReplyDocument sent");
            }
            System.out.println(isError ? "Some reply documents was not be send." : "All documents sent.");
            // после отправки последнего извещения документооборот считается завершенным.
            statusEnum = waitStatus(item, STATUS_RESPONSE_FINISHED_VAL, docflowService);
            resend = true;
            // если статус ДО не завершен, то проверяем есть ли еще неотправленные документы для этого ДО
        } while (statusEnum != Status.STATUS_RESPONSE_FINISHED);
    }

    private static byte[] sign(@NotNull CryptoProvider cryptoProvider, byte[] content, @NotNull String thumbprint) {
        QueryContext<byte[]> signCxt
                = cryptoProvider.sign(
                new QueryContext<byte[]>()
                        .setThumbprint(thumbprint)
                        .setContent(content)
        ).ensureSuccess();
        return signCxt.getContent();
    }
}
