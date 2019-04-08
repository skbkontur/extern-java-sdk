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

package ru.kontur.extern_api.sdk.service.impl.builders;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.TaskState;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderTaskInfo;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderMetaRequest;
import ru.kontur.extern_api.sdk.model.builders.DraftsBuilderType;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderDocumentService;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.Awaiter;

public abstract class DraftsBuilderServiceImpl<
        TDraftsBuilder extends DraftsBuilder,
        TDraftsBuilderMeta extends DraftsBuilderMeta,
        TDraftsBuilderMetaRequest extends DraftsBuilderMetaRequest,
        TDraftsBuilderDocumentService extends DraftsBuilderDocumentService,
        TDraftsBuildersApi extends DraftsBuildersApi<
                TDraftsBuilder,
                TDraftsBuilderMeta,
                TDraftsBuilderMetaRequest>>
        implements
        DraftsBuilderService<
                TDraftsBuilder,
                TDraftsBuilderMeta,
                TDraftsBuilderMetaRequest,
                TDraftsBuilderDocumentService> {

    protected final AccountProvider acc;
    protected final TDraftsBuildersApi api;
    private final static int DELAY_TIMEOUT = 2000;

    protected DraftsBuilderServiceImpl(
            AccountProvider accountProvider,
            TDraftsBuildersApi api
    ) {
        this.acc = accountProvider;
        this.api = api;
    }

    protected abstract DraftsBuilderType getDraftsBuilderType();

    @Override
    public CompletableFuture<TDraftsBuilder> createAsync(
            TDraftsBuilderMetaRequest meta
    ) {
        return api.create(
                acc.accountId(),
                meta
        ).whenComplete((builder, throwable) -> {
            if (builder != null) {
                CheckBuilderType(builder.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilder> getAsync(
            UUID draftsBuilderId
    ) {
        return api.get(
                acc.accountId(),
                draftsBuilderId
        ).whenComplete((builder, throwable) -> {
            if (builder != null) {
                CheckBuilderType(builder.getMeta().getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<Void> deleteAsync(
            UUID draftsBuilderId
    ) {
        return api.delete(
                acc.accountId(),
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<TDraftsBuilderMeta> getMetaAsync(
            UUID draftsBuilderId
    ) {
        return api.getMeta(
                acc.accountId(),
                draftsBuilderId
        ).whenComplete((meta, throwable) -> {
            if (meta != null) {
                CheckBuilderType(meta.getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<TDraftsBuilderMeta> updateMetaAsync(
            UUID draftsBuilderId,
            TDraftsBuilderMetaRequest newMeta
    ) {
        return api.updateMeta(
                acc.accountId(),
                draftsBuilderId,
                newMeta
        ).whenComplete((meta, throwable) -> {
            if (meta != null) {
                CheckBuilderType(meta.getBuilderType());
            }
        });
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderResult> buildAsync(
            UUID draftsBuilderId
    ) {
        return api.build(
                acc.accountId(),
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderTaskInfo> startBuildAsync(
            UUID draftsBuilderId
    ) {
        return api.startBuild(
                acc.accountId(),
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderTaskInfo> getBuildInfoAsync(
            UUID draftsBuilderId,
            UUID taskId
    ) {
        return api.getBuildResult(
                acc.accountId(),
                draftsBuilderId,
                taskId
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderResult> waitBuildResultAsync(
            UUID draftsBuilderId,
            UUID taskId
    ) {
        return Awaiter.waitForCondition(
                () -> api.getBuildResult(
                        acc.accountId(),
                        draftsBuilderId,
                        taskId
                ),
                (callResult) -> callResult.getTaskState() != TaskState.RUNNING,
                DELAY_TIMEOUT
        )
                .thenApply(result -> {
                    if (result.isFailed()) {
                        throw result.asApiException();
                    }
                    return result.getTaskResult();
                });
    }

    private void CheckBuilderType(DraftsBuilderType type) {
        DraftsBuilderType expectedType = getDraftsBuilderType();
        if (type != expectedType) {
            throw new ApiException(String.format(
                    "Incorrect drafts builder type: %s, expected: %s",
                    type,
                    expectedType
            ));
        }
    }
}
