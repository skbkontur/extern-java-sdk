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
 * <p>
 * Класс предназначен для формирования разделов УСН декларации
 * </p>
 */
public class PeriodIndicators {

    private String oktmo = null;
    private String avPu = null;
    private String dohod = null;
    private String rashod = null;
    @SerializedName("nal-baza-ubyt")
    private String nalBazaUbyt = null;
    private String stavka = null;
    private String ischisl = null;
    @SerializedName("umen-nal")
    private String umenNal = null;
    @SerializedName("rasch-torg-sbor")
    private MerchantTax raschTorgSbor = null;

    /**
     * <p>Возвращает значение поля "ОКТМО"</p>
     * @return значение "ОКТМО"
     */
    public String getOktmo() {
        return oktmo;
    }

    /**
     * <p>Устанавливает значение поля "ОКТМО"</p>
     * @param oktmo значение поля "ОКТМО"
     */
    public final void setOktmo(String oktmo) {
        this.oktmo = oktmo;
    }

    /**
     * <p>Возвращает значение поля "АвПУ"</p>
     * @return значение поля "АвПУ"
     */
    public String getAvPu() {
        return avPu;
    }

    /**
     * <p>Устанавливает значение поля "АвПУ"</p>
     * @param avPu значение поля "АвПУ"
     */
    public final void setAvPu(String avPu) {
        this.avPu = avPu;
    }

    /**
     * <p>Возвращает значение поля "Доход"</p>
     * @return значение поля "Доход"
     */
    public String getDohod() {
        return dohod;
    }

    /**
     * <p>Устанавливает значение поля "Доход"</p>
     * @param dohod значение поля "Доход"
     */
    public final void setDohod(String dohod) {
        this.dohod = dohod;
    }

    /**
     * <p>Возвращает значение поля "Расход"</p>
     * @return значение "Расход"
     */
    public String getRashod() {
        return rashod;
    }

    /**
     * <p>Устанавливает значение поля "Расход"</p>
     * @param rashod значение поля "Расход"
     */
    public final void setRashod(String rashod) {
        this.rashod = rashod;
    }

    /**
     * <p>Возвращает значение поля "НалБазаУбыт"</p>
     * @return значение "НалБазаУбыт"
     */
    public String getNalBazaUbyt() {
        return nalBazaUbyt;
    }

    /**
     * <p>Устанавливает значение поля "НалБазаУбыт"</p>
     * @param nalBazaUbyt значение поля "НалБазаУбыт"
     */
    public final void setNalBazaUbyt(String nalBazaUbyt) {
        this.nalBazaUbyt = nalBazaUbyt;
    }

    /**
     * <p>Возвращает значение поля "Ставка"</p>
     * @return значение поля "Ставка"
     */
    public String getStavka() {
        return stavka;
    }

    /**
     * <p>Устанавливает значение поля "Ставка"</p>
     * @param stavka значение поля "Ставка"
     */
    public final void setStavka(String stavka) {
        this.stavka = stavka;
    }

    /**
     * <p>Возвращает значение поля "Исчисл"</p>
     * @return значение поля "Исчисл"
     */
    public String getIschisl() {
        return ischisl;
    }

    /**
     * <p>Устанавливает значение поля "Исчисл"</p>
     * @param ischisl значение поля "Исчисл"
     */
    public final void setIschisl(String ischisl) {
        this.ischisl = ischisl;
    }

    /**
     * <p>Возвращает значение поля "УменНал"</p>
     * @return значение поля "УменНал"
     */
    public String getUmenNal() {
        return umenNal;
    }

    /**
     * <p>Устанавливает значение поля "УменНал"</p>
     * @param umenNal значение поля "УменНал"
     */
    public final void setUmenNal(String umenNal) {
        this.umenNal = umenNal;
    }

    /**
     * <p>Возвращает значение поля "РасчТоргСбор"</p>
     * @return значение поля "РасчТоргСбор"
     */
    public MerchantTax getRaschTorgSbor() {
        return raschTorgSbor;
    }

    /**
     * <p>Устанавливает значение поля "РасчТоргСбор"</p>
     * @param raschTorgSbor значение поля "РасчТоргСбор"
     */
    public final void setRaschTorgSbor(MerchantTax raschTorgSbor) {
        this.raschTorgSbor = raschTorgSbor;
    }
}
