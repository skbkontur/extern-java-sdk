/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.reflect.TypeToken;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.PrintDocumentData;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SendReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.SortOrder;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

/**
 * @author Mikhail Pavlenko
 */

public class DocflowsApi extends RestApi {

    /**
     * Get docflow page
     *
     * @param accountId String private account identifier
     * @param finished Get finished docflows (optional)
     * @param incoming Get incoming docflows (optional)
     * @param skip Get docflows with skip elements (optional)
     * @param take Get take docflows (optional)
     * @param innKpp Get docflows with specified inn-kpp (optional)
     * @param updatedFrom Get docflows with updated from specified date (optional)
     * @param updatedTo Get docflows with updated to specified date (optional)
     * @param createdFrom Get docflows with created from specified date (optional)
     * @param createdTo Get docflows with created to specified date (optional)
     * @param type Get docflows with specified type (optional)
     * @return ApiResponse&lt;DocflowPage&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DocflowPage> getDocflows(
            @PathParam("accountId") String accountId,
            @QueryParam("finished") Boolean finished,
            @QueryParam("incoming") Boolean incoming,
            @QueryParam("skip") Long skip,
            @QueryParam("take") Integer take,
            @QueryParam("innKpp") String innKpp,
            @QueryParam("updatedFrom") Date updatedFrom,
            @QueryParam("updatedTo") Date updatedTo,
            @QueryParam("createdFrom") Date createdFrom,
            @QueryParam("createdTo") Date createdTo,
            @QueryParam("orgId") String organizationId,
            @QueryParam("type") String type,
            @QueryParam("orderBy") SortOrder order
    ) throws ApiException {
        return invoke("getDocflows", null, DocflowPage.class,
                accountId,
                finished, incoming,
                skip, take,
                YAStringUtils.isNullOrEmpty(innKpp) ? null : innKpp,
                updatedFrom, updatedTo,
                createdFrom, createdTo,
                organizationId,
                type,
                Optional.ofNullable(order).map(o -> o.name().toLowerCase()).orElse(null)
        );
    }

    /**
     * Allow API user to get Docflow object
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @return ApiResponse&lt;Docflow&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Docflow> lookupDocflow(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId) throws ApiException {
        return invoke("lookupDocflow", null, new TypeToken<Docflow>() {
        }.getType(), accountId, docflowId);
    }

    /**
     * Allow API user to get all document from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @return ApiResponse&lt;List&lt;Document&gt;&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<List<Document>> getDocuments(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId) throws ApiException {
        return invoke("getDocuments", null, new TypeToken<List<Document>>() {
        }.getType(), accountId, docflowId);
    }

    /**
     * Allow API user to get discrete document from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @return ApiResponse&lt;Document&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Document> lookupDocument(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId) throws ApiException {
        return invoke("lookupDocument", null, new TypeToken<Document>() {
        }.getType(), accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get discrete document description from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @return ApiResponse&lt;DocumentDescription&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<DocflowDocumentDescription> lookupDescription(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId)
            throws ApiException {
        return invoke("lookupDescription", null, new TypeToken<DocflowDocumentDescription>() {
        }.getType(), accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get discrete encrypted document content from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @return ApiResponse&lt;byte[]&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/encrypted-content")
    @GET
    @Consumes("application/json")
    public ApiResponse<byte[]> getEncryptedContent(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId) throws ApiException {
        return invoke("getEncryptedContent", null, new TypeToken<byte[]>() {
        }.getType(), accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get discrete decrypted document content from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @return ApiResponse&lt;byte[]&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/decrypted-content")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<byte[]> getDecryptedContent(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId) throws ApiException {
        return invoke("getDecryptedContent", null, new TypeToken<byte[]>() {
        }.getType(), accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get discrete document signatures from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @return ApiResponse&lt;List&lt;Signature&gt;&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<List<Signature>> getSignatures(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId) throws ApiException {
        return invoke("getSignatures", null, new TypeToken<List<Signature>>() {
        }.getType(), accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get discrete document single signature from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param signatureId Signature identifier (required)
     * @return ApiResponse&lt;Signature&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<Signature> getSignature(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("signatureId") String signatureId) throws ApiException {
        return invoke("getSignature", null, Signature.class, accountId, docflowId, documentId,
                signatureId);
    }

    /**
     * Allow API user to get discrete document signature single content from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param signatureId Signature identifier (required)
     * @return ApiResponse&lt;byte[]&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<byte[]> getSignatureContent(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("signatureId") String signatureId) throws ApiException {
        return invoke("getSignatureContent", null, byte[].class, accountId, docflowId, documentId,
                signatureId);
    }

    /**
     * Allow API user to get document print from docflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param request (required)
     * @return ApiResponse&lt;String&gt;
     * @throws ApiException transport exception response body
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/print")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<String> print(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            PrintDocumentData request) throws ApiException {
        return invoke("print", request, String.class, accountId, docflowId, documentId);
    }

    /**
     * Allow API user to get Reply document from specified workflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param documentType Reply document identifier (required)
     * @param certificateBase64 Certificate content
     * @return ApiResponse&lt;DocumentToSend&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/generate-reply")
    @POST
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<ReplyDocument> generateReplyDocument(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @QueryParam("documentType") String documentType,
            String certificateBase64
    ) throws ApiException {
        return invoke(
                "generateReplyDocument",
                Collections.singletonMap("certificate-base64", certificateBase64),
                ReplyDocument.class,
                accountId,
                docflowId,
                documentId,
                documentType);
    }

    /**
     * Allow API user to get Reply document from specified workflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param replyId Reply document identifier (required)
     * @return ApiResponse&lt;DocumentToSend&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}")
    @GET
    @Consumes("application/json; charset=utf-8")
    public ApiResponse<ReplyDocument> getReplyDocument(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId
    ) throws ApiException {
        return invoke("getReplyDocument", null, ReplyDocument.class, accountId, docflowId,
                documentId, replyId);
    }

    /**
     * Allow API user to put content to Reply document from specified workflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param replyId Reply document identifier (required)
     * @param content (required)
     * @return ApiResponse&lt;DocumentToSend&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/content")
    @PUT
    @Consumes("application/octet-stream")
    public ApiResponse<ReplyDocument> updateReplyDocumentContent(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId,
            byte[] content
    ) throws ApiException {
        return invoke("updateReplyDocumentContent", content, ReplyDocument.class,
                accountId, docflowId, documentId, replyId);
    }

    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/signature")
    @PUT
    @Consumes("application/octet-stream")
    public ApiResponse<ReplyDocument> putReplyDocumentSignature(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId,
            byte[] signature
    ) throws ApiException {
        return invoke("putReplyDocumentSignature", signature, ReplyDocument.class,
                accountId, docflowId, documentId, replyId);
    }

    /**
     * Allow API user to put content to Reply document from specified workflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param replyId Reply document identifier (required)
     * @param data (required)
     * @return ApiResponse&lt;DocumentToSend&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/send")
    @Consumes("application/json; charset=utf-8")
    @POST
    public ApiResponse<Docflow> sendReply(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId,
            SendReplyDocumentRequestData data
    ) throws ApiException {
        return invoke("sendReply", data, Docflow.class, accountId, docflowId, documentId, replyId);
    }

    /**
     * Allow API user to cloud sign Reply document from specified workflow
     *
     * @param accountId Account identifier (required)
     * @param docflowId Docflow object identifier (required)
     * @param documentId Document identifier (required)
     * @param replyId Reply document identifier (required)
     * @return ApiResponse&lt;DocumentToSend&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/cloud-sign")
    @POST
    public ApiResponse<SignInitiation> initCloudSignReplyDocument(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId
    ) throws ApiException {
        return invoke("initCloudSignReplyDocument", null, SignInitiation.class,
                accountId,
                docflowId,
                documentId,
                replyId);
    }

    @Path("/v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/cloud-sign-confirm")
    @POST
    public ApiResponse<SignConfirmResultData> confirmCloudSignReplyDocument(
            @PathParam("accountId") String accountId,
            @PathParam("docflowId") String docflowId,
            @PathParam("documentId") String documentId,
            @PathParam("replyId") String replyId,
            @QueryParam("requestId") String requestId,
            @QueryParam("code") String code
    ) throws ApiException {
        return invoke("confirmCloudSignReplyDocument", null, SignConfirmResultData.class,
                accountId,
                docflowId,
                documentId,
                replyId,
                requestId,
                code);
    }


    @NotNull
    @Override
    public HttpClient getHttpClient() {
        return httpClient;
    }
}
