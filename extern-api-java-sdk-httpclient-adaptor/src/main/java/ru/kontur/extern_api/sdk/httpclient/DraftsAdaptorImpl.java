/*
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
 *
 */
package ru.kontur.extern_api.sdk.httpclient;

import java.util.UUID;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.DataWrapper;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;

/**
 * @author alexs
 */
public class DraftsAdaptorImpl extends BaseAdaptor implements DraftsAdaptor {

    private final DraftsApi api;

    public DraftsAdaptorImpl() {
        this.api = new DraftsApi();
    }

    @Override
    public HttpClient getHttpClient() {
        return api.getHttpClient();
    }

    @Override
    public void setHttpClient(Supplier<HttpClient> httpClient) {
        super.httpClientSupplier = httpClient;
    }

    /**
     * Create new a draft
     * <p>
     * POST /v1/{billingAccountId}/drafts
     *
     * @param cxt a context
     * @return QueryContext &lt;UUID&gt;
     */
    @Override
    public QueryContext<UUID> createDraft(QueryContext<?> cxt) {
        QueryContext<Draft> c = setResultCarefully(cxt, transport -> transport.createDraft(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftMeta()
        ));
        return new QueryContext<UUID>(c, c.getEntityName())
                .setResult(c.get().getId(), QueryContext.DRAFT_ID);
    }

    /**
     * lookup a draft by an identifier
     * <p>
     * GET /v1/{accountId}/drafts/{draftId}
     *
     * @param cxt a context
     * @return Draft
     */
    @Override
    public QueryContext<Draft> lookup(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookup(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString()
        ));
    }

    /**
     * Delete a draft
     * <p>
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * @param cxt a context
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> delete(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport
                .delete(cxt.getAccountProvider().accountId().toString(), cxt.getDraftId().toString()));
    }

    /**
     * lookup a draft meta by an identifier
     * <p>
     * GET /v1/{accountId}/drafts/{draftId}/meta
     *
     * @param cxt a context
     * @return DraftMeta
     */
    @Override
    public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookupDraftMeta(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString()
        ));
    }

    /**
     * update a draft meta
     * <p>
     * PUT /v1/{accountId}/drafts/{draftId}/meta
     *
     * @param cxt a context
     * @return DraftMeta
     */
    @Override
    public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.updateDraftMeta(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDraftMeta()
        ));
    }

    /**
     * Operate CHECK
     * <p>
     * POST /v1/{accountId}/drafts/{draftId}/check
     *
     * @param cxt a context
     * @return CheckResultData
     */
    @SuppressWarnings("unchecked")
    @Override
    public QueryContext<CheckResultData> check(QueryContext<?> cxt) {
        QueryContext<DataWrapper<CheckResultData>> c = setResultCarefully(cxt, transport -> transport.check(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString()
        ));
        return new QueryContext<CheckResultData>(c, c.getEntityName())
                .setResult(c.get().getData(), QueryContext.CHECK_RESULT_DATA);
    }

    /**
     * Operate PREPARE
     * <p>
     * POST /v1/{accountId}/drafts/{draftId}/prepare
     *
     * @param cxt a context
     * @return PrepareResult;
     */
    @Override
    public QueryContext<PrepareResult> prepare(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.prepare(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString()
        ));
    }

    /**
     * Send the draft
     * <p>
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     *
     * @param cxt a context
     * @return List&lt;Docflow&gt;
     */
    @Override
    public QueryContext<Docflow> send(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.send(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDeffered(),
                cxt.getForce()
        ));
    }

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * Delete a document from the draft
     *
     * @param cxt a context (draftId, documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> deleteDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.deleteDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * lookup a document from the draft
     *
     * @param cxt a context (draftId,documentId)
     * @return DraftDocument
     */
    @Override
    public QueryContext<DraftDocument> lookupDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.lookupDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
     * <p>
     * Update a draft document Update the document
     *
     * @param cxt a context (required: draftId,documentId,DocumentContents)
     * @return DraftDocument
     */
    @Override
    public QueryContext<DraftDocument> updateDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.updateDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getDocumentContents()
        ));
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     * <p>
     * print a document from the draft
     *
     * @param cxt a context (draftId,documentId)
     * @return DraftDocument
     */
    @Override
    public QueryContext<String> printDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.printDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents
     * <p>
     * Add a new document to the draft
     *
     * @param cxt a context (draftId,documentContent,fileName)
     * @return DraftDocument
     */
    @Override
    public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.addDecryptedDocument(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentContents()
        ));
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getDecryptedDocumentContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.updateDecryptedDocumentContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getContent()
        ));
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/encrypted-content
     * <p>
     * Get a encrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getEncryptedDocumentContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     * <p>
     * Get a document signature
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getSignatureContent(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.getSignatureContent(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString()
        ));
    }

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     * <p>
     * Update a document signature
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> updateSignature(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.updateSignature(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getContent()
        ));
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&amp;version=1
     * POST /v1/{accountId}/drafts/{draftId}/cloudSign
     * <p>
     * Initiates the process of cloud signing of the draft
     *
     * @param cxt a context (required: draftId, documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<SignInitiation> cloudSignQuery(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.cloudSignDraft(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString()
        ));
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/build?format=USN&amp;version=2
     * <p>
     * Initiates the process of cloud signing of the draft
     *
     * @param cxt a context (required: draftId, documentId, requestId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.confirmCloudSigning(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getRequestId(),
                cxt.getSmsCode()
        ));
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/build-document?format=type&amp;version=version
     * <p>
     *
     * @param cxt a context (required: draftId,documentId, version, content)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    @NotNull
    public QueryContext<Void> buildDeclaration(@NotNull QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.buildDeclaration(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getDocumentId().toString(),
                cxt.getType(),
                cxt.getVersion(),
                getHttpClient().getGson().toJson(cxt.getUsnServiceContractInfo())
        ));
    }

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/build-document</p>
     *
     * @param cxt a context (required: draftId, version, content)
     * @return QueryContext&lt;DraftDocument&gt;
     */
    @Override
    @NotNull
    public QueryContext<DraftDocument> createAndBuildDeclaration(@NotNull QueryContext<?> cxt) {
        return setResultCarefully(cxt, transport -> transport.createAndBuildDeclaration(
                cxt.getAccountProvider().accountId().toString(),
                cxt.getDraftId().toString(),
                cxt.getType(),
                cxt.getVersion(),
                getHttpClient().getGson().toJson(cxt.getUsnServiceContractInfo())
        ));
    }

    private <T> QueryContext<T> setResultCarefully
            (QueryContext<?> cxt, Function<DraftsApi, ApiResponse<T>> tSupplier) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        try {
            ApiResponse<T> response = tSupplier.apply(transport(cxt));

            if (response.isSuccessful()) {
                return new QueryContext<T>(cxt, cxt.getEntityName())
                        .setResult(response.getData(), cxt.getEntityName());
            } else {
                return new QueryContext<T>(cxt, cxt.getEntityName())
                        .setServiceError(response.asApiException());
            }

        } catch (ApiException x) {
            return new QueryContext<T>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private DraftsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
