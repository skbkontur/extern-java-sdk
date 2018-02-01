/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.rest.api;

import java.util.Map;
import ru.skbkontur.sdk.extern.rest.swagger.api.DraftsApi;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiClient;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.Draft;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftDocument;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class DraftsApiWrap extends ApiWrap {
	
	private final DraftsApi api;
	
	public DraftsApiWrap(DraftsApi api) {
		this.api = api;
	}
	
	public ApiClient getApiClient() {
		return api.getApiClient();
	}
	
	/**
	 * Create new a draft
	 *
	 * POST /v1/{billingAccountId}/drafts
	 * 
	 * @param cxt a context
	 * @return Draft
	 * @throws ApiException a restful error
	 */
	public QueryContext<Draft> createDraft(QueryContext<Draft> cxt) throws ApiException {
		return cxt.setResult(jsonToDTO((Map)api.draftsCreate(cxt.getAccountId(), cxt.getClientInfo()),Draft.class));
	}

	/**
	 * lookup a draft by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param cxt a context
	 * @return Draft
	 * @throws ApiException a restful error
	 */
	public QueryContext<Draft> getDraft(QueryContext<Draft> cxt) throws ApiException {
		return cxt.setResult(api.draftsGetDraft(cxt.getAccountId(), cxt.getDraftId()));
	}

	/**
	 * Delete a draft
	 *
	 * DELETE /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param cxt a context
	 * @return QueryContext&lt;Void&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Void> deleteDraft(QueryContext<Void> cxt) throws ApiException {
		api.draftsDeleteDraft(cxt.getAccountId(), cxt.getDraftId());
		return cxt.setResult(null);
	}

	/**
	 * lookup a draft meta by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param cxt a context
	 * @return DraftMeta
	 * @throws ApiException a restful error
	 */
	public QueryContext<DraftMeta> getDraftMeta(QueryContext<DraftMeta> cxt) throws ApiException {
		return cxt.setResult(api.draftsGetMeta(cxt.getAccountId(), cxt.getDraftId()));
	}

	/**
	 * update a draft meta
	 *
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param cxt a context
	 * @return DraftMeta
	 * @throws ApiException a restful error
	 */
	public QueryContext<DraftMeta> updateDraftMeta(QueryContext<DraftMeta> cxt) throws ApiException {
		return cxt.setResult(api.draftsUpdateDraftMeta(cxt.getAccountId(), cxt.getDraftId(), cxt.getClientInfo()));
	}

	/**
	 * Operate CHECK
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/check
	 * 
	 * @param cxt a context
	 * @return Map&lt;String,Object&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Map<String,Object>> check(QueryContext<Map<String,Object>> cxt) throws ApiException {
		return cxt.setResult((Map) api.draftsCheck(cxt.getAccountId(), cxt.getDraftId(), cxt.getDeffered()));
	}
	

	/**
	 * Operate PREPARE
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/prepare
	 * 
	 * @param cxt a context
	 * @return Map&lt;String,Object&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Map<String,Object>> prepare(QueryContext<Map<String,Object>> cxt) throws ApiException {
		return cxt.setResult((Map) api.draftsPrepare(cxt.getAccountId(), cxt.getDraftId(), cxt.getDeffered()));
	}
	
	/**
	 * Send the draft
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/send
	 * 
	 * @param cxt a context
	 * @return Docflow
	 * @throws ApiException a restful error
	 */
	public QueryContext<Docflow> send(QueryContext<Docflow> cxt) throws ApiException {
		return cxt.setResult(api.draftsSend(cxt.getAccountId(), cxt.getDraftId(), true, true));
	}

	/**
	 * POST /v1/{billingAccountId}/drafts/{draftId}/documents
	 *
	 * Add a new document to the draft
	 *
	 * @param cxt a context (draftId,documentContent,fileName)
	 * @return DraftDocument
	 * @throws ApiException a restful error
	 */
	public QueryContext<DraftDocument> addDecryptedDocument(QueryContext<DraftDocument> cxt) throws ApiException {
		return cxt.setResult(jsonToDTO((Map) api.draftDocumentsAddDocument(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentContents()), DraftDocument.class));
	}

	/**
	 * DELETE /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Delete a document from the draft
	 *
	 * @param cxt a context (draftId, documentId)
	 * @return QueryContext&lt;Void&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Void> deleteDocument(QueryContext<Void> cxt) throws ApiException {
		api.draftDocumentsDeleteDocument(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId());
		return cxt.setResult(null);
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Delete a document from the draft
	 *
	 * @param cxt a context (draftId,documentId)
	 * @return DraftDocument
	 * @throws ApiException a restful error
	 */
	public QueryContext<DraftDocument> getDocument(QueryContext<DraftDocument> cxt) throws ApiException {
		return cxt.setResult(api.draftDocumentsGetDocumentAsync(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId()));
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Update a draft document Update the document
	 *
	 * @param cxt a context (required: draftId,documentId,DocumentContents)
	 * @return DraftDocument
	 * @throws ApiException a restful error
	 */
	public QueryContext<DraftDocument> updateDocument(QueryContext<DraftDocument> cxt) throws ApiException {
		return cxt.setResult(api.draftDocumentsPutDocument(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId(), cxt.getDocumentContents()));
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
	 *
	 * Get a decrypted document content
	 *
	 * @param cxt a context (required: draftId,documentId)
	 * @return String
	 * @throws ApiException a restful error
	 */
	public QueryContext<String> getDecryptedDocumentContent(QueryContext<String> cxt) throws ApiException {
		return cxt.setResult(api.draftDocumentsGetDocumentContent(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId()));
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
	 *
	 * Get a decrypted document content
	 *
	 * @param cxt a context (required: draftId,documentId)
	 * @return QueryContext&lt;Void&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Void> updateDecryptedDocumentContent(QueryContext<Void> cxt) throws ApiException {
		api.draftDocumentsPutDocumentContent(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId(), cxt.getContent());
		return cxt.setResult(null);
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
	 *
	 * Get a ecrypted document content
	 *
	 * @param cxt a context (required: draftId,documentId)
	 * @return String
	 * @throws ApiException a restful error
	 */
	public QueryContext<String> getEncryptedDocumentContent(QueryContext<String> cxt) throws ApiException {
		return cxt.setResult(api.draftDocumentsGetEncryptedDocumentContent(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId()));
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/signature
	 *
	 * Get a document signature
	 *
	 * @param cxt a context (required: draftId,documentId)
	 * @return String
	 * @throws ApiException a restful error
	 */
	public QueryContext<String> getSignatureContent(QueryContext<String> cxt) throws ApiException {
		return cxt.setResult(api.draftDocumentsGetSignatureContent(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId()));
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/signature
	 *
	 * Update a document signature
	 *
	 * @param cxt a context (required: draftId,documentId)
	 * @return QueryContext&lt;Void&gt;
	 * @throws ApiException a restful error
	 */
	public QueryContext<Void> updateSignature(QueryContext<Void> cxt) throws ApiException {
		api.draftDocumentsPutDocumentSignature(cxt.getAccountId(), cxt.getDraftId(), cxt.getDocumentId(), cxt.getContent());
		return cxt.setResult(null);
	}
}
