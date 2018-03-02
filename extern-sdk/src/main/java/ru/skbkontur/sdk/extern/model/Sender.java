/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import com.google.gson.annotations.SerializedName;

/**
 *
 * @author AlexS
 */
public class Sender {
	@SerializedName("inn")
	private String inn;
	@SerializedName("kpp")
	private String kpp;
	@SerializedName("certificate")
	private String certificate;

	public Sender() {
	}

	public Sender(String inn, String kpp, String certificate) {
		this.inn = inn;
		this.kpp = kpp;
		this.certificate = certificate;
	}
	
	public String getInn() {
		return inn;
	}

	public void setInn(String inn) {
		this.inn = inn;
	}

	public String getKpp() {
		return kpp;
	}

	public void setKpp(String kpp) {
		this.kpp = kpp;
	}

	public String getCertificate() {
		return certificate;
	}

	public void setCertificate(String certificate) {
		this.certificate = certificate;
	}
}
