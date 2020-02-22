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

import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory.RetrofitFnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMetaRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FnsInventoryDraftsBuilderDocumentFilesApi implements
        DraftsBuilderDocumentFilesApi<
                FnsInventoryDraftsBuilderDocumentFile,
                FnsInventoryDraftsBuilderDocumentFileContents,
                FnsInventoryDraftsBuilderDocumentFileMeta,
                FnsInventoryDraftsBuilderDocumentFileMetaRequest> {

    private RetrofitFnsInventoryDraftsBuilderDocumentFilesApi specificContract;
    private RetrofitCommonDraftsBuilderDocumentFilesApi commonContract;

    public FnsInventoryDraftsBuilderDocumentFilesApi(
            RetrofitFnsInventoryDraftsBuilderDocumentFilesApi specificContract,
            RetrofitCommonDraftsBuilderDocumentFilesApi commonContract
    ) {
        this.specificContract = specificContract;
        this.commonContract = commonContract;
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFile> create(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            FnsInventoryDraftsBuilderDocumentFileContents contents
    ) {
        return specificContract.create(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                contents
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFile[]> getAll(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        return specificContract.getAll(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFile> get(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    ) {
        return specificContract.get(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFile> update(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId,
            FnsInventoryDraftsBuilderDocumentFileContents newContents
    ) {
        return specificContract.update(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newContents
        );
    }

    @Override
    public CompletableFuture<Void> delete(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    ) {
        return commonContract.delete(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getContent(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    ) {
        return commonContract.getContent(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getSignature(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    ) {
        return commonContract.getSignature(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFileMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId
    ) {
        return specificContract.getMeta(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFileMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            UUID draftsBuilderDocumentFileId,
            FnsInventoryDraftsBuilderDocumentFileMetaRequest newMeta
    ) {
        return specificContract.updateMeta(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newMeta
        );
    }
}