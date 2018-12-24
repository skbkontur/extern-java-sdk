/*
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
 *
 */

package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;
import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.join;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;


public class DraftServiceImpl implements DraftService {

    private final AccountProvider acc;
    private final DraftsApi api;

    DraftServiceImpl(AccountProvider accountProvider, DraftsApi api) {
        this.acc = accountProvider;
        this.api = api;
    }

    @Override
    public CompletableFuture<QueryContext<Draft>> createAsync(DraftMetaRequest draftMeta) {
        return api.create(acc.accountId(), draftMeta)
                .thenApply(contextAdaptor(QueryContext.DRAFT));
    }

    @Override
    public CompletableFuture<QueryContext<UUID>> createAsync(
            SenderRequest sender,
            Recipient recipient,
            OrganizationRequest payer) {
        return createAsync(new DraftMetaRequest(sender, recipient, payer))
                .thenApply(cxt -> cxt.map(QueryContext.DRAFT_ID, Draft::getId));
    }

    @Override
    public QueryContext<UUID> create(QueryContext<?> parent) {
        QueryContext<Draft> join = join(createAsync(parent.require(QueryContext.DRAFT_META_REQUEST)));
        return join.map(QueryContext.DRAFT_ID, Draft::getId);
    }

    @Override
    public CompletableFuture<QueryContext<Draft>> lookupAsync(UUID draftId) {
        return api.lookup(acc.accountId(), draftId)
                .thenApply(contextAdaptor(QueryContext.DRAFT));
    }

    @Override
    public CompletableFuture<QueryContext<Draft>> lookupAsync(String draftId) {
        return lookupAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<Draft> lookup(QueryContext<?> parent) {
        return join(lookupAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(UUID draftId) {
        return api.delete(acc.accountId(), draftId)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String draftId) {
        return deleteAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> parent) {
        return join(deleteAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(UUID draftId) {
        return api.lookupMeta(acc.accountId(), draftId)
                .thenApply(contextAdaptor(QueryContext.DRAFT_META));
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> lookupDraftMetaAsync(String draftId) {
        return lookupDraftMetaAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<DraftMeta> lookupDraftMeta(QueryContext<?> parent) {
        return join(lookupDraftMetaAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(
            UUID draftId,
            DraftMetaRequest draftMeta) {
        return api.updateMeta(acc.accountId(), draftId, draftMeta)
                .thenApply(contextAdaptor(QueryContext.DRAFT_META));
    }

    @Override
    public CompletableFuture<QueryContext<DraftMeta>> updateDraftMetaAsync(
            String draftId,
            DraftMetaRequest draftMeta) {
        return updateDraftMetaAsync(UUID.fromString(draftId), draftMeta);
    }

    @Override
    public QueryContext<DraftMeta> updateDraftMeta(QueryContext<?> parent) {
        return join(updateDraftMetaAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DRAFT_META_REQUEST)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<CheckResultData>> checkAsync(UUID draftId) {
        return api.check(acc.accountId(), draftId)
                .thenApply(contextAdaptor("data"))
                .thenApply(cxt -> cxt.map(QueryContext.CHECK_RESULT_DATA, DataWrapper::getData));
    }

    @Override
    public CompletableFuture<QueryContext<CheckResultData>> checkAsync(String draftId) {
        return checkAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<CheckResultData> check(QueryContext<?> parent) {
        return join(checkAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(UUID draftId) {
        return api.prepare(acc.accountId(), draftId)
                .thenApply(contextAdaptor(QueryContext.PREPARE_RESULT));
    }

    @Override
    public CompletableFuture<QueryContext<PrepareResult>> prepareAsync(String draftId) {
        return prepareAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<PrepareResult> prepare(QueryContext<?> parent) {
        return join(prepareAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> sendAsync(UUID draftId) {
        return api.send(acc.accountId(), draftId, false, false)
                .thenApply(contextAdaptor(QueryContext.DOCFLOW));

    }

    @Override
    public CompletableFuture<QueryContext<Docflow>> sendAsync(String draftId) {
        return sendAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<Docflow> send(QueryContext<?> parent) {
        return join(sendAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(UUID draftId, UUID documentId) {
        return api.deleteDocument(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteDocumentAsync(String draftId, String documentId) {
        return deleteDocumentAsync(UUID.fromString(draftId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<Void> deleteDocument(QueryContext<?> parent) {
        return join(deleteDocumentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(
            UUID draftId,
            UUID documentId) {
        return api.lookupDocument(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.DRAFT_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> lookupDocumentAsync(
            String draftId,
            String documentId) {
        return lookupDocumentAsync(UUID.fromString(draftId), UUID.fromString(documentId));
    }

    @Override
    public QueryContext<DraftDocument> lookupDocument(QueryContext<?> parent) {
        return join(lookupDocumentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(
            UUID draftId,
            UUID documentId,
            DocumentContents documentContents) {
        return api.updateDocument(acc.accountId(), draftId, documentId, documentContents)
                .thenApply(contextAdaptor(QueryContext.DRAFT_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> updateDocumentAsync(
            String draftId,
            String documentId,
            DocumentContents documentContents) {
        return updateDocumentAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId),
                documentContents);
    }

    @Override
    public QueryContext<DraftDocument> updateDocument(QueryContext<?> parent) {
        return join(updateDocumentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.DOCUMENT_CONTENTS)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDocumentAsPdfAsync(UUID draftId, UUID documentId) {
        return api.printDocument(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<String>> printDocumentAsync(
            String draftId,
            String documentId) {
        return getDocumentAsPdfAsync(UUID.fromString(draftId), UUID.fromString(documentId))
                .thenApply(cxt -> cxt.map(QueryContext.CONTENT_STRING, Base64.getEncoder()::encodeToString));
    }

    @Override
    public QueryContext<String> printDocument(QueryContext<?> parent) {
        return join(printDocumentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID).toString(),
                parent.<UUID>require(QueryContext.DOCUMENT_ID).toString()
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> addDecryptedDocumentAsync(
            UUID draftId,
            DocumentContents documentContents) {
        return api.addDecryptedDocument(acc.accountId(), draftId, documentContents)
                .thenApply(contextAdaptor(QueryContext.DRAFT_DOCUMENT));
    }

    @Override
    public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<?> parent) {
        return join(addDecryptedDocumentAsync(
                parent.require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_CONTENTS)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getDecryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId) {
        return api.getDecryptedDocumentContent(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<String>> getDecryptedDocumentContentAsync(
            String draftId,
            String documentId) {
        return getDecryptedDocumentContentAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId)
        ).thenApply(cxt -> cxt.map(QueryContext.CONTENT_STRING, Base64.getEncoder()::encodeToString));
    }

    @Override
    public QueryContext<String> getDecryptedDocumentContent(QueryContext<?> parent) {
        return join(getDecryptedDocumentContentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID).toString(),
                parent.<UUID>require(QueryContext.DOCUMENT_ID).toString()
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId,
            byte[] content) {
        return api.updateDecryptedDocumentContent(acc.accountId(), draftId, documentId, content)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateDecryptedDocumentContentAsync(
            String draftId,
            String documentId,
            byte[] content) {
        return updateDecryptedDocumentContentAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId),
                content);
    }

    @Override
    public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<?> parent) {
        return join(updateDecryptedDocumentContentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.CONTENT)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getEncryptedDocumentContentAsync(
            UUID draftId,
            UUID documentId) {
        return api.getEncryptedDocumentContent(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<String>> getEncryptedDocumentContentAsync(
            String draftId,
            String documentId) {
        return getEncryptedDocumentContentAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId)
        ).thenApply(cxt -> cxt.map(QueryContext.CONTENT_STRING, Base64.getEncoder()::encodeToString));
    }

    @Override
    public QueryContext<String> getEncryptedDocumentContent(QueryContext<?> parent) {
        return join(getEncryptedDocumentContentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID).toString(),
                parent.<UUID>require(QueryContext.DOCUMENT_ID).toString()
        ));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignatureContentAsync(
            UUID draftId,
            UUID documentId) {
        return api.getSignatureContent(acc.accountId(), draftId, documentId)
                .thenApply(contextAdaptor(QueryContext.CONTENT));
    }

    @Override
    public CompletableFuture<QueryContext<String>> getSignatureContentAsync(
            String draftId,
            String documentId) {
        return getSignatureContentAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId)
        ).thenApply(cxt -> cxt.map(QueryContext.CONTENT_STRING, Base64.getEncoder()::encodeToString));
    }

    @Override
    public QueryContext<String> getSignatureContent(QueryContext<?> parent) {
        return join(getSignatureContentAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID).toString(),
                parent.<UUID>require(QueryContext.DOCUMENT_ID).toString()
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateSignatureAsync(
            UUID draftId,
            UUID documentId,
            byte[] content) {
        return api.updateSignature(acc.accountId(), draftId, documentId, content)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> updateSignatureAsync(
            String draftId,
            String documentId,
            byte[] content) {
        return updateSignatureAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId),
                content
        );
    }

    @Override
    public QueryContext<Void> updateSignature(QueryContext<?> parent) {
        return join(updateSignatureAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.CONTENT)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<SignInitiation>> cloudSignInitAsync(UUID draftId) {
        return api.cloudSignDraft(acc.accountId(), draftId)
                .thenApply(contextAdaptor("sign-init"));
    }

    @Override
    public CompletableFuture<QueryContext<SignInitiation>> cloudSignInitAsync(String draftId) {
        return cloudSignInitAsync(UUID.fromString(draftId));
    }

    @Override
    public QueryContext<SignInitiation> cloudSignInit(QueryContext<?> parent) {
        return join(cloudSignInitAsync(parent.<UUID>require(QueryContext.DRAFT_ID)));
    }

    @Override
    public CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync
            (UUID draftId, String requestId, String code) {
        return api.confirmCloudSigning(acc.accountId(), draftId, requestId, code)
                .thenApply(contextAdaptor("signed"));
    }

    @Override
    public CompletableFuture<QueryContext<SignedDraft>> cloudSignConfirmAsync
            (String draftId, String requestId, String code) {
        return cloudSignConfirmAsync(UUID.fromString(draftId), requestId, code);
    }

    @Override
    public QueryContext<SignedDraft> cloudSignConfirm(QueryContext<?> parent) {
        return join(cloudSignConfirmAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.REQUEST_ID),
                parent.require(QueryContext.SMS_CODE)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<SignedDraft>> cloudSignAsync
            (UUID draftId, Function<QueryContext<SignInitiation>, String> codeProvider) {
        return cloudSignInitAsync(draftId)
                .thenApply(QueryContext::getOrThrow)
                .thenCompose(init -> cloudSignConfirmAsync(
                        draftId,
                        init.getRequestId(),
                        codeProvider.apply(new QueryContext<>("init", init))
                ))
                .exceptionally(QueryContextUtils::completeCareful);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> buildDeclarationAsync(
            UUID draftId,
            UUID documentId,
            int version,
            UsnServiceContractInfo usn) {
        return api.buildDocument(acc.accountId(), draftId, documentId, BuildDocumentType.USN, version, usn)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> buildDeclarationAsync(
            String draftId,
            String documentId,
            int version,
            UsnServiceContractInfo usn) {
        return buildDeclarationAsync(
                UUID.fromString(draftId),
                UUID.fromString(documentId),
                version,
                usn
        );
    }

    @Override
    public QueryContext<Void> buildDeclaration(QueryContext<?> parent) {
        return join(buildDeclarationAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.DOCUMENT_ID),
                parent.require(QueryContext.VERSION),
                parent.require(QueryContext.USN_SERVICE_CONTRACT_INFO)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> createAndBuildDeclarationAsync(
            UUID draftId,
            int version,
            UsnServiceContractInfo usn) {
        return api.createAndBuildDocument(acc.accountId(), draftId, BuildDocumentType.USN, version, usn)
                .thenApply(contextAdaptor(QueryContext.DRAFT_DOCUMENT));
    }

    @Override
    public CompletableFuture<QueryContext<DraftDocument>> createAndBuildDeclarationAsync(
            String draftId,
            int version,
            UsnServiceContractInfo usn) {
        return createAndBuildDeclarationAsync(UUID.fromString(draftId), version, usn);
    }

    @Override
    public QueryContext<DraftDocument> createAndBuildDeclaration(QueryContext<?> parent) {
        return join(createAndBuildDeclarationAsync(
                parent.<UUID>require(QueryContext.DRAFT_ID),
                parent.require(QueryContext.VERSION),
                parent.require(QueryContext.USN_SERVICE_CONTRACT_INFO)
        ));
    }

}
