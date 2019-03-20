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
import ru.kontur.extern_api.sdk.httpclient.api.builder.DraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderDocumentMetaRequest;
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

    protected final AccountProvider acc;
    protected final TDraftsBuilderDocumentsApi api;

    protected final UUID draftsBuilderId;

    protected DraftsBuilderDocumentServiceImpl(
            AccountProvider accountProvider,
            TDraftsBuilderDocumentsApi api,
            UUID draftsBuilderId
    ) {
        this.acc = accountProvider;
        this.api = api;

        this.draftsBuilderId = draftsBuilderId;
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument> createAsync(
            TDraftsBuilderDocumentMetaRequest meta
    ) {
        return api.create(
                acc.accountId(),
                draftsBuilderId,
                meta
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument[]> getAllAsync() {
        return api.getAll(
                acc.accountId(),
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocument> getAsync(
            UUID draftsBuilderDocumentId
    ) {
        return api.get(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture deleteAsync(
            UUID draftsBuilderDocumentId
    ) {
        return api.delete(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentMeta> getMetaAsync(
            UUID draftsBuilderDocumentId
    ) {
        return api.getMeta(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderDocumentMeta> updateMetaAsync(
            UUID draftsBuilderDocumentId,
            TDraftsBuilderDocumentMetaRequest newMeta
    ) {
        return api.updateMeta(
                acc.accountId(),
                draftsBuilderId,
                draftsBuilderId,
                newMeta
        );
    }
}
