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

package ru.kontur.extern_api.sdk.service.impl.builders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.httpclient.api.builder.DraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentService;

public abstract class DraftsBuilderDocumentFileServiceImpl<
        TDraftsBuilderDocumentFile extends DraftsBuilderDocumentFile,
        TDraftsBuilderDocumentFileContents extends DraftsBuilderDocumentFileContents,
        TDraftsBuilderDocumentFileMeta extends DraftsBuilderDocumentFileMeta,
        TDraftsBuilderDocumentFileMetaRequest extends DraftsBuilderDocumentFileMetaRequest,
        TDraftsBuilderDocumentService extends DraftsBuilderDocumentService,
        TDraftsBuilderDocumentFilesApi extends DraftsBuilderDocumentFilesApi<
                TDraftsBuilderDocumentFile,
                TDraftsBuilderDocumentFileContents,
                TDraftsBuilderDocumentFileMeta,
                TDraftsBuilderDocumentFileMetaRequest>>
        implements
        DraftsBuilderDocumentFileService<
                TDraftsBuilderDocumentFile,
                TDraftsBuilderDocumentFileContents,
                TDraftsBuilderDocumentFileMeta,
                TDraftsBuilderDocumentFileMetaRequest,
                TDraftsBuilderDocumentService> {

    protected final AccountProvider acc;
    protected final TDraftsBuilderDocumentFilesApi api;

    protected final UUID draftsBuilderId;
    protected final UUID draftsBuilderDocumentId;

    protected DraftsBuilderDocumentFileServiceImpl(
            AccountProvider accountProvider,
            TDraftsBuilderDocumentFilesApi api,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        this.acc = accountProvider;
        this.api = api;
        this.draftsBuilderId = draftsBuilderId;
        this.draftsBuilderDocumentId = draftsBuilderDocumentId;
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> createAsync(
            TDraftsBuilderDocumentFileContents contents
    ) {
        return api.create(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                contents
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile[]> getAllAsync() {
        return api.getAll(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> getAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return api.get(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> updateAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileContents newContents
    ) {
        return api.update(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newContents
        );
    }

    @Override
    public CompletableFuture deleteAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return api.delete(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getContentAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return api.getContent(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getSignatureAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return api.getSignature(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFileMeta> getMetaAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return api.getMeta(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFileMeta> updateMetaAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileMetaRequest newMeta
    ) {
        return api.updateMeta(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newMeta
        );
    }
}
