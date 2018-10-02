/*
 * MIT License
 *
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
 */
package ru.kontur.extern_api.sdk.service.impl;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.ServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author AlexS
 */
public class DocflowServiceImpl extends AbstractService implements DocflowService {

    private static final String EN_DFW = "Документооборот";
    private static final String EN_DOC = "Документ";
    private static final String EN_SGN = "Подпись";

    private final DocflowsAdaptor docflowsAdaptor;

    DocflowServiceImpl(
            ServicesFactory servicesFactory,
            DocflowsAdaptor docflowsAdaptor) {
        super(servicesFactory);
        this.docflowsAdaptor = docflowsAdaptor;
    }

    /**
     * Allow API user to get Docflow object
     * <p>
     * GET /v1/{accountId}/docflows/{docflowId}
     *
     * @param docflowId String an docflow identity
     * @return CompletableFuture&lt;QueryContext&lt;Docflow&gt;&gt;
     */
    @Override
    public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId) {
        QueryContext<Docflow> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .applyAsync(docflowsAdaptor::lookupDocflow);
    }

    @Override
    public QueryContext<Docflow> lookupDocflow(QueryContext<?> parent) {
        QueryContext<Docflow> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::lookupDocflow);
    }

    @Override
    public CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId) {
        QueryContext<List<Document>> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .applyAsync(docflowsAdaptor::getDocuments);
    }

    @Override
    public QueryContext<List<Document>> getDocuments(QueryContext<?> parent) {
        QueryContext<List<Document>> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::getDocuments);
    }

    @Override
    public CompletableFuture<QueryContext<Document>> lookupDocumentAsync(String docflowId,
            String documentId) {
        QueryContext<Document> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .applyAsync(docflowsAdaptor::lookupDocument);
    }

    @Override
    public QueryContext<Document> lookupDocument(QueryContext<?> parent) {
        QueryContext<Document> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(docflowsAdaptor::lookupDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DocflowDocumentDescription>> lookupDescriptionAsync(
            String docflowId, String documentId) {
        QueryContext<DocflowDocumentDescription> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .applyAsync(docflowsAdaptor::lookupDescription);
    }

    @Override
    public QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> parent) {
        QueryContext<DocflowDocumentDescription> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(docflowsAdaptor::lookupDescription);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(String docflowId,
            String documentId) {
        QueryContext<byte[]> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .applyAsync(docflowsAdaptor::getEncryptedContent);
    }

    @Override
    public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent) {
        QueryContext<byte[]> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(docflowsAdaptor::getEncryptedContent);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(String docflowId,
            String documentId) {
        QueryContext<byte[]> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .applyAsync(docflowsAdaptor::getDecryptedContent);
    }

    @Override
    public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent) {
        QueryContext<byte[]> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(docflowsAdaptor::getDecryptedContent);
    }

    @Override
    public CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(String docflowId,
            String documentId) {
        QueryContext<List<Signature>> cxt = createQueryContext(EN_DOC);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .applyAsync(docflowsAdaptor::getSignatures);
    }

    @Override
    public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent) {
        QueryContext<List<Signature>> cxt = createQueryContext(parent, EN_DOC);
        return cxt.apply(docflowsAdaptor::getSignatures);
    }

    @Override
    public CompletableFuture<QueryContext<Signature>> getSignatureAsync(String docflowId,
            String documentId, String signatureId) {
        QueryContext<Signature> cxt = createQueryContext(EN_SGN);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setSignatureId(signatureId)
                .applyAsync(docflowsAdaptor::getSignature);
    }

    @Override
    public QueryContext<Signature> getSignature(QueryContext<?> parent) {
        QueryContext<Signature> cxt = createQueryContext(parent, EN_SGN);
        return cxt.apply(docflowsAdaptor::getSignature);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(String docflowId,
            String documentId, String signatureId) {
        QueryContext<byte[]> cxt = createQueryContext(EN_SGN);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setSignatureId(signatureId)
                .applyAsync(docflowsAdaptor::getSignatureContent);
    }

    @Override
    public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent) {
        QueryContext<byte[]> cxt = createQueryContext(parent, EN_SGN);
        return cxt.apply(docflowsAdaptor::getSignatureContent);
    }

    @Override
    public QueryContext<ReplyDocument> generateReply(QueryContext<?> parent) {
        QueryContext<ReplyDocument> cxt = createQueryContext(parent, EN_SGN);
        return cxt.apply(docflowsAdaptor::generateReply);
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            String docflowId,
            String documentId,
            String replyType,
            String signerX509Base64) {
        QueryContext<ReplyDocument> cxt = createQueryContext(EN_SGN);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setDocumentType(replyType)
                .setCertificate(signerX509Base64)
                .applyAsync(docflowsAdaptor::generateReply);
    }


    @Override
    public QueryContext<ReplyDocument> uploadReplyDocumentSignature(QueryContext<?> parent) {
        QueryContext<ReplyDocument> cxt = createQueryContext(parent, EN_SGN);
        return cxt.apply(docflowsAdaptor::putReplyDocumentSignature);
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> uploadReplyDocumentSignatureAsync(
            String docflowId,
            String documentId,
            String replyId,
            byte[] signature) {
        QueryContext<ReplyDocument> cxt = createQueryContext(EN_SGN);

        return cxt
                .setDocumentId(documentId)
                .setDocflowId(docflowId)
                .setReplyId(replyId)
                .setContent(signature)
                .applyAsync(docflowsAdaptor::putReplyDocumentSignature);
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            String docflowId,
            String documentId,
            String replyId) {
        QueryContext<Docflow> cxt = createQueryContext(EN_DFW);
        QueryContext<String> userIPCxt = this.getUserIPProvider().userIP();
        if (userIPCxt.isFail()) {
            return CompletableFuture.completedFuture(cxt.setServiceError(userIPCxt));
        }
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setReplyId(replyId)
                .setUserIP(userIPCxt.get())
                .applyAsync(docflowsAdaptor::sendReply);
    }

    @Override
    public QueryContext<Docflow> sendReply(QueryContext<?> parent) {
        QueryContext<Docflow> cxt = createQueryContext(parent, EN_DFW);
        QueryContext<String> userIPCxt = this.getUserIPProvider().userIP();
        if (userIPCxt.isFail()) {
            return cxt.setServiceError(userIPCxt);
        }
        return cxt
                .setUserIP(userIPCxt.get())
                .apply(docflowsAdaptor::sendReply);
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> getReplyDocumentAsync(String docflowId,
            String documentId, String replyId) {
        QueryContext<ReplyDocument> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setReplyId(replyId)
                .applyAsync(docflowsAdaptor::getReplyDocument);
    }

    @Override
    public QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> parent) {
        QueryContext<ReplyDocument> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::getReplyDocument);
    }

    public CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentContentAsync(
            String docflowId, String documentId, String replyId, byte[] content) {
        QueryContext<ReplyDocument> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setReplyId(replyId)
                .setContent(content)
                .applyAsync(docflowsAdaptor::updateReplyDocumentContent);
    }

    @Override
    public QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> parent) {
        QueryContext<ReplyDocument> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::getReplyDocument);
    }

    @Override
    public CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(
            Boolean finished,
            Boolean incoming,
            long skip,
            int take,
            String innKpp,
            Date updatedFrom,
            Date updatedTo,
            Date createdFrom,
            Date createdTo,
            String type
    ) {
        QueryContext<DocflowPage> cxt = createQueryContext(EN_DFW);

        DocflowFilter filter = DocflowFilter
                .page(skip, take)
                .inn(innKpp)
                .updatedFrom(updatedFrom)
                .updatedTo(updatedTo)
                .createdFrom(createdFrom)
                .createdTo(createdTo)
                .type(type);

        Optional.ofNullable(finished).ifPresent(filter::finished);
        Optional.ofNullable(incoming).ifPresent(filter::incoming);

        return CompletableFuture.supplyAsync(() -> docflowsAdaptor.getDocflows(cxt, filter));
    }

    @Override
    public QueryContext<DocflowPage> getDocflows(QueryContext<?> parent) {
        QueryContext<DocflowPage> cxt = createQueryContext(parent, EN_DFW);

        DocflowFilter filter = DocflowFilter
                .page(parent.getSkip(), parent.getTake())
                .inn(parent.getInnKpp())
                .updatedFrom(parent.getUpdatedFrom())
                .updatedTo(parent.getUpdatedTo())
                .createdFrom(parent.getCreatedFrom())
                .createdTo(parent.getCreatedTo())
                .type(parent.getType());

        Optional.ofNullable(parent.getFinished()).ifPresent(filter::finished);
        Optional.ofNullable(parent.getIncoming()).ifPresent(filter::incoming);

        return docflowsAdaptor.getDocflows(cxt, filter);
    }

    @Override
    public QueryContext<DocflowPage> searchDocflows(DocflowFilter searchFilter) {
        return docflowsAdaptor.getDocflows(createQueryContext(EN_DFW), searchFilter);
    }

    @Override
    public CompletableFuture<QueryContext<String>> printAsync(String docflowId, String documentId,
            String documentContentBase64) {
        QueryContext<String> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setContentString(documentContentBase64)
                .applyAsync(docflowsAdaptor::print);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDocumentAsPdfAsync(
            String docflowId,
            String documentId,
            byte[] documentContent) {
        return printAsync(docflowId, documentId, Base64.getEncoder().encodeToString(documentContent))
                .thenApply(cxt -> {
                    if (cxt.isFail()) {
                        return new QueryContext<byte[]>().setServiceError(cxt.getServiceError());
                    }
                    byte[] decoded = Base64.getDecoder().decode(cxt.get());
                    return new QueryContext<byte[]>().setResult(decoded, QueryContext.CONTENT);
                });
    }

    @Override
    public QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> parent) {
        QueryContext<SignInitiation> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::cloudSignReplyDocument);
    }

    @Override
    public CompletableFuture<QueryContext<SignConfirmResultData>> cloudSignConfirmReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId,
            String requestId,
            String smsCode) {
        QueryContext<SignConfirmResultData> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setReplyId(replyId)
                .setRequestId(requestId)
                .setSmsCode(smsCode)
                .applyAsync(docflowsAdaptor::confirmSignReplyDocument);
    }

    @Override
    public QueryContext<SignConfirmResultData> cloudSignConfirmReplyDocument(
            QueryContext<?> parent) {
        QueryContext<SignConfirmResultData> cxt = createQueryContext(EN_DFW);
        return cxt.apply(docflowsAdaptor::confirmSignReplyDocument);
    }

    @Override
    public CompletableFuture<QueryContext<SignInitiation>> cloudSignReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId) {
        QueryContext<SignInitiation> cxt = createQueryContext(EN_DFW);
        return cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setReplyId(replyId)
                .applyAsync(docflowsAdaptor::cloudSignReplyDocument);
    }

    @Override
    public QueryContext<String> print(QueryContext<?> parent) {
        QueryContext<String> cxt = createQueryContext(parent, EN_DFW);
        return cxt.apply(docflowsAdaptor::print);
    }

    @Override
    public QueryContext<DecryptInitiation> cloudDecryptDocumentInit(
            String docflowId,
            String documentId,
            String certBase64) {
        QueryContext<String> cxt = this.createQueryContext(EN_DFW);
        return docflowsAdaptor.cloudDecryptDocumentInit(cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setCertificate(certBase64)
        );
    }

    @Override
    public QueryContext<byte[]> cloudDecryptDocumentConfirm(
            String docflowId,
            String documentId,
            String requestId,
            String code) {
        QueryContext<String> cxt = this.createQueryContext(EN_DFW);
        return docflowsAdaptor.cloudDecryptDocumentConfirm(cxt
                .setDocflowId(docflowId)
                .setDocumentId(documentId)
                .setRequestId(requestId)
                .setSmsCode(code)
        );

    }

    @Override
    public QueryContext<byte[]> cloudDecryptDocument(
            String docflowId,
            String documentId,
            String certBase64,
            Function<DecryptInitiation, String> smsCodeProvider) {

        QueryContext<DecryptInitiation> initCxt = cloudDecryptDocumentInit(docflowId, documentId, certBase64);

        if (initCxt.isFail()) {
            return new QueryContext<byte[]>(initCxt, initCxt.getEntityName())
                    .setServiceError(initCxt.getServiceError());
        }

        DecryptInitiation init = initCxt.get();
        return cloudDecryptDocumentConfirm(
                docflowId,
                documentId,
                init.getRequestId(),
                smsCodeProvider.apply(init)
        );
    }

}
