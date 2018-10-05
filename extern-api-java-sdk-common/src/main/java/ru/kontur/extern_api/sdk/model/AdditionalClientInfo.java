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
 * Класс предназначен для отправки информации об отправителе и подотчетной организации при создании
 * декларации. Класс инкопсулирует в себе следующие структуры:
 * </p>
 * <ul>
 * <li>{@link SignerTypeEnum} тип подписанта, возможные значения:
 * <ul>
 * <li>UNKNOWN - должность подписанта неизвестен;</li>
 * <li>CHIEF - руководитель;</li>
 * <li>REPRESENTATIVE - представитель</li>
 * </ul>
 * </li>
 * <li>{@link Taxpayer} подотчетная организация</li>
 * </ul>
 */
public class AdditionalClientInfo {

    private SignerTypeEnum signerType = null;
    private DocumentSender documentSender = null;
    private Taxpayer taxpayer = null;

    /**
     * Возвращает тип подписанта
     *
     * @return тип подписанта
     * @see SignerTypeEnum
     */
    public SignerTypeEnum getSignerType() {
        return signerType;
    }

    /**
     * Устанавливает тип подписанта
     *
     * @param signerType тип подписанта
     * @see SignerTypeEnum
     */
    public void setSignerType(SignerTypeEnum signerType) {
        this.signerType = signerType;
    }

    public DocumentSender getDocumentSender() {
        return documentSender;
    }

    public void setDocumentSender(DocumentSender documentSender) {
        this.documentSender = documentSender;
    }

    /**
     * Возвращает данные подотчетной организации
     *
     * @return данные подотчетной организации
     * @see Taxpayer
     */
    public Taxpayer getTaxpayer() {
        return taxpayer;
    }

    /**
     * Устанавливает данные подотчетной организации
     *
     * @param taxpayer данные подотчетной организации
     * @see Taxpayer
     */
    public void setTaxpayer(Taxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    /**
     * Тип подписанта
     */
    public enum SignerTypeEnum {
        /** неизвестный */
        @SerializedName("unknown")
        UNKNOWN,
        /** руководитель */
        @SerializedName("chief")
        CHIEF,
        /** представитель */
        @SerializedName("representative")
        REPRESENTATIVE

    }
}
