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
import ru.kontur.extern_api.sdk.httpclient.api.builders.submission.SubmissionDraftsBuilderDocumentFilesApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.submission.SubmissionDraftsBuilderDocumentsApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.submission.SubmissionDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentFile;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentFileContents;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentFileMeta;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderDocumentFileMetaRequest;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.submission.SubmissionDraftsBuilderDocumentFileService;
import ru.kontur.extern_api.sdk.service.builders.submission.SubmissionDraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderDocumentFileServiceImpl;

public class SubmissionDraftsBuilderDocumentFileServiceImpl extends
        DraftsBuilderDocumentFileServiceImpl<
                SubmissionDraftsBuilderDocumentFile,
                SubmissionDraftsBuilderDocumentFileContents,
                SubmissionDraftsBuilderDocumentFileMeta,
                SubmissionDraftsBuilderDocumentFileMetaRequest,
                SubmissionDraftsBuilderDocumentService,
                SubmissionDraftsBuilderDocumentFilesApi>
        implements SubmissionDraftsBuilderDocumentFileService {

    private final SubmissionDraftsBuildersApi builderApi;
    private final SubmissionDraftsBuilderDocumentsApi documentApi;

    SubmissionDraftsBuilderDocumentFileServiceImpl(
            AccountProvider accountProvider,
            SubmissionDraftsBuildersApi builderApi,
            SubmissionDraftsBuilderDocumentsApi documentApi,
            SubmissionDraftsBuilderDocumentFilesApi api,
            UUID draftsBuilderId,
            UUID draftsBuilderDocumentId
    ) {
        super(accountProvider, api, draftsBuilderId, draftsBuilderDocumentId);
        this.builderApi = builderApi;
        this.documentApi = documentApi;
    }

    @Override
    public SubmissionDraftsBuilderDocumentService getDocumentService() {
        return new SubmissionDraftsBuilderDocumentServiceImpl(
                acc,
                builderApi,
                documentApi,
                api,
                getDraftsBuilderId()
        );
    }
}
