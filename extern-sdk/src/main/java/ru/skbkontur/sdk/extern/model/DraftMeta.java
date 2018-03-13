/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author AlexS
 */
public class DraftMeta {
	@SerializedName("sender")
	private Sender sender;
	@SerializedName("recipient")
	private Recipient recipient;
	@SerializedName("organization")
	private Organization organization;

	public DraftMeta() {
	}	
	
	public DraftMeta(Sender sender, Recipient recipient, Organization organization) {
		this.sender = sender;
		this.recipient = recipient;
		this.organization = organization;
	}
	
	public Sender getSender() {
		return sender;
	}

	public void setSender(Sender sender) {
		this.sender = sender;
	}

	public Recipient getRecipient() {
		return recipient;
	}

	public void setRecipient(Recipient recipient) {
		this.recipient = recipient;
	}

	public Organization getOrganization() {
		return organization;
	}

	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
}
