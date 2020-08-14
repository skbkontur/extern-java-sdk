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

import java.util.ArrayList;
import java.util.List;
import ru.kontur.extern_api.sdk.model.Link;

/**
 * Список доверенностей
 */
public class WarrantList {

    private Integer skip = null;
    private Integer take = null;
    private Integer totalCount = null;
    private List<OrganizationWarrantInformation> organizationWarrantInformations = new ArrayList<>();

    /**
     * Возвращает смещение от начала списка
     *
     * @return смещение от начала списка
     **/
    public Integer getSkip() {
        return skip;
    }

    /**
     * Возвращает максимальный размер возвращаемого списка
     *
     * @return максимальный размер возвращаемого списка
     **/
    public Integer getTake() {
        return take;
    }

    /**
     * Возвращает общее количество доверенностей
     *
     * @return общее количество доверенностей
     **/
    public Integer getTotalCount() {
        return totalCount;
    }

    /**
     * Возвращает список организаций
     *
     * @return список организаций
     **/
    public List<OrganizationWarrantInformation> getOrganizationWarrantInformations() {
        return organizationWarrantInformations;
    }
}

