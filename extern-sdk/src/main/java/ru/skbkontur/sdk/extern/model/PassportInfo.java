/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

import org.joda.time.DateTime;

/**
 *
 * @author alexs
 */
public class PassportInfo {
  private String code = null;

  private String seriesNumber = null;

  private DateTime issuedDate = null;

  private String issuedBy = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getSeriesNumber() {
		return seriesNumber;
	}

	public void setSeriesNumber(String seriesNumber) {
		this.seriesNumber = seriesNumber;
	}

	public DateTime getIssuedDate() {
		return issuedDate;
	}

	public void setIssuedDate(DateTime issuedDate) {
		this.issuedDate = issuedDate;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

}
