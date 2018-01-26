/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import com.google.gson.annotations.SerializedName;
import ru.skbkontur.sdk.extern.rest.swagger.model.DraftMeta;

/**
 *
 * @author AlexS
 */
public class TestData {
	@SerializedName("clientInfo")
	private DraftMeta clientInfo;
	@SerializedName("docs")
	private String[] docs;

	public DraftMeta getClientInfo() {
		return clientInfo;
	}

	public void setClientInfo(DraftMeta clientInfo) {
		this.clientInfo = clientInfo;
	}

	public String[] getDocs() {
		return docs;
	}

	public void setDocs(String[] docs) {
		this.docs = docs;
	}
	
	
}
