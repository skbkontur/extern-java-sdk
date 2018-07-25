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
import java.util.ArrayList;
import java.util.List;


/**
 * @author alexs
 * <p>
 * Класс предназначен для постраничного получение списка документооборотов {@link DocflowPageItem}
 * Используетс в {@code DocflowService}
 * </p>
 */
public class DocflowPage {

    private Long skip = null;
    private Long take = null;
    @SerializedName("total-count")
    private Long totalCount = null;
    @SerializedName("docflows-page-item")
    private List<DocflowPageItem> docflowsPageItem = new ArrayList<>();

    /**
     * Возвращает смещение от начала списка
     * @return смещение от начала списка
     */
    public Long getSkip() {
        return skip;
    }

    /**
     * Устанавливает смещение от начала списка
     * @param skip смещение от начала списка
     */
    public void setSkip(Long skip) {
        this.skip = skip;
    }

    /**
     * Возвращает максимальный размер возвращаемого списка
     * @return максимальный размер возвращаемого списка
     */
    public Long getTake() {
        return take;
    }

    /**
     * Устанавливает максимальный размер возвращаемого списка
     * @param take максимальный размер возвращаемого списка
     */
    public void setTake(Long take) {
        this.take = take;
    }

    /**
     * Возвращает общее количество ДО
     * @return общее количество ДО
     */
    public Long getTotalCount() {
        return totalCount;
    }

    /**
     * Устанавливает общее количество ДО
     * @param totalCount общее количество ДО
     */
    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    /**
     * Возвращает список для получения ДО {@link DocflowPageItem}
     * @return список для получения ДО
     * @see DocflowPageItem
     */
    public List<DocflowPageItem> getDocflowsPageItem() {
        return docflowsPageItem;
    }

    /**
     * Устанавливает список для получения ДО {@link DocflowPageItem}
     * @param docflowsPageItem список для получения ДО
     * @see DocflowPageItem
     */
    public void setDocflowsPageItem(List<DocflowPageItem> docflowsPageItem) {
        this.docflowsPageItem = docflowsPageItem;
    }
}
