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

import java.util.UUID;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentFileServiceImpl;

public class FnsInventoryDraftsBuilderDocumentFileServiceImpl extends
        DraftsBuilderDocumentFileServiceImpl<
                FnsInventoryDraftsBuilderDocumentFile,
                FnsInventoryDraftsBuilderDocumentFileContents,
                FnsInventoryDraftsBuilderDocumentFileMeta,
                FnsInventoryDraftsBuilderDocumentFileMetaRequest,
                FnsInventoryDraftsBuilderDocumentService,
                FnsInventoryDraftsBuilderDocumentFilesApi>
        implements FnsInventoryDraftsBuilderDocumentFileService {

    private final FnsInventoryDraftsBuildersApi builderApi;
    private final FnsInventoryDraftsBuilderDocumentsApi documentApi;

    FnsInventoryDraftsBuilderDocumentFileServiceImpl(
            AccountProvider accountProvider,
            FnsInventoryDraftsBuildersApi builderApi,
            FnsInventoryDraftsBuilderDocumentsApi documentApi,
            FnsInventoryDraftsBuilderDocumentFilesApi api,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        super(accountProvider, api, draftsBuilderId, draftsBuilderDocumentId);
        this.builderApi = builderApi;
        this.documentApi = documentApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.FnsInventory;
    }

    @Override
    public FnsInventoryDraftsBuilderDocumentService getDocumentService() {
        return new FnsInventoryDraftsBuilderDocumentServiceImpl(
                acc,
                builderApi,
                documentApi,
                api,
                getDraftsBuilderId()
        );
    }
}
