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

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.kontur.extern_api.sdk.model.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface InventoryApi {

    /**
     * Create new a draft
     *
     * @param accountId private account identifier
     * @param clientInfo draft metadata
     */
    @POST("v1/{accountId}/drafts")
    CompletableFuture<Draft> create(
            @Path("accountId") UUID accountId,
            @Body DraftMetaRequest clientInfo
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
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories")
    CompletableFuture<InventoriesPage> getRelatedInventories(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId
    );

    /**
     * * Get specified inventory decrypted-content
     *
     * @param accountId private account identifier
     * @param docflowId docflow identifier
     * @param documentId document identifier
     * @param inventoryDocumentId document identifier
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/decrypt-content")
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
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/encrypt-content")
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
     */
    @GET("v1/{accountId}/docflows/{docflowId}/documents/{documentId}/inventories/{inventoryId}/documents/{inventoryDocumentId}/signatures")
    CompletableFuture<List<Signature>> getInventorySignatures(
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
    CompletableFuture<Signature> getInventorySignature(
            @Path("accountId") UUID accountId,
            @Path("docflowId") UUID docflowId,
            @Path("documentId") UUID documentId,
            @Path("inventoryId") UUID inventoryId,
            @Path("inventoryDocumentId") UUID inventoryDocumentId,
            @Path("signatureId") UUID signatureId
    );
}
