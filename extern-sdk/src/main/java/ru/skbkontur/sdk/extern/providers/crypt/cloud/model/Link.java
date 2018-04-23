/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
class Link {
	@SerializedName("href")
	private String href;
	@SerializedName("Rel")
	private String rel;
	@SerializedName("Method")
	private String method;
	@SerializedName("InputModelExample")
	private String inputModelExample;

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getInputModelExample() {
		return inputModelExample;
	}

	public void setInputModelExample(String inputModelExample) {
		this.inputModelExample = inputModelExample;
	}
}
