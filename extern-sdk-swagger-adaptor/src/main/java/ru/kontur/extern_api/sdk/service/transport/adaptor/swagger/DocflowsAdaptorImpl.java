/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import com.google.gson.reflect.TypeToken;
import org.joda.time.DateTime;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.*;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.DocflowsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.GenerateReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.PrintDocumentData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.*;

/**
 * @author alexs
 */
public class DocflowsAdaptorImpl extends BaseAdaptor implements DocflowsAdaptor {

    private final DocflowsApi api;

    public DocflowsAdaptorImpl() {
        this.api = new DocflowsApi();
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
    public QueryContext<DocflowPage> getDocflows(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        try {
            return new QueryContext<DocflowPage>(cxt, cxt.getEntityName()).setResult(
                    new DocflowPageDto().fromDto(
                            transport(cxt)
                                    .docflowsGetDocflowsAsync(
                                            cxt.getAccountProvider().accountId(),
                                            cxt.getFinished(),
                                            cxt.getIncoming(),
                                            cxt.getSkip(),
                                            cxt.getTake(),
                                            cxt.getInnKpp(),
                                            cxt.getUpdatedFrom() == null ? null : new DateTime(cxt.getUpdatedFrom().getTime()),
                                            cxt.getUpdatedTo() == null ? null : new DateTime(cxt.getUpdatedTo().getTime()),
                                            cxt.getCreatedFrom() == null ? null : new DateTime(cxt.getCreatedFrom().getTime()),
                                            cxt.getCreatedTo() == null ? null : new DateTime(cxt.getCreatedTo().getTime()),
                                            cxt.getType())
                    ),
                    DOCFLOW_PAGE
            );
        } catch (ApiException x) {
            return new QueryContext<DocflowPage>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Docflow> lookupDocflow(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<Docflow> resultCxt = new QueryContext<Docflow>(cxt, cxt.getEntityName()).setResult(
                    new DocflowDto().fromDto(
                            transport(cxt)
                                    .docflowsGetDocflowAsync(
                                            cxt.getAccountProvider().accountId(),
                                            cxt.getDocflowId()
                                    )
                    ),
                    DOCFLOW
            );
            return resultCxt.setDocflowId(cxt.getDocflow().getId());
        } catch (ApiException x) {
            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<List<Document>> getDocuments(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            DocumentDto documentDto = new DocumentDto();

            return new QueryContext<List<Document>>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<List<Document>>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Document> lookupDocument(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<Document> resultCxt = new QueryContext<Document>(cxt, cxt.getEntityName()).setResult(
                    new DocumentDto().fromDto(
                            transport(cxt)
                                    .docflowsGetDocumentAsync(
                                            cxt.getAccountProvider().accountId(),
                                            cxt.getDocflowId(),
                                            cxt.getDocumentId()
                                    )
                    ),
                    DOCUMENT
            );
            return resultCxt.setDocumentId(cxt.getDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<Document>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<DocumentDescription> lookupDescription(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DocumentDescription>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<DocumentDescription>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<byte[]> getEncryptedContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .docflowsGetEncryptedDocumentContentAsync(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDocflowId(),
                                    cxt.getDocumentId()
                            ),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<byte[]> getDecryptedContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .docflowsGetDecryptedDocumentContentAsync(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDocflowId(),
                                    cxt.getDocumentId()
                            ),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<List<Signature>> getSignatures(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            SignatureDto signatureDto = new SignatureDto();

            return new QueryContext<List<Signature>>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<List<Signature>>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<Signature> getSignature(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Signature>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<Signature>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<byte[]> getSignatureContent(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .docflowsGetDocumentSignatureContentAsync(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDocflowId(),
                                    cxt.getDocumentId(),
                                    cxt.getSignatureId()
                            ),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
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
    public QueryContext<DocumentToSend> generateDocumentTypeReply(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DocumentToSend>(cxt, cxt.getEntityName()).setResult(
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
        } catch (ApiException x) {
            return new QueryContext<DocumentToSend>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    /**
     * Allow API user to send Reply document for specified workflow
     * <p>
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}/send
     *
     * @param cxt QueryContext&lt;Signature&gt; context
     * @return QueryContext&lt;Signature&gt; context
     */
    @Override
    public QueryContext<Docflow> sendDocumentTypeReply(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<Docflow> resultCxt = new QueryContext<Docflow>(cxt, cxt.getEntityName()).setResult(
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
            );
            return resultCxt.setDocflowId(resultCxt.getDocflow().getId());
        } catch (ApiException x) {
            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryContext<List<ReplyDocument>> generateReplies(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            Docflow docflow = cxt.getDocflow();
            if (docflow.getLinks() != null && !docflow.getLinks().isEmpty()) {
                String x509Base64 = cxt.getCertificate();
                if (x509Base64 == null) {
                    return new QueryContext<List<ReplyDocument>>(cxt, cxt.getEntityName()).setServiceError("A signer certificate is absent in the context.");
                }
                HttpClient httpClient = prepareTransport(cxt);
                List<ReplyDocument> replies = new ArrayList<>();
                for (Link l : docflow.getLinks()) {
                    if (l.getRel().equals("reply")) {

                        ApiResponse<ReplyDocument> response
                                = httpClient
                                .setServiceBaseUri("")
                                .submitHttpRequest(
                                        l.getHref(),
                                        "POST",
                                        new HashMap<>(),
                                        new GenerateReplyDocumentRequestData().certificateBase64(x509Base64),
                                        new HashMap<>(),
                                        new HashMap<>(),
                                        new TypeToken<ReplyDocument>() {
                                        }.getType()
                                );

                        replies.add(response.getData());
                    }
                }

                return new QueryContext<List<ReplyDocument>>(cxt, cxt.getEntityName()).setResult(replies, REPLY_DOCUMENTS);
            } else {
                return new QueryContext<List<ReplyDocument>>(cxt, cxt.getEntityName()).setResult(Collections.emptyList(), REPLY_DOCUMENTS);
            }
        } catch (ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException x) {
            return new QueryContext<List<ReplyDocument>>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<Docflow> sendReply(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            DocumentToSend documentToSend = cxt.getDocumentToSend();
            if (documentToSend == null) {
                return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError("Reply is absent in the context.");
            }

            Link self = documentToSend.getLinks().stream().filter(l -> l.getRel().equals("self")).findAny().orElse(null);
            if (self == null) {
                return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError("The reply does not contain a self reference.");
            }

            HttpClient httpClient = prepareTransport(cxt);

            DocumentToSendDto documentToSendDto = new DocumentToSendDto();
            DocflowDto docflowDto = new DocflowDto();

            String httpRequest = self.getHref().toLowerCase().replaceAll("/generate", "/send");

            ApiResponse<ru.kontur.extern_api.sdk.service.transport.swagger.model.Docflow> response
                    = httpClient
                    .setServiceBaseUri("")
                    .submitHttpRequest(
                            httpRequest,
                            "POST",
                            new HashMap<>(),
                            documentToSendDto.toDto(documentToSend),
                            new HashMap<>(),
                            new HashMap<>(),
                            new TypeToken<ru.kontur.extern_api.sdk.service.transport.swagger.model.Docflow>() {
                            }.getType()
                    );

            Docflow docflow = docflowDto.fromDto(response.getData());

            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setResult(docflow, DOCFLOW);
        } catch (ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException x) {
            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<List<Docflow>> sendReplies(QueryContext<?> cxt) {
        throw new NotImplementedException();
    }

    @Override
    public QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> cxt) {
        throw new NotImplementedException();
    }

    @Override
    public QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> cxt) {
        throw new NotImplementedException();
    }

    @Override
    public QueryContext<String> print(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .docflowsGetDocumentPrintAsyncWithHttpInfo(
                                    cxt.getAccountProvider().accountId(),
                                    cxt.getDocflowId(),
                                    cxt.getDocumentId(),
                                    new PrintDocumentData().content(cxt.getContentString())
                            ).getData(),
                    CONTENT_STRING
            );
        } catch (ApiException x) {
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private DocflowsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient) prepareTransport(cxt));
        return api;
    }
}
