/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.skbkontur.sdk.extern.service;

import java.util.stream.Stream;

/**
 *
 * @author AlexS
 */
public class File {
	private static final ContentType DEFAULT_CONTENT_TYPE = ContentType.XML;
	
	public static enum ContentType {
		XML("application/xml");
		
		private final String value;
		
		ContentType(String value) {
			this.value = value;
		}
		
		public String getValue() {
			return value;
		}
		
    public static ContentType fromValue(String v) {
			return Stream.of(ContentType.values()).filter(t->t.value.equals(v)).findAny().orElse(null);
    }
	}
	
	public File() {
	}

	public File(String fileName, byte[] content, ContentType contentType) {
		this.fileName = fileName;
		this.content = content;
		this.contentType = contentType;
	}
	
	public File(String fileName, byte[] content) {
		this(fileName, content, DEFAULT_CONTENT_TYPE);
	}
	
	private ContentType contentType;
	private String fileName;
	private byte[] content;

	public ContentType getContentType() {
		return contentType;
	}

	public void setContentType(ContentType contentType) {
		this.contentType = contentType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) {
		this.content = content;
	}
}
