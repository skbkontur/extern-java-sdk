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
 * @author AlexS
 *
 * Класс предназначен для отправки информации на сервер о новой учетной записи
 * Используется в группе запросов {@code AccountService}
 */
public class CreateAccountRequest {

    private String inn = null;
    private String kpp = null;
    @SerializedName("organization-name")
    private String organizationName = null;

    /**
     * Метод возвращает ИНН организации новой учетной записи
     * @return ИНН новой организации
     */
    public String getInn() {
        return inn;
    }

    /**
     * Метод устанавливает ИНН организации новой учетной записи
     * @param inn ИНН новой организации
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * Метод возвращает КПП организации новой учетной записи
     * @return КПП новой организации
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Метод устанавливает КПП организации новой учетной записи
     * @param kpp КПП новой организации
     */
    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    /**
     * Метод возвращает имя организации новой учетной записи
     * @return
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Метод устанавливает имя организации новой учетной записи
     * @param organizationName
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
