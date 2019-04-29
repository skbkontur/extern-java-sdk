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
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMetaRequest;

public class FnsInventoryDraftsBuilderDocumentFilesApi implements
        DraftsBuilderDocumentFilesApi<
                FnsInventoryDraftsBuilderDocumentFile,
                FnsInventoryDraftsBuilderDocumentFileContents,
                FnsInventoryDraftsBuilderDocumentFileMeta,
                FnsInventoryDraftsBuilderDocumentFileMetaRequest> {

    private RetrofitFnsInventoryDraftsBuilderDocumentFilesApi api;

    public FnsInventoryDraftsBuilderDocumentFilesApi(RetrofitFnsInventoryDraftsBuilderDocumentFilesApi api) {
        this.api = api;
    }

    @Override
    public CompletableFuture<FnsInventoryDraftsBuilderDocumentFile> create(
            UUID accountId,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId,
            FnsInventoryDraftsBuilderDocumentFileContents contents
    ) {
        return api.create(
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
        return api.getAll(
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
        return api.get(
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
        return api.update(
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
        return api.delete(
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
        return api.getContent(
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
        return api.getSignature(
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
        return api.getMeta(
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
        return api.updateMeta(
                accountId,
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newMeta
        );
    }
}
