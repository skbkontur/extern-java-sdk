/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class ContentRequest {

	@SerializedName("CertificateThumbprint")
	private String thumbprint;
	@SerializedName("Files")
	private List<File> files;

	public ContentRequest() {
	}

	public ContentRequest(String thumbprint, String content) {
		this.thumbprint = thumbprint;
		this.files = new ArrayList<>();
		File file = new File(UUID.randomUUID().toString(), content);
		files.add(file);
	}

	public String getThumbprint() {
		return thumbprint;
	}

	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}

	public List<File> getFiles() {
		return files;
	}

	public void setFiles(List<File> files) {
		this.files = files;
	}
}
