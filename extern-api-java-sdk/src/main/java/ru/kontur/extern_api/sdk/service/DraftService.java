/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
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

package ru.kontur.extern_api.sdk.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.AccountInfo;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.TogsRecipient;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;


/**
 * Группа методов предоставляет доступ к операциям для работы с черновиками
 */
public interface DraftService {


    /**
     * <p>POST /v1/{accountId}/drafts</p>
     * Асинхронный метод создает черновик
     *
     * @param sender отправитель декларации {@link Sender}
     * @param recipient получатель декларации {@link FnsRecipient} | {@link TogsRecipient}
     * @param organization организация, на которую создана декларация {@link Organization}
     * @return идентификатор черновика
     */
    CompletableFuture<QueryContext<UUID>> createAsync(
            Sender sender,
            Recipient recipient,
            AccountInfo organization
    );

    /**
     * <p>POST /v1/{accountId}/drafts</p>
     * Асинхронный метод создает черновик
     *
     * @param draftMeta мета-данные черновика
     * @return идентификатор черновика
     */
    CompletableFuture<QueryContext<Draft>> createAsync(DraftMeta draftMeta);

    /**
     * <p>POST /v1/{accountId}/drafts</p>
     * Синхронный метод создает черновик
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- объект мета-данные черновика, полученный с помощью конструктора {@link
     *         DraftMeta#DraftMeta(Sender, Recipient, Organization)}, где:</p>
     *         <ul>
     *         <li>sender отправитель декларации {@link Sender};</li>
     *         <li>recipient получатель декларации {@link FnsRecipient}  | {@link
     *         TogsRecipient};</li>
     *         <li>organization организация, на которую создана декларация {@link Organization}.</li>
     *         </ul>
     *         <p>Для установки необходимо использовать метод {@link QueryContext#setDraftMeta}.</p>
     * @return идентификатор черновика
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<UUID> create(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}</p>
     * Асинхронный метод поиска черновика по идентификатору
     *
     * @param draftId идентификатор черновика
     * @return черновик
     * @see Draft
     */
    CompletableFuture<QueryContext<Draft>> lookupAsync(UUID draftId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}</p>
     * Асинхронный метод поиска черновика по идентификатору
     *
     * @param draftId идентификатор черновика
     * @return черновик
     * @see Draft
     */
    CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}</p>
     * Синхронный метод поиска черновика по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return идентификатор черновика
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Draft> lookup(QueryContext<?> cxt);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод удаления черновика
     *
     * @param draftId идентификатор черновика
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteAsync(UUID draftId);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод удаления черновика
     *
     * @param draftId идентификатор черновика
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteAsync(String draftId);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод удаления черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return {@link Void}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> delete(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод поиска мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(UUID draftId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод поиска мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод поиска мета-данных черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return мета-данные черновика
     * @see DraftMeta
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод поиска мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(
            UUID draftId,
            DraftMeta draftMeta
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод обновления мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @param draftMeta мета-данные черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(
            String draftId,
            DraftMeta draftMeta
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/meta</p>
     * Синхронный метод обновления мета-данных черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>  - мета-данные черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftMeta}.</p>
     * @return мета-данные черновика
     * @see DraftMeta
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

    /**
     * <P>POST /v1/{accountId}/drafts/{draftId}/check</P>
     * Асинхронный метод проверки черновика
     *
     * @param draftId идентификатор черновика
     * @return результат проверки
     */
    CompletableFuture<QueryContext<CheckResultData>> checkAsync(UUID draftId);

    /**
     * <P>POST /v1/{accountId}/drafts/{draftId}/check</P>
     * Асинхронный метод проверки черновика
     *
     * @param draftId идентификатор черновика
     * @return результат проверки
     */
    CompletableFuture<QueryContext<CheckResultData>> checkAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/check</p>
     * Асинхронный метод проверки черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return результат проверки
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<CheckResultData> check(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare</p>
     * Асинхронный метод подготовки черновика к отправке
     *
     * @param draftId идентификатор черновика
     * @return результат подготовки
     * @see PrepareResult
     */
    CompletableFuture<QueryContext<PrepareResult>> prepareAsync(UUID draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare</p>
     * Асинхронный метод подготовки черновика к отправке
     *
     * @param draftId идентификатор черновика
     * @return результат подготовки
     * @see PrepareResult
     */
    CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare</p>
     * Синхронный метод подготовки черновика к отправке
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return результат подготовки
     * @see PrepareResult
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<PrepareResult> prepare(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send</p>
     * Асинхронный метод отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> sendAsync(UUID draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send</p>
     * Асинхронный метод отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> sendAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send</p>
     * Синхронный метод отправки черновика в контролиоующий орган
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return список документооборотов
     * @see Docflow
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Docflow> send(QueryContext<?> cxt);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод удаления документа из черновика
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteDocumentAsync(UUID draftId, UUID documentId);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод удаления документа из черновика
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId);

    /**
     * <p>DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Синхронный метод удаления документа из черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return {@link Void}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> deleteDocument(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод поиска документа в черновике по идентификатору
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return документ
     * @see DraftDocument
     */
    CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(
            UUID draftId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод поиска документа в черновике по идентификатору
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return документ
     * @see DraftDocument
     */
    CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(
            String draftId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Синхронный метод поиска документа в черновике по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return документ
     * @see DraftDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод обновления документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param documentContents мета-данные документа
     * @return документ
     * @see DocumentContents
     */
    CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(
            UUID draftId,
            UUID documentId,
            DocumentContents documentContents
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод обновления документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param documentContents мета-данные документа
     * @return документ
     * @see DocumentContents
     */
    CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(
            String draftId,
            String documentId,
            DocumentContents documentContents
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод обновления документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- мета-данные документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentContents}</p>
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print</p>
     * Асинхронный метод создания печатной формы документа.
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return byte[] pdf
     */
    CompletableFuture<QueryContext<byte[]>> getDocumentAsPdfAsync(UUID draftId, UUID documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print</p>
     * Асинхронный метод создания печатной формы документа.
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return строка BASE64 от PDF.
     */
    CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print</p>
     * Синхронный метод создания печатной формы документа.
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return строка BASE64 от PDF.
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<String> printDocument(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents</p>
     * Асинхронный метод добавления незашифрованного контента документа в черновик
     *
     * @param draftId идентификатор черновика
     * @param documentContents объект с незашифрованным контентом документа
     * @return документ
     * @see DocumentContents
     */
    CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId,
            DocumentContents documentContents);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents</p>
     * Синхронный метод добавления незашифрованного контента документа в черновик
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- объект с незашифрованным контентом документа. Для установки необходимо использовать
     *         метод {@link QueryContext#setDocumentContents}.</p>
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);

    CompletableFuture<QueryContext<byte[]>> getDecryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted</p>
     * Асинхронный метод получения расшифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент документа в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(
            String draftId,
            String documentId
    );

    /**
     * Синхронный метод получения расшифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return контент документа в кодировке BASE64
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * Асинхронный метод обновления незашифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт незашифрованного контента документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId,
            byte[] content
    );

    /**
     * Асинхронный метод обновления незашифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт незашифрованного контента документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(
            String draftId,
            String documentId,
            byte[] content
    );

    /**
     * Синхронный метод обновления незашифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- массив байт незашифрованного контента документа. Для установки необходимо использовать
     *         метод {@link QueryContext#setContent};</p>
     * @return {@link Void}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * Асинхронный метод получения зашифрованного контента
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return зашифрованный контент
     */
    CompletableFuture<QueryContext<byte[]>> getEncryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId
    );

    /**
     * Асинхронный метод получения зашифрованного контента
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return зашифрованный контент в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(
            String draftId,
            String documentId
    );

    /**
     * Синхронный метод получения зашифрованного контента
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return зашифрованный контент в кодировке BASE64
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод получения контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент подписи
     */
    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            UUID draftId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод получения контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент подписи в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getSignatureContentAsync(
            String draftId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод получения контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return контент подписи в кодировке BASE64
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<String> getSignatureContent(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> updateSignatureAsync(
            UUID draftId,
            UUID documentId,
            byte[] content
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод обновления контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт контента подписи в формате PKCS#7
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateSignatureAsync(
            String draftId,
            String documentId,
            byte[] content
    );

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Синхронный метод обновления контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- массив байт контента подписи в формате PKCS#7. Для установки необходимо использовать
     *         метод {@link QueryContext#setContent}.</p>
     * @return {@link Void}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> updateSignature(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloud-sign Асинхронный метод для создания запроса на
     * облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @return мета-данные запроса
     */
    CompletableFuture<QueryContext<SignInitiation>> cloudSignInitAsync(UUID draftId);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloud-sign Асинхронный метод для создания запроса на
     * облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @return мета-данные запроса
     */
    CompletableFuture<QueryContext<SignInitiation>> cloudSignInitAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign</p>
     * Синхронный метод для создания запроса на облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}.</p>
     * @return запрос на облачную
     * @see SignInitiation
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<SignInitiation> cloudSignInit(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Асинхронный метод для подтверждения запроса на облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @param requestId идентификатор запроса, см. {@link SignInitiation}
     * @param code СМС-код подтверждения
     * @return список подписанных документов
     * @see SignedDraft
     */
    CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync
    (UUID draftId, String requestId, String code);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Асинхронный метод для подтверждения запроса на облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @param requestId идентификатор запроса, см. {@link SignInitiation}
     * @param code СМС-код подтверждения
     * @return список подписанных документов
     * @see SignedDraft
     */
    CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync(
            String draftId,
            String requestId,
            String code
    );

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Синхронный метод для подтверждения запроса на облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId};</p>
     *         <p>- идентификатор запроса, см. {@link SignInitiation}. Для установки необходимо
     *         использовать
     *         метод {@link QueryContext#setRequestId};</p>
     *         <p>- смс-код подтверждения. Для установки необходимо использовать метод {@link
     *         QueryContext#setSmsCode};</p>
     * @return список подписанных документов
     * @see SignedDraft
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign</p>
     * далее, используя {@code codeProvider} для получения смс кода, подтверждает подпись
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Асинхронный метод для создания облачных подписей документов
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @param codeProvider провайдер для получения смс-кода подтверждения
     * @return список подписанных документов
     * @see SignedDraft
     */
    CompletableFuture<QueryContext<SignedDraft>> cloudSignAsync(
            UUID draftId,
            Function<QueryContext<SignInitiation>, String> codeProvider
    );

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/buildDeclaration</p>
     * <p>Синхронный метод создания декларации. Контент документа будет заменен на переданный</p>
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <ul><li>индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}</li>
     *         <li>идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}</li>
     *         <li>версия УСН декрации. Для установки необходимо использовать метод {@link
     *         QueryContext#setVersion}</li>
     *         <li>описание УСН декрации. Для установки необходимо использовать метод {@link
     *         QueryContext#setUsnServiceContractInfo}</li>
     *         </ul>
     * @return {@link QueryContext}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Void> buildDeclaration(QueryContext<?> cxt);

    CompletableFuture<QueryContext<Void>> buildDeclarationAsync(
            UUID draftId,
            UUID documentId,
            int version,
            UsnServiceContractInfo usn
    );

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build</p>
     * <p>Асинхронный метод создания декларации. Контент документа будет заменен на переданный</p>
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param version версия декларации
     * @param usn описание УСН декрации
     * @return {@code CompletableFuture<QueryContext<Void>>}
     */
    CompletableFuture<QueryContext<Void>> buildDeclarationAsync(
            String draftId,
            String documentId,
            int version,
            UsnServiceContractInfo usn
    );

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/build-document</p>
     * <p>Синхронный метод создания декларации. В результате будет создан документ с переданным
     * контентом</p>
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <ul><li>индентификатор черновика. Для установки необходимо использовать метод {@link
     *         QueryContext#setDraftId}</li>
     *         <li>версия УСН декрации. Для установки необходимо использовать метод {@link
     *         QueryContext#setVersion}</li>
     *         <li>описание УСН декрации. Для установки необходимо использовать метод {@link
     *         QueryContext#setUsnServiceContractInfo}</li>
     *         </ul>
     * @return {@link QueryContext}
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DraftDocument> createAndBuildDeclaration(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/build-document</p>
     * <p>Асинхронный метод создания декларации. В результате будет создан документ с переданным
     * контентом</p>
     *
     * @param draftId идентификатор черновика
     * @param version версия декларации
     * @param usn описание УСН декрации
     * @return {@code CompletableFuture<QueryContext<DraftDocument>>}
     * @see DraftDocument
     */
    CompletableFuture<QueryContext<DraftDocument>> createAndBuildDeclarationAsync(
            UUID draftId,
            int version,
            UsnServiceContractInfo usn
    );

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/build-document</p>
     * <p>Асинхронный метод создания декларации. В результате будет создан документ с переданным
     * контентом</p>
     *
     * @param draftId идентификатор черновика
     * @param version версия декларации
     * @param usn описание УСН декрации
     * @return {@code CompletableFuture<QueryContext<DraftDocument>>}
     * @see DraftDocument
     */
    CompletableFuture<QueryContext<DraftDocument>> createAndBuildDeclarationAsync(
            String draftId,
            int version,
            UsnServiceContractInfo usn
    );
}
