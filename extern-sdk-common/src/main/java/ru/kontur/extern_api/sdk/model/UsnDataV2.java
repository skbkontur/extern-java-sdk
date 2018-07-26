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

import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Класс предназначен для формирования разделов УСН декларации
 * </p>
 * @author Aleksey Sukhorukov
 */
public class UsnDataV2 {

    @SerializedName("nom-korr")
    private Integer nomKorr = null;
    @SerializedName("po-mestu")
    private Integer poMestu = null;
    @SerializedName("priz-np")
    private Integer prizNp = null;
    @SerializedName("ubyt-pred")
    private String ubytPred = null;
    @SerializedName("ischisl-min")
    private String ischislMin = null;
    @SerializedName("za-kv")
    private PeriodIndicators zaKv = null;
    @SerializedName("za-pg")
    private PeriodIndicators zaPg = null;
    @SerializedName("za9m")
    private PeriodIndicators za9m = null;
    @SerializedName("za-nal-per")
    private TaxPeriodIndicators zaNalPer = null;

    /**
     * <p>Возвращает значение поля "НомКорр" - номер корректировки</p>
     * @return значение поля "НомКорр" - номер корректировки
     */
    public Integer getNomKorr() {
        return nomKorr;
    }

    /**
     * <p>Устанавливает значение поля "НомКорр" - номер корректировки</p>
     * @param nomKorr значение поля "НомКорр" - номер корректировки
     */
    public void setNomKorr(Integer nomKorr) {
        this.nomKorr = nomKorr;
    }

    /**
     * <p>Возвращает значение поля "ПоМесту" - код места, по которому представляется документ(120,210,215)</p>
     * @return значение поля "ПоМесту" - код места, по которому представляется документ(120,210,215)
     */
    public Integer getPoMestu() {
        return poMestu;
    }

    /**
     * <p>Устанавливает значение поля "ПоМесту" - код места, по которому представляется документ(120,210,215)</p>
     * @param poMestu значение поля "ПоМесту" - код места, по которому представляется документ(120,210,215)
     */
    public void setPoMestu(Integer poMestu) {
        this.poMestu = poMestu;
    }

    /**
     * <p>Возвращает значение поля "ПризНП" - признак налогоплательщика (1,2)</p>
     * @return значение поля "ПризНП" - признак налогоплательщика (1,2)
     */
    public Integer getPrizNp() {
        return prizNp;
    }

    /**
     * <p>Устанавливает значение поля "ПризНП" - признак налогоплательщика (1,2)</p>
     * @param prizNp значение поля "ПризНП" - признак налогоплательщика (1,2)
     */
    public void setPrizNp(Integer prizNp) {
        this.prizNp = prizNp;
    }

    /**
     * <p>Возвращает значение поля "УбытПред" - сумма убытка</p>
     * @return значение поля "УбытПред" - сумма убытка
     */
    public String getUbytPred() {
        return ubytPred;
    }

    /**
     * <p>Устанавливает значение поля "УбытПред" - сумма убытка</p>
     * @param ubytPred значение поля "УбытПред" - сумма убытка
     */
    public void setUbytPred(String ubytPred) {
        this.ubytPred = ubytPred;
    }

    /**
     * <p>Возвращает значение поля "ИсчислМин" - сумма исчисленного минимального налога за налоговый период</p>
     * @return значение поля "ИсчислМин" - сумма исчисленного минимального налога за налоговый период
     */
    public String getIschislMin() {
        return ischislMin;
    }

    /**
     * <p>Устанавливает значение поля "ИсчислМин" - сумма исчисленного минимального налога за налоговый период</p>
     * @param ischislMin значение поля "ИсчислМин" - сумма исчисленного минимального налога за налоговый период
     */
    public void setIschislMin(String ischislMin) {
        this.ischislMin = ischislMin;
    }

    /**
     * <p>Возвращает значение поля "ЗаКв" - показатели за первый квартал, могут отсутствовать</p>
     * @return значение поля "ЗаКв" - показатели за первый квартал, могут отсутствовать
     */
    public PeriodIndicators getZaKv() {
        return zaKv;
    }

    /**
     * <p>Устанавливает значение поля "ЗаКв" - показатели за первый квартал, могут отсутствовать</p>
     * @param zaKv значение поля "ЗаКв" - показатели за первый квартал, могут отсутствовать
     */
    public void setZaKv(PeriodIndicators zaKv) {
        this.zaKv = zaKv;
    }

    /**
     * <p>Возвращает значение поля "ЗаПг" - показатели за полугодие, могут отсутствовать</p>
     * @return значение поля "ЗаПг" - показатели за полугодие, могут отсутствовать
     */
    public PeriodIndicators getZaPg() {
        return zaPg;
    }

    /**
     * <p>Устанавливает значение поля "ЗаПг" - показатели за полугодие, могут отсутствовать</p>
     * @param zaPg значение поля "ЗаПг" - показатели за полугодие, могут отсутствовать
     */
    public void setZaPg(PeriodIndicators zaPg) {
        this.zaPg = zaPg;
    }

    /**
     * <p>Возвращает значение поля "За9м" - показатели за девять месяцев, могут отсутствовать</p>
     * @return значение поля "За9м" - показатели за девять месяцев, могут отсутствовать
     */
    public PeriodIndicators getZa9m() {
        return za9m;
    }

    /**
     * <p>Устанавливает значение поля "За9м" - показатели за девять месяцев, могут отсутствовать</p>
     * @param za9m значение поля "За9м" - показатели за девять месяцев, могут отсутствовать
     */
    public void setZa9m(PeriodIndicators za9m) {
        this.za9m = za9m;
    }

    /**
     * <p>Возвращает значение поля "ЗаНалПер" - показатели за налоговый период</p>
     * @return значение поля "ЗаНалПер" - показатели за налоговый период
     */
    public TaxPeriodIndicators getZaNalPer() {
        return zaNalPer;
    }

    /**
     * <p>Устанавливает значение поля "ЗаНалПер" - показатели за налоговый период</p>
     * @param zaNalPer значение поля "ЗаНалПер" - показатели за налоговый период
     */
    public void setZaNalPer(TaxPeriodIndicators zaNalPer) {
        this.zaNalPer = zaNalPer;
    }
}
