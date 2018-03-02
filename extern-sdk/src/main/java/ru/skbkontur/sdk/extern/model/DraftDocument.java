/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class DraftDocument {

	private UUID id = null;
	private Link decryptedContentLink = null;
	private Link encryptedContentLink = null;
	private Link signatureContentLink = null;
	private DocumentDescription documentDescription = null;

	/**
	 * Get id
	 *
	 * @return id
	 *
	 */
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	/**
	 * Get decryptedContentLink
	 *
	 * @return decryptedContentLink
	 *
	 */
	public Link getDecryptedContentLink() {
		return decryptedContentLink;
	}

	public void setDecryptedContentLink(Link decryptedContentLink) {
		this.decryptedContentLink = decryptedContentLink;
	}

	/**
	 * Get encryptedContentLink
	 *
	 * @return encryptedContentLink
	 *
	 */
	public Link getEncryptedContentLink() {
		return encryptedContentLink;
	}

	public void setEncryptedContentLink(Link encryptedContentLink) {
		this.encryptedContentLink = encryptedContentLink;
	}

	/**
	 * Get signatureContentLink
	 *
	 * @return signatureContentLink
	 *
	 */
	public Link getSignatureContentLink() {
		return signatureContentLink;
	}

	public void setSignatureContentLink(Link signatureContentLink) {
		this.signatureContentLink = signatureContentLink;
	}

	/**
	 * Get meta
	 *
	 * @return meta
	 *
	 */
	public DocumentDescription getDocumentDescription() {
		return documentDescription;
	}

	public void setDocumentDescription(DocumentDescription documentDescription) {
		this.documentDescription = documentDescription;
	}
}
