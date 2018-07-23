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
 * - поиск ДО по идентификатору {@link DocflowService#lookupDocflowAsync} | {@link DocflowService#lookupDocflow};
 * - получение списка документов {@link DocflowService#lookupDocflowAsync} | {@link DocflowService#lookupDocflow};
 * - получение документа ДО по идентификатору {@link DocflowService#lookupDocumentAsync} | {@link DocflowService#lookupDocument};
 * - получение мета-данных документа {@link DocflowService#lookupDescriptionAsync} | {@link DocflowService#lookupDescription};
 * - получение зашифрованного контента документа {@link DocflowService#getEncryptedContentAsync} | {@link DocflowService#getEncryptedContent};
 * - получение расшифрованного контента документа {@link DocflowService#getDecryptedContentAsync} | {@link DocflowService#getDecryptedContent};
 * - получение подписей документа {@link DocflowService#getSignaturesAsync} | {@link DocflowService#getSignatures};
 * - получение подписи документа по идентификатору {@link DocflowService#getSignatureAsync} | {@link DocflowService#getSignature};
 * - создание регламентного документа с указанным типом (УОП, ИОП) {@link DocflowService#generateDocumentTypeReplyAsync} | {@link DocflowService#generateDocumentTypeReply};
 * - отправка регламентного документа {@link DocflowService#sendDocumentTypeReplyAsync} | {@link DocflowService#sendDocumentTypeReply};
 * - создание всех регламентных документов для ДО {@link DocflowService#generateRepliesAsync} | {@link DocflowService#generateReplies};
 * - отправка регламентных документов {@link DocflowService#sendRepliesAsync} | {@link DocflowService#sendReplies};
 * - получение списка ДО {@link DocflowService#getDocflowsAsync} | {@link DocflowService#getDocflows};
 * - получение печатной формы {@link DocflowService#printAsync} | {@link DocflowService#print};
 *
 * @see QueryContext
 */
public interface DocflowService extends Providers {

    /**
     * GET /v1/{accountId}/docflows/{docflowId}
     * Асинхронный метод возвращает ДО по идентификатору
     * @param docflowId идентификатор ДО
     * @return ДО
     * @see Docflow
     */
    CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}
     * Синхронный метод возвращает ДО по идентификатору
     * @param cxt контекст. Должен содержать следующие данные:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId}
     * @return ДО
     * @see Docflow
     */
    QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents
     * Асинхронный метод возвращает список документов ДО
     * @param docflowId идентификатор ДО
     * @return список документов
     * @see Document
     */
    CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents
     * Синхронный метод возвращает список документов ДО
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId}
     * @return список документов
     * @see Document
     */
    QueryContext<List<Document>> getDocuments(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
     * Асинхронный метод возвращает документ из ДО
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return документ
     * @see Document
     */
    CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId, String documentId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
     * Cинхронный метод возвращает документ из ДО
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return документ
     * @see Document
     */
    QueryContext<Document> lookupDocument(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
     * Асинхронный метод возвращает мета-данные для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return мета-данные
     * @see DocumentDescription
     */
    CompletableFuture<QueryContext<DocumentDescription>> lookupDescriptionAsync(String docflowId, String documentId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
     * Синхронный метод возвращает мета-данные для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return мета-данные
     * @see DocumentDescription
     */
    QueryContext<DocumentDescription> lookupDescription(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
     * Асинхронный метод возвращает зашифрованный контент документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт зашифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId, String documentId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
     * Синхронный метод возвращает зашифрованный контент документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return массив байт зашифрованного контента документа
     */
    QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
     * Асинхронный метод возвращает расшифрованный контент документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return массив байт расшифрованного контента документа
     */
    CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId, String documentId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
     * Синхронный метод возвращает расшифрованный контент документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return массив байт расшифрованного контента документа
     */
    QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
     * Асинхронный метод возвращает список подписей для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @return список подписей
     */
    CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId, String documentId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
     * Синхронный метод возвращает список подписей для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     * @return список подписей
     */
    QueryContext<List<Signature>> getSignatures(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
     * Асинхронный метод возвращает подпись для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return подпись
     * @see Signature
     */
    CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId, String documentId, String signatureId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
     * Синхронный метод возвращает подпись для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     *  - идентификатор подписи. Для установки необходимо использовать метод {@link QueryContext#setSignatureId} Id}.
     * @return подпись
     * @see Signature
     */
    QueryContext<Signature> getSignature(QueryContext<?> parent);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
     * Асинхронный метод возвращает контент подписи для документа
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param signatureId идентификатор ДО
     * @return массив байт контента подписи
     */
    CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId, String documentId, String signatureId);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
     * Синхронный метод возвращает контент подписи для документа
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId}.
     *  - идентификатор подписи. Для установки необходимо использовать метод {@link QueryContext#setSignatureId} Id}.
     * @return массив байт контента подписи
     */
    QueryContext<byte[]> getSignatureContent(QueryContext<?> parent);

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate
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
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate
     * Асинхронный метод создает служебный документ для документа ДО с заданным типом
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - тип создаваемого документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentType};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};
     *  - сертификат ключа подписи в кодировке BASE64 без тегов. Для установки необходимо использовать метод {@link QueryContext#setCertificate};
     * @return структура данных для отправки
     * @see DocumentToSend
     */
    QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> parent);

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send
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
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send
     * Синхронный метод отправляет служебный документ в конролирующий орган
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - тип создаваемого документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentType};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};
     *  - структура данных для отправки, созданная с помощью метода {@link DocflowService#generateDocumentTypeReply} | {@link DocflowService#generateDocumentTypeReplyAsync}.
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
     *  - ДО {@link Docflow}. Для установки необходимо использовать метод {@link QueryContext#setDocflow}
     *  - сертификат в кодировке BASE64 без тегов. Для установки необходимо использовать метод {@link QueryContext#setCertificate};
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
     *  - структура данных для отправки, созданная с помощью метода {@link DocflowService#generateRepliesAsync} | {@link DocflowService#generateReplies}.
     *   Перед отправкой в эту структуру данных необходимо установить подпись, вычисленную для контента документа {@link DocumentToSend#getContent()},
     *   с помощью метода {@link DocumentToSend#setSignature(SignatureToSend)}. Подпись должна быть в формате PKCS#7.
     *    Для установки необходимо использовать метод {@link QueryContext#setDocumentToSend}.
     * @return ДО
     * @see DocumentToSend
     */
    QueryContext<Docflow> sendReplies(QueryContext<?> parent);

    /**
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
     * Синхронный метод постранично возвращает список ДО.
     * @param parent контекст. Должен принимать следующие параметры:
     *  - признак завершенности ДО: true - ДО завершен, false - незавершен. Для установки необходимо использовать метод {@link QueryContext#setFinished}
     *  - признак входящего ДО, true - входящие ДО, иначе - исходящие. Для установки необходимо использовать метод {@link QueryContext#setIncoming}
     *  - порядковый номер первого ДО в выходном списке. Для установки необходимо использовать метод {@link QueryContext#setSkip}
     *  - максимальное количество ДО в выходном списке. Для установки необходимо использовать метод {@link QueryContext#setTake}
     *  - ИНН+КПП подотчетной организации. Для установки необходимо использовать метод {@link QueryContext#setInnKpp}
     *  - дата обновления ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается.
     *      Для установки необходимо использовать метод {@link QueryContext#setUpdatedFrom}
     *  - дата обновления ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setUpdatedTo}
     *  - дата создания ДО, с которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setCreatedFrom}
     *  - дата создания ДО, до которой производится поиск. Если передано значение null, то критерий не учитывается
     *      Для установки необходимо использовать метод {@link QueryContext#setCreatedTo}
     *  - type типы ДО, для которых производится поиск. Можно передавать список значений, разделенных запятой
     *      Для установки необходимо использовать метод {@link QueryContext#setType}
     * @return список ДО
     * @see DocflowPage
     */
    QueryContext<DocflowPage> getDocflows(QueryContext<?> parent);

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print
     * Асинхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке BASE64
     * @param docflowId идентификатор ДО
     * @param documentId идентификатор документа
     * @param documentContentBase64 формализованный документ в кодировке BASE64, для которого необходимо создать печатную форму
     * @return печатная форма
     */
    CompletableFuture<QueryContext<String>> printAsync(String docflowId, String documentId, String documentContentBase64);

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print
     * Синхронный метод возвращает печатную форму документа. Печатная форма - PDF файл в кодировке BASE64
     * @param parent контекст. Должен содержать следующие параметры:
     *  - идентификатор ДО. Для установки необходимо использовать метод {@link QueryContext#setDocflowId};
     *  - идентификатор документа. Для установки необходимо использовать метод {@link QueryContext#setDocumentId};
     *  - формализованный документ в кодировке BASE64. Для установки необходимо использовать метод {@link QueryContext#setContentString}.
     * @return печатная форма
     */
    QueryContext<String> print(QueryContext<?> parent);
}
