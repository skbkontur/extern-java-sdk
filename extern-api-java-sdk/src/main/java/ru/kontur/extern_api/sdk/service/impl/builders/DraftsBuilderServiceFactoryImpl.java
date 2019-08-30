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

import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderServiceFactory;
import ru.kontur.extern_api.sdk.service.builders.fns_inventory.FnsInventoryDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory.FnsInventoryDraftsBuilderServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory.FnsInventoryDraftsBuildersApiFactory;
import ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory.PfrReportDraftsBuildersApiFactory;
import ru.kontur.extern_api.sdk.service.impl.builders.pfr_report.PfrReportDraftsBuilderServiceImpl;

public class DraftsBuilderServiceFactoryImpl implements DraftsBuilderServiceFactory {

    private final AccountProvider acc;

    private final FnsInventoryDraftsBuildersApiFactory fnsInventoryDraftsBuildersApiFactory;
    private final PfrReportDraftsBuildersApiFactory pfrReportDraftsBuildersApiFactory;

    public DraftsBuilderServiceFactoryImpl(
            AccountProvider accountProvider,

            FnsInventoryDraftsBuildersApiFactory fnsInventoryDraftsBuildersApiFactory,
            PfrReportDraftsBuildersApiFactory pfrReportDraftsBuildersApiFactory
    ) {
        this.acc = accountProvider;

        this.fnsInventoryDraftsBuildersApiFactory = fnsInventoryDraftsBuildersApiFactory;
        this.pfrReportDraftsBuildersApiFactory = pfrReportDraftsBuildersApiFactory;
    }

    @Override
    public FnsInventoryDraftsBuilderService fnsInventory() {
        return new FnsInventoryDraftsBuilderServiceImpl(
                acc,
                fnsInventoryDraftsBuildersApiFactory.createDraftsBuildersApi(),
                fnsInventoryDraftsBuildersApiFactory.createDraftsBuildersDocumentApi(),
                fnsInventoryDraftsBuildersApiFactory.createDraftsBuildersDocumentFilesApi()
        );
    }

    @Override
    public PfrReportDraftsBuilderServiceImpl pfrReport() {
        return new PfrReportDraftsBuilderServiceImpl(
                acc,
                pfrReportDraftsBuildersApiFactory.createDraftsBuildersApi(),
                pfrReportDraftsBuildersApiFactory.createDraftsBuildersDocumentApi(),
                pfrReportDraftsBuildersApiFactory.createDraftsBuildersDocumentFilesApi()
        );
    }
}
