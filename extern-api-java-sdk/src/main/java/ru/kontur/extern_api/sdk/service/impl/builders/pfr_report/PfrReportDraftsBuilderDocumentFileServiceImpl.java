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
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentFileServiceImpl;

public class PfrReportDraftsBuilderDocumentFileServiceImpl extends
        DraftsBuilderDocumentFileServiceImpl<
                        PfrReportDraftsBuilderDocumentFile,
                        PfrReportDraftsBuilderDocumentFileContents,
                        PfrReportDraftsBuilderDocumentFileMeta,
                        PfrReportDraftsBuilderDocumentFileMetaRequest,
                        PfrReportDraftsBuilderDocumentService,
                        PfrReportDraftsBuilderDocumentFilesApi>
        implements PfrReportDraftsBuilderDocumentFileService {

    private final PfrReportDraftsBuildersApi buildersApi;
    private final PfrReportDraftsBuildersDocumentsApi builderDocumentsApi;

    PfrReportDraftsBuilderDocumentFileServiceImpl(
            AccountProvider accountProvider,
            PfrReportDraftsBuildersApi buildersApi,
            PfrReportDraftsBuildersDocumentsApi builderDocumentsApi,
            PfrReportDraftsBuilderDocumentFilesApi api,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        super(accountProvider, api, draftsBuilderId, draftsBuilderDocumentId);
        this.buildersApi = buildersApi;
        this.builderDocumentsApi = builderDocumentsApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.PfrReport;
    }

    @Override
    public PfrReportDraftsBuilderDocumentService getDocumentService() {
        return new PfrReportDraftsBuilderDocumentServiceImpl(
                accountProvider,
                buildersApi,
                builderDocumentsApi,
                builderDocumentFilesApi,
                getDraftsBuilderId()
        );
    }
}