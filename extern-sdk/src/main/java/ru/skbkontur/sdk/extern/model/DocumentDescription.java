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
public class DocumentDescription {

	private String type = null;
	private String filename = null;
	private String contentType = null;
	private String compressionType = null;

	public DocumentDescription type(String type) {
		this.type = type;
		return this;
	}

	/**
	 * Get type
	 *
	 * @return type
	 *
	 */
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public DocumentDescription filename(String filename) {
		this.filename = filename;
		return this;
	}

	/**
	 * Get filename
	 *
	 * @return filename
	 *
	 */
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public DocumentDescription contentType(String contentType) {
		this.contentType = contentType;
		return this;
	}

	/**
	 * Get contentType
	 *
	 * @return contentType
	 *
	 */
	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public DocumentDescription compressionType(String compressionType) {
		this.compressionType = compressionType;
		return this;
	}

	/**
	 * Get compressionType
	 *
	 * @return compressionType
	 *
	 */
	public String getCompressionType() {
		return compressionType;
	}

	public void setCompressionType(String compressionType) {
		this.compressionType = compressionType;
	}
}
