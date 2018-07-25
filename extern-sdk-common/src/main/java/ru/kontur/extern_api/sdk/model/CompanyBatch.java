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

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksey Sukhorukov
 *
 * <p>
 * Класс содержит список компаний, возвращаемый сервисом {@code OrganizationService.search}
 * </p>
 */
public class CompanyBatch {
    @SerializedName("organizations")
    private List<Company> companies;

    @SerializedName("total-count")
    private Long totalCount;

    public CompanyBatch() {
        companies = new ArrayList<>();
        totalCount = 0L;
    }

    /**
     * Возвращает список организаций
     * @return список организаций
     * @see Company
     */
    public List<Company> getCompanies() {
        return companies;
    }

    /**
     * Устанавливает список организаций
     * @param companies список организаций
     * @see Company
     */
    public void setCompanies(List<Company> companies) {
        this.companies = companies;
    }

    /**
     * Возвращает общее количество организаций удовлетворяющих критерию поиска
     * @return общее количество организаций удовлетворяющих критерию поиска
     */
    public Long getTotalCount() {
        return totalCount;
    }

    /**
     * Устанавливает общее количество организаций удовлетворяющих критерию поиска
     * @param totalCount общее количество организаций удовлетворяющих критерию поиска
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }
}
