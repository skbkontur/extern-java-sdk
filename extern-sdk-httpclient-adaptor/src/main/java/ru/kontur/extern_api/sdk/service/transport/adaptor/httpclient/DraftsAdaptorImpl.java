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
package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CHECK_RESULT_DATA;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CONTENT_STRING;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOWS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_DOCUMENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_ID;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_META;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.NOTHING;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.PREPARE_RESULT;

import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.DraftsApi;

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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            Draft draft
                    = transport(cxt)
                    .createDraft(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftMeta()
                    )
                    .getData();

            return new QueryContext<UUID>(cxt, cxt.getEntityName()).setDraft(draft).setResult(draft.getId(), DRAFT_ID);
        } catch (ApiException x) {
            return new QueryContext<UUID>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Draft>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .lookup(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString()
                            )
                            .getData(),
                    DRAFT
            );
        } catch (ApiException x) {
            return new QueryContext<Draft>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt).delete(cxt.getAccountProvider().accountId().toString(), cxt.getDraftId().toString());

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName())
                    .setResult(
                            transport(cxt)
                                    .lookupDraftMeta(
                                            cxt.getAccountProvider().accountId().toString(),
                                            cxt.getDraftId().toString()
                                    )
                                    .getData(),
                            DRAFT_META
                    );
        } catch (ApiException x) {
            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .updateDraftMeta(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDraftMeta()
                            )
                            .getData(),
                    DRAFT_META
            );
        } catch (ApiException x) {
            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<CheckResultData>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .check(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString()
                            )
                            .getData(),
                    CHECK_RESULT_DATA
            );
        } catch (ApiException x) {
            return new QueryContext<CheckResultData>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<PrepareResult>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .prepare(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString()
                            )
                            .getData(),
                    PREPARE_RESULT
            );

        } catch (ApiException x) {
            return new QueryContext<PrepareResult>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .send(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDeffered(),
                                    cxt.getForce()
                            )
                            .getData(),
                    DOCFLOWS
            );
        } catch (ApiException x) {
            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt).deleteDocument(
                    cxt.getAccountProvider().accountId().toString(),
                    cxt.getDraftId().toString(),
                    cxt.getDocumentId().toString()
            );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<DraftDocument> resultCxt = new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .lookupDocument(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString()
                            )
                            .getData(),
                    DRAFT_DOCUMENT
            );
            return resultCxt.setDocumentId(resultCxt.getDraftDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<DraftDocument> resultCxt = new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .updateDocument(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString(),
                                    cxt.getDocumentContents()
                            )
                            .getData(),
                    DRAFT_DOCUMENT
            );
            return resultCxt.setDocumentId(resultCxt.getDraftDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .printDocument(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString()
                            )
                            .getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<DraftDocument> resultCxt =  new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .addDecryptedDocument(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentContents()
                            )
                            .getData(),
                    DRAFT_DOCUMENT
            );
            return resultCxt.setDocumentId(resultCxt.getDraftDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .getDecryptedDocumentContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString()
                            )
                            .getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    /**
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     * <p>
     * Get a decrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt)
                    .updateDecryptedDocumentContent(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString(),
                            cxt.getDocumentId().toString(),
                            cxt.getContent()
                    );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     * <p>
     * Get a encrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .getEncryptedDocumentContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString()
                            )
                            .getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .getSignatureContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDraftId().toString(),
                                    cxt.getDocumentId().toString()
                            )
                            .getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt)
                    .updateSignature(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString(),
                            cxt.getDocumentId().toString(),
                            cxt.getContent()
                    );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            ApiResponse<SignInitiation> response = transport(cxt)
                    .cloudSignDraft(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString()
                    );

            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName())
                    .setRequestId(response.getData().getRequestId())
                    .setResult(response.getData(), "sign request data");
        } catch (ApiException x) {
            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            ApiResponse<SignedDraft> response = transport(cxt)
                    .confirmCloudSigning(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString(),
                            cxt.getRequestId(),
                            cxt.getSmsCode()
                    );

            return new QueryContext<SignedDraft>(cxt, cxt.getEntityName()).setResult(response.getData(), "signed documents");
        } catch (ApiException x) {
            return new QueryContext<SignedDraft>(cxt, cxt.getEntityName()).setServiceError(x);
        }
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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt)
                    .buildDeclaration(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString(),
                            cxt.getDocumentId().toString(),
                            cxt.getType(),
                            cxt.getVersion(),
                            getHttpClient().getGson().toJson(cxt.getUsnServiceContractInfo())
                    );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }

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
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            ApiResponse<DraftDocument> createAndBuildDeclaration = transport(cxt)
                    .createAndBuildDeclaration(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getDraftId().toString(),
                            cxt.getType(),
                            cxt.getVersion(),
                            getHttpClient().getGson().toJson(cxt.getUsnServiceContractInfo())
                    );

            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(createAndBuildDeclaration.getData(), DRAFT_DOCUMENT);
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private DraftsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
