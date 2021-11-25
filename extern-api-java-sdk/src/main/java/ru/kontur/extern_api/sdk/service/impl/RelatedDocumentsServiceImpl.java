/*
 * Copyright (c) 2019 SKB Kontur
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

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.RelatedDocflowApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.UserIPProvider;
import ru.kontur.extern_api.sdk.service.RelatedDocumentsService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;


public class RelatedDocumentsServiceImpl implements RelatedDocumentsService {

    private final AccountProvider acc;
    private final UserIPProvider ip;
    private final RelatedDocflowApi api;
    private final UUID relatedDocflowId;
    private final UUID relatedDocumentId;

    public RelatedDocumentsServiceImpl(
            AccountProvider accountProvider,
            UserIPProvider ip,
            RelatedDocflowApi api,
            @NotNull UUID docflowId,
            @NotNull UUID relatedDocumentId) {
        this.acc = accountProvider;
        this.ip = ip;
        this.api = api;
        this.relatedDocflowId = docflowId;
        this.relatedDocumentId = relatedDocumentId;
    }

    public RelatedDocumentsServiceImpl(
            AccountProvider accountProvider,
            UserIPProvider ip,
            RelatedDocflowApi api,
            @NotNull Docflow docflow,
            @NotNull Document relatedDocument) {
        this.acc = accountProvider;
        this.ip = ip;
        this.api = api;
        this.relatedDocflowId = docflow.getId();
        this.relatedDocumentId = relatedDocument.getId();
    }

    @Override
    public CompletableFuture<Inventory> getInventory(UUID inventoryId) {
        return api.getInventory(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId);
    }

    @Override
    public CompletableFuture<byte[]> getEncryptedContentAsync(UUID inventoryId, UUID inventoryDocumentId) {
        return api
                .getInventoryDocumentEncryptedContent(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId,
                        inventoryDocumentId);
    }

    @Override
    public CompletableFuture<byte[]> getDecryptedContentAsync(UUID inventoryId, UUID inventoryDocumentId) {
        return api
                .getInventoryDocumentDecryptedContent(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId,
                        inventoryDocumentId);
    }

    @Override
    public CompletableFuture<List<Signature>> getSignatures(UUID inventoryId, UUID inventoryDocumentId) {
        return api.getInventory(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId)
                .thenApply(inventory -> inventory.getDocuments().stream()
                        .filter(document -> document.getId().equals(inventoryDocumentId)).findFirst().get()
                        .getSignatures());
    }

    @Override
    public CompletableFuture<byte[]> getSignatureContent(UUID inventoryId, UUID inventoryDocumentId, UUID signatureId) {
        return api.getInventorySignatureContent(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId,
                inventoryDocumentId, signatureId);
    }

    @Override
    public CompletableFuture<DocflowPage> getRelatedDocflows() {
        return getRelatedDocflows(DocflowFilter.page(0, 1000));
    }

    @Override
    public CompletableFuture<DocflowPage> getRelatedDocflows(DocflowFilter filter) {
        return api.getRelatedDocflows(acc.accountId(), relatedDocflowId, relatedDocumentId, filter.getSkip(),
                filter.getTake(), filter.getOrder(), filter.asFilterMap());
    }


    @Override
    public CompletableFuture<InventoriesPage> getRelatedInventories(DocflowFilter filter) {
        return api.getRelatedInventories(acc.accountId(), relatedDocflowId, relatedDocumentId, filter.getSkip(),
                filter.getTake(), filter.getOrder(), filter.asFilterMap());
    }

    @Override
    public CompletableFuture<InventoriesPage> getRelatedInventories() {
        return getRelatedInventories(DocflowFilter.page(0, 1000));
    }

    @Override
    public CompletableFuture<Draft> createRelatedDraft(DraftMetaRequest draftMeta) {
        DraftMetaRequest relatedDraftMetaRequest = new DraftMetaRequest(draftMeta,
                new RelatedDocumentRequest(relatedDocflowId, relatedDocumentId));
        return api.create(acc.accountId(), relatedDraftMetaRequest);
    }

    public CompletableFuture<QueryContext<ReplyDocument>> generateReplyAsync(
            UUID inventoryId,
            UUID inventoryDocumentId,
            String replyType,
            byte[] signerCert
    ) {
        return api.generateReply(
                acc.accountId(),
                relatedDocflowId,
                relatedDocumentId,
                inventoryId,
                inventoryDocumentId,
                replyType,
                new CertificateContent(signerCert)
        ).thenApply(contextAdaptor(QueryContext.REPLY_DOCUMENT));
    }

    public CompletableFuture<QueryContext<ReplyDocument>> updateReplyDocumentSignature(
            UUID inventoryId,
            UUID inventoryDocumentId,
            UUID replyId,
            byte[] signature
    ) {
        return api.updateReplyDocumentSignature(
                acc.accountId(),
                relatedDocflowId,
                relatedDocumentId,
                inventoryId,
                inventoryDocumentId,
                replyId,
                signature
        ).thenApply(contextAdaptor(QueryContext.REPLY_DOCUMENT));
    }

    public CompletableFuture<QueryContext<Docflow>> sendReplyAsync(
            UUID inventoryId,
            UUID inventoryDocumentId,
            UUID replyId
    ) {
        return api.sendReplyAsync(
                acc.accountId(),
                relatedDocflowId,
                relatedDocumentId,
                inventoryId,
                inventoryDocumentId,
                replyId,
                new SenderIp(ip.userIP())
        ).thenApply(contextAdaptor(QueryContext.DOCFLOW));
    }

    public CompletableFuture<byte[]> print(
            UUID inventoryId,
            UUID inventoryDocumentId,
            byte[] content
    ) {
        return api.print(
                acc.accountId(),
                relatedDocflowId,
                relatedDocumentId,
                inventoryId,
                inventoryDocumentId,
                new ByteContent(content)
        );
    }

    @Override
    public CompletableFuture<TaskInfo> getTaskResult(UUID inventoryId, UUID inventoryDocumentId, UUID taskId) {
        return api.getTaskInfo(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId, inventoryDocumentId, taskId);
    }
}
