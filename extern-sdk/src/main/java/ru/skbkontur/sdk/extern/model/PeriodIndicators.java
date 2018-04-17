/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.skbkontur.sdk.extern.model;

/**
 * @author alexs
 */
public class PeriodIndicators {

    protected String oktmo = null;
    protected String avPu = null;
    protected String dohod = null;
    protected String rashod = null;
    protected String nalBazaUbyt = null;
    protected String stavka = null;
    protected String ischisl = null;
    protected String umenNal = null;
    protected MerchantTax raschTorgSbor = null;

    public String getOktmo() {
        return oktmo;
    }

    public void setOktmo(String oktmo) {
        this.oktmo = oktmo;
    }

    public String getAvPu() {
        return avPu;
    }

    public void setAvPu(String avPu) {
        this.avPu = avPu;
    }

    public String getDohod() {
        return dohod;
    }

    public void setDohod(String dohod) {
        this.dohod = dohod;
    }

    public String getRashod() {
        return rashod;
    }

    public void setRashod(String rashod) {
        this.rashod = rashod;
    }

    public String getNalBazaUbyt() {
        return nalBazaUbyt;
    }

    public void setNalBazaUbyt(String nalBazaUbyt) {
        this.nalBazaUbyt = nalBazaUbyt;
    }

    public String getStavka() {
        return stavka;
    }

    public void setStavka(String stavka) {
        this.stavka = stavka;
    }

    public String getIschisl() {
        return ischisl;
    }

    public void setIschisl(String ischisl) {
        this.ischisl = ischisl;
    }

    public String getUmenNal() {
        return umenNal;
    }

    public void setUmenNal(String umenNal) {
        this.umenNal = umenNal;
    }

    public MerchantTax getRaschTorgSbor() {
        return raschTorgSbor;
    }

    public void setRaschTorgSbor(MerchantTax raschTorgSbor) {
        this.raschTorgSbor = raschTorgSbor;
    }
}
