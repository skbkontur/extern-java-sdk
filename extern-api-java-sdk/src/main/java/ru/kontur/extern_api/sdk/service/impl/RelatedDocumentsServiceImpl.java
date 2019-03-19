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

import ru.kontur.extern_api.sdk.httpclient.api.InventoryApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.RelatedDocumentsService;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


public class RelatedDocumentsServiceImpl implements RelatedDocumentsService {

    private final AccountProvider acc;
    private final InventoryApi api;
    private final UUID relatedDocflowId;
    private final UUID relatedDocumentId;

    public RelatedDocumentsServiceImpl(
            AccountProvider accountProvider,
            InventoryApi api,
            UUID docflowId,
            UUID relatedDocumentId) {
        this.acc = accountProvider;
        this.api = api;
        this.relatedDocflowId = docflowId;
        this.relatedDocumentId = relatedDocumentId;
    }

    public RelatedDocumentsServiceImpl(
            AccountProvider accountProvider,
            InventoryApi api,
            Docflow docflow,
            Document relatedDocument) {
        this.acc = accountProvider;
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
        return api.getInventoryDocumentEncryptedContent(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId, inventoryDocumentId);
    }

    @Override
    public CompletableFuture<byte[]> getDecryptedContentAsync(UUID inventoryId, UUID inventoryDocumentId) {
        return api.getInventoryDocumentDecryptedContent(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId, inventoryDocumentId);
    }

    @Override
    public CompletableFuture<List<Signature>> getSignatures(UUID inventoryId, UUID inventoryDocumentId) {
        return api.getInventorySignatures(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId, inventoryDocumentId);
    }

    @Override
    public CompletableFuture<Signature> getSignature(UUID inventoryId, UUID inventoryDocumentId, UUID signatureId) {
        return api.getInventorySignature(acc.accountId(), relatedDocflowId, relatedDocumentId, inventoryId, inventoryDocumentId, signatureId);
    }

    @Override
    public CompletableFuture<Docflow> getLetter(UUID letterId) {
        return null;
    }

    @Override
    public CompletableFuture<InventoriesPage> getRelatedInventories() {
        return api.getRelatedInventories(acc.accountId(), relatedDocflowId, relatedDocumentId);
    }

    @Override
    public CompletableFuture<Draft> createRelatedDraft(DraftMetaRequest draftMeta) {
        RelatedDraftMetaRequest relatedDraftMetaRequest = new RelatedDraftMetaRequest(draftMeta, new RelatedDocument(relatedDocflowId, relatedDocumentId));
        return api.create(acc.accountId(), relatedDraftMetaRequest);
    }
}
