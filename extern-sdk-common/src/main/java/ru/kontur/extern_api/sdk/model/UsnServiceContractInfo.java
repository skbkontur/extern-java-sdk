/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.model;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

/**
 * <p>
 * Класс предназначен для формирования разделов УСН декларации
 * </p>
 * @author Mikhail Pavlenko
 */
public class UsnServiceContractInfo {

    private UsnFormatPeriod period = null;
    @SerializedName("additional-org-info")
    private AdditionalClientInfo additionalOrgInfo = null;
    private Object data = null;

    /**
     * <p>Возвращает информацию об отчетном периоде</p>
     * @return информация об отчетном периоде
     * @see UsnFormatPeriod
     */
    public UsnFormatPeriod getPeriod() {
        return period;
    }

    /**
     * <p>Устанавливает информацию об отчетном периоде</p>
     * @param period информация об отчетном периоде
     * @see UsnFormatPeriod
     */
    public void setPeriod(UsnFormatPeriod period) {
        this.period = period;
    }

    /**
     * <p>Возвращает информацию об организации</p>
     * @return информация об организации
     * @see AdditionalClientInfo
     */
    public AdditionalClientInfo getAdditionalOrgInfo() {
        return additionalOrgInfo;
    }

    /**
     * <p>Устанавливает информацию об организации</p>
     * @param additionalOrgInfo информация об организации
     * @see AdditionalClientInfo
     */
    public void setAdditionalOrgInfo(AdditionalClientInfo additionalOrgInfo) {
        this.additionalOrgInfo = additionalOrgInfo;
    }

    /**
     * Возвращает JSON документ с информацией об УСН декларации
     * @return JSON документ с информацией об УСН декларации
     */
    public Object getData() {
        return data;
    }

    /**
     * Устанавливает JSON документ с информацией об УСН декларации
     * @param data JSON документ с информацией об УСН декларации
     */
    public void setData(Object data) {
        this.data = data;
    }
}
