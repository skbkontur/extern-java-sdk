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


/**
 *  Класс содержит информацию об организации
 */
public class Organization {

    private String inn;
    private String name;
    private final OrganizationInfo organization;

    public Organization(String inn, String kpp, String name) {
        this.inn = inn;
        this.name = name;
        this.organization = new OrganizationInfo(kpp);
    }

    /**
     * Возвращает ИНН.
     * @return ИНН
     */
    public String getInn() { return inn; }

    /**
     * Устанавливает ИНН.
     * @param inn ИНН
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * Возвращает название.
     * @return название
     */
    public String getName() { return name; }

    /**
     * Устанавливает название.
     * @param name название
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Возвращает КПП.
     * @return КПП
     */
    public String getKpp() {
        return organization.getKpp();
    }

    /**
     * Устанавливает КПП.
     * @param kpp КПП
     */
    public void setKpp(String kpp) {
        this.organization.setKpp(kpp);
    }

    public static class OrganizationInfo {
        private String kpp;
        
        public OrganizationInfo(String kpp) {
            this.kpp = kpp;
        }
        
        public String getKpp() {
            return kpp;
        }

        public void setKpp(String kpp) {
            this.kpp = kpp;
        }
    }
}
