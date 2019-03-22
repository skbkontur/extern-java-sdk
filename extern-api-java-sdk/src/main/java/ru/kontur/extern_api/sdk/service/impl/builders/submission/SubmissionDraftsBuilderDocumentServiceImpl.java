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

package ru.kontur.extern_api.sdk.service.impl.builders.submission;

import java.util.UUID;
import ru.kontur.extern_api.sdk.httpclient.api.builder.submission.SubmissionDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builder.submission.SubmissionDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builder.submission.SubmissionDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.submission.SubmissionDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.submission.SubmissionDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.submission.SubmissionDraftsBuilderService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentServiceImpl;

public class SubmissionDraftsBuilderDocumentServiceImpl extends
        DraftsBuilderDocumentServiceImpl<
                SubmissionDraftsBuilderDocument,
                SubmissionDraftsBuilderDocumentMeta,
                SubmissionDraftsBuilderDocumentMetaRequest,
                SubmissionDraftsBuilderService,
                SubmissionDraftsBuilderDocumentFileService,
                SubmissionDraftsBuilderDocumentsApi>
        implements SubmissionDraftsBuilderDocumentService {

    private final SubmissionDraftsBuildersApi builderApi;
    private final SubmissionDraftsBuilderDocumentFilesApi fileApi;

    SubmissionDraftsBuilderDocumentServiceImpl(
            AccountProvider accountProvider,
            SubmissionDraftsBuildersApi builderApi,
            SubmissionDraftsBuilderDocumentsApi api,
            SubmissionDraftsBuilderDocumentFilesApi fileApi,
            UUID draftsBuilderId
    ) {
        super(accountProvider, api, draftsBuilderId);
        this.builderApi = builderApi;
        this.fileApi = fileApi;
    }

    @Override
    public SubmissionDraftsBuilderService getBuilderService() {
        return new SubmissionDraftsBuilderServiceImpl(
                acc,
                builderApi,
                api,
                fileApi
        );
    }

    @Override
    public SubmissionDraftsBuilderDocumentFileService getFileService(
            UUID draftsBuilderDocumentId
    ) {
        return new SubmissionDraftsBuilderDocumentFileServiceImpl(
                acc,
                builderApi,
                api,
                fileApi,
                getDraftsBuilderId(),
                draftsBuilderDocumentId
        );
    }
}
