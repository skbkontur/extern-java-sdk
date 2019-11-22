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

package ru.kontur.extern_api.sdk.httpclient.api.builders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderTaskInfo;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderMetaRequest;

public interface DraftsBuildersApi<
        TDraftsBuilder extends DraftsBuilder,
        TDraftsBuilderMeta extends DraftsBuilderMeta,
        TDraftsBuilderMetaRequest extends DraftsBuilderMetaRequest> {

    CompletableFuture<TDraftsBuilder> create(
            UUID accountId,
            TDraftsBuilderMetaRequest meta
    );

    CompletableFuture<TDraftsBuilder> get(
            UUID accountId,
            UUID draftsBuilderId
    );

    CompletableFuture<TDraftsBuilder> update(
            UUID accountId,
            UUID draftsBuilderId,
            TDraftsBuilderMetaRequest meta
    );

    CompletableFuture<Void> delete(
            UUID accountId,
            UUID draftsBuilderId
    );

    CompletableFuture<TDraftsBuilderMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId
    );

    CompletableFuture<TDraftsBuilderMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            TDraftsBuilderMetaRequest newMeta
    );

    CompletableFuture<BuildDraftsBuilderResult> build(
            UUID accountId,
            UUID draftsBuilderId
    );

    CompletableFuture<BuildDraftsBuilderTaskInfo> startBuild(
            UUID accountId,
            UUID draftsBuilderId
    );

    CompletableFuture<BuildDraftsBuilderTaskInfo> getBuildResult(
            UUID accountId,
            UUID draftsBuilderId,
            UUID taskId
    );
}
