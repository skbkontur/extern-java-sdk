/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.joda.time.DateTime;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CONTENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CONTENT_STRING;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOW;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOW_PAGE;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENTS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT_DESCRIPTION;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT_TO_SEND;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT_TO_SENDS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SIGNATURE;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SIGNATURES;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocflowDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocflowPageDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocumentDescriptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocumentDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.DocumentToSendDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.SignatureDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.DocflowsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.GenerateReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.PrintDocumentData;

/**
 *
 * @author alexs
 */
public class DocflowsAdaptorImpl extends BaseAdaptor implements DocflowsAdaptor {

    private final DocflowsApi api;

    public DocflowsAdaptorImpl() {
        this.api = new DocflowsApi();
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
        api.setApiClient(apiClient);
    }

    /**
     * Get docflow page
     * <p>
     * GET /v1/{accountId}/docflows
     *
     * @param cxt QueryContext&lt;Docflow&gt;
     * @return QueryContext&lt;Docflow&gt;
     */
    @Override
    public QueryContext<DocflowPage> getDocflows(QueryContext<DocflowPage> cxt) {
        if (cxt.isFail()) {
            return cxt;
        }

        try {
            return cxt.setResult(
                new DocflowPageDto().fromDto(
                    transport(cxt)
                        .docflowsGetDocflowsAsync(
                            cxt.getAccountProvider().accountId(),
                            cxt.getFinished(),
                            cxt.getIncoming(),
                            cxt.getSkip(),
                            cxt.getTake(),
                            cxt.getInnKpp(),
                            cxt.getUpdatedFrom()==null ? null : new DateTime(cxt.getUpdatedFrom().getTime()),
                            cxt.getUpdatedTo()==null ? null : new DateTime(cxt.getUpdatedTo().getTime()),
                            cxt.getCreatedFrom()==null ? null : new DateTime(cxt.getCreatedFrom().getTime()),
                            cxt.getCreatedTo()==null ? null : new DateTime(cxt.getCreatedTo().getTime()),
                            cxt.getType())
                ),
                DOCFLOW_PAGE
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get Docflow object
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}
     *
     * @param cxt QueryContext&lt;Docflow&gt;
     * @return QueryContext&lt;Docflow&gt;
     */
    @Override
    public QueryContext<Docflow> lookupDocflow(QueryContext<Docflow> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DocflowDto().fromDto(
                    transport(cxt)
                        .docflowsGetDocflowAsync(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDocflowId()
                        )
                ),
                DOCFLOW
            ).setDocflowId(cxt.getDocflow().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get all document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    @Override
    public QueryContext<List<Document>> getDocuments(QueryContext<List<Document>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocumentDto documentDto = new DocumentDto();

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetDocumentsAsync(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId()
                    )
                    .stream()
                    .map(documentDto::fromDto)
                    .collect(Collectors.toList()),
                DOCUMENTS
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
     *
     * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
     * @return QueryContext&lt;List&lt;Document&gt;&gt; context
     */
    @Override
    public QueryContext<Document> lookupDocument(QueryContext<Document> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DocumentDto().fromDto(
                    transport(cxt)
                        .docflowsGetDocumentAsync(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDocflowId(),
                            cxt.getDocumentId()
                        )
                ),
                DOCUMENT
            )
                .setDocumentId(cxt.getDocument().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete document description from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
     *
     * @param cxt QueryContext&lt;DocumentDescription&gt; context
     * @return QueryContext&lt;DocumentDescription&gt; context
     */
    @Override
    public QueryContext<DocumentDescription> lookupDescription(QueryContext<DocumentDescription> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DocumentDescriptionDto().fromDto(
                    transport(cxt)
                        .docflowsGetDocumentDescriptionAsync(
                            cxt.getAccountProvider().accountId(),
                            cxt.getDocflowId(),
                            cxt.getDocumentId()
                        )
                ),
                DOCUMENT_DESCRIPTION
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete encrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    @Override
    public QueryContext<byte[]> getEncryptedContent(QueryContext<byte[]> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetEncryptedDocumentContentAsync(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId(),
                        cxt.getDocumentId()
                    ),
                CONTENT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete decrypted document content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
     *
     * @param cxt QueryContext&lt;byte[]&gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    @Override
    public QueryContext<byte[]> getDecryptedContent(QueryContext<byte[]> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetDecryptedDocumentContentAsync(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId(),
                        cxt.getDocumentId()
                    ),
                CONTENT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete document signatures from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
     *
     * @param cxt QueryContext&lt;List&lt;Signature&gt;&gt; context
     * @return QueryContext&lt;List&lt;Signature&gt;&gt; context
     */
    @Override
    public QueryContext<List<Signature>> getSignatures(QueryContext<List<Signature>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            SignatureDto signatureDto = new SignatureDto();

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetDocumentSignaturesAsync(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId(),
                        cxt.getDocumentId()
                    )
                    .stream()
                    .map(signatureDto::fromDto)
                    .collect(Collectors.toList()),
                SIGNATURES
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete document single signature from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    @Override
    public QueryContext<Signature> getSignature(QueryContext<Signature> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new SignatureDto()
                    .fromDto(
                        transport(cxt)
                            .docflowsGetDocumentSignatureAsync(
                                cxt.getAccountProvider().accountId(),
                                cxt.getDocflowId(),
                                cxt.getDocumentId(),
                                cxt.getSignatureId()
                            )
                    ),
                SIGNATURE
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to get discrete document signature single content from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
     *
     * @param cxt QueryContext&lt;byte[]gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    @Override
    public QueryContext<byte[]> getSignatureContent(QueryContext<byte[]> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetDocumentSignatureContentAsync(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId(),
                        cxt.getDocumentId(),
                        cxt.getSignatureId()
                    ),
                CONTENT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to create Reply document for specified workflow
     * <p>
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/generate
     *
     * @param cxt QueryContext&lt;DocumentToSend&gt; context
     * @return QueryContext&lt;DocumentToSend&gt; context
     */
    @Override
    public QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<DocumentToSend> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DocumentToSendDto()
                    .fromDto(
                        transport(cxt)
                            .docflowsGenerateReplyDocumentAsync(
                                cxt.getAccountProvider().accountId(),
                                cxt.getDocflowId(),
                                cxt.getDocumentType(),
                                cxt.getDocumentId(),
                                new GenerateReplyDocumentRequestData().certificateBase64(cxt.getContentString())
                            )
                    ),
                DOCUMENT_TO_SEND
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to send Reply document for specified workflow
     * <p>
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    @Override
    public QueryContext<Docflow> addDocumentTypeReply(QueryContext<Docflow> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new DocflowDto()
                    .fromDto(
                        transport(cxt)
                            .docflowsSendReplyDocumentAsync(
                                cxt.getAccountProvider().accountId(),
                                cxt.getDocflowId(),
                                cxt.getDocumentType(),
                                cxt.getDocumentId(),
                                new DocumentToSendDto().toDto(cxt.getDocumentToSend())
                            )
                    ),
                DOCFLOW
            ).setDocflowId(cxt.getDocflow().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

		@SuppressWarnings("unchecked")
    @Override
    public QueryContext<List<DocumentToSend>> generateReplies(QueryContext<List<DocumentToSend>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            Docflow docflow = cxt.getDocflow();
            if (docflow.getLinks() != null && !docflow.getLinks().isEmpty()) {
                String x509Base64 = cxt.getCertificate();
                if (x509Base64 == null) {
                    return cxt.setServiceError("A signer certificate is absent in the context.");
                }
                prepareTransport(cxt);
                List<DocumentToSend> replies = new ArrayList<>();
                for (Link l : docflow.getLinks()) {
                    if (l.getRel().equals("reply")) {
                        DocumentToSend documentToSend
                            = new DocumentToSendDto()
                                .fromDto(
                                    getApiClient()
                                        .setServiceBaseUri("")
                                        .submitHttpRequest(
                                            l.getHref(),
                                            "POST",
                                            new HashMap<>(),
                                            new GenerateReplyDocumentRequestData().certificateBase64(x509Base64),
                                            new HashMap<>(),
                                            new HashMap<>(),
                                            Map.class
                                        )
                                        .getData()
                                );
                        replies.add(documentToSend);
                    }
                }

                return cxt.setResult(replies, DOCUMENT_TO_SENDS);
            }
            else {
                return cxt.setResult(Collections.emptyList(), DOCUMENT_TO_SENDS);
            }
        }
        catch (ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<Docflow> sendReply(QueryContext<Docflow> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocumentToSend documentToSend = cxt.getDocumentToSend();
            if (documentToSend == null) {
                return cxt.setServiceError("Reply is absent in the context.");
            }

            Link self = documentToSend.getLinks().stream().filter(l -> l.getRel().equals("self")).findAny().orElse(null);
            if (self == null) {
                return cxt.setServiceError("The reply does not contain a self reference.");
            }

            prepareTransport(cxt);

            DocumentToSendDto documentToSendDto = new DocumentToSendDto();
            DocflowDto docflowDto = new DocflowDto();

            String httpRequest = self.getHref().toLowerCase().replaceAll("/generate", "/send");
            Docflow docflow
                = docflowDto
                    .fromDto(
                        getApiClient()
                            .setServiceBaseUri("")
                            .submitHttpRequest(
                                httpRequest,
                                "POST",
                                new HashMap<>(),
                                documentToSendDto.toDto(documentToSend),
                                new HashMap<>(),
                                new HashMap<>(),
                                ru.kontur.extern_api.sdk.service.transport.swagger.model.Docflow.class
                            )
                            .getData()
                    );

            return cxt.setResult(docflow, DOCFLOW);
        }
        catch (ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<String> print(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .docflowsGetDocumentPrintAsyncWithHttpInfo(
                        cxt.getAccountProvider().accountId(),
                        cxt.getDocflowId(),
                        cxt.getDocumentId(),
                        new PrintDocumentData().content(cxt.getContentString())
                    ).getData(),
                CONTENT_STRING
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private DocflowsApi transport(QueryContext<?> cxt) {
        prepareTransport(cxt);
        return api;
    }
}
