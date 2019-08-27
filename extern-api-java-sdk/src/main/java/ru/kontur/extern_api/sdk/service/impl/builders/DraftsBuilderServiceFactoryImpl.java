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

import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuildersApi;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderServiceFactory;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory.FnsInventoryDraftsBuilderServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.builders.pfr_report.PfrReportDraftsBuilderServiceImpl;

public class DraftsBuilderServiceFactoryImpl implements DraftsBuilderServiceFactory {

    private final AccountProvider acc;

    private final FnsInventoryDraftsBuildersApi fnsInventoryDraftsApi;
    private final FnsInventoryDraftsBuilderDocumentsApi fnsInventoryDocumentApi;
    private final FnsInventoryDraftsBuilderDocumentFilesApi fnsInventoryFileApi;

    private final PfrReportDraftsBuildersApi pfrReportDraftsApi;
    private final PfrReportDraftsBuilderDocumentsApi pfrReportDocumentApi;
    private final PfrReportDraftsBuilderDocumentFilesApi pfrReportFileApi;

    public DraftsBuilderServiceFactoryImpl(
            AccountProvider accountProvider,
            FnsInventoryDraftsBuildersApi fnsInventoryDraftsApi,
            FnsInventoryDraftsBuilderDocumentsApi fnsInventoryDocumentApi,
            FnsInventoryDraftsBuilderDocumentFilesApi fnsInventoryFileApi,
            PfrReportDraftsBuildersApi pfrReportDraftsApi,
            PfrReportDraftsBuilderDocumentsApi pfrReportDocumentApi,
            PfrReportDraftsBuilderDocumentFilesApi pfrReportFileApi
    ) {
        this.acc = accountProvider;
        this.fnsInventoryDraftsApi = fnsInventoryDraftsApi;
        this.fnsInventoryDocumentApi = fnsInventoryDocumentApi;
        this.fnsInventoryFileApi = fnsInventoryFileApi;
        this.pfrReportDraftsApi = pfrReportDraftsApi;
        this.pfrReportDocumentApi = pfrReportDocumentApi;
        this.pfrReportFileApi = pfrReportFileApi;
    }

    @Override
    public FnsInventoryDraftsBuilderService fnsInventory() {
        return new FnsInventoryDraftsBuilderServiceImpl(
                acc,
                fnsInventoryDraftsApi,
                fnsInventoryDocumentApi,
                fnsInventoryFileApi
        );
    }

    @Override
    public PfrReportDraftsBuilderServiceImpl pfrReport() {
        return new PfrReportDraftsBuilderServiceImpl(
                acc,
                pfrReportDraftsApi,
                pfrReportDocumentApi,
                pfrReportFileApi
        );
    }
}
