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

package ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory;

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderServiceImpl;

import java.util.UUID;

public class FnsInventoryDraftsBuilderServiceImpl extends
        DraftsBuilderServiceImpl<
                FnsInventoryDraftsBuilder,
                FnsInventoryDraftsBuilderMeta,
                FnsInventoryDraftsBuilderMetaRequest,
                FnsInventoryDraftsBuilderDocumentService,
                FnsInventoryDraftsBuildersApi>
        implements FnsInventoryDraftsBuilderService {

    private final FnsInventoryDraftsBuilderDocumentsApi builderDocumentsApi;
    private final FnsInventoryDraftsBuilderDocumentFilesApi builderDocumentFilesApi;

    public FnsInventoryDraftsBuilderServiceImpl(
            AccountProvider accountProvider,
            FnsInventoryDraftsBuildersApi buildersApi,
            FnsInventoryDraftsBuilderDocumentsApi builderDocumentsApi,
            FnsInventoryDraftsBuilderDocumentFilesApi builderDocumentFilesApi
    ) {
        super(accountProvider, buildersApi);
        this.builderDocumentsApi = builderDocumentsApi;
        this.builderDocumentFilesApi = builderDocumentFilesApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.FnsInventory;
    }

    @Override
    public FnsInventoryDraftsBuilderDocumentService getDocumentService(
            @NotNull UUID draftsBuilderId
    ) {
        return new FnsInventoryDraftsBuilderDocumentServiceImpl(
                accountProvider,
                specificApi,
                builderDocumentsApi,
                builderDocumentFilesApi,
                draftsBuilderId
        );
    }
}