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

import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.service.transport.adaptor.*;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.DocflowsApi;
import ru.kontur.extern_api.sdk.validator.LinkExists;
import ru.kontur.extern_api.sdk.validator.NoFail;
import ru.kontur.extern_api.sdk.validator.ParamExists;
import ru.kontur.extern_api.sdk.validator.ParamExistsForAll;
import ru.kontur.extern_api.sdk.validator.Stub;

import java.util.*;
import java.util.function.Supplier;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.*;

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
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/generate-reply
     *
     * @param cxt контекст для генерации ответных документов
     * @return контекст с документом, подлежащим отправке
     */
    @Override
    public QueryContext<ReplyDocument> generateReply(QueryContext<?> cxt) {
        return
                new NoFail(
                        new ParamExistsForAll(CERTIFICATE, new GenerateReply())
                ).apply(cxt);
    }

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/generate-reply
     *
     * @param cxt контекст для генерации ответных документов
     * @return контекст со списоком документов, подлежащим отправке
     */
    @Override
    public QueryContext<List<ReplyDocument>> generateReplies(QueryContext<?> cxt) {
        return
                new NoFail(
                        new ParamExistsForAll(CERTIFICATE, new GenerateReply())
                ).applyAtAll(cxt);
    }

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/send
     * Отправка ответного документа
     *
     * @param cxt контекст для отправки документов
     * @return контекст с  документооборотом
     */
    @Override
    public QueryContext<Docflow> sendReply(QueryContext<?> cxt) {
        QueryContext<ReplyDocument> saveSignatureCxt =
                new NoFail<>(
                        new ParamExists<>(REPLY_DOCUMENT,
                                new LinkExists<>("save-signature", cxt.getReplyDocument(),
                                        new SaveSignature()))
                ).apply(cxt);

        return
                new NoFail<>(
                        new ParamExists<>(USER_IP,
                                new LinkExists<>("send", cxt.getReplyDocument(), new SendReply()))
                ).apply(saveSignatureCxt);
    }

    /**
     * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/send
     * Отправка ответного документа
     *
     * @param cxt контекст для отправки документов
     * @return контекст с  документооборотом
     */
    @Override
    public QueryContext<List<Docflow>> sendReplies(QueryContext<?> cxt) {

        QueryContext<List<ReplyDocument>> replyDocumentsCxt =
                new NoFail<>(
                        new ParamExists<>(REPLY_DOCUMENTS, new Stub<List<ReplyDocument>>())
                ).apply(cxt);

        if (replyDocumentsCxt.isFail()) {
            return new QueryContext<List<Docflow>>(replyDocumentsCxt,
                    replyDocumentsCxt.getEntityName()).setServiceError(replyDocumentsCxt);
        }

        List<Docflow> docflows = new ArrayList<>();

        for (ReplyDocument replyDocument : replyDocumentsCxt.getReplyDocuments()) {
            QueryContext<Docflow> docflowCxt =
                    sendReply(
                            new QueryContext<ReplyDocument>(
                                    replyDocumentsCxt,
                                    replyDocumentsCxt.getEntityName()
                            ).setReplyDocument(replyDocument)
                    );
            if (docflowCxt.isFail()) {
                return new QueryContext<>(docflowCxt, docflowCxt.getEntityName());
            }

            docflows.add(docflowCxt.get());
        }

        return new QueryContext<List<Docflow>>(replyDocumentsCxt, replyDocumentsCxt.getEntityName())
                .setResult(docflows, DOCFLOWS);
    }

    /**
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId} Получение
     * ответного документа
     *
     * @param cxt контекст для получения ответного документа
     * @return контекст с данными ответного документа
     * @see ReplyDocument
     */
    @Override
    public QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> cxt) {
        return new NoFail<>(
                new ParamExists<>(DOCFLOW_ID,
                        new ParamExists<>(DOCUMENT_ID,
                                new ParamExists<>(REPLY_ID, new AcqureReplyDocument())
                        )
                )
        ).apply(cxt);
    }

    @Override
    public QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> cxt) {
        return new NoFail<>(
                new ParamExists<>(DOCFLOW_ID,
                        new ParamExists<>(DOCUMENT_ID,
                                new ParamExists<>(REPLY_ID,
                                        new ParamExists<>(CONTENT, new UpdateReplyDocumentContent())
                                )
                        )
                )
        ).apply(cxt);
    }


    /**
     * Allow API user to init cloud sign for reply document from docflow
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/cloud-sign
     *
     * @param cxt QueryContext&lt;byte[]gt; context
     * @return QueryContext&lt;byte[]&gt; context
     */
    @Override
    public QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName()).setResult(
                    transport(cxt)
                            .initCloudSignReplyDocument(
                                    cxt.getAccountProvider().accountId().toString(),
                                    cxt.getDocflowId().toString(),
                                    cxt.getDocumentId().toString(),
                                    cxt.getSignatureId().toString())
                            .getData(),
                    cxt.getEntityName()
            );
        } catch (ApiException x) {
            return new QueryContext<SignInitiation>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    /**
     * Allow API user to confirm cloud sign for reply document from docflow POST
     * /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/cloud-sign-confirm
     *
     * @param cxt контекст
     * @return контекст со списоком документов, подлежащих отправки
     */
    @Override
    public QueryContext<SignConfirmResultData> confirmSignReplyDocument(QueryContext<?> cxt) {
        return
                new NoFail<>(
                        new ParamExists<>(cxt.getEntityName(), new SignConfirmReply())
                ).apply(cxt);
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

    private class SaveSignature implements Query<ReplyDocument> {

        @Override
        public QueryContext<ReplyDocument> apply(QueryContext<?> cxt) {
            try {
                Link saveSignatureLink = Link.class.cast(cxt.get("save-signature-link"));
                HttpClient httpClient = api.getHttpClient();
                httpClient.setServiceBaseUri("");
                ApiResponse<ReplyDocument> signResponse
                        = transport(cxt)
                        .getHttpClient()
                        .setServiceBaseUri("")
                        .submitHttpRequest(
                                saveSignatureLink.getHref(),
                                "PUT",
                                new HashMap<>(),
                                cxt.getReplyDocument().getSignature(),
                                new HashMap<>(),
                                new HashMap<>(),
                                ReplyDocument.class
                        );
                return new QueryContext<ReplyDocument>(cxt, REPLY_DOCUMENT)
                        .setResult(signResponse.getData(), REPLY_DOCUMENT);
            } catch (ApiException x) {
                return new QueryContext<ReplyDocument>(cxt, REPLY_DOCUMENT).setServiceError(x);
            }
        }
    }

    private class SendReply implements Query<Docflow> {

        @Override
        public QueryContext<Docflow> apply(QueryContext<?> cxt) {
            try {
                Link sendLink = Link.class.cast(cxt.get("send-link"));
                HttpClient httpClient = api.getHttpClient();
                httpClient.setServiceBaseUri("");
                ApiResponse<Docflow> sendResponse
                        = transport(cxt)
                        .getHttpClient()
                        .setServiceBaseUri("")
                        .submitHttpRequest(
                                sendLink.getHref(),
                                "POST",
                                new HashMap<>(),
                                new SenderIP(cxt.getUserIP()),
                                new HashMap<>(),
                                new HashMap<>(),
                                Docflow.class
                        );
                return new QueryContext<Docflow>(cxt, DOCFLOW)
                        .setResult(sendResponse.getData(), DOCFLOW);
            } catch (ApiException x) {
                return new QueryContext<Docflow>(cxt, DOCFLOW).setServiceError(x);
            }
        }
    }

    private class GenerateReply implements QueryForComplex<ReplyDocument> {

        @Override
        public QueryContext<ReplyDocument> apply(QueryContext<?> cxt) {
            try {
                Document document = cxt.getDocument();
                String x509Base64 = cxt.getCertificate();

                if (document.getLinks() == null || document.getLinks().isEmpty()) {
                    return new QueryContext<ReplyDocument>(cxt, cxt.getEntityName())
                            .setResult(null, REPLY_DOCUMENT);
                }

                if (x509Base64 == null) {
                    return new QueryContext<ReplyDocument>(cxt, cxt.getEntityName())
                            .setServiceError("A signer certificate is absent in the context.");
                }

                ReplyDocument reply = generateReply(document, x509Base64, cxt);

                return new QueryContext<ReplyDocument>(cxt, cxt.getEntityName())
                        .setResult(reply, REPLY_DOCUMENT);

            } catch (ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException x) {
                return new QueryContext<ReplyDocument>(cxt, cxt.getEntityName())
                        .setServiceError(x);
            }
        }

        @Override
        public QueryContext<List<ReplyDocument>> applyAtAll(QueryContext<?> cxt) {
            try {
                Docflow docflow = cxt.getDocflow();
                String x509Base64 = cxt.getCertificate();

                if (docflow.getLinks() == null || docflow.getLinks().isEmpty()) {
                    return new QueryContext<List<ReplyDocument>>(cxt, REPLY_DOCUMENTS)
                            .setResult(Collections.emptyList(), REPLY_DOCUMENTS);
                }

                if (x509Base64 == null) {
                    return new QueryContext<List<ReplyDocument>>(cxt, cxt.getEntityName())
                            .setServiceError("A signer certificate is absent in the context.");
                }

                List<ReplyDocument> replies = new ArrayList<>();
                for (Document document : docflow.getDocuments()) {
                    ReplyDocument reply = generateReply(document, x509Base64, cxt);
                    if (reply != null) {
                        replies.add(generateReply(document, x509Base64, cxt));
                    }
                }
                return new QueryContext<List<ReplyDocument>>(cxt, REPLY_DOCUMENTS)
                        .setResult(replies, REPLY_DOCUMENTS);

            } catch (ApiException x) {
                return new QueryContext<List<ReplyDocument>>(cxt, REPLY_DOCUMENTS)
                        .setServiceError(x);
            }
        }

        private ReplyDocument generateReply(
                Document document,
                String certificate,
                QueryContext<?> cxt
        ) throws ApiException {

            for (Link l : document.getLinks()) {
                if (!l.getRel().equals("reply")) {
                    continue;
                }

                ApiResponse<ReplyDocument> response
                        = transport(cxt)
                        .getHttpClient()
                        .setServiceBaseUri("")
                        .submitHttpRequest(
                                l.getHref(),
                                "POST",
                                new HashMap<>(),
                                new GenerateReplyDocumentRequestData()
                                        .certificateBase64(certificate),
                                new HashMap<>(),
                                new HashMap<>(),
                                ReplyDocument.class
                        );

                return response.getData();
            }
            return null;
        }
    }

    private class AcqureReplyDocument implements Query<ReplyDocument> {

        @Override
        public QueryContext<ReplyDocument> apply(QueryContext<?> cxt) {
            try {
                return new QueryContext<ReplyDocument>(cxt, "reply-document").setResult(
                        transport(cxt)
                                .getReplyDocument(
                                        cxt.getAccountProvider().accountId().toString(),
                                        cxt.getDocflowId().toString(),
                                        cxt.getDocumentId().toString(),
                                        cxt.getReplyId().toString()
                                ).getData(),
                        REPLY_DOCUMENT
                );
            } catch (ApiException x) {
                return new QueryContext<ReplyDocument>(cxt, "reply-document")
                        .setServiceError(x);
            }
        }
    }

    private class UpdateReplyDocumentContent implements Query<ReplyDocument> {

        @Override
        public QueryContext<ReplyDocument> apply(QueryContext<?> cxt) {
            try {
                return new QueryContext<ReplyDocument>(cxt, "reply-document").setResult(
                        transport(cxt)
                                .updateReplyDocumentContent(
                                        cxt.getAccountProvider().accountId().toString(),
                                        cxt.getDocflowId().toString(),
                                        cxt.getDocumentId().toString(),
                                        cxt.getReplyId().toString(),
                                        cxt.getContent()
                                ).getData(),
                        REPLY_DOCUMENT
                );
            } catch (ApiException x) {
                return new QueryContext<ReplyDocument>(cxt, "reply-document")
                        .setServiceError(x);
            }
        }
    }

    private class SignConfirmReply implements Query<SignConfirmResultData> {

        @Override
        public QueryContext<SignConfirmResultData> apply(QueryContext<?> cxt) {
            try {
                Link sendLink = Link.class.cast(cxt.get("sign-confirm"));
                HttpClient httpClient = api.getHttpClient();
                httpClient.setServiceBaseUri("");
                ApiResponse<SignConfirmResultData> sendResponse
                        = transport(cxt)
                        .getHttpClient()
                        .setServiceBaseUri("")
                        .submitHttpRequest(
                                sendLink.getHref(),
                                "POST",
                                new HashMap<>(),
                                cxt.getSmsCode(),
                                new HashMap<>(),
                                new HashMap<>(),
                                SignConfirmResultData.class
                        );
                return new QueryContext<SignConfirmResultData>(cxt, cxt.getEntityName())
                        .setResult(sendResponse.getData(), cxt.getEntityName());
            } catch (ApiException x) {
                return new QueryContext<SignConfirmResultData>(cxt, cxt.getEntityName())
                        .setServiceError(x);
            }
        }
    }
}
