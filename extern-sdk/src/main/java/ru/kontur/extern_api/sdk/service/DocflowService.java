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
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.SignatureToSend;
import ru.kontur.extern_api.sdk.provider.Providers;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;


/**
 * @author AlexS
 *
 * Группа методов предоставляет доступ к операциям для работы с докуметооборотом (ДО):
 *<p>- поиск ДО по идентификатору {@link DocflowService#lookupDocflowAsync} | {@link DocflowService#lookupDocflow};</p>
 *<p>- получение списка документов {@link DocflowService#lookupDocflowAsync} | {@link DocflowService#lookupDocflow};</p>
 *<p>- получение документа ДО по идентификатору {@link DocflowService#lookupDocumentAsync} | {@link DocflowService#lookupDocument};</p>
 *<p>- получение мета-данных документа {@link DocflowService#lookupDescriptionAsync} | {@link DocflowService#lookupDescription};</p>
 *<p>- получение зашифрованного контента документа {@link DocflowService#getEncryptedContentAsync} | {@link DocflowService#getEncryptedContent};</p>
 *<p>- получение расшифрованного контента документа {@link DocflowService#getDecryptedContentAsync} | {@link DocflowService#getDecryptedContent};</p>
 *<p>- получение подписей документа {@link DocflowService#getSignaturesAsync} | {@link DocflowService#getSignatures};</p>
 *<p>- получение подписи документа по идентификатору {@link DocflowService#getSignatureAsync} | {@link DocflowService#getSignature};</p>
 *<p>- создание регламентного документа с указанным типом (УОП, ИОП) {@link DocflowService#generateDocumentTypeReplyAsync} | {@link DocflowService#generateDocumentTypeReply};</p>
 *<p>- отправка регламентного документа {@link DocflowService#sendDocumentTypeReplyAsync} | {@link DocflowService#sendDocumentTypeReply};</p>
 *<p>- создание всех регламентных документов для ДО {@link DocflowService#generateRepliesAsync} | {@link DocflowService#generateReplies};</p>
 *<p>- отправка регламентных документов {@link DocflowService#sendRepliesAsync} | {@link DocflowService#sendReplies};</p>
 *<p>- получение списка ДО {@link DocflowService#getDocflowsAsync} | {@link DocflowService#getDocflows};</p>
 *<p>- получение печатной формы {@link DocflowService#printAsync} | {@link DocflowService#print};</p>
 *
 * @see QueryContext
 */
public interface DocflowService extends Providers {

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}</p>
     * Асинхронный метод возвращает ДО по идентификатору
     * @param docflowId идентификатор ДО
     * @return ДО
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}</p>
     * Синхронный метод возвращает ДО по идентификатору
     * @param cxt контекст. Должен содержать следующие данные:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId}</p>
     * @return ДО
     * @see Docflow
     */
    QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents</p>
     * Асинхронный метод возвращает список документов ДО
     * @param docflowId идентификатор ДО
     * @return список документов
     * @see Document
     */
    CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents</p>
     * Синхронный метод возвращает список документов ДО
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId}</p>
     * @return список документов
     * @see Document
     */
    QueryContext<List<Document>> getDocuments(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}</p>
     * Асинхронный метод возвращает документ из ДО
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return документ
     * @see Document
     */
    CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId, String documentId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}</p>
     * Cинхронный метод возвращает документ из ДО
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.</p>
     * @return документ
     * @see Document
     */
    QueryContext<Document> lookupDocument(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description</p>
     * Асинхронный метод возвращает мета-данные для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return мета-данные
     * @see DocumentDescription
     */
    CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(String docflowId, String documentId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description</p>
     * Синхронный метод возвращает мета-данные для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.</p>
     * @return мета-данные
     * @see DocumentDescription
     */
    QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted</p>
     * Асинхронный метод возвращает зашифрованный контент документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт зашифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId, String documentId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted</p>
     * Синхронный метод возвращает зашифрованный контент документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.</p>
     * @return массив байт зашифрованного контента документа
     */
    QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted</p>
     * Асинхронный метод возвращает расшифрованный контент документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт расшифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId, String documentId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted</p>
     * Синхронный метод возвращает расшифрованный контент документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.</p>
     * @return массив байт расшифрованного контента документа
     */
    QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures</p>
     * Асинхронный метод возвращает список подписей для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return список подписей
     */
    CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId, String documentId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures</p>
     * Синхронный метод возвращает список подписей для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.</p>
     * @return список подписей
     */
    QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}</p>
     * Асинхронный метод возвращает подпись для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return подпись
     * @see Signature
     */
    CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId, String documentId, String signatureId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}</p>
     * Синхронный метод возвращает подпись для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};</p>
     *<p>- идентификатор подписи. Для установки необходимо использовать метод {@link QueryContext#setSignatureId} Id}.</p>
     * @return подпись
     * @see Signature
     */
    QueryContext<Signature> getSignature(QueryContext<?> parent);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content</p>
     * Асинхронный метод возвращает контент подписи для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return массив байт контента подписи
     */
    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId, String documentId, String signatureId);

    /**
     *<p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content</p>
     * Синхронный метод возвращает контент подписи для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};
     *<p>- идентификатор подписи. Для установки необходимо использовать метод {@link QueryContext#setSignatureId} Id}.</p>
     * @return массив байт контента подписи
     */
    QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);

    /**
     *<p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate</p>
     * Асинхронный метод создает служебный документ для документа ДО с заданным типом
     * @param docflowId идентификатор ДО
     * @param documentType тип создаваемого документа
     * @param documentId идентификатор документа для которого создается служебный документ
     * @param x509Base64 сертификат ключа подписи в кодировке BASE64 без тегов
     * @return структура данных для отправки
     * @see DocumentToSend
     */
    CompletableFuture<QueryContext<DocumentToSend>> generateDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, String x509Base64);

    /**
     *<p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate</p>
     * Асинхронный метод создает служебный документ для документа ДО с заданным типом
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- тип создаваемого документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentType};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};</p>
     *<p>- сертификат ключа подписи в кодировке BASE64 без тегов. Для установки необходимо использовать метод {@link QueryContext#setCertificate}.</p>
     * @return структура данных для отправки
     * @see DocumentToSend
     */
    QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> parent);

    /**
     *<p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send</p>
     * Асинхронный метод отправляет служебный документ в конролирующий орган
     * @param docflowId идентификатор ДО
     * @param documentType тип создаваемого документа
     * @param documentId идентификатор документа для которого создается служебный документ
     * @param documentToSend структура данных для отправки, созданная с помощью метода {@link DocflowService#generateDocumentTypeReply} | {@link DocflowService#generateDocumentTypeReplyAsync}
     *   Перед отправкой в эту структуру данных необходимо установить подпись, вычисленную для контента документа {@link DocumentToSend#getContent()},
     *   с помощью метода {@link DocumentToSend#setSignature(SignatureToSend)}. Подпись должна быть в формате PKCS#7.
     * @return ДО
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> sendDocumentTypeReplyAsync(String docflowId, String documentType, String documentId, DocumentToSend documentToSend);

    /**
     *<p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send</p>
     * Синхронный метод отправляет служебный документ в конролирующий орган
     * @param parent контекст. Должен содержать следующие параметры:
     *<p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     *<p>- тип создаваемого документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentType};</p>
     *<p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};</p>
     *<p>- структура данных для отправки, созданная с помощью метода {@link DocflowService#generateDocumentTypeReply} | {@link DocflowService#generateDocumentTypeReplyAsync}.</p>
     *   Перед отправкой в эту структуру данных необходимо установить подпись, вычисленную для контента документа {@link DocumentToSend#getContent()},
     *   с помощью метода {@link DocumentToSend#setSignature(SignatureToSend)}. Подпись должна быть в формате PKCS#7.
     *    Для установки необходимо использовать метод {@link QueryContext#setDocumentToSend}.
     * @return ДО
     * @see Docflow
     */
    QueryContext<Docflow> sendDocumentTypeReply(QueryContext<?> parent);

    /**
     * Асинхронный метод создает необходимые регламентные документы для всех документов ДО
     * @param docflow ДО {@link Docflow}
     * @param signerX509Base64 сертификат подписи в кодировке BASE64 без тегов
     * @return список документов предназначенных к отправке в контралирующий орган
     * @see DocumentToSend
     */
    CompletableFuture<QueryContext<List<DocumentToSend>>> generateRepliesAsync(Docflow docflow, String signerX509Base64);

    /**
     * Синхронный метод создает необходимые регламентные документы для всех документов ДО
     * @param parent контекст. Должен принимать следующие параметры:
     *<p>- ДО {@link Docflow}. Для установки необходимо использовать метод {@link QueryContext#setDocflow};</p>
     *<p>- сертификат в кодировке BASE64 без тегов. Для установки необходимо использовать метод {@link QueryContext#setCertificate}.</p>
     * @return список документов для отправки
     * @see DocumentToSend
     */
    QueryContext<List<DocumentToSend>> generateReplies(QueryContext<?> parent);

    /**
     * Асинхронный метод отправляет документ в контролирующий орган
     * @param documentToSend структура данных для отправки, созданная с помощью метода {@link DocflowService#generateRepliesAsync} | {@link DocflowService#generateReplies}
     *   Перед отправкой в эту структуру данных необходимо установить подпись, вычисленную для контента документа {@link DocumentToSend#getContent()},
     *   с помощью метода {@link DocumentToSend#setSignature(SignatureToSend)}. Подпись должна быть в формате PKCS#7.
     * @return ДО
     * @see DocumentToSend
     */
    CompletableFuture<QueryContext<Docflow>> sendRepliesAsync(DocumentToSend documentToSend);

    /**
     * Синхронный метод отправляет документ в контролирующий орган
     * @param parent контекст. Должен принимать следующие параметры:
     *<p>  - структура данных для отправки, созданная с помощью метода {@link DocflowService#generateRepliesAsync} | {@link DocflowService#generateReplies}
     *   Перед отправкой в эту структуру данных необходимо установить подпись, вычисленную для контента документа {@link DocumentToSend#getContent()},
     *   с помощью метода {@link DocumentToSend#setSignature(SignatureToSend)}. Подпись должна быть в формате PKCS#7.
     *    Для установки необходимо использовать метод {@link QueryContext#setDocumentToSend}.</p>
     * @return ДО
     * @see DocumentToSend
     */
    QueryContext<Docflow> sendReplies(QueryContext<?> parent);

    /**
     * <p>GET /v1/{accountId}/docflows</p>
     * Асинхронный метод постранично возвращает список ДО.
     * @param finished признак завершенности ДО
     * @param incoming признак входящего ДО, true - входящие ДО, иначе - исходящие
     * @param skip порядковый номер первого ДО в выходном списке
     * @param take максимальное количество ДО в выходном списке
     * @param innKpp ИНН+КПП подотчетной организации
     * @param updatedFrom дата обновления ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается
     * @param updatedTo  дата обновления ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     * @param createdFrom дата создания ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается
     * @param createdTo дата создания ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     * @param type типы ДО, для которых производится поиск. Можно передавать список значений, разделенных запятой
     * @return список ДО
     * @see DocflowPage
     */
    CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(boolean finished, boolean incoming, long skip, int take, String innKpp, Date updatedFrom, Date updatedTo, Date createdFrom, Date createdTo, String type);

    /**
     * <p>GET /v1/{accountId}/docflows</p>
     * Синхронный метод постранично возвращает список ДО.
     * @param parent контекст. Должен принимать следующие параметры:
     *<p>- признак завершенности ДО: true - ДО завершен, false - незавершен. Для установки необходимо использовать метод {@link QueryContext#setFinished}</p>
     *<p>- признак входящего ДО, true - входящие ДО, иначе - исходящие. Для установки необходимо использовать метод {@link QueryContext#setIncoming}</p>
     *<p>- порядковый номер первого ДО в выходном списке. Для установки необходимо использовать метод {@link QueryContext#setSkip}</p>
     *<p>- максимальное количество ДО в выходном списке. Для установки необходимо использовать метод {@link QueryContext#setTake}</p>
     *<p>- ИНН+КПП подотчетной организации. Для установки необходимо использовать метод {@link QueryContext#setInnKpp}</p>
     *<p>- дата обновления ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается.
     *      Для установки необходимо использовать метод {@link QueryContext#setUpdatedFrom}</p>
     *<p>- дата обновления ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setUpdatedTo}</p>
     *<p>- дата создания ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setCreatedFrom}</p>
     *<p>- дата создания ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setCreatedTo}</p>
     *<p>- type типы ДО, для которых производится поиск. Можно передавать список значений, разделенных запятой
     *      Для установки необходимо использовать метод {@link QueryContext#setType}</p>
     * @return список ДО
     * @see DocflowPage
     */
    QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print</p>
     * Асинхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке BASE64
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param documentContentBase64 формализованный документ в кодировке BASE64, для которого необходимо создать печатную форму
     * @return печатная форма
     */
    CompletableFuture<QueryContext<String>> printAsync(String docflowId, String documentId, String documentContentBase64);

    /**
     * <p>POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print</p>
     * Синхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке BASE64
     * @param parent контекст. Должен содержать следующие параметры:
     * <p>- идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};</p>
     * <p>- идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};</p>
     * <p>- формализованный документ в кодировке BASE64. Для установки необходимо использовать метод {@link QueryContext#setContentString}.</p>
     * @return печатная форма
     */
    QueryContext<String> print(QueryContext<?> parent);
}
