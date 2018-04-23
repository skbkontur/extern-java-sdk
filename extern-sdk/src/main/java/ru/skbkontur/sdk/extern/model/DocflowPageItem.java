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
 * @author alexs
 */
@SuppressWarnings("unused")
public class DocflowPageItem {
  private UUID id = null;
  private String type = null;
  private String status = null;
  private List<Link> links = null;
  private DateTime sendDate = null;
  private DateTime lastChangeDate = null;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}

	public DateTime getSendDate() {
		return sendDate;
	}

	public void setSendDate(DateTime sendDate) {
		this.sendDate = sendDate;
	}

	public DateTime getLastChangeDate() {
		return lastChangeDate;
	}

	public void setLastChangeDate(DateTime lastChangeDate) {
		this.lastChangeDate = lastChangeDate;
	}
}
