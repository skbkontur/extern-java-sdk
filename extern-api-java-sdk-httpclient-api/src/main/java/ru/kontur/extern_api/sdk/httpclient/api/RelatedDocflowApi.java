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

package ru.kontur.extern_api.sdk.httpclient.api;

import retrofit2.http.*;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.Raw;
import ru.kontur.extern_api.sdk.model.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface RelatedDocflowApi {

    /**
     * Create new a draft
     *
     * @param accountId private account identifier
     * @param clientInfo related draft metadata
     */
    @POST("v1/{accountId}/drafts")
    CompletableFuture<Draft> create(
            @Path("accountId") UUID accountId,
            @Body DraftMetaRequest clientInfo
    );

    /**
     * TODO:
     */
    @POST("v1/{accountId}/docflows/{relatedDocflowId}/documents/{relatedDocumentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/generate-reply")
    CompletableFuture<ApiResponse<ReplyDocument>> generateReply(
            @Path("accountId") UUID accountId,
            @Path("relatedDocflowId") UUID relatedDocflowId,
            @Path("relatedDocumentId") UUID relatedDocumentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Query("documentType") String documentType,
            @Body CertificateContent certificate
    );

    /**
     * TODO:
     */
    @PUT("/v1/{accountId}/docflows/{relatedDocflowId}/documents/{relatedDocumentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/replies/{replyId}/signature")
    CompletableFuture<ApiResponse<ReplyDocument>> updateReplyDocumentSignature(
            @Path("accountId") UUID accountId,
            @Path("relatedDocflowId") UUID relatedDocflowId,
            @Path("relatedDocumentId") UUID relatedDocumentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Path("replyId") UUID replyId,
            @Body @Raw byte[] signature
    );

    /**
     * TODO:
     */
    @POST("/v1/{accountId}/docflows/{relatedDocflowId}/documents/{relatedDocumentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/replies/{replyId}/send")
    CompletableFuture<ApiResponse<Docflow>> sendReplyAsync(
            @Path("accountId") UUID accountId,
            @Path("relatedDocflowId") UUID relatedDocflowId,
            @Path("relatedDocumentId") UUID relatedDocumentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Path("replyId") UUID replyId,
            @Body SenderIp data
    );

    /**
     * TODO:
     */
    @POST("/v1/{accountId}/docflows/{relatedDocflowId}/documents/{relatedDocumentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/print")
    CompletableFuture<byte[]> print(
            @Path("accountId") UUID accountId,
            @Path("relatedDocflowId") UUID relatedDocflowId,
            @Path("relatedDocumentId") UUID relatedDocumentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Body ByteContent request
    );

    /**
     * Get specified inventory
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId document identifier
     * @param inventoryId inventory identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}")
    CompletableFuture<Inventory> getInventory(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId
    );

    /**
     * Get all inventories
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId docflow document identifier
     * @param skip offset
     * @param take size
     * @param order sort order (sorting field -- date)
     * @param filters filters by {@link DocflowFilter}
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories")
    CompletableFuture<InventoriesPage> getRelatedInventories(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Query("skip") long skip,
            @Query("take") int take,
            @Query("orderBy") SortOrder order,
            @QueryMap Map<String, String> filters
    );

    /**
     * * Get specified inventory decrypted-content
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId document identifier
     * @param inventoryDocumentId document identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/decrypted-content")
    CompletableFuture<byte[]> getInventoryDocumentDecryptedContent(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId
    );

    /**
     * Get specified inventory encrypted-content
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId document identifier
     * @param inventoryDocumentId document identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/encrypted-content")
    CompletableFuture<byte[]> getInventoryDocumentEncryptedContent(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId
    );

    /**
     * Get specified inventory encrypted-content
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId document identifier
     * @param signatureId document identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/signatures/{signatureId}/content")
    CompletableFuture<byte[]> getInventorySignatureContent(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Path("signatureId") UUID signatureId
    );

    /**
     * Get all related docflows
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId docflow document identifier
     * @param skip offset
     * @param take size
     * @param order sort order (sorting field -- date)
     * @param filters filters by {@link DocflowFilter}
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/related")
    CompletableFuture<DocflowPage> getRelatedDocflows(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Query("skip") long skip,
            @Query("take") int take,
            @Query("orderBy") SortOrder order,
            @QueryMap Map<String, String> filters
    );

    /**
     * Get task result
     *
     * @param accountId           private account identifier
     * @param docflowId           docflow identifier
     * @param documentId          document identifier
     * @param inventoryDocumentId document identifier
     * @param taskId              task identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/tasks/{taskId}")
    CompletableFuture<TaskInfo> getTaskInfo(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Path("taskId") UUID taskId
    );
}
