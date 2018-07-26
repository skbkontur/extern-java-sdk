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
     * {@code POST /v1/{accountId}/drafts}
     * <p>Create new a draft</p>
     * @param cxt a context(required: draftMeta)
     * @return {@code QueryContext<UUID>}
     */
    QueryContext<UUID> createDraft(QueryContext<UUID> cxt);
    
    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}}
     * <p>lookup a draft by an identifier</p>
     * @param cxt a context(required: draftId)
     * @return a context with a {@link ru.kontur.extern_api.sdk.model.Draft}
     */
    QueryContext<ru.kontur.extern_api.sdk.model.Draft> lookup(QueryContext<ru.kontur.extern_api.sdk.model.Draft> cxt);

    /**
     * {@code DELETE /v1/{accountId}/drafts/{draftId}}
     * <p>Delete a draft</p>
     * @param cxt a context(required: draftId)
     * @return {@code QueryContext<Void>}
     */
    QueryContext<Void> delete(QueryContext<Void> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/meta}
     * <p>lookup a draft meta by an identifier</p>
     * @param cxt a context(required: draftId)
     * @return a context with a {@link ru.kontur.extern_api.sdk.model.DraftMeta}
     */
    QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> lookupDraftMeta(QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt);

    /**
     * {@code PUT /v1/{accountId}/drafts/{draftId}/meta}
     * <p>update a draft meta</p>
     * @param cxt a context(required: draftId)
     * @return a context with a {@link ru.kontur.extern_api.sdk.model.DraftMeta}
     */
    QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> updateDraftMeta(QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/check}
     * <p>Operate CHECK</p>
     * @param cxt a context(required: draftId)
     * @return a context with a {@link ru.kontur.extern_api.sdk.model.CheckResultData}
     */
    QueryContext<CheckResultData> check(QueryContext<CheckResultData> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/prepare}
     * <p>Operate PREPARE</p>
     * @param cxt a context(required: draftId)
     * @return a context with a {@link ru.kontur.extern_api.sdk.model.PrepareResult}
     */
    QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> prepare(QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/drafts/{draftId}/send}
     * <p>Send the draft</p>
     * @param cxt a context(required: draftId)
     * @return a context with {@code List<Docflow>}
     */
    QueryContext<List<Docflow>> send(QueryContext<List<Docflow>> cxt);

    /**
     * {@code DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}}
     * <p>Delete a document from the draft</p>
     * @param cxt a context (required: draftId, documentId)
     * @return {@code QueryContext<Void>}
     */
    QueryContext<Void> deleteDocument(QueryContext<Void> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}}
     * <p>lookup a document from the draft</p>
     * @param cxt a context (required: draftId,documentId)
     * @return a context with a {@link DraftDocument}
     */
    QueryContext<DraftDocument> lookupDocument(QueryContext<DraftDocument> cxt);

    /**
     * {@code PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}}
     * <p>Update a draft document Update the document</p>
     * @param cxt a context (required: draftId,documentId,DocumentContents)
     * @return a context with a {@link DraftDocument}
     */
    QueryContext<DraftDocument> updateDocument(QueryContext<DraftDocument> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print}
     * <p>print a document from the draft</p>
     * @param cxt a context (required: draftId,documentId)
     * @return a context with print form as PDF of a base64 code
     */
    QueryContext<String> printDocument(QueryContext<String> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/documents}
     * <p>Add a new document to the draft</p>
     * @param cxt a context (required: draftId,documentContent,fileName)
     * @return a context with a {@link DraftDocument}
     */
    QueryContext<DraftDocument> addDecryptedDocument(QueryContext<DraftDocument> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted}
     * <p>Get a decrypted document content</p>
     * @param cxt a context (required: draftId,documentId)
     * @return a context with a decrypted PKCS#7 context of a base64 code
     */
    QueryContext<String> getDecryptedDocumentContent(QueryContext<String> cxt);

    /**
     * {@code PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted}
     * <p>Get a decrypted document content</p>
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> updateDecryptedDocumentContent(QueryContext<Void> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted}
     * <p>Get a ecrypted document content</p>
     * @param cxt a context (required: draftId,documentId)
     * @return a context with a encrypted PKCS#7 context of a base64 code
     */
    QueryContext<String> getEncryptedDocumentContent(QueryContext<String> cxt);

    /**
     * {@code GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature}
     * <p>Get a document signature</p>
     * @param cxt a context (required: draftId,documentId)
     * @return a context with a PKCS#7 signature of a base64 code
     */
    QueryContext<String> getSignatureContent(QueryContext<String> cxt);

    /**
     * {@code PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature}
     * <p>Update a document signature</p>
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> updateSignature(QueryContext<Void> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=1}
     * <p>Create an USN declaration, version 1</p>
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createUSN1(QueryContext<Void> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&version=2}
     * <p>Create an USN declaration, version 2</p>
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createUSN2(QueryContext<Void> cxt);

    /**
     * {@code POST /v1/{accountId}/drafts/{draftId}/build-document?format=&type=&version=version}
     * <p>Creates a Declaration with a selected type</p>
     * @param cxt a context
     * @return QueryContext&lt;Void&gt;
     */
    QueryContext<Void> createDeclOfType(QueryContext<Void> cxt);

    QueryContext<SignInitiation> cloudSignQuery(QueryContext<SignInitiation> cxt);

    QueryContext<SignedDraft> cloudSignConfirm(QueryContext<SignedDraft> cxt);

}
