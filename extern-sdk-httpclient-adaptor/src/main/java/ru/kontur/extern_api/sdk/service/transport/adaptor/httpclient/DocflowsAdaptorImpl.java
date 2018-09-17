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
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOW_ID;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCFLOW_PAGE;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENTS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT_DESCRIPTION;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.DOCUMENT_ID;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.FORCE_CONFIRMATION;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.REPLY_DOCUMENT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.REPLY_ID;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SIGNATURE;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SIGNATURES;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SMS_CODE;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.USER_IP;
import static ru.kontur.extern_api.sdk.utils.UncheckedSupplier.get;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.GenerateReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.PrintDocumentData;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SendReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.SenderIP;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.Query;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.DocflowsApi;
import ru.kontur.extern_api.sdk.validator.LinkExists;
import ru.kontur.extern_api.sdk.validator.NoFail;
import ru.kontur.extern_api.sdk.validator.ParamExists;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowsAdaptorImpl extends BaseAdaptor implements DocflowsAdaptor {

    private final DocflowsApi api;

    DocflowsAdaptorImpl() {
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
    public QueryContext<DocflowPage> getDocflows(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        try {
            return new QueryContext<DocflowPage>(cxt, cxt.getEntityName()).setResult(
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
            return new QueryContext<DocflowPage>(cxt, cxt.getEntityName()).setServiceError(x);
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

            QueryContext<Docflow> resultCxt = new QueryContext<>(cxt, cxt.getEntityName());
            return resultCxt.setResult(
                    transport(cxt)
                            .lookupDocflow(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString())
                            .getData(),
                    DOCFLOW
            ).setDocflowId(resultCxt.getDocflow().getId());
        } catch (ApiException x) {
            return new QueryContext<Docflow>(cxt, cxt.getEntityName()).setServiceError(x);
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

            return new QueryContext<List<Document>>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .getDocuments(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString()
                            )
                            .getData(),
                    DOCUMENTS
            );
        } catch (ApiException x) {
            return new QueryContext<List<Document>>(cxt, cxt.getEntityName()).setServiceError(x);
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

            QueryContext<Document> resultCxt = new QueryContext<Document>(cxt, cxt.getEntityName())
                    .setResult(
                            transport(cxt)
                                    .lookupDocument(
                                            cxt.getAccountProvider().accountId().toString(),
                                            cxt.getDocflowId().toString(),
                                            cxt.getDocumentId().toString())
                                    .getData(),
                            DOCUMENT
                    );
            return resultCxt.setDocumentId(resultCxt.getDocument().getId());
        } catch (ApiException x) {
            return new QueryContext<Document>(cxt, cxt.getEntityName()).setServiceError(x);
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
    public QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<DocflowDocumentDescription>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .lookupDescription(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString())
                            .getData(),
                    DOCUMENT_DESCRIPTION
            );
        } catch (ApiException x) {
            return new QueryContext<DocflowDocumentDescription>(cxt, cxt.getEntityName())
                    .setServiceError(x);
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
                            .getEncryptedContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString())
                            .getData(),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(x);
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
                            .getDecryptedContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString())
                            .getData(),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(x);
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

            return new QueryContext<List<Signature>>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .getSignatures(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString())
                            .getData(),
                    SIGNATURES
            );
        } catch (ApiException x) {
            return new QueryContext<List<Signature>>(cxt, cxt.getEntityName()).setServiceError(x);
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
            return new QueryContext<Signature>(cxt, cxt.getEntityName()).setServiceError(x);
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
                            .getSignatureContent(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString(),
                                    cxt.getSignatureId().toString())
                            .getData(),
                    CONTENT
            );
        } catch (ApiException x) {
            return new QueryContext<byte[]>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    /**
     * Allow API user to get document print from docflow
     *
     * @param cxt QueryContext&lt;String&gt; context
     * @return QueryContext&lt;String&gt; context
     */
    @Override
    public QueryContext<String> print(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<String>(cxt, cxt.getEntityName()).setResult(
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
            return new QueryContext<String>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private DocflowsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
