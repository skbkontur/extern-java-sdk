/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.model;

/**
 * @author alexs
 */
public class TaxPeriodIndicators extends PeriodIndicators {

    private String nalPumin = null;

    public TaxPeriodIndicators(String nalPumin, PeriodIndicators p) {
        this.nalPumin = nalPumin;
        this.avPu = p.getAvPu();
        this.dohod = p.getDohod();
        this.ischisl = p.getIschisl();
        this.nalBazaUbyt = p.getNalBazaUbyt();
        this.oktmo = p.getOktmo();
        this.raschTorgSbor = p.getRaschTorgSbor();
        this.rashod = p.getRashod();
        this.stavka = p.getStavka();
        this.umenNal = p.getUmenNal();
    }

    public TaxPeriodIndicators() {
    }

    public String getNalPumin() {
        return nalPumin;
    }

    public void setNalPumin(String nalPumin) {
        this.nalPumin = nalPumin;
    }
}
