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
public class MerchantTax {
  private String dohod = null;
  private String ischisl = null;
  private String torgSborFact = null;
  private String torgSborUmen = null;

	public String getDohod() {
		return dohod;
	}

	public void setDohod(String dohod) {
		this.dohod = dohod;
	}

	public String getIschisl() {
		return ischisl;
	}

	public void setIschisl(String ischisl) {
		this.ischisl = ischisl;
	}

	public String getTorgSborFact() {
		return torgSborFact;
	}

	public void setTorgSborFact(String torgSborFact) {
		this.torgSborFact = torgSborFact;
	}

	public String getTorgSborUmen() {
		return torgSborUmen;
	}

	public void setTorgSborUmen(String torgSborUmen) {
		this.torgSborUmen = torgSborUmen;
	}
}
