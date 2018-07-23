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
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;

/**
 *
 * @author alexs
 */
public interface DraftsAdaptor {
    
    /**
     * Create new a draft
     * <p>
     * POST /v1/{billingAccountId}/drafts
     *
     * @param cxt a context
     * @return QueryContext &lt;UUID&gt;
     */
    QueryContext<UUID> createDraft(QueryContext<UUID> cxt);
    
    /**
     * lookup a draft by an identifier
     * <p>
     * GET /v1/{accountId}/drafts/{draftId}
     *
     * @param cxt a context
     * @return Draft
     */
    QueryContext<ru.kontur.extern_api.sdk.model.Draft> lookup(QueryContext<ru.kontur.extern_api.sdk.model.Draft> cxt);

    /**
     * Delete a draft
     * <p>
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * @param cxt a context
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> delete(QueryContext<Void> cxt);

    /**
     * lookup a draft meta by an identifier
     * <p>
     * GET /v1/{accountId}/drafts/{draftId}/meta
     *
     * @param cxt a context
     * @return DraftMeta
     */
    QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> lookupDraftMeta(QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt);

    /**
     * update a draft meta
     * <p>
     * PUT /v1/{accountId}/drafts/{draftId}/meta
     *
     * @param cxt a context
     * @return DraftMeta
     */
    QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> updateDraftMeta(QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt);

    /**
     * Operate CHECK
     * <p>
     * POST /v1/{accountId}/drafts/{draftId}/check
     *
     * @param cxt a context
     * @return Map&lt;String,Object&gt;
     */
    QueryContext<CheckResultData> check(QueryContext<CheckResultData> cxt);

    /**
     * Operate PREPARE
     * <p>
     * POST /v1/{accountId}/drafts/{draftId}/prepare
     *
     * @param cxt a context
     * @return PrepareResult;
     */
    QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> prepare(QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> cxt);

    /**
     * Send the draft
     * <p>
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     *
     * @param cxt a context
     * @return List&lt;Docflow&gt;
     */
    QueryContext<List<Docflow>> send(QueryContext<List<Docflow>> cxt);

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * Delete a document from the draft
     *
     * @param cxt a context (draftId, documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> deleteDocument(QueryContext<Void> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * lookup a document from the draft
     *
     * @param cxt a context (draftId,documentId)
     * @return DraftDocument
     */
    QueryContext<DraftDocument> lookupDocument(QueryContext<DraftDocument> cxt);

    /**
     * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * Update a draft document Update the document
     *
     * @param cxt a context (required: draftId,documentId,DocumentContents)
     * @return DraftDocument
     */
    QueryContext<DraftDocument> updateDocument(QueryContext<DraftDocument> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     * <p>
     * print a document from the draft
     *
     * @param cxt a context (draftId,documentId)
     * @return DraftDocument
     */
    QueryContext<String> printDocument(QueryContext<String> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents
     * <p>
     * Add a new document to the draft
     *
     * @param cxt a context (draftId,documentContent,fileName)
     * @return DraftDocument
     */
    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<DraftDocument> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    QueryContext<String> getDecryptedDocumentContent(QueryContext<String> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<Void> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     * <p>
     * Get a ecrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    QueryContext<String> getEncryptedDocumentContent(QueryContext<String> cxt);

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     * <p>
     * Get a document signature
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    QueryContext<String> getSignatureContent(QueryContext<String> cxt);

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     * <p>
     * Update a document signature
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> updateSignature(QueryContext<Void> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=1
     * <p>
     * Create an USN declaration, version 1
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createUSN1(QueryContext<Void> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=2
     * <p>
     * Create an USN declaration, version 2
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createUSN2(QueryContext<Void> cxt);

    /**
     * POST /v1/{accountId}/drafts/{draftId}/build-document?format=&type=&version=version
     * <p>
     *
     * @param cxt a context
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createDeclOfType(QueryContext<Void> cxt);

    QueryContext<SignInitiation> cloudSignQuery(QueryContext<SignInitiation> cxt);

    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<SignedDraft> cxt);

}
