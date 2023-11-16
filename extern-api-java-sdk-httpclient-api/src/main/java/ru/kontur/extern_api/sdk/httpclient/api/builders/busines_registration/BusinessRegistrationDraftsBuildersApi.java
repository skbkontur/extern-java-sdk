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

package ru.kontur.extern_api.sdk.httpclient.api.builders.busines_registration;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.httpclient.api.builders.DraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.business_registration.RetrofitBusinessRegistrationDraftsBuildersApi;
import ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common.RetrofitCommonDraftsBuildersApi;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderTaskInfo;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.business_registration.BusinessRegistrationDraftsBuilderMetaRequest;

public class BusinessRegistrationDraftsBuildersApi implements
        DraftsBuildersApi<
                BusinessRegistrationDraftsBuilder,
                BusinessRegistrationDraftsBuilderMeta,
                BusinessRegistrationDraftsBuilderMetaRequest> {

    private RetrofitBusinessRegistrationDraftsBuildersApi specificContract;
    private RetrofitCommonDraftsBuildersApi commonContract;

    public BusinessRegistrationDraftsBuildersApi(
            RetrofitBusinessRegistrationDraftsBuildersApi specificContract,
            RetrofitCommonDraftsBuildersApi commonContract
    ) {
        this.specificContract = specificContract;
        this.commonContract = commonContract;
    }

    @Override
    public CompletableFuture<BusinessRegistrationDraftsBuilder> create(
            UUID accountId,
            BusinessRegistrationDraftsBuilderMetaRequest meta
    ) {
        return specificContract.create(
                accountId,
                meta
        );
    }

    @Override
    public CompletableFuture<BusinessRegistrationDraftsBuilder> get(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return specificContract.get(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BusinessRegistrationDraftsBuilder> update(
            UUID accountId,
            UUID draftsBuilderId,
            BusinessRegistrationDraftsBuilderMetaRequest meta
    ) {
        return specificContract.update(
                accountId,
                draftsBuilderId,
                meta
        );
    }

    @Override
    public CompletableFuture<Void> delete(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return commonContract.delete(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BusinessRegistrationDraftsBuilderMeta> getMeta(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return specificContract.getMeta(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BusinessRegistrationDraftsBuilderMeta> updateMeta(
            UUID accountId,
            UUID draftsBuilderId,
            BusinessRegistrationDraftsBuilderMetaRequest newMeta
    ) {
        return specificContract.updateMeta(
                accountId,
                draftsBuilderId,
                newMeta
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderResult> build(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return commonContract.build(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderTaskInfo> startBuild(
            UUID accountId,
            UUID draftsBuilderId
    ) {
        return commonContract.startBuild(
                accountId,
                draftsBuilderId
        );
    }

    @Override
    public CompletableFuture<BuildDraftsBuilderTaskInfo> getBuildResult(
            UUID accountId,
            UUID draftsBuilderId,
            UUID taskId
    ) {
        return commonContract.getBuildResult(
                accountId,
                draftsBuilderId,
                taskId
        );
    }
}