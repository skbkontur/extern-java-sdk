/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.util.Map;
import java.util.UUID;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.skbkontur.sdk.extern.rest.swagger.api.DocumentsApi;
import ru.skbkontur.sdk.extern.rest.swagger.invoker.ApiException;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentContents;
import ru.skbkontur.sdk.extern.rest.swagger.model.DocumentMeta;
import ru.skbkontur.sdk.extern.rest.swagger.model.Urn;

/**
 *
 * @author AlexS
 */
public class ExternSDKDocument extends ExternSDKBase {

	private final DocumentsApi api;

	public ExternSDKDocument(ExternSDK externSDK) {
		super(externSDK);
		this.api = new DocumentsApi();
		this.configureApiClient(api.getApiClient());
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
	public Map<String, Object> addUncryptedDocument(String draftId, byte[] documentContent, String fileName) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return addDocument(draftId, documentContent, fileName, false);
			}
			catch (ApiException x) {
				errorHandler(x, i, "Черновик", draftId);
			}
		}
		// never too be
		return null;
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
		for (int i = 0; i < 2; i++) {
			try {
				api.documentsDeleteDocument(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
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
	public Map<String, Object> getDocument(String draftId, String documentId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map<String, Object>) api.documentsGetDocumentAsync(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
		// never too be
		return null;
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
	public Map<String, Object> updateDocument(String draftId, String documentId, DocumentContents documentContents) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map<String, Object>) api.documentsPutDocument(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), documentContents, getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
		// never too be
		return null;
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
		for (int i = 0; i < 2; i++) {
			try {
				return (String) api.documentsGetDocumentContent(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
		// never too be
		return null;
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
	 *
	 * Get a decrypted document content
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String, Object> updateDecryptedDocumentContent(String draftId, String documentId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.documentsPutDocumentContent(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
		// never too be
		return null;
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
	public Map<String, Object> getEncryptedDocumentContent(String draftId, String documentId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.documentsGetEncryptedDocumentContent(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Документ", documentId);
			}
		}
		// never too be
		return null;
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
		for (int i = 0; i < 2; i++) {
			try {
				return (String) api.documentsGetSignatureContent(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Подпись к документу", documentId);
			}
		}
		// never too be
		return null;
	}

	/**
	 * PUT /v1/{billingAccountId}/drafts/{draftId}/documents/{documentId}/signature
	 *
	 * Update a document signature
	 *
	 * @param draftId a draft identifier
	 * @param documentId a document identifier
	 * @return DTO
	 * @throws ExternSDKException a business error
	 */
	public Map<String, Object> updateSignature(String draftId, String documentId) throws ExternSDKException {
		for (int i = 0; i < 2; i++) {
			try {
				return (Map) api.documentsPutDocumentSignature(getBillingAccountId(), UUID.fromString(draftId), UUID.fromString(documentId), getAccessToken(), getApiKey());
			}
			catch (ApiException x) {
				errorHandler(x, i, "Подпись к документу", documentId);
			}
		}
		// never too be
		return null;
	}

	private Map<String, Object> addDocument(String draftId, byte[] documentContent, String fileName, boolean encrypted) throws ApiException, ExternSDKException {
		Urn urn = null;
		try {
			DocumentMeta meta = new DocumentMeta();
			meta.setCompressionType(urn); // ?????????
			meta.setContentType("text/plain");
			meta.setEncrypted(encrypted);
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

			return (Map) api.documentsAddDocument(getBillingAccountId(), UUID.fromString(draftId), content, getAccessToken(), getApiKey());
		}
		catch (CryptoException x) {
			throw new ExternSDKException(ExternSDKException.CRYPTO_ERROR, x);
		}
	}
}
