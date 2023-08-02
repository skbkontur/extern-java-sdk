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

package ru.kontur.extern_api.sdk.service.impl.builders.business_registration;

import java.util.UUID;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration.BusinessRegistrationDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.business_registration.BusinessRegistrationDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.business_registration.BusinessRegistrationDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.business_registration.BusinessRegistrationDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentServiceImpl;

public class BusinessRegistrationDraftsBuilderDocumentServiceImpl extends
        DraftsBuilderDocumentServiceImpl<
                BusinessRegistrationDraftsBuilderDocument,
                BusinessRegistrationDraftsBuilderDocumentMeta,
                BusinessRegistrationDraftsBuilderDocumentMetaRequest,
                BusinessRegistrationDraftsBuilderService,
                BusinessRegistrationDraftsBuilderDocumentFileService,
                BusinessRegistrationDraftsBuilderDocumentsApi>
        implements BusinessRegistrationDraftsBuilderDocumentService {

    private final BusinessRegistrationDraftsBuildersApi builderApi;
    private final BusinessRegistrationDraftsBuilderDocumentFilesApi fileApi;

    BusinessRegistrationDraftsBuilderDocumentServiceImpl(
            AccountProvider accountProvider,
            BusinessRegistrationDraftsBuildersApi builderApi,
            BusinessRegistrationDraftsBuilderDocumentsApi api,
            BusinessRegistrationDraftsBuilderDocumentFilesApi fileApi,
            UUID draftsBuilderId
    ) {
        super(accountProvider, api, draftsBuilderId);
        this.builderApi = builderApi;
        this.fileApi = fileApi;
    }

    @Override
    protected DraftsBuilderType getDraftsBuilderType() {
        return DraftsBuilderType.IndividualBusinessRegistration;
    }

    @Override
    public BusinessRegistrationDraftsBuilderService getBuilderService() {
        return new BusinessRegistrationDraftsBuilderServiceImpl(
                accountProvider,
                builderApi,
                builderDocumentsApi,
                fileApi
        );
    }

    @Override
    public BusinessRegistrationDraftsBuilderDocumentFileService getFileService(
            UUID draftsBuilderDocumentId
    ) {
        return new BusinessRegistrationDraftsBuilderDocumentFileServiceImpl(
                accountProvider,
                builderApi,
                builderDocumentsApi,
                fileApi,
                getDraftsBuilderId(),
                draftsBuilderDocumentId
        );
    }
}