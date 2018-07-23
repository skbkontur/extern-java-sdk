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
 * @author alexs
 *
 * Класс содержит информацию для представителя организации
 */
public class Representative {

    @SerializedName("representative-document")
    private String representativeDocument = null;

    private PassportInfo passport = null;

    /**
     * Возвращает наименования документа представителя
     * @return наименования документа представителя
     */
    public String getRepresentativeDocument() {
        return representativeDocument;
    }

    /**
     * Устанавливает наименования документа представителя
     * @param representativeDocument
     */
    public void setRepresentativeDocument(String representativeDocument) {
        this.representativeDocument = representativeDocument;
    }

    /**
     * Возвращает паспортные данные представителя
     * @return паспортные данные представителя
     * @see PassportInfo
     */
    public PassportInfo getPassport() {
        return passport;
    }

    /**
     * Устанавливат паспортные данные представителя
     * @param passport паспортные данные представителя
     * @see PassportInfo
     */
    public void setPassport(PassportInfo passport) {
        this.passport = passport;
    }
}
