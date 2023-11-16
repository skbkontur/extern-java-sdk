package ru.kontur.extern_api.sdk.service.impl.builders.business_registration;

import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.business_registration.RetrofitBusinessRegistrationDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.business_registration.RetrofitBusinessRegistrationDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.business_registration.RetrofitBusinessRegistrationDraftsBuildersApi;
import ru.kontur.extern_api.sdk.service.impl.builders.CommonRetrofitDraftsBuilderFactory;

public class BusinessRegistrationDraftsBuilderApiFactory {

    private final KonturConfiguredClient client;
    private final CommonRetrofitDraftsBuilderFactory commonRetrofitDraftsBuildersFactory;

    public BusinessRegistrationDraftsBuilderApiFactory(
            KonturConfiguredClient client,
            CommonRetrofitDraftsBuilderFactory commonRetrofitFactory
    ) {
        this.client = client;
        this.commonRetrofitDraftsBuildersFactory = commonRetrofitFactory;
    }

    public BusinessRegistrationDraftsBuildersApi createDraftsBuildersApi() {
        return new BusinessRegistrationDraftsBuildersApi(
                client.createApi(RetrofitBusinessRegistrationDraftsBuildersApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersApi()
        );
    }

    public BusinessRegistrationDraftsBuilderDocumentsApi createDraftsBuildersDocumentApi() {
        return new BusinessRegistrationDraftsBuilderDocumentsApi(
                client.createApi(RetrofitBusinessRegistrationDraftsBuilderDocumentsApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentApi()
        );
    }

    public BusinessRegistrationDraftsBuilderDocumentFilesApi createDraftsBuildersDocumentFilesApi() {
        return new BusinessRegistrationDraftsBuilderDocumentFilesApi(
                client.createApi(RetrofitBusinessRegistrationDraftsBuilderDocumentFilesApi.class),
                commonRetrofitDraftsBuildersFactory.createDraftsBuildersDocumentFilesApi()
        );
    }
}