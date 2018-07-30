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
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
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
     * @return QueryContext&lt;Docflow&gt;
     */
    public QueryContext<DocflowPage> getDocflows(QueryContext<DocflowPage> cxt);

    /**
     * Allow API user to get Docflow object
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}
     *
     * @param cxt QueryContext&lt;Docflow&gt;
     * @return QueryContext&lt;Docflow&gt;
     */
    public QueryContext<Docflow> lookupDocflow(QueryContext<Docflow> cxt);

    /**
     * Allow API user to get all document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    public QueryContext<List<Document>> getDocuments(QueryContext<List<Document>> cxt);

    /**
     * Allow API user to get discrete document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    public QueryContext<Document> lookupDocument(QueryContext<Document> cxt);

    /**
     * Allow API user to get discrete document description from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
     *
     * @param cxt QueryContext&lt;DocumentDescription&gt; context
     * @return QueryContext&lt;DocumentDescription&gt; context
     */
    public QueryContext<DocumentDescription> lookupDescription(QueryContext<DocumentDescription> cxt);

    /**
     * Allow API user to get discrete encrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    public QueryContext<byte[]> getEncryptedContent(QueryContext<byte[]> cxt);

    /**
     * Allow API user to get discrete decrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    public QueryContext<byte[]> getDecryptedContent(QueryContext<byte[]> cxt);

    /**
     * Allow API user to get discrete document signatures from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
     *
     * @param cxt QueryContext&lt;List&lt;Signature&gt;&gt; context
     * @return QueryContext&lt;List&lt;Signature&gt;&gt; context
     */
    public QueryContext<List<Signature>> getSignatures(QueryContext<List<Signature>> cxt);

    /**
     * Allow API user to get discrete document single signature from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    public QueryContext<Signature> getSignature(QueryContext<Signature> cxt);

    /**
     * Allow API user to get discrete document signature single content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
     *
     * @param cxt QueryContext&lt;byte[]gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    public QueryContext<byte[]> getSignatureContent(QueryContext<byte[]> cxt);

    /**
     * Allow API user to create Reply document for specified workflow
     * <p>
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate
     *
     * @param cxt QueryContext&lt;DocumentToSend&gt; context
     * @return QueryContext&lt;DocumentToSend&gt; context
     */
    public QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<DocumentToSend> cxt);

    /**
     * Allow API user to send Reply document for specified workflow
     * <p>
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    public QueryContext<Docflow> sendDocumentTypeReply(QueryContext<Docflow> cxt);

    public QueryContext<List<DocumentToSend>> generateReplies(QueryContext<List<DocumentToSend>> cxt);

    public QueryContext<Docflow> sendReplies(QueryContext<Docflow> cxt);

    public QueryContext<String> print(QueryContext<String> cxt);
}
