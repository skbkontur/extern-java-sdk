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
 */
public class UsnFormatPeriod {

    @SerializedName("period-modifiers")
    private PeriodModifiersEnum periodModifiers = null;
    private Integer year = null;

    public PeriodModifiersEnum getPeriodModifiers() {
        return periodModifiers;
    }

    public void setPeriodModifiers(PeriodModifiersEnum periodModifiers) {
        this.periodModifiers = periodModifiers;
    }

    public Integer getYear() {
        return year;
    }

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
