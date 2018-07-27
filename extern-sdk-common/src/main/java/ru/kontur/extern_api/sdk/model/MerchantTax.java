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
public class MerchantTax {

    private String dohod = null;
    private String ischisl = null;
    @SerializedName("torg-sbor-fact")
    private String torgSborFact = null;
    @SerializedName("torg-sbor-umen")
    private String torgSborUmen = null;

    /**
     * <p>Возвращает значение поля "Доход".</p>
     *
     * @return значение поля "Доход"
     */
    public String getDohod() {
        return dohod;
    }

    /**
     * <p>Устанавливает значение поля "Доход".</p>
     *
     * @param dohod значение поля "Доход"
     */
    public void setDohod(String dohod) {
        this.dohod = dohod;
    }

    /**
     * <p>Возвращает значение поля "Исчисл".</p>
     *
     * @return значение поля "Исчисл"
     */
    public String getIschisl() {
        return ischisl;
    }

    /**
     * <p>Возвращает значение поля "Исчисл".</p>
     *
     * @param ischisl значение поля "Исчисл"
     */
    public void setIschisl(String ischisl) {
        this.ischisl = ischisl;
    }

    /**
     * <p>Возвращает значение поля "ТоргСборФакт".</p>
     *
     * @return значение поля "ТоргСборФакт"
     */
    public String getTorgSborFact() {
        return torgSborFact;
    }

    /**
     * <p>Устанавливает значение поля "ТоргСборФакт".</p>
     *
     * @param torgSborFact значение поля "ТоргСборФакт"
     */
    public void setTorgSborFact(String torgSborFact) {
        this.torgSborFact = torgSborFact;
    }

    /**
     * <p>Возвращает значение поля "ТоргСборУмен".</p>
     *
     * @return значение поля "ТоргСборУмен"
     */
    public String getTorgSborUmen() {
        return torgSborUmen;
    }

    /**
     * <p>Устанавливает значение поля "ТоргСборУмен".</p>
     *
     * @param torgSborUmen значение поля "ТоргСборУмен"
     */
    public void setTorgSborUmen(String torgSborUmen) {
        this.torgSborUmen = torgSborUmen;
    }
}
