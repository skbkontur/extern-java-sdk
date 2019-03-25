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

package ru.kontur.extern_api.sdk.httpclient.api.builders.submission;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.submission.SubmissionDraftsBuilderMetaRequest;

public class SubmissionDraftsBuildersApi implements
        DraftsBuildersApi<
                SubmissionDraftsBuilder,
                SubmissionDraftsBuilderMeta,
                SubmissionDraftsBuilderMetaRequest> {

    private RetrofitSubmissionDraftsBuildersApi api;

    public SubmissionDraftsBuildersApi(
            RetrofitSubmissionDraftsBuildersApi api
    ) {
        this.api = api;
    }

    @Override
    public CompletableFuture<SubmissionDraftsBuilder> create(
            UUID accountId,
            SubmissionDraftsBuilderMetaRequest meta
    ) {
        return api.create(
                accountId,
                meta
        );
    }

    @Override
    public CompletableFuture<SubmissionDraftsBuilder> get(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return api.get(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture delete(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return api.delete(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<SubmissionDraftsBuilderMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return api.getMeta(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<SubmissionDraftsBuilderMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            SubmissionDraftsBuilderMetaRequest newMeta
    ) {
        return api.updateMeta(
                accountId,
                draftsBuilderId,
                newMeta
        );
    }
}
