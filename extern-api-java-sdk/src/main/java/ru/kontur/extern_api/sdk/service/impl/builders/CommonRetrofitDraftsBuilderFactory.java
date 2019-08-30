package ru.kontur.extern_api.sdk.service.impl.builders;

import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuildersApi;

public class CommonRetrofitDraftsBuilderFactory {

    private final KonturConfiguredClient client;

    public CommonRetrofitDraftsBuilderFactory(KonturConfiguredClient client) {
        this.client = client;
    }

    public RetrofitCommonDraftsBuildersApi createDraftsBuildersApi() {
        return client.createApi(RetrofitCommonDraftsBuildersApi.class);
    }

    public RetrofitCommonDraftsBuilderDocumentsApi createDraftsBuildersDocumentApi() {
        return client.createApi(RetrofitCommonDraftsBuilderDocumentsApi.class);
    }

    public RetrofitCommonDraftsBuilderDocumentFilesApi createDraftsBuildersDocumentFilesApi() {
        return client.createApi(RetrofitCommonDraftsBuilderDocumentFilesApi.class);
    }
}
