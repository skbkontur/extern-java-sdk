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

import com.google.gson.TypeAdapter;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;

/**
 * @author alexs
 *
 * Класс предназначен для отправки информации об отправителе и подотчетной организации
 * при создании декларации. Класс инкопсулирует в себе следующие структуры:
 * <p>
 *     {@link SignerTypeEnum} тип подписанта, возможные значения:
 *     <li>UNKNOWN - должность подписанта неизвестен;</li>
 *     <li>CHIEF - руководитель;</li>
 *     <li>REPRESENTATIVE - представитель</li>
 * </p>
 * <p>{@link Taxpayer} - подотчетная организация</p>
 */
public class AdditionalClientInfo {

    @SerializedName("signer-type")
    private SignerTypeEnum signerType = null;
    @SerializedName("sender-full-name")
    private String senderFullName = null;
    private Taxpayer taxpayer = null;

    /**
     * Возвращает тип подписанта
     * @return тип подписанта
     * @see SignerTypeEnum
     */
    public SignerTypeEnum getSignerType() {
        return signerType;
    }

    /**
     * Устанавливает тип подписанта
     * @param signerType тип подписанта
     * @see SignerTypeEnum
     */
    public void setSignerType(SignerTypeEnum signerType) {
        this.signerType = signerType;
    }

    /**
     * Возвращает полное имя отправителя
     * @return полное имя отправителя
     */
    public String getSenderFullName() {
        return senderFullName;
    }

    /**
     * Устанавливает полное имя отправителя
     * @param senderFullName полное имя отправителя
     */
    public void setSenderFullName(String senderFullName) {
        this.senderFullName = senderFullName;
    }

    /**
     * Возвращает данные подотчетной организации
     * @return данные подотчетной организации
     * @see Taxpayer
     */
    public Taxpayer getTaxpayer() {
        return taxpayer;
    }

    /**
     * Устанавливает данные подотчетной организации
     * @param taxpayer данные подотчетной организации
     * @see Taxpayer
     */
    public void setTaxpayer(Taxpayer taxpayer) {
        this.taxpayer = taxpayer;
    }

    @JsonAdapter(SignerTypeEnum.Adapter.class)
    /**
     * Тип подписанта
     */
    public enum SignerTypeEnum {
        /** неизвестный */
        UNKNOWN("unknown"),
        /** руководитель */
        CHIEF("chief"),
        /** представитель */
        REPRESENTATIVE("representative");

        private final String value;

        SignerTypeEnum(String value) {
            this.value = value;
        }

        /**
         * Возвращает тип подписанта по его значению.
         * Если значение не найдено, то тип будет null.
         * @param text значение типа: "unknown","chief","representative"
         * @return тип подписанта
         */
        public static SignerTypeEnum fromValue(String text) {
            for (SignerTypeEnum b : SignerTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        /**
         * Возвращает значение типа
         * @return значение типа
         */
        public String getValue() {
            return value;
        }

        /**
         * Возвращает значение типа
         * @return значение типа
         */
        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static class Adapter extends TypeAdapter<SignerTypeEnum> {

            @Override
            public void write(final JsonWriter jsonWriter, final SignerTypeEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public SignerTypeEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return SignerTypeEnum.fromValue(String.valueOf(value));
            }
        }
    }
}
