/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import com.google.gson.annotations.SerializedName;
import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class SignatureToSend {

	@SerializedName("id")
	private UUID id = null;
	@SerializedName("content-data")
	private byte[] contentData = null;

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

	/**
	 * Get contentData
	 *
	 * @return contentData
	 *
	 */
	public byte[] getContentData() {
		return contentData;
	}

	public void setContentData(byte[] contentData) {
		this.contentData = contentData;
	}
}
