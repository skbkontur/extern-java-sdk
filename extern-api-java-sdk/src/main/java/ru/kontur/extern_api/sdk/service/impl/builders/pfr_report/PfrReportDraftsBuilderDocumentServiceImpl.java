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

package ru.kontur.extern_api.sdk.service.impl.builders.pfr_report;

import java.util.UUID;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuildersDocumentsApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentServiceImpl;

public class PfrReportDraftsBuilderDocumentServiceImpl extends
        DraftsBuilderDocumentServiceImpl<
                        PfrReportDraftsBuilderDocument,
                        PfrReportDraftsBuilderDocumentMeta,
                        PfrReportDraftsBuilderDocumentMetaRequest,
                        PfrReportDraftsBuilderService,
                        PfrReportDraftsBuilderDocumentFileService,
                PfrReportDraftsBuildersDocumentsApi>
        implements PfrReportDraftsBuilderDocumentService {

    private final PfrReportDraftsBuildersApi buildersApi;
    private final PfrReportDraftsBuilderDocumentFilesApi builderDocumentFilesApi;

    PfrReportDraftsBuilderDocumentServiceImpl(
            AccountProvider accountProvider,
            PfrReportDraftsBuildersApi builderApi,
            PfrReportDraftsBuildersDocumentsApi api,
            PfrReportDraftsBuilderDocumentFilesApi builderDocumentFilesApi,
            UUID draftsBuilderId
    ) {
        super(accountProvider, api, draftsBuilderId);
        this.buildersApi = builderApi;
        this.builderDocumentFilesApi = builderDocumentFilesApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.PfrReport;
    }

    @Override
    public PfrReportDraftsBuilderService getBuilderService() {
        return new PfrReportDraftsBuilderServiceImpl(
                accountProvider,
                buildersApi,
                builderDocumentsApi,
                builderDocumentFilesApi
        );
    }

    @Override
    public PfrReportDraftsBuilderDocumentFileService getFileService(
            UUID draftsBuilderDocumentId
    ) {
        return new PfrReportDraftsBuilderDocumentFileServiceImpl(
                accountProvider,
                buildersApi,
                builderDocumentsApi,
                builderDocumentFilesApi,
                getDraftsBuilderId(),
                draftsBuilderDocumentId
        );
    }
}