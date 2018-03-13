/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class DocumentToSend {

	@SerializedName("id")
	private UUID id = null;
	@SerializedName("content")
	private byte[] content = null;
	@SerializedName("filename")
	private String filename = null;
	@SerializedName("signature")
	private SignatureToSend signature = null;
	@SerializedName("sender-ip")
	private String senderIp = null;
  @SerializedName("links")
  private List<Link> links = null;


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

	public void setId(String id) {
		this.id = UUID.fromString(id);
	}

	/**
	 * Get content
	 *
	 * @return content
  *
	 */
	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}

	/**
	 * Get filename
	 *
	 * @return filename
  *
	 */
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/**
	 * Get signature
	 *
	 * @return signature
  *
	 */
	public SignatureToSend getSignature() {
		return signature;
	}

	public void setSignature(SignatureToSend signature) {
		this.signature = signature;
	}

	/**
	 * Get senderIp
	 *
	 * @return senderIp
  *
	 */
	public String getSenderIp() {
		return senderIp;
	}

	public void setSenderIp(String senderIp) {
		this.senderIp = senderIp;
	}

	public List<Link> getLinks() {
		return links;
	}

	public void setLinks(List<Link> links) {
		this.links = links;
	}
}
