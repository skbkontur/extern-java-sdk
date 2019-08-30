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

package ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.common;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderTaskInfo;

@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface RetrofitCommonDraftsBuildersApi {

    /**
     * Delete a drafts builder
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @DELETE("v1/{accountId}/drafts/builders/{draftsBuilderId}")
    CompletableFuture<Void> delete(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

    /**
     * Build the drafts builder
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @POST("v1/{accountId}/drafts/builders/{draftsBuilderId}/build?deferred=false")
    CompletableFuture<BuildDraftsBuilderResult> build(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

    /**
     * Starts build drafts builder process and return taskInfo object
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @POST("v1/{accountId}/drafts/builders/{draftsBuilderId}/build?deferred=true")
    @Headers({"CONNECT_TIMEOUT:1200000", "READ_TIMEOUT:1200000", "WRITE_TIMEOUT:1200000",
            "X-Kontur-Request-Timeout:1200000"})
    CompletableFuture<BuildDraftsBuilderTaskInfo> startBuild(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

    /**
     * Get build drafts builder process taskInfo object
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param taskId send task identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}/tasks/{taskId}")
    CompletableFuture<BuildDraftsBuilderTaskInfo> getBuildResult(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Path("taskId") UUID taskId
    );
}
