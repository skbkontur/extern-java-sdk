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

package ru.kontur.extern_api.sdk.model;

/**
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
    private PeriodIndicators za9m = null;
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

    public PeriodIndicators getZa9m() {
        return za9m;
    }

    public void setZa9m(PeriodIndicators za9m) {
        this.za9m = za9m;
    }

    public TaxPeriodIndicators getZaNalPer() {
        return zaNalPer;
    }

    public void setZaNalPer(TaxPeriodIndicators zaNalPer) {
        this.zaNalPer = zaNalPer;
    }

}
