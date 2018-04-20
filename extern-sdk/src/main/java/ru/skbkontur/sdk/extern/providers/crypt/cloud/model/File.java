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
class File {

	@SerializedName("Id")
	private String id;
	@SerializedName("Name")
	private String name;
	@SerializedName("Base64Content")
	private String content;

	File() {
	}

	File(String name, String content) {
		this.name = name;
		this.content = content;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getСontent() {
		return content;
	}

	public void setСontent(String content) {
		this.content = content;
	}
}
