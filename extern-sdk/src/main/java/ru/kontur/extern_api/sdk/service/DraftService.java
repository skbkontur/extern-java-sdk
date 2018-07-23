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
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.ISmsCodeProvider;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfoV2;
import ru.kontur.extern_api.sdk.provider.Providers;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * @author AlexS
 *
 * Группа методов предоставляет доступ к операциям для работы с черновиками: - создание черновика
 * {@link DraftService#createAsync} | {@link DraftService#createAsync}; - поиск черновика по
 * идентификатору {@link DraftService#lookupAsync | {@link DraftService#lookup}; - удаление
 * черновика {@link DraftService#deleteAsync} | {@link DraftService#delete}; - поиск мета-данных
 * черновика {@link DraftService#lookupDraftMetaAsync | {@link DraftService#lookupDraftMeta}; -
 * обновление мета-данных черновика {@link DraftService#updateDraftMetaAsync | {@link
 * DraftService#updateDraftMeta}; - проверка черновика {@link DraftService#checkAsync} | {@link
 * DraftService#check}; - подготовка черновика к отправке {@link DraftService#prepareAsync | {@link
 * DraftService##prepare}; - отправка черновика в контролирующий орган {@link
 * DraftService#sendAsync} | {@link DraftService#send}; - удаление документа из черновика {@link
 * DraftService#deleteDocumentAsync} | {@link DraftService#deleteDocument}; - поиска документа в
 * черновике по идентификатору {@link DraftService#lookupDocumentAsync} | {@link
 * DraftService#lookupDocument}; - метод обновления документа {@link
 * DraftService#updateDocumentAsync} | {@link DraftService#updateDocument}; - создания печатной
 * формы документа {@link DraftService#printDocumentAsync} | {@link DraftService#printDocument}; -
 * метод добавления незашифрованного контента документа в черновик {@link
 * DraftService#addDecryptedDocumentAsync} | {@link DraftService#addDecryptedDocument}; - метод
 * обновления незашифрованного контента документа {@link DraftService#updateDecryptedDocumentContentAsync}
 * | {@link DraftService#updateDecryptedDocumentContent}; - метод получения зашифрованного контента
 * {@link DraftService#getEncryptedDocumentContentAsync} | {@link DraftService#getEncryptedDocumentContent};
 * - метод получения контента подписи {@link DraftService#getSignatureContentAsync} | {@link
 * DraftService#getSignatureContent}; - метод обновления контента подписи {@link
 * DraftService#updateSignatureAsync} | {@link DraftService#updateSignature}; - метод создания УСН
 * декларации по JSON описанию {@link DraftService#createUSN1Async} | {@link
 * DraftService#createUSN1}; - метод создания УСН декларации с помощью объектной модели {@link
 * DraftService#createUSN2Async} | {@link DraftService#createUSN2}; - метод для создания запроса на
 * облачную подпись {@link DraftService#cloudSignQueryAsync} | {@link DraftService#cloudSignQuery};
 * - метод для подтверждения запроса на облачную подпись {@link DraftService#cloudSignConfirmAsync}
 * | {@link DraftService#cloudSignConfirm}; - метод для создания облачных подписей документов {@link
 * DraftService#cloudSignAsync}};
 */
public interface DraftService extends Providers {

    /**
     * POST /v1/{accountId}/drafts Асинхронный метод создает черновик
     *
     * @param sender отправитель декларации {@link Sender}
     * @param recipient получатель декларации {@link ru.kontur.extern_api.sdk.model.FnsRecipient}
     * @param organization организация, на которую создана декларация {@link Organization}
     * @return идентификатор черновика
     */
    CompletableFuture<QueryContext<UUID>> createAsync(Sender sender, Recipient recipient,
        Organization organization);

    /**
     * POST /v1/{accountId}/drafts Синхронный метод создает черновик
     *
     * @param cxt контекст. Должен содержать следующие данные: - объект мета-данные черновика,
     * полученный с помощью конструктора {@link DraftMeta#DraftMeta(Sender, Recipient,
     * Organization)}, где: - sender отправитель декларации {@link Sender}; - recipient получатель
     * декларации {@link ru.kontur.extern_api.sdk.model.FnsRecipient}; - organization организация,
     * на которую создана декларация {@link Organization}. Для установки необходимо использовать
     * метод {@link QueryContext#setDraftMeta}.
     * @return идентификатор черновика
     */
    QueryContext<UUID> create(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId} Асинхронный метод поиска черновика по идентификатору
     *
     * @param draftId идентификатор черновика
     * @return черновик
     * @see Draft
     */
    CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId);

    /**
     * GET /v1/{accountId}/drafts/{draftId} Синхронный метод поиска черновика по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return идентификатор черновика
     */
    QueryContext<Draft> lookup(QueryContext<?> cxt);

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод удаления
     * черновика
     *
     * @param draftId идентификатор черновика
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteAsync(String draftId);

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод удаления
     * черновика
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return {@link Void}
     */
    QueryContext<Void> delete(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/meta Асинхронный метод поиска мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/meta Асинхронный метод поиска мета-данных черновика
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return мета-данные черновика
     * @see DraftMeta
     */
    QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/meta Асинхронный метод обновления мета-данных черновика
     *
     * @param draftId идентификатор черновика
     * @param draftMeta мета-данные черновика
     * @return мета-данные черновика
     * @see DraftMeta
     */
    CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(String draftId,
        DraftMeta draftMeta);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/meta Синхронный метод обновления мета-данных черновика
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - мета-данные
     * черновика. Для установки необходимо использовать метод {@link QueryContext#setDraftMeta}.
     * @return мета-данные черновика
     * @see DraftMeta
     */
    QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/check Асинхронный метод проверки черновика
     *
     * @param draftId идентификатор черновика
     * @return протокол проверки
     */
    CompletableFuture<QueryContext<Map<String, Object>>> checkAsync(String draftId);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/check Асинхронный метод проверки черновика
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return протокол проверки
     */
    QueryContext<Map<String, Object>> check(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/prepare Асинхронный метод подготовки черновика к
     * отправке
     *
     * @param draftId идентификатор черновика
     * @return результат подготовки
     * @see PrepareResult
     */
    CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/prepare Синхронный метод подготовки черновика к
     * отправке
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return результат подготовки
     * @see PrepareResult
     */
    QueryContext<PrepareResult> prepare(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/send Асинхронный метод отправки черновика в
     * контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<QueryContext<List<Docflow>>> sendAsync(String draftId);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/send Синхронный метод отправки черновика в
     * контролиоующий орган
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return список документооборотов
     * @see Docflow
     */
    QueryContext<List<Docflow>> send(QueryContext<?> cxt);

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод удаления
     * документа из черновика
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId);

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId} Синхронный метод удаления
     * документа из черновика
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}/
     * @return {@link Void}
     */
    QueryContext<Void> deleteDocument(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод поиска
     * документа в черновике по идентификатору
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return документ
     * @see DraftDocument
     */
    CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(String draftId,
        String documentId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId} Синхронный метод поиска документа
     * в черновике по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return документ
     * @see DraftDocument
     */
    QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод обновления
     * документа
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
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId} Асинхронный метод обновления
     * документа
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}; -
     * мета-данные документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentContents} .
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     */
    QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print Асинхронный метод создания
     * печатной формы документа.
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return строка BASE64 от PDF.
     */
    CompletableFuture<QueryContext<String>> printDocumentAsync(String draftId, String documentId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print Синхронный метод создания
     * печатной формы документа.
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return строка BASE64 от PDF.
     */
    QueryContext<String> printDocument(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents Асинхронный метод добавления незашифрованного
     * контента документа в черновик
     *
     * @param draftId идентификатор черновика
     * @param documentContents объект с незашифрованным контентом документа
     * @return документ
     * @see DocumentContents
     */
    CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(UUID draftId,
        DocumentContents documentContents);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents Синхронный метод добавления незашифрованного
     * контента документа в черновик
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - объект с
     * незашифрованным контентом документа. Для установки необходимо использовать метод {@link
     * QueryContext#setDocumentContents}.
     * @return документ
     * @see DocumentContents
     * @see DraftDocument
     */
    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted Асинхронный
     * метод получения расшифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент документа в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(String draftId,
        String documentId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted Синхронный
     * метод получения расшифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return контент документа в кодировке BASE64
     */
    QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted Асинхронный
     * метод обновления незашифрованного контента документа
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт незашифрованного контента документа
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(String draftId,
        String documentId, byte[] content);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted Синхронный
     * метод обновления незашифрованного контента документа
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}; -
     * массив байт незашифрованного контента документа. Для установки необходимо использовать метод
     * {@link QueryContext#setContent};
     * @return {@link Void}
     */
    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted Асинхронный
     * метод получения зашифрованного контента
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return зашифрованный контент в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(String draftId,
        String documentId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted Синхронный
     * метод получения зашифрованного контента
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return зашифрованный контент в кодировке BASE64
     */
    QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature Асинхронный метод
     * получения контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @return контент подписи в кодировке BASE64
     */
    CompletableFuture<QueryContext<String>> getSignatureContentAsync(String draftId,
        String documentId);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature Асинхронный метод
     * получения контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return контент подписи в кодировке BASE64
     */
    QueryContext<String> getSignatureContent(QueryContext<?> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature Асинхронный метод
     * обновления контента подписи
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param content массив байт контента подписи в формате PKCS#7
     * @return {@link Void}
     */
    CompletableFuture<QueryContext<Void>> updateSignatureAsync(String draftId, String documentId,
        byte[] content);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature Синхронный метод
     * обновления контента подписи
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}; -
     * массив байт контента подписи в формате PKCS#7. Для установки необходимо использовать метод
     * {@link QueryContext#setContent}.
     * @return {@link Void}
     */
    QueryContext<Void> updateSignature(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=1 Асинхронный
     * метод создания УСН декларации по JSON описанию
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param usn описание УСН декрации
     * @return {@link Void}
     * @see UsnServiceContractInfo
     */
    CompletableFuture<QueryContext<Void>> createUSN1Async(String draftId, String documentId,
        UsnServiceContractInfo usn);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=1 Синхронный
     * метод создания УСН декларации по JSON описанию
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}; -
     * описание УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setUsnServiceContractInfo}.
     * @return {@link Void}
     * @see UsnServiceContractInfo
     */
    QueryContext<Void> createUSN1(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=2 Асинхронный
     * метод создания УСН декларации с помощью объектной модели
     *
     * @param draftId идентификатор черновика
     * @param documentId идентификатор документа
     * @param usn описание УСН декрации
     * @return {@link Void}
     * @see UsnServiceContractInfoV2
     */
    CompletableFuture<QueryContext<Void>> createUSN2Async(String draftId, String documentId,
        UsnServiceContractInfoV2 usn);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=2 Синхронный
     * метод создания УСН декларации с помощью объектной модели
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}; -
     * описание УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setUsnServiceContractInfoV2};
     * @return {@link Void}
     * @see UsnServiceContractInfoV2
     */
    QueryContext<Void> createUSN2(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/build-document?format=&type=&version=version Синхронный
     * метод создания УСН декларации различных версий
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - версия УСН
     * декрации. Для установки необходимо использовать метод {@link QueryContext#setVersion}; -
     * описание УСН декрации. Для установки необходимо использовать метод {@link
     * QueryContext#setUsnServiceContractInfo};
     * @return {@link Void}
     * @see UsnServiceContractInfo
     */
    QueryContext<Void> createUSN(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/build-document?format=&type=&version=version Асинхронный
     * метод создания УСН декларации различных версий
     *
     * @param draftId идентификатор черновика
     * @param version версия декларации
     * @param usn описание УСН декрации
     * @return {@link Void}
     * @see UsnServiceContractInfo
     */
    CompletableFuture<QueryContext<Void>> createUSNAsync(String draftId, int version,
        UsnServiceContractInfo usn);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign Асинхронный метод для создания запроса на
     * облачную подпись
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @return запрос на облачную
     */
    CompletableFuture<QueryContext<SignInitiation>> cloudSignQueryAsync(String draftId);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign Cинхронный метод для создания запроса на
     * облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}.
     * @return запрос на облачную
     * @see SignInitiation
     */
    QueryContext<SignInitiation> cloudSignQuery(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign/confirm Асинхронный метод для подтверждения
     * запроса на облачную подпись
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
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign/confirm Синхронный метод для подтверждения
     * запроса на облачную подпись
     *
     * @param cxt контекст. Должен содержать следующие данные: - индентификатор черновика. Для
     * установки необходимо использовать метод {@link QueryContext#setDraftId}; - идентификатор
     * запроса, см. {@link SignInitiation}. Для установки необходимо использовать метод {@link
     * QueryContext#setRequestId}; - смс-код подтверждения. Для установки необходимо использовать
     * метод {@link QueryContext#setSmsCode};
     * @return список подписанных документов
     * @see SignedDraft
     */
    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign POST /v1/{accountId}/drafts/{draftId}/cloudSign/confirm
     * Асинхронный метод для создания облачных подписей документов
     *
     * @param draftId идентификатор черновика, документы которого необходимо подписать
     * @param codeProvider провайдер для получения смс-кода подтверждения {@link ISmsCodeProvider}
     * @return список подписанных документов
     * @see SignedDraft
     */
    CompletableFuture<QueryContext<SignedDraft>> cloudSignAsync(String draftId,
        ISmsCodeProvider codeProvider);
}
