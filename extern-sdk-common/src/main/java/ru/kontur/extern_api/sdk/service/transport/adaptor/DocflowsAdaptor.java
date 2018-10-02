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
package ru.kontur.extern_api.sdk.service.transport.adaptor;

import java.util.List;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;

/**
 * @author Aleksey Sukhorukov
 */
public interface DocflowsAdaptor {

    /**
     * Get docflow page
     * <p>
     * GET /v1/{accountId}/docflows
     *
     * @param cxt QueryContext&lt;Docflow&gt;
     * @param docflowFilter search filter
     * @return QueryContext&lt;Docflow&gt;
     */
    QueryContext<DocflowPage> getDocflows(QueryContext<?> cxt, DocflowFilter docflowFilter);

    /**
     * Allow API user to get Docflow object
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}
     *
     * @param cxt QueryContext&lt;Docflow&gt;
     * @return QueryContext&lt;Docflow&gt;
     */
    QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt);

    /**
     * Allow API user to get all document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    QueryContext<List<Document>> getDocuments(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    QueryContext<Document> lookupDocument(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete document description from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
     *
     * @param cxt QueryContext&lt;DocumentDescription&gt; context
     * @return QueryContext&lt;DocumentDescription&gt; context
     */
    QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete encrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    QueryContext<byte[]> getEncryptedContent(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete decrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    QueryContext<byte[]> getDecryptedContent(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete document signatures from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
     *
     * @param cxt QueryContext&lt;List&lt;Signature&gt;&gt; context
     * @return QueryContext&lt;List&lt;Signature&gt;&gt; context
     */
    QueryContext<List<Signature>> getSignatures(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete document single signature from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    QueryContext<Signature> getSignature(QueryContext<?> cxt);

    /**
     * Allow API user to get discrete document signature single content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
     *
     * @param cxt QueryContext&lt;byte[]gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    QueryContext<byte[]> getSignatureContent(QueryContext<?> cxt);

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/generate-reply
     *
     * @param cxt контекст для генерации ответных документов
     * @return контекст с документом, подлежащим отправке
     */
    QueryContext<ReplyDocument> generateReply(QueryContext<?> cxt);

    /**
     * <p>Загрузить подпись ответного документа</p>
     * <p>cxt должен содержать:</p>
     * <p>{@link QueryContext#getDocflowId}</p>
     * <p>{@link QueryContext#getDocumentId}</p>
     * <p>{@link QueryContext#getReplyId}</p>
     * <p>{@link QueryContext#getContent} -- подпись</p>
     *
     * @param cxt см. выше
     * @return Обновлённую модель ReplyDocument
     */
    QueryContext<ReplyDocument> putReplyDocumentSignature(QueryContext<?> cxt);

    /**
     * Отправка ответного документа
     *
     * @param cxt контекст для отправки документов
     * @return контекст с  документооборотом
     */
    QueryContext<Docflow> sendReply(QueryContext<?> cxt);

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId} Получение
     * ответного документа
     *
     * @param cxt контекст для получения ответного документа
     * @return контекст с данными ответного документа
     * @see ReplyDocument
     */
    QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> cxt);

    QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> cxt);

    /**
     * Allow API user to init cloud sign for reply document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/cloud-sign
     *
     * @param cxt QueryContext&lt;byte[]gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> cxt);

    /**
     * Allow API user to confirm cloud sign for reply document from docflow POST
     * /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/cloud-sign-confirm
     *
     * @param cxt контекст
     * @return контекст со списоком документов, подлежащих отправки
     */
    QueryContext<SignConfirmResultData> confirmSignReplyDocument(QueryContext<?> cxt);

    /**
     * Allow API user to get document print from docflow
     *
     * @param cxt QueryContext&lt;String&gt; context
     * @return QueryContext&lt;String&gt; context
     */
    QueryContext<String> print(QueryContext<?> cxt);

    /**
     * Инициация облачного расшифрования документа из Docflow
     * @param cxt <p>with params</p>
     * <p>{@link QueryContext#getDocflowId()}</p>
     * <p>{@link QueryContext#getDocumentId()}</p>
     * <p>{@link QueryContext#getCertificate()}</p>
     * @return ссылка на подтверждение расшифрования
     */
    QueryContext<DecryptInitiation> cloudDecryptDocumentInit(QueryContext<?> cxt);

    /**
     * Подтверждение облачного расшифрования документа из Docflow
     * @param cxt <p>with params</p>
     * <p>{@link QueryContext#getDocflowId()}</p>
     * <p>{@link QueryContext#getDocumentId()}</p>
     * <p>{@link QueryContext#getRequestId()}</p>
     * <p>{@link QueryContext#getSmsCode()}</p>
     * @return Расшифрованый конент документа
     */
    QueryContext<byte[]> cloudDecryptDocumentConfirm(QueryContext<?> cxt);

}
