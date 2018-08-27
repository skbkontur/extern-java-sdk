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

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.provider.ISmsCodeProvider;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.provider.Providers;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * Группа методов предоставляет доступ к операциям для работы с черновиками:
 * <p>- создание черновика {@link DraftService#createAsync} | {@link DraftService#createAsync};</p>
 * <p>- поиск черновика по идентификатору {@link DraftService#lookupAsync} | {@link
 * DraftService#lookup};</p>
 * <p>- удаление черновика {@link DraftService#deleteAsync} | {@link DraftService#delete};</p>
 * <p>- поиск мета-данных черновика {@link DraftService#lookupDraftMetaAsync} | {@link
 * DraftService#lookupDraftMeta};</p>
 * <p>- обновление мета-данных черновика {@link DraftService#updateDraftMetaAsync} | {@link
 * DraftService#updateDraftMeta};</p>
 * <p>- проверка черновика {@link DraftService#checkAsync} | {@link DraftService#check};</p>
 * <p>- подготовка черновика к отправке {@link DraftService#prepareAsync} | {@link
 * DraftService#prepare};</p>
 * <p>- отправка черновика в контролирующий орган {@link DraftService#sendAsync} | {@link
 * DraftService#send};</p>
 * <p>- удаление документа из черновика {@link DraftService#deleteDocumentAsync} | {@link
 * DraftService#deleteDocument};</p>
 * <p>- поиска документа в черновике по идентификатору {@link DraftService#lookupDocumentAsync} |
 * {@link DraftService#lookupDocument};</p>
 * <p>- метод обновления документа {@link DraftService#updateDocumentAsync} | {@link
 * DraftService#updateDocument};</p>
 * <p>- создания печатной формы документа {@link DraftService#printDocumentAsync} | {@link
 * DraftService#printDocument};</p>
 * <p>- метод добавления незашифрованного контента документа в черновик {@link
 * DraftService#addDecryptedDocumentAsync} | {@link DraftService#addDecryptedDocument};</p>
 * <p>- метод обновления незашифрованного контента документа {@link DraftService#updateDecryptedDocumentContentAsync}
 * | {@link DraftService#updateDecryptedDocumentContent};</p>
 * <p>- метод получения зашифрованного контента {@link DraftService#getEncryptedDocumentContentAsync}
 * | {@link DraftService#getEncryptedDocumentContent};</p>
 * <p>- метод получения контента подписи {@link DraftService#getSignatureContentAsync} | {@link
 * DraftService#getSignatureContent};</p>
 * <p>- метод обновления контента подписи {@link DraftService#updateSignatureAsync} | {@link
 * DraftService#updateSignature};</p>
 * <p>- метод для создания запроса на облачную подпись {@link DraftService#cloudSignInitAsync} |
 * {@link DraftService#cloudSignInit };</p>
 * <p>- метод для подтверждения запроса на облачную подпись {@link DraftService#cloudSignConfirmAsync}
 * | {@link DraftService#cloudSignConfirm};</p>
 * <p>- метод для облачного подписания документов черновика {@link DraftService#cloudSignAsync}.</p>
 *
 * @author Aleksey Sukhorukov
 */
public interface DraftService extends Providers {

    /**
     * <p>POST /v1/{accountId}/drafts</p>
     * Асинхронный метод создает черновик
     *
     * @param sender отправитель декларации {@link Sender}
     * @param recipient получатель декларации {@link ru.kontur.extern_api.sdk.model.FnsRecipient} | {@link ru.kontur.extern_api.sdk.model.TogsRecipient}
     * @param organization организация, на которую создана декларация {@link Organization}
     * @return идентификатор черновика
     */
    CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient,
            Organization organization);

    /**
     * <p>POST /v1/{accountId}/drafts</p>
     * Синхронный метод создает черновик
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- объект мета-данные черновика, полученный с помощью конструктора {@link
     * DraftMeta#DraftMeta(Sender, Recipient, Organization)}, где:</p>
     * <ul>
     *  <li>sender отправитель декларации {@link Sender};</li>
     *  <li>recipient получатель декларации {@link ru.kontur.extern_api.sdk.model.FnsRecipient}  | {@link ru.kontur.extern_api.sdk.model.TogsRecipient};</li>
     *  <li>organization организация, на которую создана декларация {@link Organization}.</li>
     *</ul>
     *<p>Для установки необходимо использовать метод {@link QueryContext#setDraftMeta}.</p>
     *
     * @return идентификатор черновика
     */
    QueryContext<UUID> create(QueryContext<?> cxt);

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
     * <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return идентификатор черновика
     */
    QueryContext<Draft> lookup(QueryContext<?> cxt);

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
     * <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return {@link Void}
     */
    QueryContext<Void> delete(QueryContext<?> cxt);

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
     * <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return мета-данные черновика
     * @see DraftMeta
     */
    QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/meta</p>
     * Асинхронный метод обновления мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @param draftMeta мета-данные черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId,
            DraftMeta draftMeta);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/meta</p>
     * Синхронный метод обновления мета-данных черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>  - мета-данные черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftMeta}.</p>
     * @return мета-данные черновика
     * @see DraftMeta
     */
    QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

    /**
     * <P>POST /v1/{accountId}/drafts/{draftId}/check</P>
     * Асинхронный метод проверки черновика
     *
     * @param draftId идентификатор черновика
     * @return протокол проверки
     */
    CompletableFuture<QueryContext<CheckResultData>> checkAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/check</p>
     * Асинхронный метод проверки черновика
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>  - индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return протокол проверки
     */
    QueryContext<CheckResultData> check(QueryContext<?> cxt);

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
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return результат подготовки
     * @see PrepareResult
     */
    QueryContext<PrepareResult> prepare(QueryContext<?> cxt);

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
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return список документооборотов
     * @see Docflow
     */
    QueryContext<Docflow> send(QueryContext<?> cxt);

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
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return {@link Void}
     */
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
    CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId,
            String documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Синхронный метод поиска документа в черновике по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return документ
     * @see DraftDocument
     */
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
    CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(String draftId,
            String documentId, DocumentContents documentContents);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}</p>
     * Асинхронный метод обновления документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId};</p>
     * <p>- мета-данные документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentContents}</p>
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     */
    QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);

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
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return строка BASE64 от PDF.
     */
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
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- объект с незашифрованным контентом документа. Для установки необходимо использовать
     * метод {@link QueryContext#setDocumentContents}.</p>
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     */
    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted</p>
     * Асинхронный метод получения расшифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент документа в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId,
            String documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted</p>
     * Синхронный метод получения расшифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return контент документа в кодировке BASE64
     */
    QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted</p>
     * Асинхронный метод обновления незашифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт незашифрованного контента документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId,
            String documentId, byte[] content);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted</p>
     * Синхронный метод обновления незашифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId};</p>
     * <p>- массив байт незашифрованного контента документа. Для установки необходимо использовать
     * метод {@link QueryContext#setContent};</p>
     * @return {@link Void}
     */
    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted</p>
     * Асинхронный метод получения зашифрованного контента
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return зашифрованный контент в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId,
            String documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted</p>
     * Синхронный метод получения зашифрованного контента
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return зашифрованный контент в кодировке BASE64
     */
    QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод получения контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент подписи в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId,
            String documentId);

    /**
     * <p>GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод получения контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}.</p>
     * @return контент подписи в кодировке BASE64
     */
    QueryContext<String> getSignatureContent(QueryContext<?> cxt);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Асинхронный метод обновления контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт контента подписи в формате PKCS#7
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId,
            byte[] content);

    /**
     * <p>PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature</p>
     * Синхронный метод обновления контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId};</p>
     * <p>- массив байт контента подписи в формате PKCS#7. Для установки необходимо использовать
     * метод {@link QueryContext#setContent}.</p>
     * @return {@link Void}
     */
    QueryContext<Void> updateSignature(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloud-sign Асинхронный метод для создания запроса на
     * облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @return запрос на облачную
     */
    CompletableFuture<QueryContext<SignInitiation>> cloudSignInitAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign</p>
     * Cинхронный метод для создания запроса на облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}.</p>
     * @return запрос на облачную
     * @see SignInitiation
     */
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
    CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync(String draftId,
            String requestId, String code);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Синхронный метод для подтверждения запроса на облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <p>- индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId};</p>
     * <p>- идентификатор запроса, см. {@link SignInitiation}. Для установки необходимо
     * использовать
     * метод {@link QueryContext#setRequestId};</p>
     * <p>- смс-код подтверждения. Для установки необходимо использовать метод {@link
     * QueryContext#setSmsCode};</p>
     * @return список подписанных документов
     * @see SignedDraft
     */
    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign</p>
     * далее, используя {@code codeProvider} для получения смс кода, подтверждает подпись
     * <p>POST /v1/{accountId}/drafts/{draftId}/cloud-sign-confirm</p>
     * Асинхронный метод для создания облачных подписей документов
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @param codeProvider провайдер для получения смс-кода подтверждения {@link ISmsCodeProvider}
     * @return список подписанных документов
     * @see SignedDraft
     */
    CompletableFuture<QueryContext<SignedDraft>> cloudSignAsync(String draftId,
            ISmsCodeProvider codeProvider);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/buildDeclaration</p>
     * <p>Синхронный метод создания декларации. Контент документа будет заменен на переданный</p>
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <ul><li>индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}</li>
     * <li>идентификатор документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentId}</li>
     * <li>версия УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setVersion}</li>
     * <li>описание УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setUsnServiceContractInfo}</li>
     * </ul>
     * @return {@link QueryContext}
     */
    @NotNull
    QueryContext<Void> buildDeclaration(@NotNull QueryContext<?> cxt);

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
    @NotNull
    CompletableFuture<QueryContext<Void>> buildDeclarationAsync(@NotNull String draftId,
            @NotNull String documentId,
            int version, @NotNull UsnServiceContractInfo usn);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/build-document</p>
     * <p>Синхронный метод создания декларации. В результате будет создан документ с переданным
     * контентом</p>
     *
     * @param cxt контекст. Должен содержать следующие данные:
     * <ul><li>индентификатор черновика. Для установки необходимо использовать метод {@link
     * QueryContext#setDraftId}</li>
     * <li>версия УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setVersion}</li>
     * <li>описание УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setUsnServiceContractInfo}</li>
     * </ul>
     * @return {@link QueryContext}
     */

    @NotNull
    QueryContext<DraftDocument> createAndBuildDeclaration(@NotNull QueryContext<?> cxt);

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
    @NotNull
    CompletableFuture<QueryContext<DraftDocument>> createAndBuildDeclarationAsync(
            @NotNull String draftId,
            int version, @NotNull UsnServiceContractInfo usn);
}
