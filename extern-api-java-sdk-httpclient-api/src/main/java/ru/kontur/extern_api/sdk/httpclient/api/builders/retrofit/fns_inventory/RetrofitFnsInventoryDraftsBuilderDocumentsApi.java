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

package ru.kontur.extern_api.sdk.httpclient.api.builders.retrofit.fns_inventory;

import retrofit2.http.*;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMeta;
import ru.kontur.extern_api.sdk.model.builders.fns_inventory.FnsInventoryDraftsBuilderDocumentMetaRequest;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface RetrofitFnsInventoryDraftsBuilderDocumentsApi {

    /**
     * Create new a drafts builder document
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param meta drafts builder document metadata
     */
    @POST("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents")
    CompletableFuture<FnsInventoryDraftsBuilderDocument> create(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Body FnsInventoryDraftsBuilderDocumentMetaRequest meta
    );

    /**
     * Get all drafts builder documents inside drafts builder
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents")
    CompletableFuture<FnsInventoryDraftsBuilderDocument[]> getAll(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId
    );

    /**
     * Get a drafts builder document by an identifier
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}")
    CompletableFuture<FnsInventoryDraftsBuilderDocument> get(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Path("draftsBuilderDocumentId") UUID draftsBuilderDocumentId
    );

    /**
     * Update a drafts builder document
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     * @param meta drafts builder document metadata
     */
    @PUT("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}")
    CompletableFuture<FnsInventoryDraftsBuilderDocument> update(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Path("draftsBuilderDocumentId") UUID draftsBuilderDocumentId,
            @Body FnsInventoryDraftsBuilderDocumentMetaRequest meta
    );

    /**
     * Get a drafts builder document meta by an identifier
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     */
    @GET("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/meta")
    CompletableFuture<FnsInventoryDraftsBuilderDocumentMeta> getMeta(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Path("draftsBuilderDocumentId") UUID draftsBuilderDocumentId
    );

    /**
     * Update a drafts builder document meta
     *
     * @param accountId private account identifier
     * @param draftsBuilderId drafts builder identifier
     * @param draftsBuilderDocumentId drafts builder document identifier
     * @param newMeta drafts builder document metadata
     */
    @PUT("v1/{accountId}/drafts/builders/{draftsBuilderId}/documents/{draftsBuilderDocumentId}/meta")
    CompletableFuture<FnsInventoryDraftsBuilderDocumentMeta> updateMeta(
            @Path("accountId") UUID accountId,
            @Path("draftsBuilderId") UUID draftsBuilderId,
            @Path("draftsBuilderDocumentId") UUID draftsBuilderDocumentId,
            @Body FnsInventoryDraftsBuilderDocumentMetaRequest newMeta
    );
}