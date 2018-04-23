/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class Taxpayer {
  private String taxpayerChiefFio = null;

  private Representative representative = null;

  private String taxpayerPhone = null;

  private String taxpayerOkved = null;

  private String taxpayerFullName = null;

	public String getTaxpayerChiefFio() {
		return taxpayerChiefFio;
	}

	public void setTaxpayerChiefFio(String taxpayerChiefFio) {
		this.taxpayerChiefFio = taxpayerChiefFio;
	}

	public Representative getRepresentative() {
		return representative;
	}

	public void setRepresentative(Representative representative) {
		this.representative = representative;
	}

	public String getTaxpayerPhone() {
		return taxpayerPhone;
	}

	public void setTaxpayerPhone(String taxpayerPhone) {
		this.taxpayerPhone = taxpayerPhone;
	}

	public String getTaxpayerOkved() {
		return taxpayerOkved;
	}

	public void setTaxpayerOkved(String taxpayerOkved) {
		this.taxpayerOkved = taxpayerOkved;
	}

	public String getTaxpayerFullName() {
		return taxpayerFullName;
	}

	public void setTaxpayerFullName(String taxpayerFullName) {
		this.taxpayerFullName = taxpayerFullName;
	}
}
