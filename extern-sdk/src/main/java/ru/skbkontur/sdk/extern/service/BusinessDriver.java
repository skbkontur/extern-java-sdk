/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
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
package ru.skbkontur.sdk.extern.service;

import java.lang.reflect.Array;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.ExternEngine;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.DraftDocument;
import ru.skbkontur.sdk.extern.model.Organization;
import ru.skbkontur.sdk.extern.model.Recipient;
import ru.skbkontur.sdk.extern.model.Sender;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public class BusinessDriver {

    private final ExternEngine engine;

    public BusinessDriver(ExternEngine engine) {
        this.engine = engine;
    }

    public QueryContext<List<Docflow>> sendDocument(File[] files, Sender sender, Recipient recipient, Organization organization) throws InterruptedException, ExecutionException {
        DraftService draft = engine.getDraftService();
        // получаем сертификат подписанта
        QueryContext<byte[]> x509DerCxt = engine.getCryptoProvider().getSignerCertificate(new QueryContext<byte[]>().setThumbprint(sender.getThumbprint()));
        if (x509DerCxt.isFail()) {
            return new QueryContext<List<Docflow>>().setServiceError(x509DerCxt);
        }
        sender.setCertificate(IOUtil.encodeBase64(x509DerCxt.get()));
        // запрос на создание черновика
        CompletableFuture<QueryContext<UUID>> draftCxtFuture = draft.createAsync(sender, recipient, organization);
        // ожидаем создания черновика,подписываем и добавляем контент документа в черновик
        List<CompletableFuture<QueryContext<DraftDocument>>> ddl
            = Stream
                .of(files)
                .map(
                    f -> draftCxtFuture
                        .thenCombine(
                            engine.getCryptoProvider().signAsync(sender.getThumbprint(), f.getContent()),
                            (QueryContext<UUID> draftCxt, QueryContext<byte[]> signCxt) -> draft.addDecryptedDocument(draftCxt.setDocumentContents(DocumentContentsBuilder.build(f, signCxt.get())))
                        )
                )
                .collect(Collectors.toList());
        // массив задач: создание черновика и подпись документов
        // преобразовываем список в массив по-тупому, чтобы не было WARNING
        @SuppressWarnings("unchecked")
        CompletableFuture<QueryContext<DraftDocument>>[] dda = (CompletableFuture<QueryContext<DraftDocument>>[]) Array.newInstance(CompletableFuture.class, ddl.size());
        for (int i=0; i<ddl.size(); i++) {
            dda[i] = ddl.get(i);
        }
        // ожидаем: создание черновика и подписи документов
        CompletableFuture
            .allOf(dda)
            // ожидаем завершения всех операций
            .join();
        // 1) делаем подготовку данных перед отправкой
        // 2) отправляем черновик
        return draftCxtFuture
            // выполняем подготовку документа к отправке (шифрование, ...)
            .thenApply(draft::prepare)
            // отправляем черновик получателю, формируем ДО
            .thenApply(draft::send)
            // ожидаем получения результат
            .get();
    }
}
