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
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.DraftsBuilderModels.DraftsBuilderDocumentFileMetaRequest;

public interface DraftsBuilderDocumentFileService<
        TDraftsBuilderDocumentFile extends DraftsBuilderDocumentFile,
        TDraftsBuilderDocumentFileContents extends DraftsBuilderDocumentFileContents,
        TDraftsBuilderDocumentFileMeta extends DraftsBuilderDocumentFileMeta,
        TDraftsBuilderDocumentFileMetaRequest extends DraftsBuilderDocumentFileMetaRequest> {
    /**
     * <p>POST /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files</p>
     * Асинхронный метод создания файла документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param contents данные документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> createAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, TDraftsBuilderDocumentFileContents contents);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files</p>
     * Асинхронный метод поиска файлов документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @return файлы документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile[]> getAllAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод поиска файла документа билдера черновиков по идентификатору
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> getAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId);

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод обновления данных файла документа билдера черновиков по идентификатору
     *
     * @param draftsBuilderId идентификатор билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @param newContents данные документа билдера черновиков
     * @return файл документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFile> updateAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId, TDraftsBuilderDocumentFileContents newContents);

    /**
     * <p>DELETE /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}</p>
     * Асинхронный метод удаления файла документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return {@link Void}
     */
    CompletableFuture deleteAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/content</p>
     * Асинхронный метод получения контента файла
     *
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     * @param draftsBuilderDocumentFileId drafts builder document file identifier
     */
    CompletableFuture<byte[]> getContentAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/signature</p>
     * Асинхронный метод получения контента подписи файла
     *
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     * @param draftsBuilderDocumentFileId drafts builder document file identifier
     */
    CompletableFuture<byte[]> getSignatureAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId);

    /**
     * <p>GET /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/meta</p>
     * Асинхронный метод поиска мета-данных файла документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @return мета-данные файла документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFileMeta> getMetaAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId);

    /**
     * <p>PUT /v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/files/{draftsBuilderDocumentFileId}/meta</p>
     * Асинхронный метод обновления мета-данных файла документа билдера черновиков
     *
     * @param draftsBuilderId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentId идентификатор документа билдера черновиков
     * @param draftsBuilderDocumentFileId идентификатор файла документа билдера черновиков
     * @param newMeta мета-данные файла документа билдера черновиков
     * @return мета-данные файла документа билдера черновиков
     */
    CompletableFuture<TDraftsBuilderDocumentFileMeta> updateMetaAsync(UUID draftsBuilderId, UUID draftsBuilderDocumentId, UUID draftsBuilderDocumentFileId, TDraftsBuilderDocumentFileMetaRequest newMeta);
}
