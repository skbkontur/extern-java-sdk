/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

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
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.CheckResultDataDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocflowDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocumentContentsDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftDocumentDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DraftMetaDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.PrepareResultDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.SignConfirmDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.SignInitResultDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.DraftsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.SignConfirmResult;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.SignInitResult;

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
    public QueryContext<UUID> createDraft(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            Draft draft = new DraftDto()
                    .fromDto(
                            transport(cxt)
                                    .draftsCreate(
                                            cxt.getAccountProvider().accountId(),
                                            new DraftMetaDto().toDto(cxt.getDraftMeta())
                                    )
                    );

            return new QueryContext<UUID>(cxt, cxt.getEntityName()).setDraft(draft).setResult(draft.getId(), DRAFT_ID);
        } catch (ApiException x) {
            return new QueryContext<UUID>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.Draft> lookup(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Draft>(cxt, cxt.getEntityName()).setResult(
                    new DraftDto().fromDto(
                            transport(cxt)
                                    .draftsGetDraft(
                                            cxt.getAccountProvider().accountId(),
                                            cxt.getDraftId()
                                    )
                    ),
                    DRAFT
            );
        } catch (ApiException x) {
            return new QueryContext<Draft>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            transport(cxt)
                    .draftsDeleteDraft(cxt.getAccountProvider().accountId(), cxt.getDraftId());

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> lookupDraftMeta(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setResult(
                    new DraftMetaDto().fromDto(
                            transport(cxt).draftsGetMeta(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId()
                            )
                    ),
                    DRAFT_META
            );
        } catch (ApiException x) {
            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.DraftMeta> updateDraftMeta(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setResult(
                    new DraftMetaDto().fromDto(
                            transport(cxt).draftsUpdateDraftMeta(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    new DraftMetaDto().toDto(cxt.getDraftMeta())
                            )
                    ),
                    DRAFT_META
            );
        } catch (ApiException x) {
            return new QueryContext<DraftMeta>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
                    new CheckResultDataDto().fromDto(
                            (Map<String, Object>) transport(cxt).draftsCheck(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId()
                            )
                    ),
                    CHECK_RESULT_DATA
            );
        } catch (ApiException x) {
            return new QueryContext<CheckResultData>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<ru.kontur.extern_api.sdk.model.PrepareResult> prepare(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<PrepareResult>(cxt, cxt.getEntityName()).setResult(
                    new PrepareResultDto().fromDto(
                            transport(cxt).draftsPrepare(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId()
                            )
                    ),
                    PREPARE_RESULT
            );

        } catch (ApiException x) {
            return new QueryContext<PrepareResult>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<List<Docflow>> send(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            DocflowDto docflowDto = new DocflowDto();

            return new QueryContext<List<Docflow>>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<List<Docflow>>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            transport(cxt).draftDocumentsDeleteDocument(cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(), cxt.getDocumentId());

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(new DraftDocumentDto().fromDto(
                    transport(cxt).draftDocumentsGetDocumentAsync(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDraftId(),
                            cxt.getDocumentId()
                    )
                    ),
                    DRAFT_DOCUMENT
            ).setDocumentId(cxt.getDraftDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            DocumentContentsDto documentContentsDto = new DocumentContentsDto();

            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setResult(new DraftDocumentDto()
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
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
                            .draftDocumentsGetDocumentPrintAsyncWithHttpInfo(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    cxt.getDocumentId()
                            ).getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            DocumentContentsDto documentContentsDto = new DocumentContentsDto();

            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName())
                    .setResult(
                            new DraftDocumentDto()
                                    .fromDto(
                                            transport(cxt)
                                                    .draftDocumentsAddDocument(
                                                            cxt.getAccountProvider().accountId(),
                                                            cxt.getDraftId(),
                                                            documentContentsDto.toDto(cxt
                                                                    .getDocumentContents())
                                                    )
                                    ),
                            DRAFT_DOCUMENT
                    )
                    .setDocumentId(cxt.getDraftDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<DraftDocument>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(transport(cxt)
                            .draftDocumentsGetDocumentContent(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    cxt.getDocumentId()
                            ),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
                    .draftDocumentsPutDocumentContent(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDraftId(),
                            cxt.getDocumentId(),
                            cxt.getContent()
                    );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(transport(cxt)
                            .draftDocumentsGetEncryptedDocumentContent(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    cxt.getDocumentId()
                            ),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
                            .draftDocumentsGetSignatureContent(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDraftId(),
                                    cxt.getDocumentId()
                            ),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
                    .draftDocumentsPutDocumentSignature(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDraftId(),
                            cxt.getDocumentId(),
                            cxt.getContent()
                    );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<SignInitiation> cloudSignQuery(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            SignInitResult signInitResult = transport(cxt).draftsSign(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId()
            );

            cxt.setRequestId(signInitResult.getRequestId());
            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName()).setResult(new SignInitResultDto().fromDto(signInitResult), "sign init");
        } catch (ApiException x) {
            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            SignConfirmResult signInitResult = transport(cxt).draftsSignConfirm(
                    cxt.getAccountProvider().accountId(),
                    cxt.getDraftId(),
                    cxt.getRequestId(),
                    cxt.getSmsCode()
            );

            return new QueryContext<SignedDraft>(cxt, cxt.getEntityName()).setResult(new SignConfirmDto().fromDto(signInitResult), "signed documents");
        } catch (ApiException x) {
            return new QueryContext<SignedDraft>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/documents/{documentId}/buildDeclaration</p>
     *
     * @param cxt a context (required: draftId,documentId, version, content)
     * @return QueryContext&lt;Void&gt;
     */
    @Override
    @NotNull
    public QueryContext<Void> buildDeclaration(@NotNull QueryContext<?> cxt) {
        // todo uncomment after fix swagger
//        try {
//            if (cxt.isFail()) {
//                return cxt;
//            }
//
//            transport(cxt).draftDocumentBuildBuildContentFromFormatWithHttpInfo(
//                    cxt.getAccountProvider().accountId(),
//                    cxt.getDraftId(),
//                    cxt.getDocumentId(),
//                    cxt.getType(),
//                    cxt.getVersion(),
//                    new UsnServiceContractInfoDto().toDto(cxt.getUsnServiceContractInfo())
//            );
//            return cxt.setResult(null, QueryContext.NOTHING);
//        } catch (ApiException x) {
//            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
//        }
        return null;
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
        // todo uncomment after fix swagger
//        try {
//            if (cxt.isFail()) {
//                return cxt;
//            }
//
//            return cxt.setResult(new DraftDocumentDto().fromDto(
//                    (ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftDocument)
//                            transport(cxt)
//                                    .draftDocumentBuildCreateDocumentWithBuildContentFromFormat(
//                                            cxt.getAccountProvider().accountId(),
//                                            cxt.getDraftId(),
//                                            cxt.getType(),
//                                            cxt.getVersion(),
//                                            new UsnServiceContractInfoDto()
//                                                    .toDto(cxt.getUsnServiceContractInfo())
//                                    )), QueryContext.DRAFT_DOCUMENT);
//        } catch (ApiException x) {
//            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
//        }
        return null;
    }

    private DraftsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient) prepareTransport(cxt));
        return api;
    }
}
