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
 * <p>Класс содержит информацию о налогоплательщике</p>
 * @author Aleksey Sukhorukov
 */
public class Taxpayer {

    @SerializedName("taxpayer-chief-fio")
    private String taxpayerChiefFio = null;

    private Representative representative = null;

    @SerializedName("taxpayer-phone")
    private String taxpayerPhone = null;

    @SerializedName("taxpayer-okved")
    private String taxpayerOkved = null;

    @SerializedName("taxpayer-full-name")
    private String taxpayerFullName = null;

    /**
     * <p>Возвращает ФИО руководителя</p>
     * @return ФИО руководителя
     */
    public String getTaxpayerChiefFio() {
        return taxpayerChiefFio;
    }

    /**
     * <p>Устанавливает ФИО руководителя</p>
     * @param taxpayerChiefFio ФИО руководителя
     */
    public void setTaxpayerChiefFio(String taxpayerChiefFio) {
        this.taxpayerChiefFio = taxpayerChiefFio;
    }

    /**
     * <p>Возвращает представителя организации. Если отсутствует, то возвращает null</p>
     * @return представитель организации
     * @see Representative
     */
    public Representative getRepresentative() {
        return representative;
    }

    /**
     * <p>Устанавливает представителя организации.</p>
     * @param representative представитель организации
     * @see Representative
     */
    public void setRepresentative(Representative representative) {
        this.representative = representative;
    }

    /**
     * <p>Возвращает телефон организации</p>
     * @return телефон организации
     */
    public String getTaxpayerPhone() {
        return taxpayerPhone;
    }

    /**
     * <p>Устанавливает телефон организации</p>
     * @param taxpayerPhone телефон организации
     */
    public void setTaxpayerPhone(String taxpayerPhone) {
        this.taxpayerPhone = taxpayerPhone;
    }

    /**
     * <p>Возвращает ОКВЭД организации</p>
     * @return ОКВЭД организации
     */
    public String getTaxpayerOkved() {
        return taxpayerOkved;
    }

    /**
     * <p>Устанавливает ОКВЕД организации</p>
     * @param taxpayerOkved ОКВЭД организации
     */
    public void setTaxpayerOkved(String taxpayerOkved) {
        this.taxpayerOkved = taxpayerOkved;
    }

    /**
     * <p>Возвращает полное имя организации</p>
     * @return полное имя организации
     */
    public String getTaxpayerFullName() {
        return taxpayerFullName;
    }

    /**
     * <p>Устанавливает полное имя организации</p>
     * @param taxpayerFullName полное имя организации
     */
    public void setTaxpayerFullName(String taxpayerFullName) {
        this.taxpayerFullName = taxpayerFullName;
    }
}
