/*
 * Copyright (c) 2019 SKB Kontur
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
 */

package ru.kontur.extern_api.sdk.service.DraftsBuilder;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentMetaRequest;

/**
 * <p>Группа методов предоставляет доступ к операциям для работы с документами билдера черновиков</p>
 */
public interface DraftsBuilderDocumentService<
        TDraftsBuilderDocument extends DraftsBuilderDocument,
        TDraftsBuilderDocumentMeta extends DraftsBuilderDocumentMeta,
        TDraftsBuilderDocumentMetaRequest extends DraftsBuilderDocumentMetaRequest> {

    /**
     * <p>POST /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents</p>
     * Асинхронный метод создания документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param meta мета-данные документа билдера черновиков
     * @return документ билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocument> createAsync(
            UUID draftsBuilderId,
            TDraftsBuilderDocumentMetaRequest meta
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents</p>
     * Асинхронный метод поиска документов билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @return документы билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocument[]> getAllAsync(
            UUID draftsBuilderId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}</p>
     * Асинхронный метод поиска документа билдера черновиков по идентификатору
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @return документ билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocument> getAsync(
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    );

    /**
     * <p>DELETE /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}</p>
     * Асинхронный метод удаления документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @return {@link Void}
     */
    CompletableFuture deleteAsync(
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/meta</p>
     * Асинхронный метод поиска мета-данных документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @return мета-данные документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentMeta> getMetaAsync(
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/meta</p>
     * Асинхронный метод обновления мета-данных документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param newMeta мета-данные документа билдера черновиков
     * @return мета-данные документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentMeta> updateMetaAsync(
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            TDraftsBuilderDocumentMetaRequest newMeta
    );
}
