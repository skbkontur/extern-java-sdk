/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

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

import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentToSend;
import ru.kontur.extern_api.sdk.model.GenerateReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.PrintDocumentData;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.DocflowsApi;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowsAdaptorImpl extends BaseAdaptor implements DocflowsAdaptor {

    private final DocflowsApi api;

    public DocflowsAdaptorImpl() {
        api = new DocflowsApi();
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
                transport(cxt)
                    .getDocflows(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getFinished(),
                        cxt.getIncoming(),
                        cxt.getSkip(),
                        cxt.getTake(),
                        cxt.getInnKpp(),
                        cxt.getUpdatedFrom(),
                        cxt.getUpdatedTo(),
                        cxt.getCreatedFrom(),
                        cxt.getCreatedTo(),
                        cxt.getType())
                    .getData(),
                DOCFLOW_PAGE
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                transport(cxt)
                    .lookupDocflow(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString())
                    .getData(),
                DOCFLOW
            ).setDocflowId(cxt.getDocflow().getId());
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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

            return cxt.setResult(
                transport(cxt)
                    .getDocuments(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString()
                    )
                    .getData(),
                DOCUMENTS
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                transport(cxt)
                    .lookupDocument(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString())
                    .getData(),
                DOCUMENT
            )
                .setDocumentId(cxt.getDocument().getId());
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
    public QueryContext<DocumentDescription> lookupDescription(
        QueryContext<DocumentDescription> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .lookupDescription(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString())
                    .getData(),
                DOCUMENT_DESCRIPTION
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                    .getEncryptedContent(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString())
                    .getData(),
                CONTENT
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                    .getDecryptedContent(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString())
                    .getData(),
                CONTENT
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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

            return cxt.setResult(
                transport(cxt)
                    .getSignatures(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString())
                    .getData(),
                SIGNATURES
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                transport(cxt)
                    .getSignature(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString(),
                        cxt.getSignatureId().toString())
                    .getData(),
                SIGNATURE
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
                    .getSignatureContent(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString(),
                        cxt.getSignatureId().toString())
                    .getData(),
                CONTENT
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
    public QueryContext<DocumentToSend> generateDocumentTypeReply(
        QueryContext<DocumentToSend> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .generateDocumentTypeReply(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString(),
                        cxt.getDocumentType().toLowerCase(),
                        new GenerateReplyDocumentRequestData()
                            .certificateBase64(cxt.getContentString()))
                    .getData(),
                DOCUMENT_TO_SEND
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
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
    public QueryContext<Docflow> sendDocumentTypeReply(QueryContext<Docflow> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .sendReplyDocument(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentType(),
                        cxt.getDocumentId().toString(),
                        cxt.getDocumentToSend())
                    .getData(),
                DOCFLOW
            ).setDocflowId(cxt.getDocflow().getId());
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<List<DocumentToSend>> generateReplies(
        QueryContext<List<DocumentToSend>> cxt) {
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

                List<DocumentToSend> replies = new ArrayList<>();
                for (Link l : docflow.getLinks()) {
                    if (l.getRel().equals("reply")) {
                        ApiResponse<Map<String, Object>> response
                            = transport(cxt)
                            .getHttpClient()
							.setServiceBaseUri("")
                            .submitHttpRequest(
                                l.getHref(),
                                "POST",
                                new HashMap<>(),
                                new GenerateReplyDocumentRequestData().certificateBase64(x509Base64),
                                new HashMap<>(),
                                new HashMap<>(),
                                new TypeToken<DocumentToSend>() {
                                }.getType()
                            );
                        replies.add((DocumentToSend) response.getData());
                    }
                }

                return cxt.setResult(replies, DOCUMENT_TO_SENDS);
            } else {
                return cxt.setResult(Collections.emptyList(), DOCUMENT_TO_SENDS);
            }
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<Docflow> sendReplies(QueryContext<Docflow> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            DocumentToSend documentToSend = cxt.getDocumentToSend();
            if (documentToSend == null) {
                return cxt.setServiceError("Reply is absent in the context.");
            }

            Link self = documentToSend.getLinks().stream().filter(l -> l.getRel().equals("self"))
                .findAny().orElse(null);
            if (self == null) {
                return cxt.setServiceError("The reply does not contain a self reference.");
            }

            String httpRequest = self.getHref().toLowerCase().replaceAll("/generate", "/send");

            ApiResponse<Docflow> response
                = api
                .getHttpClient()
                .submitHttpRequest(
                    httpRequest,
                    "POST",
                    new HashMap<>(),
                    documentToSend,
                    new HashMap<>(),
                    new HashMap<>(),
                    new TypeToken<Docflow>() {
                    }.getType()
                );

            return cxt.setResult(response.getData(), DOCFLOW);
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    /**
     * Allow API user to get document print from docflow
     *
     * @param cxt QueryContext&lt;String&gt; context
     * @return QueryContext&lt;String&gt; context
     */
    @Override
    public QueryContext<String> print(QueryContext<String> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                transport(cxt)
                    .print(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getDocflowId().toString(),
                        cxt.getDocumentId().toString(),
                        new PrintDocumentData().content(cxt.getContentString())
                    ).getData(),
                CONTENT_STRING
            );
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    private DocflowsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
