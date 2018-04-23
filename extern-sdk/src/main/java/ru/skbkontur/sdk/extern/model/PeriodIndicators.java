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
public class PeriodIndicators {
  private String oktmo = null;
  private String avPu = null;
  private String dohod = null;
  private String rashod = null;
  private String nalBazaUbyt = null;
  private String stavka = null;
  private String ischisl = null;
  private String umenNal = null;
  private MerchantTax raschTorgSbor = null;

	public String getOktmo() {
		return oktmo;
	}

	public final void setOktmo(String oktmo) {
		this.oktmo = oktmo;
	}

	public String getAvPu() {
		return avPu;
	}

	public final void setAvPu(String avPu) {
		this.avPu = avPu;
	}

	public String getDohod() {
		return dohod;
	}

	public final void setDohod(String dohod) {
		this.dohod = dohod;
	}

	public String getRashod() {
		return rashod;
	}

	public final void setRashod(String rashod) {
		this.rashod = rashod;
	}

	public String getNalBazaUbyt() {
		return nalBazaUbyt;
	}

	public final void setNalBazaUbyt(String nalBazaUbyt) {
		this.nalBazaUbyt = nalBazaUbyt;
	}

	public String getStavka() {
		return stavka;
	}

	public final void setStavka(String stavka) {
		this.stavka = stavka;
	}

	public String getIschisl() {
		return ischisl;
	}

	public final void setIschisl(String ischisl) {
		this.ischisl = ischisl;
	}

	public String getUmenNal() {
		return umenNal;
	}

	public final void setUmenNal(String umenNal) {
		this.umenNal = umenNal;
	}

	public MerchantTax getRaschTorgSbor() {
		return raschTorgSbor;
	}

	public final void setRaschTorgSbor(MerchantTax raschTorgSbor) {
		this.raschTorgSbor = raschTorgSbor;
	}
}
