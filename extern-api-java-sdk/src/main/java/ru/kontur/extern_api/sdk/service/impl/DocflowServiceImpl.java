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

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;
import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.join;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DocflowsApi;
import ru.kontur.extern_api.sdk.model.ByteContent;
import ru.kontur.extern_api.sdk.model.CertificateContent;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.RecognizedMeta;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SenderIp;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.UserIPProvider;
import ru.kontur.extern_api.sdk.service.DocflowService;


public class DocflowServiceImpl implements DocflowService {

    private final AccountProvider acc;
    private final UserIPProvider ip;
    private final DocflowsApi api;

    DocflowServiceImpl(
            AccountProvider accountProvider,
            UserIPProvider ipProvider,
            DocflowsApi api) {
        this.acc = accountProvider;
        this.ip = ipProvider;
        this.api = api;
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(UUID docflowId) {
        return api.get(acc.accountId(), docflowId)
                .thenApply(contextAdaptor(QueryContext.DOCFLOW));
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> lookupDocflowAsync(String docflowId) {
        return lookupDocflowAsync(UUID.fromString(docflowId));
    }

    @Override
    public QueryContext<Docflow> lookupDocflow(QueryContext<?> parent) {
        return join(lookupDocflowAsync(parent.<UUID>require(QueryContext.DOCFLOW_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(UUID docflowId) {
        return api.getDocuments(acc.accountId(), docflowId)
                .thenApply(contextAdaptor(QueryContext.DOCUMENTS));
    }

    @Override
    public CompletableFuture<QueryContext<List<Document>>> getDocumentsAsync(String docflowId) {
        return getDocumentsAsync(UUID.fromString(docflowId));
    }

    @Override
    public QueryContext<List<Document>> getDocuments(QueryContext<?> parent) {
        return join(getDocumentsAsync(parent.<UUID>require(QueryContext.DOCFLOW_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<Document>> lookupDocumentAsync(
            UUID docflowId,
            UUID documentId) {
        return api.getDocument(acc.accountId(), docflowId, documentId)
                .thenApply(contextAdaptor(QueryContext.DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<Document>> lookupDocumentAsync(
            String docflowId,
            String documentId) {
        return lookupDocumentAsync(UUID.fromString(docflowId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<Document> lookupDocument(QueryContext<?> parent) {
        return join(lookupDocumentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DocflowDocumentDescription>> lookupDescriptionAsync(
            UUID docflowId,
            UUID documentId) {
        return api.getDocumentDescription(acc.accountId(), docflowId, documentId)
                .thenApply(contextAdaptor(QueryContext.DOCUMENT_DESCRIPTION));
    }

    @Override
    public CompletableFuture<QueryContext<DocflowDocumentDescription>> lookupDescriptionAsync(
            String docflowId,
            String documentId) {
        return lookupDescriptionAsync(UUID.fromString(docflowId), UUID.fromString(documentId));
    }

    @Override
    public CompletableFuture<QueryContext<RecognizedMeta>> recognizeAsync(
            UUID docflowId,
            UUID documentId,
            byte[] documentContent
    ) {
        return this.api.recognize(acc.accountId(), docflowId, documentId, new ByteContent (documentContent)).thenApply(contextAdaptor(QueryContext.RECOGNITION_META));
    }

    @Override
    public QueryContext<DocflowDocumentDescription> lookupDescription(QueryContext<?> parent) {
        return join(lookupDescriptionAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(
            UUID docflowId,
            UUID documentId) {
        return api.getEncryptedContent(acc.accountId(), docflowId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getEncryptedContentAsync(
            String docflowId,
            String documentId) {
        return getEncryptedContentAsync(UUID.fromString(docflowId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<byte[]> getEncryptedContent(QueryContext<?> parent) {
        return join(getEncryptedContentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(
            UUID docflowId,
            UUID documentId) {
        return api.getDecryptedContent(acc.accountId(), docflowId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDecryptedContentAsync(
            String docflowId,
            String documentId) {
        return getDecryptedContentAsync(UUID.fromString(docflowId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<byte[]> getDecryptedContent(QueryContext<?> parent) {
        return join(getDecryptedContentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(
            UUID docflowId,
            UUID documentId) {
        return api.getSignatures(acc.accountId(), docflowId, documentId)
                .thenApply(contextAdaptor(QueryContext.SIGNATURES));
    }

    @Override
    public CompletableFuture<QueryContext<List<Signature>>> getSignaturesAsync(
            String docflowId,
            String documentId) {
        return getSignaturesAsync(UUID.fromString(docflowId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<List<Signature>> getSignatures(QueryContext<?> parent) {
        return join(getSignaturesAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Signature>> getSignatureAsync(
            UUID docflowId,
            UUID documentId,
            UUID signatureId) {
        return api.getSignature(acc.accountId(), docflowId, documentId, signatureId)
                .thenApply(contextAdaptor(QueryContext.SIGNATURE));
    }

    @Override
    public CompletableFuture<QueryContext<Signature>> getSignatureAsync(
            String docflowId,
            String documentId,
            String signatureId) {
        return getSignatureAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(signatureId)
        );
    }

    @Override
    public QueryContext<Signature> getSignature(QueryContext<?> parent) {
        return join(getSignatureAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.SIGNATURE_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            UUID docflowId,
            UUID documentId,
            UUID signatureId) {
        return api.getSignatureContent(acc.accountId(), docflowId, documentId, signatureId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            String docflowId,
            String documentId,
            String signatureId) {
        return getSignatureContentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(signatureId)
        );
    }

    @Override
    public QueryContext<byte[]> getSignatureContent(QueryContext<?> parent) {
        return join(getSignatureContentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.SIGNATURE_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            UUID docflowId,
            UUID documentId,
            String replyType,
            byte[] signerCert) {
        return api.generateReplyDocument(
                acc.accountId(),
                docflowId,
                documentId,
                replyType,
                new CertificateContent(signerCert)
        )
                .thenApply(contextAdaptor(QueryContext.REPLY_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            String docflowId,
            String documentId,
            String replyType,
            String signerCertBase64) {
        return generateReplyAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                replyType,
                Base64.getDecoder().decode(signerCertBase64)
        );
    }

    @Override
    public QueryContext<ReplyDocument> generateReply(QueryContext<?> parent) {
        return join(generateReplyAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.TYPE),
                parent.require(QueryContext.CONTENT)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> uploadReplyDocumentSignatureAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            byte[] signature) {
        return api.updateReplyDocumentContent(acc.accountId(), docflowId, documentId, replyId, signature)
                .thenApply(contextAdaptor(QueryContext.REPLY_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> uploadReplyDocumentSignatureAsync(
            String docflowId,
            String documentId,
            String replyId,
            byte[] signature) {
        return uploadReplyDocumentSignatureAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId),
                signature
        );
    }

    @Override
    public QueryContext<ReplyDocument> uploadReplyDocumentSignature(QueryContext<?> parent) {
        return join(uploadReplyDocumentSignatureAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID),
                parent.require(QueryContext.CONTENT)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId) {
        return api.sendReply(acc.accountId(), docflowId, documentId, replyId, new SenderIp(ip.userIP()))
                .thenApply(contextAdaptor(QueryContext.DOCFLOW));
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            String docflowId,
            String documentId,
            String replyId) {
        return sendReplyAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId)
        );
    }

    @Override
    public QueryContext<Docflow> sendReply(QueryContext<?> parent) {
        return join(sendReplyAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> getReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId) {
        return api.getReplyDocument(acc.accountId(), docflowId, documentId, replyId)
                .thenApply(contextAdaptor(QueryContext.REPLY_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> getReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId) {
        return getReplyDocumentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId)
        );
    }

    @Override
    public QueryContext<ReplyDocument> getReplyDocument(QueryContext<?> parent) {
        return join(getReplyDocumentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentContentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            byte[] content) {
        return api.updateReplyDocumentContent(acc.accountId(), docflowId, documentId, replyId, content)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentContentAsync(
            String docflowId,
            String documentId,
            String replyId,
            byte[] content) {
        return updateReplyDocumentContentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId),
                content
        );
    }

    @Override
    public QueryContext<ReplyDocument> updateReplyDocumentContent(QueryContext<?> parent) {
        return join(updateReplyDocumentContentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID),
                parent.require(QueryContext.CONTENT)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DocflowPage>> getDocflowsAsync(
            @Nullable Boolean finished,
            @Nullable Boolean incoming,
            long skip,
            int take,
            @Nullable String innKpp,
            @Nullable Date updatedFrom,
            @Nullable Date updatedTo,
            @Nullable Date createdFrom,
            @Nullable Date createdTo,
            @Nullable String type
    ) {
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

        return searchDocflowsAsync(filter);
    }

    @Override
    public QueryContext<DocflowPage> getDocflows(QueryContext<?> parent) {
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

        return join(searchDocflowsAsync(filter));
    }

    @Override
    public CompletableFuture<QueryContext<DocflowPage>> searchDocflowsAsync(DocflowFilter filter) {
        return api.search(
                acc.accountId(),
                filter.getSkip(),
                filter.getTake(),
                filter.getOrder(),
                filter.asFilterMap()
        )
                .thenApply(contextAdaptor(QueryContext.DOCFLOW_PAGE));
    }

    @Override
    public QueryContext<DocflowPage> searchDocflows(DocflowFilter filter) {
        return join(searchDocflowsAsync(filter));
    }

    @Override
    public CompletableFuture<QueryContext<String>> printAsync(
            String docflowId,
            String documentId,
            String documentContentBase64) {
        return getDocumentAsPdfAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                Base64.getDecoder().decode(documentContentBase64)
        )
                .thenApply(cxt -> cxt.map(QueryContext.CONTENT_STRING, Base64.getEncoder()::encodeToString));
    }

    @Override
    public QueryContext<String> print(QueryContext<?> parent) {
        return join(printAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID).toString(),
                parent.<UUID>require(QueryContext.DOCUMENT_ID).toString(),
                parent.require(QueryContext.CONTENT_STRING)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDocumentAsPdfAsync(
            UUID docflowId,
            UUID documentId,
            byte[] documentContent) {

        return api.print(acc.accountId(), docflowId, documentId, new ByteContent(documentContent))
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<SignInitiation>> cloudSignReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId) {
        return api.cloudSignReplyDocumentInit(acc.accountId(), docflowId, documentId, replyId, false)
                .thenApply(contextAdaptor("sign-reply"));
    }

    @Override
    public CompletableFuture<QueryContext<SignInitiation>> cloudSignReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId) {
        return cloudSignReplyDocumentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId)
        );
    }

    @Override
    public QueryContext<SignInitiation> cloudSignReplyDocument(QueryContext<?> parent) {
        return join(cloudSignReplyDocumentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<SignConfirmResultData>> cloudSignConfirmReplyDocumentAsync(
            UUID docflowId,
            UUID documentId,
            UUID replyId,
            String requestId,
            String smsCode) {
        return api.cloudSignReplyDocumentConfirm(
                acc.accountId(),
                docflowId,
                documentId,
                replyId,
                requestId,
                smsCode
        ).thenApply(contextAdaptor("sign-reply-result"));
    }

    @Override
    public CompletableFuture<QueryContext<SignConfirmResultData>> cloudSignConfirmReplyDocumentAsync(
            String docflowId,
            String documentId,
            String replyId,
            String requestId,
            String smsCode) {
        return cloudSignConfirmReplyDocumentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                UUID.fromString(replyId),
                requestId,
                smsCode
        );
    }

    @Override
    public QueryContext<SignConfirmResultData> cloudSignConfirmReplyDocument(QueryContext<?> parent) {
        return join(cloudSignConfirmReplyDocumentAsync(
                parent.<UUID>require(QueryContext.DOCFLOW_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.REPLY_ID),
                parent.require(QueryContext.REQUEST_ID),
                parent.require(QueryContext.SMS_CODE)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DecryptInitiation>> cloudDecryptDocumentInitAsync(
            UUID docflowId,
            UUID documentId,
            byte[] certificate) {
        return api.cloudDecryptDocumentInit(
                acc.accountId(),
                docflowId,
                documentId,
                new CertificateContent(certificate)
        )
                .thenApply(contextAdaptor("decrypt-init"));
    }

    @Override
    public QueryContext<DecryptInitiation> cloudDecryptDocumentInit(
            String docflowId,
            String documentId,
            String certBase64) {
        return join(cloudDecryptDocumentInitAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                Base64.getDecoder().decode(certBase64)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> cloudDecryptDocumentConfirmAsync(
            UUID docflowId,
            UUID documentId,
            String requestId,
            String code) {
        return api.cloudDecryptDocumentConfirm(acc.accountId(), docflowId, documentId, requestId, code, true)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public QueryContext<byte[]> cloudDecryptDocumentConfirm(
            String docflowId,
            String documentId,
            String requestId,
            String code) {
        return join(cloudDecryptDocumentConfirmAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                requestId,
                code
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> cloudDecryptDocumentAsync(
            UUID docflowId,
            UUID documentId,
            byte[] certBase64,
            Function<QueryContext<DecryptInitiation>, String> smsCodeProvider) {

        CompletableFuture<QueryContext<DecryptInitiation>> future = cloudDecryptDocumentInitAsync(
                docflowId, documentId, certBase64
        );
        return future.thenCompose(cxt -> future
                .thenApply(smsCodeProvider)
                .thenCompose(code -> cloudDecryptDocumentConfirmAsync(
                        docflowId,
                        documentId,
                        cxt.get().getRequestId(),
                        code)
                )
        );
    }

    @Override
    public QueryContext<byte[]> cloudDecryptDocument(
            String docflowId,
            String documentId,
            String certBase64,
            Function<DecryptInitiation, String> smsCodeProvider) {

        return join(cloudDecryptDocumentAsync(
                UUID.fromString(docflowId),
                UUID.fromString(documentId),
                Base64.getDecoder().decode(certBase64),
                cxt -> smsCodeProvider.apply(cxt.get())
        ));
    }

}
