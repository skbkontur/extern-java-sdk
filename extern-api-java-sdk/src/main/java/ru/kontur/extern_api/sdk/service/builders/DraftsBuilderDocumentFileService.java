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

package ru.kontur.extern_api.sdk.service.builders;

import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMetaRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * <p>Группа методов предоставляет доступ к операциям для работы с файлами документа билдера черновиков</p>
 */
public interface DraftsBuilderDocumentFileService<
        TDraftsBuilderDocumentFile extends DraftsBuilderDocumentFile,
        TDraftsBuilderDocumentFileContents extends DraftsBuilderDocumentFileContents,
        TDraftsBuilderDocumentFileMeta extends DraftsBuilderDocumentFileMeta,
        TDraftsBuilderDocumentFileMetaRequest extends DraftsBuilderDocumentFileMetaRequest,
        TDraftsBuilderDocumentService extends DraftsBuilderDocumentService> {

    /**
     * Получить идентификатор билдера черновиков, с которым работает текущий сервис
     *
     * @return идентификатор билдера черновиков
     */
    UUID getDraftsBuilderId();

    /**
     * Получить идентификатор документа билдера черновиков, с которым работает текущий сервис
     *
     * @return идентификатор документа билдера черновиков
     */
    UUID getDraftsBuilderDocumentId();

    /**
     * Получить сервис для работы с документами билдера черновиков
     *
     * @return сервис для работы с документами билдера черновиков
     */
    TDraftsBuilderDocumentService getDocumentService();

    /**
     * <p>POST /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files</p>
     * Асинхронный метод создания файла документа билдера черновиков
     *
     * @param contents данные документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> createAsync(
            TDraftsBuilderDocumentFileContents contents
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files</p>
     * Асинхронный метод поиска файлов документа билдера черновиков
     *
     * @return файлы документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile[]> getAllAsync();

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод поиска файла документа билдера черновиков по идентификатору
     *
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> getAsync(
            UUID draftsBuilderDocumentFileId
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод обновления данных файла документа билдера черновиков по идентификатору
     *
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @param newContents данные документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> updateAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileContents newContents
    );

    /**
     * <p>DELETE /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод удаления файла документа билдера черновиков
     *
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return {@link Void}
     */
    CompletableFuture<Void> deleteAsync(
            UUID draftsBuilderDocumentFileId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/content</p>
     * Асинхронный метод получения контента файла
     *
     * @param draftsBuilderDocumentFileId drafts builder document file identifier
     * @return Контент файла
     */
    CompletableFuture<byte[]> getContentAsync(
            UUID draftsBuilderDocumentFileId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/signature</p>
     * Асинхронный метод получения контента подписи файла
     *
     * @param draftsBuilderDocumentFileId drafts builder document file identifier
     * @return Контент подписи
     */
    CompletableFuture<byte[]> getSignatureAsync(
            UUID draftsBuilderDocumentFileId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/meta</p>
     * Асинхронный метод поиска мета-данных файла документа билдера черновиков
     *
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return мета-данные файла документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFileMeta> getMetaAsync(
            UUID draftsBuilderDocumentFileId
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/meta</p>
     * Асинхронный метод обновления мета-данных файла документа билдера черновиков
     *
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @param newMeta мета-данные файла документа билдера черновиков
     * @return мета-данные файла документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFileMeta> updateMetaAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileMetaRequest newMeta
    );
}
