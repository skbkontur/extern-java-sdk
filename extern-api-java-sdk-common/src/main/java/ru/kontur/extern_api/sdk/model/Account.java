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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * Класс содержит информацию об учетной записи организации. Используется методами сервиса {@code
 * AccountService}
 * </p>
 *
 * @author Aleksey Sukhorukov
 */
public class Account {

    private UUID id = null;
    private String inn = null;
    private String kpp = null;
    private String organizationName = null;
    private List<Link> links = new ArrayList<>();

    /**
     * Метод возвращает идентификатор учетной записи
     *
     * @return UUID идентификатор учетной записи
     * @see java.util.UUID
     */
    public UUID getId() {
        return id;
    }

    /**
     * Метод устанавливает идентификатор учетной записи
     *
     * @param id идентификатор учетной записи в формате UUID (@see http://www.faqs.org/rfcs/rfc4122.html)
     */
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * Метод устанавливает идентификатор учетной записи
     *
     * @param id идентификатор учетной записи в формате UUID
     * @see java.util.UUID
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Метод возвращает ИНН организации учетной записи
     *
     * @return ИНН
     */
    public String getInn() {
        return inn;
    }

    /**
     * Метод устанавливает ИНН организации учетной записи
     *
     * @param inn ИНН
     */
    public void setInn(String inn) {
        this.inn = inn;
    }

    /**
     * Метод возвращает КПП организации учетной записи
     *
     * @return КПП
     */
    public String getKpp() {
        return kpp;
    }

    /**
     * Метод устанавливает КПП организации учетной записи
     *
     * @param kpp КПП
     */
    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    /**
     * Метод возвращает имя организации учетной записи
     *
     * @return имя организации
     */
    public String getOrganizationName() {
        return organizationName;
    }

    /**
     * Метод устанавливает имя организации учетной записи
     *
     * @param organizationName имя организации
     */
    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    /**
     * Метод возвращает список объектов Link. В текущей реализации не несет дополнительной
     * информации.
     *
     * @return список объектов Link
     * @see Link
     */
    public List<Link> getLinks() {
        return links;
    }

    /**
     * Устанавливает список объектов Link
     *
     * @param links список объектов Link
     */
    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
