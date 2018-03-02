/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;

/**
 *
 * @author AlexS
 */
public class Reply {

	private DocumentToSend document = null;

	private List<Link> links = null;

	/**
	 * Get document
	 *
	 * @return document
	 *
	 */
	public DocumentToSend getDocument() {
		return document;
	}

	public void setDocument(DocumentToSend document) {
		this.document = document;
	}

	/**
	 * Get links
	 *
	 * @return links
	 *
	 */
	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
