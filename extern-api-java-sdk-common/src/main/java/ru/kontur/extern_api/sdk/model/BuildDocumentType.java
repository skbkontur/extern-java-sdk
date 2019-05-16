/*
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
 *
 */

package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;


public enum BuildDocumentType {

    @SerializedName("USN")
    USN,

    /** "Cправка о состоянии расчетов по налогам, сборам, страховым взносам, пеням, штрафам, процентам" */
    @SerializedName("ION1")
    ION1,

    /** "Выписка операций по расчетам с бюджетом" */
    @SerializedName("ION2")
    ION2,

    /**  "Перечень налоговых деклараций (расчетов) и бухгалтерской отчетности" */
    @SerializedName("ION3")
    ION3,

    /**  "Акт совместной сверки расчетов по  налогам, сборам, страховым взносам, пеням, штрафам, процентам" */
    @SerializedName("ION4")
    ION4,

    /** "Cправка об исполнении налогоплательщиком (плательщиком сбора, плательщиком страховых взносов, налоговым агентом)
     * обязанности по уплате налогов, сборов, страховых взносов, пеней, штрафов, процентов" */
    @SerializedName("ION5")
    ION5
}
