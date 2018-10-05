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
 * <p>
 * Класс предназначен для формирования разделов УСН декларации
 * </p>
 */
public class UsnFormatPeriod {

    private PeriodModifiersEnum periodModifiers = null;
    private Integer year = null;

    /**
     * <p>Возвращает налоговый период. Возможны следующие значения:</p>
     * <ul>
     *     <li>NONE - календарный год;</li>
     *     <li>LIQUIDATIONREORGANIZATION - последний налоговый период при реорганизации (ликвидации) организации (при прекращении деятельности в качестве индивидуального предпринимателя);</li>
     *     <li>TAXREGIMECHANGE - последний налоговый период при переходе на иной режим налогообложения;</li>
     *     <li>LASTPERIODFORTAXREGIME - последний налоговый период при прекращении предпринимательской деятельности, в отношении которой налогоплательщиком применялась упрощенная система налогообложения.</li>
     * </ul>
     * @return налоговый период
     */
    public PeriodModifiersEnum getPeriodModifiers() {
        return periodModifiers;
    }

    /**
     * <p>Устанавливает налоговый период.</p>
     * @param periodModifiers налоговый период. Возможны следующие значения:
     * <ul>
     *     <li>NONE - календарный год;</li>
     *     <li>LIQUIDATIONREORGANIZATION - последний налоговый период при реорганизации (ликвидации) организации (при прекращении деятельности в качестве индивидуального предпринимателя);</li>
     *     <li>TAXREGIMECHANGE - последний налоговый период при переходе на иной режим налогообложения;</li>
     *     <li>LASTPERIODFORTAXREGIME - последний налоговый период при прекращении предпринимательской деятельности, в отношении которой налогоплательщиком применялась упрощенная система налогообложения.</li>
     * </ul>
     */
    public void setPeriodModifiers(PeriodModifiersEnum periodModifiers) {
        this.periodModifiers = periodModifiers;
    }

    /**
     * <p>Возвращает отчетный год</p>
     * @return отчетный год
     */
    public Integer getYear() {
        return year;
    }

    /**
     * <p>Устанавливает отчетный год</p>
     * @param year отчетный год
     */
    public void setYear(Integer year) {
        this.year = year;
    }

    @JsonAdapter(PeriodModifiersEnum.Adapter.class)
    public enum PeriodModifiersEnum {
        NONE("none"),
        LIQUIDATIONREORGANIZATION("liquidationReorganization"),
        TAXREGIMECHANGE("taxRegimeChange"),
        LASTPERIODFORTAXREGIME("lastPeriodForTaxRegime");

        private final String value;

        PeriodModifiersEnum(String value) {
            this.value = value;
        }

        public static PeriodModifiersEnum fromValue(String text) {
            for (PeriodModifiersEnum b : PeriodModifiersEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String toString() {
            return String.valueOf(value);
        }

        public static class Adapter extends TypeAdapter<PeriodModifiersEnum> {

            @Override
            public void write(final JsonWriter jsonWriter, final PeriodModifiersEnum enumeration) throws IOException {
                jsonWriter.value(enumeration.getValue());
            }

            @Override
            public PeriodModifiersEnum read(final JsonReader jsonReader) throws IOException {
                String value = jsonReader.nextString();
                return PeriodModifiersEnum.fromValue(String.valueOf(value));
            }
        }
    }
}
