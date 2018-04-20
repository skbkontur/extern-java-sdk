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
public class TaxPeriodIndicators extends PeriodIndicators {
  private String nalPumin = null;

	public TaxPeriodIndicators(String nalPumin,PeriodIndicators p) {
		this.nalPumin = nalPumin;
		setAvPu(p.getAvPu());
		setDohod(p.getDohod());
		setIschisl(p.getIschisl());
		setNalBazaUbyt(p.getNalBazaUbyt());
		setOktmo(p.getOktmo());
		setRaschTorgSbor(p.getRaschTorgSbor());
		setRashod(p.getRashod());
		setStavka(p.getStavka());
		setUmenNal(p.getUmenNal());
	}

	public TaxPeriodIndicators() {
	}

	public String getNalPumin() {
		return nalPumin;
	}

	public final void setNalPumin(String nalPumin) {
		this.nalPumin = nalPumin;
	}
}
