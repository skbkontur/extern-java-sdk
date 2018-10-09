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

import com.google.gson.reflect.TypeToken;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.DataWrapper;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;

/**
 * @author alexs
 */
public class DraftsApi extends RestApi {

    /**
     * Create new a draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param clientInfo DraftMeta draft meta data
     * @return ApiResponse &lt;UUID&gt; draft identifier
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Draft> createDraft(@PathParam("accountId") String accountId,
            DraftMeta clientInfo) throws ApiException {
        return invoke("createDraft", clientInfo, Draft.class, accountId);
    }

    /**
     * Delete a draft
     * <p>
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}")
    @DELETE
    public ApiResponse<Void> delete(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId) throws ApiException {
        return invoke("delete", null, new TypeToken<Void>() {
        }.getType(), accountId, draftId);
    }

    /**
     * lookup a draft by an identifier
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;Draft&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Draft> lookup(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId) throws ApiException {
        return invoke("lookup", null, Draft.class, accountId, draftId);
    }

    /**
     * lookup a draft meta by an identifier
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;DraftMeta&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/meta")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftMeta> lookupDraftMeta(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId) throws ApiException {
        return invoke("lookupDraftMeta", null, new TypeToken<DraftMeta>() {
        }.getType(), accountId, draftId);
    }

    /**
     * update a draft meta
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param clientInfo DraftMeta draft meta data
     * @return ApiResponse&lt;DraftMeta&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/meta")
    @PUT
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftMeta> updateDraftMeta(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, DraftMeta clientInfo) throws ApiException {
        return invoke("updateDraftMeta", clientInfo, new TypeToken<DraftMeta>() {
        }.getType(), accountId, draftId);
    }

    /**
     * Operate CHECK
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;CheckResultData&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/check")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DataWrapper<CheckResultData>> check(
            @PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId) throws ApiException {
        return invoke("check", null, new TypeToken<DataWrapper<CheckResultData>>() {
        }.getType(), accountId, draftId);
    }

    /**
     * Operate PREPARE
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;PrepareResult&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/prepare")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<PrepareResult> prepare(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId) throws ApiException {
        return invoke("prepare", null, new TypeToken<PrepareResult>() {
        }.getType(), accountId, draftId);
    }

    /**
     * Send the draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param deferred boolean
     * @param force boolean
     * @return ApiResponse&lt;List&lt;Docflow&gt;&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/send")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Docflow> send(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @QueryParam("deferred") boolean deferred,
            @QueryParam("force") boolean force) throws ApiException {
        return invoke("send", null, new TypeToken<Docflow>() {
        }.getType(), accountId, draftId, deferred, force);
    }

    /**
     * Delete a document from the draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    @DELETE
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Void> deleteDocument(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("deleteDocument", null, new TypeToken<Void>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * lookup a document from the draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;DraftDocument&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftDocument> lookupDocument(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("lookupDocument", null, new TypeToken<DraftDocument>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Update a draft document Update the document
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @param documentContents DocumentContents
     * @return ApiResponse&lt;DraftDocument&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}")
    @PUT
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftDocument> updateDocument(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId,
            DocumentContents documentContents) throws ApiException {
        return invoke("updateDocument", documentContents, new TypeToken<DraftDocument>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * print a document from the draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/print")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<String> printDocument(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("printDocument", null, String.class, accountId, draftId, documentId);
    }

    /**
     * Add a new document to the draft
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentContents DocumentContents
     * @return ApiResponse&lt;DraftDocument&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftDocument> addDecryptedDocument(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, DocumentContents documentContents)
            throws ApiException {
        return invoke("addDecryptedDocument", documentContents, new TypeToken<DraftDocument>() {
        }.getType(), accountId, draftId);
    }

    /**
     * Get a decrypted document content
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<String> getDecryptedDocumentContent(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("getDecryptedDocumentContent", null, new TypeToken<String>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Update a decrypted document content
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @param content byte[], base64 decrypted document content
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/decrypted-content")
    @PUT
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Void> updateDecryptedDocumentContent(
            @PathParam("accountId") String accountId, @PathParam("draftId") String draftId,
            @PathParam("documentId") String documentId, byte[] content) throws ApiException {
        return invoke("updateDecryptedDocumentContent", content, new TypeToken<Void>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Get a encrypted document content
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/encrypted-content")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<String> getEncryptedDocumentContent(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("getEncryptedDocumentContent", null, new TypeToken<String>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Get a document signature
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<String> getSignatureContent(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("getSignatureContent", null, new TypeToken<String>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Update a document signature
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @param content byte[], base64 signature content
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature")
    @PUT
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Void> updateSignature(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId,
            byte[] content) throws ApiException {
        return invoke("updateSignature", content, new TypeToken<Void>() {
        }.getType(), accountId, draftId, documentId);
    }

    /**
     * Initiates the process of cloud signing of the draft
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @return ApiResponse&lt;SignInitiation&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/cloud-sign")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<SignInitiation> cloudSignDraft(
            @PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId)
            throws ApiException {
        return invoke(
                "cloudSignDraft", null,
                TypeToken.get(SignInitiation.class).getType(),
                accountId,
                draftId
        );
    }

    /**
     * Confirms given sign operation by requestId via sms-code.
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param requestId Sign operation identifier
     * @param code Confirmation code from sms
     * @return ApiResponse&lt;SignedDraft&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/cloud-sign-confirm")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<SignedDraft> confirmCloudSigning(
            @PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId,
            @QueryParam("requestId") String requestId,
            @QueryParam("code") String code)
            throws ApiException {

        return invoke("confirmCloudSigning", null,
                TypeToken.get(SignedDraft.class).getType(),
                accountId,
                draftId,
                requestId,
                code
        );
    }

    /**
     * Create an USN declaration and replace context in document
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param documentId String document identifier
     * @param type Document type
     * @param version Declaration version
     * @param data metadata of an USN document
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/documents/{documentId}/build")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Void> buildDeclaration(@PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId, @PathParam("documentId") String documentId,
            @QueryParam("type") String type, @QueryParam("version") int version, String data)
            throws ApiException {
        return invoke("buildDeclaration", data, new TypeToken<Void>() {
        }.getType(), accountId, draftId, documentId, type, version);
    }

    /**
     * Create an USN declaration and replace context in document
     * <p>
     *
     * @param accountId String private account identifier
     * @param draftId String draft identifier
     * @param type Document type
     * @param version Declaration version
     * @param data UsnServiceContractInfo meta data of an USN document
     * @return ApiResponse&lt;Void&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/drafts/{draftId}/build-document")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DraftDocument> createAndBuildDeclaration(
            @PathParam("accountId") String accountId,
            @PathParam("draftId") String draftId,
            @QueryParam("type") String type,
            @QueryParam("version") int version,
            String data) throws ApiException {
        return invoke("createAndBuildDeclaration", data, new TypeToken<DraftDocument>() {
        }.getType(), accountId, draftId, type, version);
    }
}
