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
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderServiceImpl;


public class PfrReportDraftsBuilderServiceImpl extends
        DraftsBuilderServiceImpl<
                        PfrReportDraftsBuilder,
                        PfrReportDraftsBuilderMeta,
                        PfrReportDraftsBuilderMetaRequest,
                        PfrReportDraftsBuilderDocumentService,
                        PfrReportDraftsBuildersApi>
        implements PfrReportDraftsBuilderService {

    private final PfrReportDraftsBuilderDocumentsApi documentApi;
    private final PfrReportDraftsBuilderDocumentFilesApi fileApi;

    public PfrReportDraftsBuilderServiceImpl(
            AccountProvider accountProvider,
            PfrReportDraftsBuildersApi builderApi,
            PfrReportDraftsBuilderDocumentsApi documentApi,
            PfrReportDraftsBuilderDocumentFilesApi fileApi
    ) {
        super(accountProvider, builderApi);
        this.documentApi = documentApi;
        this.fileApi = fileApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.PfrReport;
    }

    @Override
    public PfrReportDraftsBuilderDocumentService getDocumentService(
            @NotNull UUID draftsBuilderId
    ) {
        return new PfrReportDraftsBuilderDocumentServiceImpl(
                acc,
                api,
                documentApi,
                fileApi,
                draftsBuilderId
        );
    }
}
