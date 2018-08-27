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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowListExample {

    public static void main(String[] args)
            throws IOException, InterruptedException, ExecutionException {
        // first argument is a path to property file
        if (args.length == 0) {
            System.out.println("There is no path to the property file in the command line.");
            return;
        }

        // настраиваем engine сервис
        ConfiguratorService configuratorService = new ConfiguratorService(getProperties(args[0]));
        ExternEngine externEngine = configuratorService.getExternEngine();
        DocflowService docflowService = externEngine.getDocflowService();
        // 1. Получаем список первых 10 (или меньше) незавершенных документооборотов
        // и запоминаем для дальнейшей обработки
        DocflowPage docflowPage = docflowService.getDocflows(
                new QueryContext<>()
                        .setFinished(false)
                        .setIncoming(false)
                        .setSkip(0L)
                        .setTake(10)
                        .setType("fns534-report")
        ).get();
        System.out.println("DocflowPage received");

        if (docflowPage.getDocflowsPageItem().isEmpty()) {
            System.out.println("There is no one unfinished docflow!");
            return;
        }
        // сохраняем в список
        System.out.println("Id collected:");
        List<String> docflowIds = docflowPage.getDocflowsPageItem().stream()
                .map(d -> d.getId().toString()).collect(Collectors.toList());
        docflowIds.forEach(System.out::println);

        // 2. проверяем, что все документообороты имеют статус "Ответ обработан", если нет,
        // то ждем когда ДО приймет необходимый статус
        // статус "Ответ обработан" (response-arrived) означает, что пришли результаты проверки
        // отправленного документа и можно продолжать работать с данным документооборотом
        for (String docflowId : docflowIds) {
            waitStatus(docflowId, DocflowStatus.ARRIVED, docflowService);
        }

        //3. необходимо отправить в налоговую извещения о получении
        // для этого сначала получаем список ответных документов, подписываем каждый из них
        // и отправляем в налоговую
        for (String docflowId : docflowIds) {
            QueryContext<Docflow> docflowCtx = new QueryContext<>();
            docflowCtx.setDocflowId(UUID.fromString(docflowId));
            // получаем документооборот
            Docflow docflow = docflowService.lookupDocflow(docflowCtx).get();
            System.out.println("Start working with docflow " + docflowId);
            // получам спосок документов для отправки
            QueryContext<List<DocumentToSend>> listDocToSendCtx = new QueryContext<>();
            listDocToSendCtx.setDocflow(docflow);
            listDocToSendCtx.setCertificate(configuratorService.getSender().getCertificate());
            ReplyDocument replyDocument = docflowService.generateReply(listDocToSendCtx).get();
            System.out.println("List of DocumentToSend received");
            System.out.println(
                    "Start sending DocumentToSend: id = " + replyDocument.getId()
                            + ", filename = " + replyDocument.getFilename());
            QueryContext<?> sendDocflowCtx = new QueryContext<>();
            // подписываем каждый документ
            replyDocument.setSignature("signature".getBytes());
            sendDocflowCtx.setReplyDocument(replyDocument);
            // и отправляем его
            docflowService.sendReply(sendDocflowCtx);
            System.out.println("ReplyDocument sent");
            System.out.println("All documents sent");

            // после отправки последнего извещения документооборот считается завершенным.
            waitStatus(docflowId, DocflowStatus.FINISHED, docflowService);
        }

    }

    // получаем параметры из файла конфигурации
    @Nullable
    private static Properties getProperties(@NotNull String path) throws IOException {
        File parameterFile = new File(path);
        if (!parameterFile.exists() || !parameterFile.isFile()) {
            System.out.println("Parameter file not found: " + path);
            return null;
        }

        // loads properties
        Properties properties = new Properties();
        try (InputStream is = new FileInputStream(parameterFile)) {
            properties.load(is);
        }

        System.out.println("Properties loaded");

        return properties;
    }

    // ждем пока документооборот не изменит статус на указанный
    private static void waitStatus(String docflowId, DocflowStatus status, DocflowService docflowService)
            throws InterruptedException {

        System.out.println("Start waiting: docflow = " + docflowId + ", status = " + status);
        while (true) {
            QueryContext<Docflow> docflowCtx = new QueryContext<>();
            docflowCtx.setDocflowId(UUID.fromString(docflowId));
            DocflowStatus currentStatus = docflowService.lookupDocflow(docflowCtx).get().getStatus();
            if (!currentStatus.equals(status)) {
                System.out.println("\tStill waiting: current status = " + currentStatus);
                Thread.sleep(1000);
            } else {
                System.out.println("Stop waiting: current status = " + currentStatus);
                return;
            }
        }
    }
}
