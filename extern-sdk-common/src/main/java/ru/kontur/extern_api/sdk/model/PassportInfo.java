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
import java.util.Date;


/**
 * @author alexs
 *
 * Класс для хранения паспортных данных
 */
public class PassportInfo {

    private String code = null;

    @SerializedName("series-number")
    private String seriesNumber = null;

    @SerializedName("issued-date")
    private Date issuedDate = null;

    @SerializedName("issued-by")
    private String issuedBy = null;

    /**
     * Возвращает код подразделения
     * @return код подразделения
     */
    public String getCode() {
        return code;
    }

    /**
     * Устанавливает код подразделения
     * @param code код подразделения
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * Возвращает серию паспорта
     * @return серии паспорта
     */
    public String getSeriesNumber() {
        return seriesNumber;
    }

    /**
     * Устанавливает серию паспорта
     * @param seriesNumber серия паспорта
     */
    public void setSeriesNumber(String seriesNumber) {
        this.seriesNumber = seriesNumber;
    }

    /**
     * Возвращает дату выдачи
     * @return дата выдачи
     */
    public Date getIssuedDate() {
        return issuedDate;
    }

    /**
     * Устанвливает дату выдачи
     * @param issuedDate дата выдачи
     */
    public void setIssuedDate(Date issuedDate) {
        this.issuedDate = issuedDate;
    }

    /**
     * Возвращает имя учереждения, выдавщего паспорт
     * @return имя учереждения, выдавщего паспорт
     */
    public String getIssuedBy() {
        return issuedBy;
    }

    /**
     * Устанавливает имя учереждения, выдавщего паспорт
     * @param issuedBy имя учереждения, выдавщего паспорт
     */
    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }
}
