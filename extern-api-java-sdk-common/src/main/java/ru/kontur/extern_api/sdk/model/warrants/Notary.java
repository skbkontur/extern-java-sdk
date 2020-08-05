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

import ru.kontur.extern_api.sdk.model.Fio;

public class Notary {

    private Fio fio;
    private String inn;
    private String address;
    private WarrantOrganization notaryOrganization;

    /**
     * ФИО нотариуса
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
     * Адрес
     *
     * @return address
     **/
    public String getAddress() {
        return address;
    }

    /**
     * Информация об организации, в случае если это нотариальная контора
     *
     * @return notaryOrganization
     **/
    public WarrantOrganization getNotaryOrganization() {
        return notaryOrganization;
    }
}

