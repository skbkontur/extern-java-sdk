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
	@SerializedName("payer")
	private Organization payer;

	public DraftMeta() {
	}	
	
	public DraftMeta(Sender sender, Recipient recipient, Organization payer) {
		this.sender = sender;
		this.recipient = recipient;
		this.payer = payer;
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

	public Organization getPayer() {
		return payer;
	}

	public void setPayer(Organization payer) {
		this.payer = payer;
	}
}
