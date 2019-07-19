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

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.RecognizedMeta;

/**
 * Группа методов предоставляет доступ к операциям для работы с докуметооборотом (ДО)
 */
public interface DocflowService {

    CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(UUID docflowId);

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}</p>
     * Асинхронный метод возвращает ДО по идентификатору
     *
     * @param docflowId идентификатор ДО
     * @return ДО
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}</p>
     * Синхронный метод возвращает ДО по идентификатору
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId}</p>
     * @return ДО
     * @see Docflow
     */
    @Deprecated
    QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

    CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(UUID docflowId);

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents</p>
     * Асинхронный метод возвращает список документов ДО
     *
     * @param docflowId идентификатор ДО
     * @return список документов
     * @see Document
     */
    CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents</p>
     * Синхронный метод возвращает список документов ДО
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId}</p>
     * @return список документов
     * @see Document
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<List<Document>> getDocuments(QueryContext<?> parent);

    CompletableFuture<QueryContext<Document>> lookupDocumentAsync(
            UUID docflowId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}</p>
     * Асинхронный метод возвращает документ из ДО
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return документ
     * @see Document
     */
    CompletableFuture<QueryContext<Document>> lookupDocumentAsync(
            String docflowId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}</p>
     * Cинхронный метод возвращает документ из ДО
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return документ
     * @see Document
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Document> lookupDocument(QueryContext<?> parent);

    CompletableFuture<QueryContext<DocflowDocumentDescription>> lookupDescriptionAsync(
            UUID docflowId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description</p>
     * Асинхронный метод возвращает мета-данные для документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return мета-данные
     * @see DocumentDescription
     */
    CompletableFuture<QueryContext<DocflowDocumentDescription>> lookupDescriptionAsync(
            String docflowId,
            String documentId
    );

    CompletableFuture<QueryContext<RecognizedMeta>> recognizeAsync(
            UUID docflowId,
            UUID documentId,
            byte[] documentContent
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description</p>
     * Синхронный метод возвращает мета-данные для документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return мета-данные
     * @see DocumentDescription
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(
            UUID docflowId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/encrypted-content/</p>
     * Асинхронный метод возвращает зашифрованный контент документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт зашифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(
            String docflowId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/encrypted-content</p>
     * Синхронный метод возвращает зашифрованный контент документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return массив байт зашифрованного контента документа
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(
            UUID docflowId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/decrypted-content</p>
     * Асинхронный метод возвращает расшифрованный контент документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт расшифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(
            String docflowId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/decrypted-content</p>
     * Синхронный метод возвращает расшифрованный контент документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return массив байт расшифрованного контента документа
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(
            UUID docflowId,
            UUID documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures</p>
     * Асинхронный метод возвращает список подписей для документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return список подписей
     */
    CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(
            String docflowId,
            String documentId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures</p>
     * Синхронный метод возвращает список подписей для документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId}.</p>
     * @return список подписей
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);

    CompletableFuture<QueryContext<Signature>> getSignatureAsync(
            UUID docflowId,
            UUID documentId,
            UUID signatureId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}</p>
     * Асинхронный метод возвращает подпись для документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return подпись
     * @see Signature
     */
    CompletableFuture<QueryContext<Signature>> getSignatureAsync(
            String docflowId,
            String documentId,
            String signatureId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}</p>
     * Синхронный метод возвращает подпись для документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- идентификатор подписи. Для установки необходимо использовать метод {@link
     *         QueryContext#setSignatureId} Id}.</p>
     * @return подпись
     * @see Signature
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Signature> getSignature(QueryContext<?> parent);

    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            UUID docflowId,
            UUID documentId,
            UUID signatureId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content</p>
     * Асинхронный метод возвращает контент подписи для документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return массив байт контента подписи
     */
    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            String docflowId,
            String documentId,
            String signatureId
    );

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content</p>
     * Синхронный метод возвращает контент подписи для документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};
     *         <p>- идентификатор подписи. Для установки необходимо использовать метод {@link
     *         QueryContext#setSignatureId} Id}.</p>
     * @return массив байт контента подписи
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);

    CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            UUID docflowId,
            UUID documentId,
            String replyType,
            byte[] signerCert
    );

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate</p>
     * <p>Асинхронный метод создает служебный документ для документа ДО с заданным типом.</p>
     * <p>Возможные типы ответного документа можно найти в Document#getReplyOptions </p>
     *
     * @param docflowId id документооборота
     * @param documentId id документа для которого генерируется ответный
     * @param replyType тип ответного документа (см выше)
     * @param signerX509Base64 сертификат подписанта
     * @return структура данных для отправки
     * @see ReplyDocument
     */
    CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            String docflowId,
            String documentId,
            String replyType,
            String signerX509Base64
    );

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate</p>
     * Асинхронный метод создает служебный документ для документа ДО с заданным типом
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- id ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- id создаваемого документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- тип документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentType};</p>
     *         <p>- сертификат ключа подписи в кодировке BASE64 без тегов. Для установки необходимо
     *         использовать метод {@link QueryContext#setCertificate}.</p>
     * @return структура данных для отправки
     * @see DocumentToSend
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<ReplyDocument> generateReply(QueryContext<?> parent);

    /**
     * <p>Загрузить подпись ответного документа</p>
     * <p>cxt должен содержать:</p>
     * <p>{@link QueryContext#getDocflowId}</p>
     * <p>{@link QueryContext#getDocumentId}</p>
     * <p>{@link QueryContext#getReplyId}</p>
     * <p>{@link QueryContext#getContent} -- подпись</p>
     *
     * @param parent см. выше
     * @return Обновлённую модель ReplyDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<ReplyDocument> uploadReplyDocumentSignature(QueryContext<?> parent);

    CompletableFuture<QueryContext<ReplyDocument>> uploadReplyDocumentSignatureAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            byte[] signature
    );

    CompletableFuture<QueryContext<ReplyDocument>> uploadReplyDocumentSignatureAsync(
            String docflowId,
            String documentId,
            String replyId,
            byte[] signature
    );

    CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId
    );

    /**
     * Асинхронный метод отправляет документ в контролирующий орган
     *
     * @return ДО
     * @see ReplyDocument
     */
    CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            String docflowId,
            String documentId,
            String replyId
    );

    /**
     * Синхронный метод отправляет документ в контролирующий орган
     *
     * @param parent контекст. с id документооборота, документа и ответного документа
     * @return ДО
     * @see ReplyDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<Docflow> sendReply(QueryContext<?> parent);

    CompletableFuture<QueryContext<ReplyDocument>> getReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId
    );

    /**
     * Асинхронный метод получения ответного документа по идентификатору
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param replyId идентификатор ответного документа
     * @return объект с данными ответного документа
     * @see ReplyDocument
     */
    CompletableFuture<QueryContext<ReplyDocument>> getReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId
    );

    /**
     * Синхронный метод получения ответного документа по идентификатору
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <ul>
     *         <li> идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</li>
     *         <li> идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</li>
     *         <li> идентификатор ответного документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setReplyId}.</li>
     *         </ul>
     * @return объект с данными ответного документа
     * @see ReplyDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> parent);

    CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentContentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            byte[] content
    );

    /**
     * Асинхронный метод обновления контента ответного документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param replyId идентификатор ответного документа
     * @param content массив байт ответного документа
     * @return объект с данными ответного документа
     * @see ReplyDocument
     */
    CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentContentAsync(
            String docflowId,
            String documentId,
            String replyId,
            byte[] content
    );

    /**
     * Синхронный метод обновления контента ответного документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <ul>
     *         <li> идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</li>
     *         <li> идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</li>
     *         <li> идентификатор ответного документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setReplyId};</li>
     *         <li> контент ответного документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setContent}.</li>
     *         </ul>
     * @return объект с данными ответного документа
     * @see ReplyDocument
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> parent);

    /**
     * <p>GET /v1/{accountId}/docflows</p>
     * Асинхронный метод постранично возвращает список ДО.
     *
     * @deprecated use {@link DocflowService#searchDocflows(DocflowFilter)} instead
     */
    @Deprecated
    CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(
            @Nullable Boolean finished,
            @Nullable Boolean incoming,
            long skip,
            int take,
            @Nullable String innKpp,
            @Nullable Date updatedFrom,
            @Nullable Date updatedTo,
            @Nullable Date createdFrom,
            @Nullable Date createdTo,
            @Nullable String type
    );

    /**
     * <p>GET /v1/{accountId}/docflows</p>
     * Синхронный метод постранично возвращает список ДО.
     *
     * @see DocflowPage
     * @deprecated use {@link DocflowService#searchDocflows(DocflowFilter)} instead
     */
    @Deprecated
    QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);

    /**
     * Возвращает документообороты удовлетворяющие поисковому фильтру
     *
     * @param searchFilter {@link DocflowFilter}
     * @return список документооборотов
     */
    CompletableFuture<QueryContext<DocflowPage>> searchDocflowsAsync(DocflowFilter searchFilter);

    /**
     * Возвращает документообороты удовлетворяющие поисковому фильтру
     *
     * @param searchFilter {@link DocflowFilter}
     * @return список документооборотов
     */
    QueryContext<DocflowPage> searchDocflows(DocflowFilter searchFilter);

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print</p>
     * Асинхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке
     * BASE64
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param documentContentBase64 формализованный документ в кодировке BASE64, для которого
     *         необходимо создать печатную форму
     * @return печатная форма
     */
    CompletableFuture<QueryContext<String>> printAsync(
            String docflowId,
            String documentId,
            String documentContentBase64
    );

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print</p>
     * Асинхронный метод возвращает печатную форму документа. Печатная форма - PDF файл.
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param documentContent расшифрованый формализованный документ, для которого
     *         необходимо создать печатную форму
     * @return печатная форма
     */
    CompletableFuture<QueryContext<byte[]>> getDocumentAsPdfAsync(
            UUID docflowId,
            UUID documentId,
            byte[] documentContent
    );


    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print</p>
     * Синхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке
     * BASE64
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- формализованный документ в кодировке BASE64. Для установки необходимо использовать метод
     *         {@link QueryContext#setContentString}.</p>
     * @return печатная форма
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<String> print(QueryContext<?> parent);

    CompletableFuture<QueryContext<SignInitiation>> cloudSignReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId
    );

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/[replyId}/cloud-sign</p>
     * Асинхронный метод инициирует облачное подписание ответного документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param replyId идентификатор ответного документа
     * @return объект с результатом инициации облачной паодписи
     */
    CompletableFuture<QueryContext<SignInitiation>> cloudSignReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId
    );


    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/[replyId}/cloud-sign</p>
     * Cинхронный метод инициирует облачное подписание ответного документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- идентификатор ответного документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setReplyId(String)};</p>
     * @return объект с результатом инициации облачной паодписи
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> parent);

    CompletableFuture<QueryContext<SignConfirmResultData>> cloudSignConfirmReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            String requestId,
            String smsCode
    );

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/[replyId}/cloud-sign-confirm</p>
     * Асинхронный метод подтверждает облачное подписание ответного документа
     *
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param replyId идентификатор ответного документа
     * @param smsCode смс код подтверждения подписания
     * @return объект с результатом инициации облачной паодписи
     */
    CompletableFuture<QueryContext<SignConfirmResultData>> cloudSignConfirmReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId,
            String requestCode,
            String smsCode
    );


    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/[replyId}/cloud-sign-confirm</p>
     * Cинхронный метод подтверждает облачное подписание ответного документа
     *
     * @param parent контекст. Должен содержать следующие параметры:
     *         <p>- идентификатор ДО. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocflowId};</p>
     *         <p>- идентификатор документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setDocumentId};</p>
     *         <p>- идентификатор ответного документа. Для установки необходимо использовать метод {@link
     *         QueryContext#setReplyId(String)};</p>
     * @return объект с результатом инициации облачной паодписи
     * @deprecated use async method instead
     */
    @Deprecated
    QueryContext<SignConfirmResultData> cloudSignConfirmReplyDocument(QueryContext<?> parent);

    CompletableFuture<QueryContext<DecryptInitiation>> cloudDecryptDocumentInitAsync(
            UUID docflowId,
            UUID documentId,
            byte[] certificate
    );

    /**
     * Инициация процесса облачного расшифрования документа из Docflow
     *
     * @return ссылка на подтверждение расшифрования
     */
    QueryContext<DecryptInitiation> cloudDecryptDocumentInit(
            String docflowId,
            String documentId,
            String certBase64
    );

    CompletableFuture<QueryContext<byte[]>> cloudDecryptDocumentConfirmAsync(
            UUID docflowId,
            UUID documentId,
            String requestId,
            String code
    );

    /**
     * Подтверждение облачного расшифрования документа из Docflow
     *
     * @return Расшифрованый конент документа
     */
    QueryContext<byte[]> cloudDecryptDocumentConfirm(
            String docflowId,
            String documentId,
            String requestId,
            String code
    );

    CompletableFuture<QueryContext<byte[]>> cloudDecryptDocumentAsync(
            UUID docflowId,
            UUID documentId,
            byte[] certificate,
            Function<QueryContext<DecryptInitiation>, String> smsCodeProvider
    );

    /**
     * Инициация и подтверждение облачного расшифрования документа из Docflow
     *
     * @param smsCodeProvider метод получения кода подтверждения.
     * @return Расшифрованый конент документа
     */
    QueryContext<byte[]> cloudDecryptDocument(
            String docflowId,
            String documentId,
            String certBase64,
            Function<DecryptInitiation, String> smsCodeProvider
    );
}
