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
public class UsnDataV2 {
  private Integer nomKorr = null;
  private Integer poMestu = null;
  private Integer prizNp = null;
  private String ubytPred = null;
  private String ischislMin = null;
  private PeriodIndicators zaKv = null;
  private PeriodIndicators zaPg = null;
  private PeriodIndicators zaem = null;
  private TaxPeriodIndicators zaNalPer = null;

	public Integer getNomKorr() {
		return nomKorr;
	}

	public void setNomKorr(Integer nomKorr) {
		this.nomKorr = nomKorr;
	}

	public Integer getPoMestu() {
		return poMestu;
	}

	public void setPoMestu(Integer poMestu) {
		this.poMestu = poMestu;
	}

	public Integer getPrizNp() {
		return prizNp;
	}

	public void setPrizNp(Integer prizNp) {
		this.prizNp = prizNp;
	}

	public String getUbytPred() {
		return ubytPred;
	}

	public void setUbytPred(String ubytPred) {
		this.ubytPred = ubytPred;
	}

	public String getIschislMin() {
		return ischislMin;
	}

	public void setIschislMin(String ischislMin) {
		this.ischislMin = ischislMin;
	}

	public PeriodIndicators getZaKv() {
		return zaKv;
	}

	public void setZaKv(PeriodIndicators zaKv) {
		this.zaKv = zaKv;
	}

	public PeriodIndicators getZaPg() {
		return zaPg;
	}

	public void setZaPg(PeriodIndicators zaPg) {
		this.zaPg = zaPg;
	}

	public PeriodIndicators getZaem() {
		return zaem;
	}

	public void setZaem(PeriodIndicators zaem) {
		this.zaem = zaem;
	}

	public TaxPeriodIndicators getZaNalPer() {
		return zaNalPer;
	}

	public void setZaNalPer(TaxPeriodIndicators zaNalPer) {
		this.zaNalPer = zaNalPer;
	}

}
