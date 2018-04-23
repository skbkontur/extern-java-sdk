/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class DocumentContents {

	private String base64Content = null;
	private String signature = null;
	private DocumentDescription documentDescription = null;

	/**
	 * Get base64Content
	 *
	 * @return base64Content
	 *
	 */
	public String getBase64Content() {
		return base64Content;
	}

	public void setBase64Content(String base64Content) {
		this.base64Content = base64Content;
	}

	/**
	 * Get signature
	 *
	 * @return signature
	 *
	 */
	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
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
