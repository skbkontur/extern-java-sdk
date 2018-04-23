/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import java.util.List;
import java.util.UUID;
import org.joda.time.DateTime;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class Docflow {

	private UUID id = null;
	private String type = null;
	private String status = null;
	private DocflowDescription description = null;
	private List<Document> documents = null;
	private List<Link> links = null;
	private DateTime sendDate = null;
	private DateTime lastChangeDate = null;

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
	 * Get type
	 *
	 * @return type
	 *
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	/**
	 * Get status
	 *
	 * @return status
	 *
	 */
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Get description
	 *
	 * @return description
	 *
	 */
	public DocflowDescription getDescription() {
		return description;
	}

	public void setDescription(DocflowDescription description) {
		this.description = description;
	}

	/**
	 * Get documents
	 *
	 * @return documents
	 *
	 */
	public List<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(List<Document> documents) {
		this.documents = documents;
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

	/**
	 * Get sendDate
	 *
	 * @return sendDate
	 *
	 */
	public DateTime getSendDate() {
		return sendDate;
	}

	public void setSendDate(DateTime sendDate) {
		this.sendDate = sendDate;
	}

	/**
	 * Get lastChangeDate
	 *
	 * @return lastChangeDate
	 *
	 */
	public DateTime getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(DateTime lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
}
