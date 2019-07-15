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

import java.util.Date;

/**
 * <p>
 * Класс предназначен для хронения информации сертификата см. класс {@code CertificateService}
 * </p>
 * @author Aleksey Sukhorukov
 */
public class Certificate {

    private String fio = null;
    private String inn = null;
    private String kpp = null;
    private Boolean isValid = null;
    private Boolean isCloud = null;
    private Boolean isQualified = null;
    private String content = null;
    private Date expiredAt = null;

    /**
     * Возвращает дату истечения сертификата
     * @return Дата истечения сертификата
     */
    public Date getExpiredAt() {
        return expiredAt;
    }

    /**
     * Устанавливает дату истечения сертификата
     * @param expireAt дата истечения сертификата
     */
    public void setExpiredAt(Date expireAt) {
        this.expiredAt = expireAt;
    }

    /**
     * Возвращает ФИО владельца сертификата
     * @return ФИО владельца сертификата
     */
    public String getFio() {
        return fio;
    }

    /**
     * Устанавливает ФИО владельца сертификата
     * @param fio ФИО
     */
    public void setFio(String fio) {
        this.fio = fio;
    }

    /**
     * Возвращает ИНН
     * @return ИНН
     */
    public String getInn() {
        return inn;
    }

    /**
     * Устанавливает ИНН
     * @param inn ИНН
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * Возвращает КПП
     * @return КПП
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Устанавливает КПП
     * @param kpp КПП
     */
    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    /**
     * Возвращает признак действительности сертификата
     * @return признак действительности сертификата. True - действителен, иначе - недействителен.
     */
    public Boolean getIsValid() {
        return isValid;
    }

    /**
     * Устанавливает признак действительности сертификата
     * @param isValid признак действительности сертификата. True - действителен, иначе - недействителен.
     */
    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    /**
     * Возвращает признак облочного сертификата
     * @return признак облочного сертификата. True - облачный, иначе - не облачный
     */
    public Boolean getIsCloud() {
        return isCloud;
    }

    /**
     * Устанавливает признак облачного сертификата
     * @param isCloud признак облачного сертификата. True - облачный, иначе - не облачный
     */
    public void setIsCloud(Boolean isCloud) {
        this.isCloud = isCloud;
    }

    /**
     * Возвращает признак квалифицированного сертификата
     * @return признак квалифицированного сертификата. True - квалифицированный, иначе - не квалифицированный
     */
    public Boolean getIsQualified() {
        return isQualified;
    }

    /**
     * Устанавливает признак квалифицированного сертификата
     * @param isQualified признак квалифицированного сертификата. True - квалифицированный, иначе - не квалифицированный
     */
    public void setIsQualified(Boolean isQualified) {
        this.isQualified = isQualified;
    }

    /**
     * Возвращает контент сертификата в кодировке BASE64 без тегов
     * @return контент сертификата в кодировке BASE64 без тегов
     */
    public String getContent() {
        return content;
    }

    /**
     * Устанавливает контент сертификата в кодировке BASE64 без тегов
     * @param content контент сертификата в кодировке BASE64 без тегов
     */
    public void setContent(String content) {
        this.content = content;
    }
}
