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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowExample {

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

        // 1. После отправки черновика мы получаем идентификатор документооборота
        // (в некоторых случаях их может быть несколько)
        QueryContext<List<Docflow>> sendCxt = externEngine.getBusinessDriver()
                .sendDocument(configuratorService.getFile(),
                        configuratorService.getSender(),
                        configuratorService.getRecipient(),
                        configuratorService.getOrganization());
        System.out.println("Draft sent");
        List<String> docflowIds = new ArrayList<>();
        if (sendCxt.isFail()) {
            System.out.println("Error sending document: " + sendCxt.getServiceError().toString());
            docflowIds.add("8248b9de-1fc5-4a10-a8c2-59a293c06fdf");
        } else { // запоминаем для дальнейшей обработки
            docflowIds = sendCxt.getDocflows().stream().map(d -> d.getId().toString())
                    .collect(Collectors.toList());
            System.out.println("Id collected:");
            docflowIds.forEach(System.out::println);
        }

        DocflowService docflowService = externEngine.getDocflowService();
        // 2. периодически запрашиваем статус документооборота (или нескольких).
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
            Docflow docflow = docflowService.lookupDocflow(docflowCtx).ensureSuccess().get();

            // получам спосок документов для отправки
            QueryContext<ReplyDocument> replyDocumentCxt = new QueryContext<>();
            replyDocumentCxt.setDocflow(docflow);
            replyDocumentCxt.setCertificate(configuratorService.getSender().getCertificate());
            replyDocumentCxt = docflowService.generateReply(replyDocumentCxt).ensureSuccess();

            ReplyDocument replyDocument = replyDocumentCxt.get();

            QueryContext<?> sendDocflowCtx = new QueryContext<>();
            // подписываем документ
            byte[] signature = sign(externEngine.getCryptoProvider(), replyDocument.getContent(),
                    configuratorService.getSender().getThumbprint());
            replyDocument.setSignature(signature);
            sendDocflowCtx.setReplyDocument(replyDocument);
            // и отправляем его
            docflowService.sendReply(sendDocflowCtx).ensureSuccess();

            // после отправки последнего извещения документооборот считается завершенным.
            waitStatus(docflowId, DocflowStatus.FINISHED, docflowService);
        }

    }

    // получаем парамеьры из файла конфигурации
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
    private static void waitStatus(String docflowId, DocflowStatus status, DocflowService service)
            throws InterruptedException {
        System.out.println("Start waiting: docflow = " + docflowId + ", status = " + status);
        while (true) {
            QueryContext<Docflow> docflowCtx = new QueryContext<>();
            docflowCtx.setDocflowId(UUID.fromString(docflowId));
            DocflowStatus currentStatus = service.lookupDocflow(docflowCtx).get().getStatus();
            if (!currentStatus.equals(status)) {
                System.out.println("\tStill waiting: current status = " + currentStatus);
                Thread.sleep(1000);
            } else {
                System.out.println("Stop waiting: current status = " + currentStatus);
                return;
            }
        }
    }

    private static byte[] sign(@NotNull CryptoProvider cryptoProvider, byte[] content,
            @NotNull String thumbprint) {
        QueryContext<byte[]> signCxt
                = cryptoProvider.sign(
                new QueryContext<byte[]>()
                        .setThumbprint(thumbprint)
                        .setContent(content)
        ).ensureSuccess();
        return signCxt.getContent();
    }
}
