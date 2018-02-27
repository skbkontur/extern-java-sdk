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
public class Organization {
	private String inn;
	private String kpp;

	public Organization(String inn, String kpp) {
		this.inn = inn;
		this.kpp = kpp;
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
	
	
}
