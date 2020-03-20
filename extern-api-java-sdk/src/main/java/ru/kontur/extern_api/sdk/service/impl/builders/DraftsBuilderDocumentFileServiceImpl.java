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

import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.model.builders.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

    protected final AccountProvider accountProvider;
    protected final TDraftsBuilderDocumentFilesApi builderDocumentFilesApi;

    private final UUID draftsBuilderId;
    private final UUID draftsBuilderDocumentId;

    protected DraftsBuilderDocumentFileServiceImpl(
            AccountProvider accountProvider,
            TDraftsBuilderDocumentFilesApi builderDocumentFilesApi,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        this.accountProvider = accountProvider;
        this.builderDocumentFilesApi = builderDocumentFilesApi;
        this.draftsBuilderId = draftsBuilderId;
        this.draftsBuilderDocumentId = draftsBuilderDocumentId;
    }

    protected abstract DraftsBuilderType getDraftsBuilderType();

    @Override
    public UUID getDraftsBuilderId() {
        return draftsBuilderId;
    }

    @Override
    public UUID getDraftsBuilderDocumentId() {
        return draftsBuilderDocumentId;
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> createAsync(
            TDraftsBuilderDocumentFileContents contents
    ) {
        return builderDocumentFilesApi.create(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                contents
        ).whenComplete((file, throwable) -> {
            if (file != null) {
                CheckBuilderType(file.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile[]> getAllAsync() {
        return builderDocumentFilesApi.getAll(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId
        ).whenComplete((files, throwable) -> {
            if (files != null) {
                for (TDraftsBuilderDocumentFile file : files) {
                    CheckBuilderType(file.getMeta().getBuilderType());
                }
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> getAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return builderDocumentFilesApi.get(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        ).whenComplete((file, throwable) -> {
            if (file != null) {
                CheckBuilderType(file.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFile> updateAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileContents newContents
    ) {
        return builderDocumentFilesApi.update(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newContents
        ).whenComplete((file, throwable) -> {
            if (file != null) {
                CheckBuilderType(file.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return builderDocumentFilesApi.delete(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getContentAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return builderDocumentFilesApi.getContent(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<byte[]> getSignatureAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return builderDocumentFilesApi.getSignature(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFileMeta> getMetaAsync(
            UUID draftsBuilderDocumentFileId
    ) {
        return builderDocumentFilesApi.getMeta(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId
        ).whenComplete((meta, throwable) -> {
            if (meta != null) {
                CheckBuilderType(meta.getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentFileMeta> updateMetaAsync(
            UUID draftsBuilderDocumentFileId,
            TDraftsBuilderDocumentFileMetaRequest newMeta
    ) {
        return builderDocumentFilesApi.updateMeta(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                draftsBuilderDocumentFileId,
                newMeta
        ).whenComplete((meta, throwable) -> {
            if (meta != null) {
                CheckBuilderType(meta.getBuilderType());
            }
        });
    }

    private void CheckBuilderType(DraftsBuilderType type) {
        DraftsBuilderType expectedType = getDraftsBuilderType();
        if (type != expectedType) {
            throw new ApiException(String.format(
                    "Incorrect drafts builder document file type: %s, expected: %s",
                    type,
                    expectedType
            ));
        }
    }
}
