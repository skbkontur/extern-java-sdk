/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.util.Map;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.skbkontur.sdk.extern.rest.api.DraftsApiWrap;
import ru.skbkontur.sdk.extern.rest.swagger.api.DraftsApi;
import ru.skbkontur.sdk.extern.rest.swagger.model.Docflow;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentMeta;
import ru.skbkontur.sdk.extern.rest.swagger.model.Draft;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftDocument;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;
import ru.skbkontur.sdk.extern.rest.swagger.model.PrepareResult;
import ru.skbkontur.sdk.extern.rest.swagger.model.Urn;

/**
 *
 * @author AlexS
 */
public class ExternSDKDraft extends ExternSDKBase {
	private static final String EN_DFT = "Черновик";
	private static final String EN_DOC = "Документ";
	private static final String EN_SIGN = "Подпись";
	
	private final DraftsApiWrap api;

	public ExternSDKDraft(ExternSDK externSDK) {
		super(externSDK);
		this.api = new DraftsApiWrap(new DraftsApi());
		this.setApiClient(api.getApiClient());
		this.configureApiClient();
	}

	/**
	 * Create new a draft
	 *
	 * POST /v1/{billingAccountId}/drafts
	 *
	 * @param clientInfo DraftMeta
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Draft createDraft(DraftMeta clientInfo) throws ExternSDKException {
		return invokeApply(api::createDraft,newCxt(EN_DFT,Draft.class).setClientInfo(clientInfo)).get();
	}

	/**
	 * lookup a draft by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Draft getDraft(String draftId) throws ExternSDKException {
		return invokeApply(api::getDraft,newCxt(EN_DFT, Draft.class).setDraftId(draftId).setEntityId(draftId)).get();
	}

	/**
	 * Delete a draft
	 *
	 * DELETE /v1/{billingAccountId}/drafts/{draftId}
	 *
	 * @param draftId String a draft identifier
	 * @throws ExternSDKException a business error
	 */
	public void deleteDraft(String draftId) throws ExternSDKException {
		invokeApply(api::deleteDraft,newCxt(EN_DFT,Void.class).setDraftId(draftId).setEntityId(draftId));
	}

	/**
	 * lookup a draft meta by an identifier
	 *
	 * GET /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftMeta getDraftMeta(String draftId) throws ExternSDKException {
		return invokeApply(api::getDraftMeta,newCxt(EN_DFT, DraftMeta.class).setDraftId(draftId).setEntityId(draftId)).get();
	}

	/**
	 * update a draft meta
	 *
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/meta
	 *
	 * @param clientInfo DraftMeta
	 * @param draftId String a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftMeta updateDraftMeta(String draftId, DraftMeta clientInfo) throws ExternSDKException {
		return invokeApply(api::updateDraftMeta,newCxt(EN_DFT,DraftMeta.class).setDraftId(draftId).setClientInfo(clientInfo).setEntityId(draftId)).get();
	}

	/**
	 * Operate CHECK
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/check
	 * 
	 * @param draftId String draft identifier
	 * @param deffered boolean
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String,Object> check(String draftId, boolean deffered) throws ExternSDKException {
		return invokeApply(api::check,newCxtForMap(EN_DFT).setDraftId(draftId).setDeffered(deffered).setEntityId(draftId)).get();
	}
	
	/**
	 * Operate PREPARE
	 * 
	 * POST /v1/{billingAccountId}/drafts/{draftId}/prepare
	 * 
	 * @param draftId String draft identifier
	 * @param deffered boolean
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public PrepareResult prepare(String draftId, boolean deffered) throws ExternSDKException {
		return invokeApply(api::prepare,newCxt(EN_DFT,PrepareResult.class).setDraftId(draftId).setDeffered(deffered).setEntityId(draftId)).get();
	}
	
	/**
	 * Send the draft
	 *
	 * POST /v1/{billingAccountId}/drafts/drafts/{draftId}/send
	 * 
	 * @param draftId a draft identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Docflow send(String draftId) throws ExternSDKException {
		return invokeApply(api::send,newCxt(EN_DFT,Docflow.class).setDraftId(draftId).setEntityId(draftId)).get();
	}

	/**
	 * POST /v1/{billingAccountId}/drafts/{draftId}/documents
	 *
	 * Add a new document to the draft
	 *
	 * @param draftId String a draft identifier
	 * @param documentContent byte[] a document content
	 * @param fileName String a file name
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftDocument addDecryptedDocument(String draftId, byte[] documentContent, String fileName) throws ExternSDKException {
		DocumentContents contents = createDocumentContents(documentContent, fileName);
		
		return invokeApply(api::addDecryptedDocument,newCxt(EN_DFT,DraftDocument.class).setDraftId(draftId).setDocumentContents(contents).setEntityId(draftId)).get();
	}

	/**
	 * DELETE /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Delete a document from the draft
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @throws ExternSDKException a business error
	 */
	public void deleteDocument(String draftId, String documentId) throws ExternSDKException {
		invokeApply(api::deleteDocument,newCxt(EN_DOC, Void.class).setDraftId(draftId).setDocumentId(documentId).setEntityId(documentId));
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Delete a document from the draft
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftDocument getDocument(String draftId, String documentId) throws ExternSDKException {
		return invokeApply(api::getDocument,newCxt(EN_DOC, DraftDocument.class).setDraftId(draftId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}
	 *
	 * Update a draft document Update the document
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @param documentContents DocumentContents a new document content
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public DraftDocument updateDocument(String draftId, String documentId, DocumentContents documentContents) throws ExternSDKException {
		return invokeApply(api::updateDocument,newCxt(EN_DOC, DraftDocument.class).setDraftId(draftId).setDocumentId(documentId).setDocumentContents(documentContents).setEntityId(documentId)).get();
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
	 *
	 * Get a decrypted document content
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return a document content in base64
	 * @throws ExternSDKException a business error
	 */
	public String getDecryptedDocumentContent(String draftId, String documentId) throws ExternSDKException {
		return invokeApply(api::getDecryptedDocumentContent,newCxt(EN_DOC, String.class).setDraftId(draftId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
	 *
	 * Get a decrypted document content
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @param content byte[] a new document content
	 * @throws ExternSDKException a business error
	 */
	public void updateDecryptedDocumentContent(String draftId, String documentId, byte[] content) throws ExternSDKException {
		invokeApply(api::updateDecryptedDocumentContent,newCxt(EN_DOC, Void.class).setDraftId(draftId).setDocumentId(documentId).setContent(content).setEntityId(documentId));
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
	 *
	 * Get a ecrypted document content
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public String getEncryptedDocumentContent(String draftId, String documentId) throws ExternSDKException {
		return invokeApply(api::getEncryptedDocumentContent,newCxt(EN_DOC, String.class).setDraftId(draftId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	/**
	 * GET /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/signature
	 *
	 * Get a document signature
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return a signature content in base64
	 * @throws ExternSDKException a business error
	 */
	public String getSignatureContent(String draftId, String documentId) throws ExternSDKException {
		return invokeApply(api::getSignatureContent,newCxt(EN_SIGN, String.class).setDraftId(draftId).setDocumentId(documentId).setEntityId(documentId)).get();
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/signature
	 *
	 * Update a document signature
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @param content a new signatute cintent
	 * @throws ExternSDKException a business error
	 */
	public void updateSignature(String draftId, String documentId, byte[] content) throws ExternSDKException {
		invokeApply(api::updateSignature,newCxt(EN_DOC, Void.class).setDraftId(draftId).setDocumentId(documentId).setContent(content).setEntityId(documentId));
	}

	public DocumentContents createDocumentContents(byte[] documentContent, String fileName) throws ExternSDKException {
		Urn urn = null;
		try {
			DocumentMeta meta = new DocumentMeta();
			meta.setCompressionType(urn); // ?????????
			meta.setContentType("text/plain");
			meta.setFilename(fileName);
			meta.setType(urn); // ?????????

			String signature = null;
			if (getCryptoService() != null && getSignKey() != null) {
				PKCS7 p7prov = new PKCS7(getCryptoService());
				byte[] p7s = p7prov.sign(getSignKey(), null, documentContent, false);
				signature = ENCODER_BASE64.encodeToString(p7s);
			}

			DocumentContents content = new DocumentContents();
			content.setBase64Content(ENCODER_BASE64.encodeToString(documentContent));
			content.setSignature(signature);
			content.setMeta(meta);

			return content;
		}
		catch (CryptoException x) {
			throw new ExternSDKException(ExternSDKException.C_CRYPTO_ERROR, x);
		}
	}
}
