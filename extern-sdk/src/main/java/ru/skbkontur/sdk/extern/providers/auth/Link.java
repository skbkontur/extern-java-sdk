/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class Link {

	@SerializedName("Rel")
	private String rel;
	@SerializedName("Href")
	private String href;

	public Link() {

	}

	public Link(String rel, String href) {
		this.rel = rel;
		this.href = href;
	}

	public String getRel() {
		return rel;
	}

	public void setRel(String rel) {
		this.rel = rel;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}
}
