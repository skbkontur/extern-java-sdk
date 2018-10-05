/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

/**
 * <p>Класс содержит информацию об отправителе черновика</p>
 * @author Aleksey Sukhorukov
 */
public class Sender {

	private String inn;
	private String kpp;
	private Certificate certificate;
	private String ipaddress;

	private String thumbprint;

	public Sender() {
	}

	public Sender(String inn, String kpp, String certificate, String ipaddress) {
		this.inn = inn;
		this.kpp = kpp;
		this.certificate = new Certificate(certificate);
		this.ipaddress = ipaddress;
	}

	/**
	 * Возвращает ИНН
	 * @return ИНН
	 */
	public String getInn() {
		return inn;
	}

	/**
	 * Устанавливает ИНН
	 * @param inn ИНН
	 */
	public void setInn(String inn) {
		this.inn = inn;
	}

	/**
	 * Возвращает КПП
	 * @return КПП
	 */
	public String getKpp() {
		return kpp;
	}

	/**
	 * Устанавливает КПП
	 * @param kpp КПП
	 */
	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	/**
	 * Возвращает сертификат в кодировке BASE64
	 * @return сертификат в кодировке BASE64
	 */
	public String getCertificate() {
		return certificate.content;
	}

	/**
	 * Устанавливает сертификат в кодировке BASE64
	 * @param certificate сертификат в кодировке BASE64
	 */
	public void setCertificate(String certificate) {
		this.certificate = new Certificate(certificate);
	}

	/**
	 * Возвращает IP адрес отправителя
	 * @return IP адрес отправителя
	 */
	public String getIpaddress() {
		return ipaddress;
	}

	/**
	 * Устанавливает IP адрес отправителя
	 * @param ipaddress IP адрес отправителя
	 */
	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	/**
	 * Возвращает отпечаток сертификата отправителя
	 * @return отпечаток сертификата отправителя
	 */
	public String getThumbprint() {
		return thumbprint;
	}

	/**
	 * Устанавливает отпечаток сертификата отправителя
	 * @param thumbprint отпечаток сертификата отправителя
	 */
	public void setThumbprint(String thumbprint) {
		this.thumbprint = thumbprint;
	}

	public static class Certificate {

		@SerializedName("content")
		private String content = null;

		public Certificate(String content) {
			this.content = content;
		}
		
		public Certificate content(String content) {
			this.content = content;
			return this;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}
	}
}
