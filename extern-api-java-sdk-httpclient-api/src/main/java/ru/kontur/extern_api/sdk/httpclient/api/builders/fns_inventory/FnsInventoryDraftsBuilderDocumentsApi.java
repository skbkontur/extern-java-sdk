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

package ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory.RetrofitFnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMetaRequest;

public class FnsInventoryDraftsBuilderDocumentsApi implements
        DraftsBuilderDocumentsApi<
                FnsInventoryDraftsBuilderDocument,
                FnsInventoryDraftsBuilderDocumentMeta,
                FnsInventoryDraftsBuilderDocumentMetaRequest> {

    private RetrofitFnsInventoryDraftsBuilderDocumentsApi specificContract;
    private RetrofitCommonDraftsBuilderDocumentsApi commonContract;

    public FnsInventoryDraftsBuilderDocumentsApi(
            RetrofitFnsInventoryDraftsBuilderDocumentsApi specificContract,
            RetrofitCommonDraftsBuilderDocumentsApi commonContract
    ) {
        this.specificContract = specificContract;
        this.commonContract = commonContract;
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocument> create(
            UUID accountId,
            UUID draftsBuilderId,
            FnsInventoryDraftsBuilderDocumentMetaRequest meta
    ) {
        return specificContract.create(
                accountId,
                draftsBuilderId,
                meta
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocument[]> getAll(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return specificContract.getAll(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocument> get(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        return specificContract.get(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<Void> delete(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        return commonContract.delete(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        return specificContract.getMeta(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            FnsInventoryDraftsBuilderDocumentMetaRequest newMeta
    ) {
        return specificContract.updateMeta(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                newMeta
        );
    }
}