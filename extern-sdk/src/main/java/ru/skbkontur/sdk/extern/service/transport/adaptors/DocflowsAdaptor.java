/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.Docflow;
import ru.skbkontur.sdk.extern.model.Document;
import ru.skbkontur.sdk.extern.model.DocumentDescription;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.model.Reply;
import ru.skbkontur.sdk.extern.model.Signature;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.CONTENT;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.DOCFLOW;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.DOCUMENT;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.DOCUMENTS;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.DOCUMENT_DESCRIPTION;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.REPLIES;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.REPLY;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SIGNATURE;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SIGNATURES;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.DocflowDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.DocumentDescriptionDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.DocumentDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.DocumentToSendDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.ReplyDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.SignatureDto;
import ru.skbkontur.sdk.extern.service.transport.swagger.api.DocflowsApi;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author AlexS
 */
public class DocflowsAdaptor extends BaseAdaptor {

	private final DocflowsApi api;

	public DocflowsAdaptor() {
		this(new DocflowsApi());
	}

	public DocflowsAdaptor(DocflowsApi api) {
		this.api = api;
	}

	@Override
	public ApiClient getApiClient() {
		return (ApiClient) api.getApiClient();
	}

	@Override
	public void setApiClient(ApiClient apiClient) {
		api.setApiClient(apiClient);
	}

	/**
	 * Allow API user to get Docflow object
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}
	 *
	 * @param cxt QueryContext&lt;Docflow&gt;
	 * @return QueryContext&lt;Docflow&gt;
	 */
	public QueryContext<Docflow> lookupDocflow(QueryContext<Docflow> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new DocflowDto().fromDto(
					transport(cxt)
						.docflowsGetDocflowAsync(
							cxt.getAccountProvider().accountId(),
							cxt.getDocflowId()
						)
				),
				DOCFLOW
			).setDocflowId(cxt.getDocflow().getId());
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get all document from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents
	 *
	 * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
	 * @return QueryContext&lt;List&lt;Document&gt;&gt; context
	 */
	public QueryContext<List<Document>> getDocuments(QueryContext<List<Document>> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			DocumentDto documentDto = new DocumentDto();

			return cxt.setResult(
				transport(cxt)
					.docflowsGetDocumentsAsync(
						cxt.getAccountProvider().accountId(),
						cxt.getDocflowId()
					)
					.stream()
					.map(documentDto::fromDto)
					.collect(Collectors.toList()),
				DOCUMENTS
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete document from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}
	 *
	 * @param cxt QueryContext&lt;List&lt;Document&gt;&gt; context
	 * @return QueryContext&lt;List&lt;Document&gt;&gt; context
	 */
	public QueryContext<Document> lookupDocument(QueryContext<Document> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new DocumentDto().fromDto(
					transport(cxt)
						.docflowsGetDocumentAsync(
							cxt.getAccountProvider().accountId(),
							cxt.getDocflowId(),
							cxt.getDocumentId()
						)
				),
				DOCUMENT
			)
				.setDocumentId(cxt.getDocument().getId());
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete document description from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/description
	 *
	 * @param cxt QueryContext&lt;DocumentDescription&gt; context
	 * @return QueryContext&lt;DocumentDescription&gt; context
	 */
	public QueryContext<DocumentDescription> lookupDescription(QueryContext<DocumentDescription> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new DocumentDescriptionDto().fromDto(
					transport(cxt)
						.docflowsGetDocumentDescriptionAsync(
							cxt.getAccountProvider().accountId(),
							cxt.getDocflowId(),
							cxt.getDocumentId()
						)
				),
				DOCUMENT_DESCRIPTION
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete encrypted document content from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/encrypted
	 *
	 * @param cxt QueryContext&lt;byte[]&gt; context
	 * @return QueryContext&lt;byte[]&gt; context
	 */
	public QueryContext<byte[]> getEncryptedContent(QueryContext<byte[]> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				transport(cxt)
					.docflowsGetEncryptedDocumentContentAsync(
						cxt.getAccountProvider().accountId(),
						cxt.getDocflowId(),
						cxt.getDocumentId()
					),
				CONTENT
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete decrypted document content from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/content/decrypted
	 *
	 * @param cxt QueryContext&lt;byte[]&gt; context
	 * @return QueryContext&lt;byte[]&gt; context
	 */
	public QueryContext<byte[]> getDecryptedContent(QueryContext<byte[]> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				transport(cxt)
					.docflowsGetDecryptedDocumentContentAsync(
						cxt.getAccountProvider().accountId(),
						cxt.getDocflowId(),
						cxt.getDocumentId()
					),
				CONTENT
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete document signatures from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures
	 *
	 * @param cxt QueryContext&lt;List&lt;Signature&gt;&gt; context
	 * @return QueryContext&lt;List&lt;Signature&gt;&gt; context
	 */
	public QueryContext<List<Signature>> getSignatures(QueryContext<List<Signature>> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			SignatureDto signatureDto = new SignatureDto();

			return cxt.setResult(
				transport(cxt)
					.docflowsGetDocumentSignaturesAsync(
						cxt.getAccountProvider().accountId(),
						cxt.getDocflowId(),
						cxt.getDocumentId()
					)
					.stream()
					.map(signatureDto::fromDto)
					.collect(Collectors.toList()),
				SIGNATURES
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete document single signature from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}
	 *
	 * @param cxt QueryContext&lt;Signature&gt; context
	 * @return QueryContext&lt;Signature&gt; context
	 */
	public QueryContext<Signature> getSignature(QueryContext<Signature> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new SignatureDto()
					.fromDto(
						transport(cxt)
							.docflowsGetDocumentSignatureAsync(
								cxt.getAccountProvider().accountId(),
								cxt.getDocflowId(),
								cxt.getDocumentId(),
								cxt.getSignatureId()
							)
					),
				SIGNATURE
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get discrete document signature single content from docflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/signatures/{signatureId}/content
	 *
	 * @param cxt QueryContext&lt;byte[]gt; context
	 * @return QueryContext&lt;byte[]&gt; context
	 */
	public QueryContext<byte[]> getSignatureContent(QueryContext<byte[]> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				transport(cxt)
					.docflowsGetDocumentSignatureContentAsync(
						cxt.getAccountProvider().accountId(),
						cxt.getDocflowId(),
						cxt.getDocumentId(),
						cxt.getSignatureId()
					),
				CONTENT
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to get Reply document for specified workflow
	 *
	 * GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}
	 *
	 * @param cxt QueryContext&lt;Signature&gt; context
	 * @return QueryContext&lt;Signature&gt; context
	 */
	public QueryContext<Reply> getDocumentTypeReply(QueryContext<Reply> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new ReplyDto()
					.fromDto(
						transport(cxt)
							.docflowsGetReplyDocumentAsync(
								cxt.getAccountProvider().accountId(),
								cxt.getDocflowId(),
								cxt.getDocumentType(),
								cxt.getDocumentId()
							)
					),
				REPLY
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	/**
	 * Allow API user to send Reply document for specified workflow
	 *
	 * POST /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/reply/{documentType}
	 *
	 * @param cxt QueryContext&lt;Signature&gt; context
	 * @return QueryContext&lt;Signature&gt; context
	 */
	public QueryContext<Docflow> addDocumentTypeReply(QueryContext<Docflow> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			return cxt.setResult(
				new DocflowDto()
					.fromDto(
						transport(cxt)
							.docflowsSendReplyDocumentAsync(
								cxt.getAccountProvider().accountId(),
								cxt.getDocflowId(),
								cxt.getDocumentType(),
								cxt.getDocumentId(),
								new DocumentToSendDto().toDto(cxt.getDocumentToSend())
							)
					),
				DOCFLOW
			).setDocflowId(cxt.getDocflow().getId());
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	public QueryContext<Object> createReplies(QueryContext<Object> cxt) {
		try {
			if (cxt.isFail()) {
				return cxt;
			}

			Docflow docflow = cxt.getDocflow();
			if (docflow.getLinks() != null && !docflow.getLinks().isEmpty()) {
				prepareTransport(cxt);
				List<Reply> replies = new ArrayList<>();
				for (Link l: docflow.getLinks()) {
					if (l.getRel().equals("reply")) {
						replies.add(new ReplyDto().fromDto((Map)submitHttpRequest(l.getHref(),"GET",null, Object.class)));
					}
				}
				return cxt.setResult(replies, REPLIES);
			}
			else {
				return cxt.setResult(Collections.emptyList(), REPLIES);
			}
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	private DocflowsApi transport(QueryContext<?> cxt) {
		prepareTransport(cxt);
		return api;
	}
}
