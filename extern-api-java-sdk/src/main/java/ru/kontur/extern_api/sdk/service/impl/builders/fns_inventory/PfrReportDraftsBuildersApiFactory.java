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

import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.pfr_report.PfrReportDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.pfr_report.RetrofitPfrReportDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.pfr_report.RetrofitPfrReportDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.pfr_report.RetrofitPfrReportDraftsBuildersApi;

public class PfrReportDraftsBuildersApiFactory {

    private final KonturConfiguredClient client;
    private final CommonRetrofitDraftsBuildersFactory commonRetrofitDraftsBuildersFactory;

    public PfrReportDraftsBuildersApiFactory(
            KonturConfiguredClient client,
            CommonRetrofitDraftsBuildersFactory commonRetrofitDraftsBuildersFactory
    ) {
        this.client = client;
        this.commonRetrofitDraftsBuildersFactory = commonRetrofitDraftsBuildersFactory;
    }

    public PfrReportDraftsBuildersApi createDraftsBuildersApi() {
        return new PfrReportDraftsBuildersApi(
                client.createApi(RetrofitPfrReportDraftsBuildersApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersApi()
        );
    }

    public PfrReportDraftsBuilderDocumentsApi createDraftsBuildersDocumentApi() {
        return new PfrReportDraftsBuilderDocumentsApi(
                client.createApi(RetrofitPfrReportDraftsBuilderDocumentsApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentApi()
        );
    }

    public PfrReportDraftsBuilderDocumentFilesApi createDraftsBuildersDocumentFilesApi() {
        return new PfrReportDraftsBuilderDocumentFilesApi(
                client.createApi(RetrofitPfrReportDraftsBuilderDocumentFilesApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentFilesApi()
        );
    }
}
