/*
 * Copyright (c) 2019 SKB Kontur
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

package ru.kontur.extern_api.sdk.model.warrants;

import java.util.Date;
import ru.kontur.extern_api.sdk.model.Fio;

/**
 * Информация о частном лице или индивидуальном предпринимателе из доверенности
 */
public class WarrantIndividual {

    private Fio fio = null;
    private String inn = null;
    private IdentityCard document = null;
    private Date birthDate = null;
    private String ogrnip = null;
    private String citizenship = null;
    private String address = null;

    /**
     * ФИО
     *
     * @return fio
     **/
    public Fio getFio() {
        return fio;
    }

    /**
     * ИНН
     *
     * @return inn
     **/
    public String getInn() {
        return inn;
    }

    /**
     * Удостоверение личности
     *
     * @return document
     **/
    public IdentityCard getDocument() {
        return document;
    }

    /**
     * Дата рождения
     *
     * @return birthDate
     **/
    public Date getBirthDate() {
        return birthDate;
    }

    /**
     * ОГРНИП
     *
     * @return ogrnip
     **/
    public String getOgrnip() {
        return ogrnip;
    }

    /**
     * Гражданство
     *
     * @return citizenship
     **/
    public String getCitizenship() {
        return citizenship;
    }


    /**
     * Место жительства
     *
     * @return address
     **/
    public String getAddress() {
        return address;
    }
}

