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
public class UsnServiceContractInfo {
  private UsnFormatPeriod period = null;
  private AdditionalClientInfo additionalOrgInfo = null;
  private Object data = null;

	public UsnFormatPeriod getPeriod() {
		return period;
	}

	public void setPeriod(UsnFormatPeriod period) {
		this.period = period;
	}

	public AdditionalClientInfo getAdditionalOrgInfo() {
		return additionalOrgInfo;
	}

	public void setAdditionalOrgInfo(AdditionalClientInfo additionalOrgInfo) {
		this.additionalOrgInfo = additionalOrgInfo;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

}
