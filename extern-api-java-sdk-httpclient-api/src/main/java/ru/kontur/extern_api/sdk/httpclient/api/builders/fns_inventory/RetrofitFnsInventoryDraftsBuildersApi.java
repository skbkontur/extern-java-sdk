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

package ru.kontur.extern_api.sdk.httpclient.api.builders.fns_inventory;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderTaskInfo;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderMetaRequest;

@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface RetrofitFnsInventoryDraftsBuildersApi {

    /**
     * Create new a drafts builder
     *
     * @param accountId private account identifier
     * @param meta drafts builder metadata
     */
    @POST("v1/{accountId}/drafts/builders")
    CompletableFuture<FnsInventoryDraftsBuilder> create(
            @Path("accountId") UUID accountId,
            @Body FnsInventoryDraftsBuilderMetaRequest meta
    );

    /**
     * Get a drafts builder by an identifier
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}")
    CompletableFuture<FnsInventoryDraftsBuilder> get(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

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
     * Get a drafts builder meta by an identifier
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}/meta")
    CompletableFuture<FnsInventoryDraftsBuilderMeta> getMeta(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

    /**
     * Update a drafts builder meta
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param newMeta drafts builder metadata
     */
    @PUT("v1/{accountId}/drafts/builders/{draftsBuilderId}/meta")
    CompletableFuture<FnsInventoryDraftsBuilderMeta> updateMeta(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Body FnsInventoryDraftsBuilderMetaRequest newMeta
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
