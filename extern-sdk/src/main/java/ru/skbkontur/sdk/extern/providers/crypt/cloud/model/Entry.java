/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class Entry {
	@SerializedName("Id")
	private String id;
	@SerializedName("FileName")
	private String fileName;
	@SerializedName("Content")
	private String content;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
