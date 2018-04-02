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
public class Representative {
  private String representativeDocument = null;

  private PassportInfo passport = null;

	public String getRepresentativeDocument() {
		return representativeDocument;
	}

	public void setRepresentativeDocument(String representativeDocument) {
		this.representativeDocument = representativeDocument;
	}

	public PassportInfo getPassport() {
		return passport;
	}

	public void setPassport(PassportInfo passport) {
		this.passport = passport;
	}

}
