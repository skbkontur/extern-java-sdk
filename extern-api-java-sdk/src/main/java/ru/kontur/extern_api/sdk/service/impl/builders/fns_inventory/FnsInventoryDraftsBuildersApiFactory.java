package ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory;

import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory.FnsInventoryDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory.RetrofitFnsInventoryDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory.RetrofitFnsInventoryDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory.RetrofitFnsInventoryDraftsBuildersApi;

public class FnsInventoryDraftsBuildersApiFactory {

    private final KonturConfiguredClient client;
    private final CommonRetrofitDraftsBuildersFactory commonRetrofitDraftsBuildersFactory;

    public FnsInventoryDraftsBuildersApiFactory(
            KonturConfiguredClient client,
            CommonRetrofitDraftsBuildersFactory commonRetrofitFactory
    ) {
        this.client = client;
        this.commonRetrofitDraftsBuildersFactory = commonRetrofitFactory;
    }

    public FnsInventoryDraftsBuildersApi createDraftsBuildersApi() {
        return new FnsInventoryDraftsBuildersApi(
                client.createApi(RetrofitFnsInventoryDraftsBuildersApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersApi()
        );
    }

    public FnsInventoryDraftsBuilderDocumentsApi createDraftsBuildersDocumentApi() {
        return new FnsInventoryDraftsBuilderDocumentsApi(
                client.createApi(RetrofitFnsInventoryDraftsBuilderDocumentsApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentApi()
        );
    }

    public FnsInventoryDraftsBuilderDocumentFilesApi createDraftsBuildersDocumentFilesApi() {
        return new FnsInventoryDraftsBuilderDocumentFilesApi(
                client.createApi(RetrofitFnsInventoryDraftsBuilderDocumentFilesApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentFilesApi()
        );
    }
}

