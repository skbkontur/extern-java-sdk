/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CONTENT_STRING;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOWS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_DOCUMENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_ID;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DRAFT_META;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.MAP;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.NOTHING;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.PREPARE_RESULT;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocflowDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocumentContentsDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftDocumentDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftMetaDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.PrepareResultDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.UsnServiceContractInfoDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.UsnServiceContractInfoV2Dto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.DraftsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;

/**
 * @author alexs
 */
public class DraftsAdaptorImpl extends BaseAdaptor implements DraftsAdaptor {

    private final DraftsApi api;

    public DraftsAdaptorImpl() {
        this(new DraftsApi());
    }

    public DraftsAdaptorImpl(DraftsApi api) {
        this.api = api;
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
    public QueryContext<UUID> createDraft(QueryContext<UUID> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            Draft draft
                = new DraftDto()
                    .fromDto(
                        transport(cxt)
                            .draftsCreate(
                                cxt.getAccountProvider().accountId(),
                                new DraftMetaDto().toDto(cxt.getDraftMeta())
                            )
                    );

            return cxt.setDraft(draft).setResult(draft.getId(), DRAFT_ID);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.Draft> lookup(
        QueryContext<ru.kontur.extern_api.sdk.model.Draft> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DraftDto().fromDto(
                    transport(cxt)
                        .draftsGetDraft(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDraftId()
                        )
                ),
                DRAFT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Void> delete(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftsDeleteDraft(cxt.getAccountProvider().accountId(), cxt.getDraftId());

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> lookupDraftMeta(
        QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DraftMetaDto().fromDto(
                    transport(cxt).draftsGetMeta(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId()
                    )
                ),
                DRAFT_META
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> updateDraftMeta(
        QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DraftMetaDto().fromDto(
                    transport(cxt).draftsUpdateDraftMeta(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId(),
                        new DraftMetaDto().toDto(cxt.getDraftMeta())
                    )
                ),
                DRAFT_META
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Operate CHECK
     * <p>
     * POST /v1/{accountId}/drafts/{draftId}/check
     *
     * @param cxt a context
     * @return Map&lt;String,Object&gt;
     */
    @SuppressWarnings("unchecked")
    @Override
    public QueryContext<Map<String, Object>> check(QueryContext<Map<String, Object>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                (Map<String, Object>) transport(cxt).draftsCheck(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId()
                ),
                MAP
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> prepare(
        QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new PrepareResultDto().fromDto(
                    transport(cxt).draftsPrepare(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId()
                    )
                ),
                PREPARE_RESULT
            );

        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<List<Docflow>> send(QueryContext<List<Docflow>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocflowDto docflowDto = new DocflowDto();

            return cxt.setResult(
                transport(cxt).draftsSend(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDeffered(),
                    cxt.getForce()
                )
                    .stream()
                    .map(docflowDto::fromDto)
                    .collect(Collectors.toList()),
                DOCFLOWS
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Void> deleteDocument(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt).draftDocumentsDeleteDocument(cxt.getAccountProvider().accountId(),
                cxt.getDraftId(), cxt.getDocumentId());

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<DraftDocument> lookupDocument(QueryContext<DraftDocument> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(new DraftDocumentDto().fromDto(
                transport(cxt).draftDocumentsGetDocumentAsync(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId()
                )
            ),
                DRAFT_DOCUMENT
            ).setDocumentId(cxt.getDraftDocument().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<DraftDocument> updateDocument(QueryContext<DraftDocument> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocumentContentsDto documentContentsDto = new DocumentContentsDto();

            return cxt.setResult(new DraftDocumentDto()
                .fromDto(transport(cxt)
                    .draftDocumentsPutDocument(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId(),
                        cxt.getDocumentId(),
                        documentContentsDto.toDto(cxt.getDocumentContents())
                    )
                ),
                DRAFT_DOCUMENT
            ).setDocumentId(cxt.getDraftDocument().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<String> printDocument(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .draftDocumentsGetDocumentPrintAsyncWithHttpInfo(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId(),
                        cxt.getDocumentId()
                    ).getData(),
                CONTENT_STRING
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<DraftDocument> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocumentContentsDto documentContentsDto = new DocumentContentsDto();

            return cxt
                .setResult(
                    new DraftDocumentDto()
                        .fromDto(
                            transport(cxt)
                                .draftDocumentsAddDocument(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    documentContentsDto.toDto(cxt.getDocumentContents())
                                )
                        ),
                    DRAFT_DOCUMENT
                )
                .setDocumentId(cxt.getDraftDocument().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt)
                .draftDocumentsGetDocumentContent(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId()
                ),
                CONTENT_STRING
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftDocumentsPutDocumentContent(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId(),
                    cxt.getContent()
                );

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     * <p>
     * Get a ecrypted document content
     *
     * @param cxt a context (required: draftId,documentId)
     * @return String
     */
    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt)
                .draftDocumentsGetEncryptedDocumentContent(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId()
                ),
                CONTENT_STRING
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<String> getSignatureContent(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .draftDocumentsGetSignatureContent(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDraftId(),
                        cxt.getDocumentId()
                    ),
                CONTENT_STRING
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Void> updateSignature(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftDocumentsPutDocumentSignature(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId(),
                    cxt.getContent()
                );

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/format/USN/1
     * <p>
     * Create an USN declaration, version 1
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> createUSN1(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftDocumentsBuildContentFromFormatWithHttpInfo(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId(),
                    new UsnServiceContractInfoDto().toDto(cxt.getUsnServiceContractInfo())
                );

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/format/USN/2
     * <p>
     * Create an USN declaration, version 2
     *
     * @param cxt a context (required: draftId,documentId)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> createUSN2(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftDocumentsBuildContentFromFormat_0WithHttpInfo(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getDocumentId(),
                    new UsnServiceContractInfoV2Dto().toDto(cxt.getUsnServiceContractInfoV2())
                );

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * POST /v1/{accountId}/drafts/{draftId}/documents/content/format/{type}/{version}
     * <p>
     *
     * @param cxt a context
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    public QueryContext<Void> createType(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt)
                .draftDocumentsCreateDocumentWithBuildContentFromFormatWithHttpInfo(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getType(),
                    cxt.getVersion(),
                    cxt.getContentString()
                );

            return cxt.setResult(null, NOTHING);
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private DraftsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient) prepareTransport(cxt));
        return api;
    }
}
