/*
 * Copyright (c) 2018 SKB Kontur
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
 *
 */

package ru.kontur.extern_api.sdk.httpclient.api;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;
import ru.kontur.extern_api.sdk.model.*;


@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface DraftsApi {

    /**
     * Create new a draft
     *
     * @param accountId private account identifier
     * @param clientInfo draft metadata
     */
    @POST("v1/{accountId}/drafts")
    CompletableFuture<ApiResponse<Draft>> create(
            @Path("accountId") UUID accountId,
            @Body DraftMetaRequest clientInfo
    );

    /**
     * Delete a draft
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @DELETE("v1/{accountId}/drafts/{draftId}")
    CompletableFuture<ApiResponse<Void>> delete(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * lookup a draft by an identifier
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}")
    CompletableFuture<ApiResponse<Draft>> lookup(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * lookup a draft meta by an identifier
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/meta")
    CompletableFuture<ApiResponse<DraftMeta>> lookupMeta(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * update a draft meta
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param newMeta DraftMetaRequest draft meta data
     */
    @PUT("v1/{accountId}/drafts/{draftId}/meta")
    CompletableFuture<ApiResponse<DraftMeta>> updateMeta(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Body DraftMetaRequest newMeta
    );

    /**
     * Operation CHECK
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @POST("v1/{accountId}/drafts/{draftId}/check")
    CompletableFuture<ApiResponse<DataWrapper<CheckResultData>>> check(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * Operation PREPARE
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @POST("v1/{accountId}/drafts/{draftId}/prepare")
    CompletableFuture<ApiResponse<PrepareResult>> prepare(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * Send the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param deferred if set then server perform async operation
     * @param force force send
     */
    @POST("v1/{accountId}/drafts/{draftId}/send")
    CompletableFuture<ApiResponse<Docflow>> send(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Query("deferred") boolean deferred,
            @Query("force") boolean force
    );

    /**
     * Delete a document from the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @DELETE("v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    CompletableFuture<ApiResponse<Void>> deleteDocument(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * lookup a document from the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    CompletableFuture<ApiResponse<DraftDocument>> lookupDocument(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * Update a draft document Update the document
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     * @param documentContents DocumentContents
     */
    @PUT("v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    CompletableFuture<ApiResponse<DraftDocument>> updateDocument(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId,
            @Body DocumentContents documentContents
    );

    /**
     * print a document from the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/documents/{documentId}/print")
    CompletableFuture<ApiResponse<byte[]>> printDocument(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * Add a new document to the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentContents DocumentContents
     */
    @POST("v1/{accountId}/drafts/{draftId}/documents")
    CompletableFuture<ApiResponse<DraftDocument>> addDecryptedDocument(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Body DocumentContents documentContents
    );

    /**
     * Get a decrypted document content
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content")
    CompletableFuture<ApiResponse<byte[]>> getDecryptedDocumentContent(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * Update a decrypted document content
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     * @param content decrypted document content
     */
    @PUT("v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content")
    CompletableFuture<ApiResponse<Void>> updateDecryptedDocumentContent(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId,
            @Body byte[] content
    );

    /**
     * Get a encrypted document content
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/documents/{documentId}/encrypted-content")
    CompletableFuture<ApiResponse<byte[]>> getEncryptedDocumentContent(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * Get a document signature
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     */
    @GET("v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature")
    CompletableFuture<ApiResponse<byte[]>> getSignatureContent(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId
    );

    /**
     * Update a document signature
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     * @param content byte[], base64 signature content
     */
    @PUT("v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature")
    CompletableFuture<ApiResponse<Void>> updateSignature(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId,
            @Body byte[] content
    );

    /**
     * Initiates the process of cloud signing of the draft
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     */
    @POST("v1/{accountId}/drafts/{draftId}/cloud-sign")
    CompletableFuture<ApiResponse<SignInitiation>> cloudSignDraft(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId
    );

    /**
     * Confirms given sign operation by requestId via sms-code.
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param requestId Sign operation identifier
     * @param code Confirmation code from sms
     */
    @POST("v1/{accountId}/drafts/{draftId}/cloud-sign-confirm")
    CompletableFuture<ApiResponse<SignedDraft>> confirmCloudSigning(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Query("requestId") String requestId,
            @Query("code") String code
    );

    /**
     * Create an USN declaration and replace context in document
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param documentId document identifier
     * @param type Document type
     * @param version Declaration version
     * @param data metadata of an USN document
     */
    @POST("v1/{accountId}/drafts/{draftId}/documents/{documentId}/build")
    CompletableFuture<ApiResponse<Void>> buildDeclaration(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Path("documentId") UUID documentId,
            @Query("type") String type,
            @Query("version") int version,
            @Body UsnServiceContractInfo data
    );

    /**
     * Create an USN declaration and replace context in document
     *
     * @param accountId private account identifier
     * @param draftId draft identifier
     * @param type Document type
     * @param version Declaration version
     * @param data UsnServiceContractInfo meta data of an USN document
     */
    @POST("v1/{accountId}/drafts/{draftId}/build-document")
    CompletableFuture<ApiResponse<DraftDocument>> createAndBuildDeclaration(
            @Path("accountId") UUID accountId,
            @Path("draftId") UUID draftId,
            @Query("type") String type,
            @Query("version") int version,
            @Body UsnServiceContractInfo data
    );

}
