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
public class UsnServiceContractInfoV2 {
  private UsnFormatPeriod period = null;
  private AdditionalClientInfo additionalOrgInfo = null;
  private UsnDataV2 data = null;

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

	public UsnDataV2 getData() {
		return data;
	}

	public void setData(UsnDataV2 data) {
		this.data = data;
	}

}
