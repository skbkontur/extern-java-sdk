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
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderService;

public abstract class DraftsBuilderDocumentServiceImpl<
        TDraftsBuilderDocument extends DraftsBuilderDocument,
        TDraftsBuilderDocumentMeta extends DraftsBuilderDocumentMeta,
        TDraftsBuilderDocumentMetaRequest extends DraftsBuilderDocumentMetaRequest,
        TDraftsBuilderService extends DraftsBuilderService,
        TDraftsBuilderDocumentFileService extends DraftsBuilderDocumentFileService,
        TDraftsBuilderDocumentsApi extends DraftsBuilderDocumentsApi<
                TDraftsBuilderDocument,
                TDraftsBuilderDocumentMeta,
                TDraftsBuilderDocumentMetaRequest>>
        implements
        DraftsBuilderDocumentService<
                TDraftsBuilderDocument,
                TDraftsBuilderDocumentMeta,
                TDraftsBuilderDocumentMetaRequest,
                TDraftsBuilderService,
                TDraftsBuilderDocumentFileService> {

    protected final AccountProvider accountProvider;
    protected final TDraftsBuilderDocumentsApi builderDocumentsApi;

    private final UUID draftsBuilderId;

    protected DraftsBuilderDocumentServiceImpl(
            AccountProvider accountProvider,
            TDraftsBuilderDocumentsApi builderDocumentsApi,
            UUID draftsBuilderId
    ) {
        this.accountProvider = accountProvider;
        this.builderDocumentsApi = builderDocumentsApi;

        this.draftsBuilderId = draftsBuilderId;
    }

    protected abstract DraftsBuilderType getDraftsBuilderType();

    @Override
    public UUID getDraftsBuilderId() {
        return draftsBuilderId;
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument> createAsync(
            TDraftsBuilderDocumentMetaRequest meta
    ) {
        return builderDocumentsApi.create(
                accountProvider.accountId(),
                draftsBuilderId,
                meta
        ).whenComplete((document, throwable) -> {
            if (document != null) {
                CheckBuilderType(document.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument[]> getAllAsync() {
        return builderDocumentsApi.getAll(
                accountProvider.accountId(),
                draftsBuilderId
        ).whenComplete((documents, throwable) -> {
            if (documents != null) {
                for (TDraftsBuilderDocument document : documents) {
                    CheckBuilderType(document.getMeta().getBuilderType());
                }
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument> getAsync(
            UUID draftsBuilderDocumentId
    ) {
        return builderDocumentsApi.get(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId
        ).whenComplete((document, throwable) -> {
            if (document != null) {
                CheckBuilderType(document.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument> updateAsync(
            UUID draftsBuilderDocumentId,
            TDraftsBuilderDocumentMetaRequest meta
    ) {
        return builderDocumentsApi.update(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
                meta
        ).whenComplete((document, throwable) -> {
            if (document != null) {
                CheckBuilderType(document.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAsync(
            UUID draftsBuilderDocumentId
    ) {
        return builderDocumentsApi.delete(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentMeta> getMetaAsync(
            UUID draftsBuilderDocumentId
    ) {
        return builderDocumentsApi.getMeta(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId
        ).whenComplete((meta, throwable) -> {
            if (meta != null) {
                CheckBuilderType(meta.getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentMeta> updateMetaAsync(
            UUID draftsBuilderDocumentId,
            TDraftsBuilderDocumentMetaRequest newMeta
    ) {
        return builderDocumentsApi.updateMeta(
                accountProvider.accountId(),
                draftsBuilderId,
                draftsBuilderDocumentId,
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
                    "Incorrect drafts builder document type: %s, expected: %s",
                    type,
                    expectedType
            ));
        }
    }
}
