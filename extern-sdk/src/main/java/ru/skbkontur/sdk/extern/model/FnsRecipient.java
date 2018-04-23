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
public class FnsRecipient implements Recipient {
	private String ifnsCode;

	public FnsRecipient() {
	}
	
	public FnsRecipient(String ifnsCode) {
		this.ifnsCode = ifnsCode;
	}
	
	public String getIfnsCode() {
		return ifnsCode;
	}
	
	public void setIfnsCode(String ifnsCode) {
		this.ifnsCode = ifnsCode;
	}
	
}
