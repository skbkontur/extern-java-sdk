/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

/**
 * <p>
 * Класс содержит информацию об организации.
 * Используется в сервисе {@code OrganizationService}
 * </p>
 * @author Aleksey Sukhorukov
 */
public class Company {
    private UUID id;
    private CompanyGeneral general = null;

    /**
     * Возвращает идентификатор организации
     * @return идентификатор организации
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор организации
     * @param id идентификатор организации
     */
    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Устанавливает идентификатор организации
     * @param id идентификатор организации
     */
    public void setId(String id) {
        this.id = UUID.fromString(id);
    }

    /**
     * Возвращает структуру данных с информацией об организации {@link CompanyGeneral}
     * @return структуру данных с информацией об организации
     * @see CompanyGeneral
     */
    public CompanyGeneral getGeneral() {
        return general;
    }

    /**
     * Устанавливает структуру данных с информацией об организации {@link CompanyGeneral}
     * @param general структура данных с информацией об организации
     */
    public void setGeneral(CompanyGeneral general) {
        this.general = general;
    }
}
