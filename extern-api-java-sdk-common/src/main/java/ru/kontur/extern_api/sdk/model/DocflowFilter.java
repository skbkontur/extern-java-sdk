/*
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
 *
 */

package ru.kontur.extern_api.sdk.model;

import static ru.kontur.extern_api.sdk.PublicDateFormat.formatDatetime;

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

public class DocflowFilter implements Filter {

    private int skip;
    private int take = 1000;
    private SortOrder orderBy;

    private final HashMap<String, String> filterMap = new HashMap<>(14);

    private DocflowFilter() {

    }

    private DocflowFilter skip(int skip) {
        this.skip = skip;
        return this;
    }

    private DocflowFilter take(int take) {
        this.take = take;
        return this;
    }

    public DocflowFilter finished(boolean finished) {
        filterMap.put("finished", String.valueOf(finished));
        return this;
    }

    public DocflowFilter incoming(boolean incoming) {
        filterMap.put("incoming", String.valueOf(incoming));
        return this;
    }

    public DocflowFilter innKpp(String inn, String kpp) {
        filterMap.put("innKpp", YAStringUtils.joinIfExists("-", inn, kpp));
        return this;
    }

    public DocflowFilter inn(String inn) {
        filterMap.put("innKpp", inn);
        return this;
    }

    public DocflowFilter orgId(UUID orgId) {
        filterMap.put("orgId", orgId.toString());
        return this;
    }

    /**
     * @param orderBy SendDate is used to order docflows
     */
    public DocflowFilter orderBy(SortOrder orderBy) {
        this.orderBy = orderBy;
        return this;
    }

    public DocflowFilter updatedFrom(Date updatedFrom) {
        filterMap.put("updatedFrom", formatDatetime(updatedFrom));
        return this;
    }

    public DocflowFilter updatedTo(Date updatedTo) {
        filterMap.put("updatedTo", formatDatetime(updatedTo));
        return this;
    }

    public DocflowFilter createdFrom(Date createdFrom) {
        filterMap.put("createdFrom", formatDatetime(createdFrom));
        return this;
    }

    public DocflowFilter createdTo(Date createdTo) {
        filterMap.put("createdTo", formatDatetime(createdTo));
        return this;
    }

    public DocflowFilter periodFrom(Date periodFrom) {
        filterMap.put("periodFrom", formatDatetime(periodFrom));
        return this;
    }

    public DocflowFilter periodTo(Date periodTo) {
        filterMap.put("periodTo", formatDatetime(periodTo));
        return this;
    }

    public DocflowFilter knd(String knd) {
        filterMap.put("knd", knd);
        return this;
    }

    public DocflowFilter knd(int knd) {
        return knd(String.valueOf(knd));
    }

    public DocflowFilter type(DocflowType type) {
        filterMap.put("type", type.getName());
        return this;
    }

    /**
     * @deprecated it's a unreliable solution to use raw string types in a search query
     */
    @Deprecated
    public DocflowFilter type(String rawType) {
        filterMap.put("type", rawType);
        return this;
    }

    public SortOrder getOrder() {
        return orderBy;
    }

    @Override
    public int getSkip() {
        return skip;
    }

    @Override
    public int getTake() {
        return take;
    }

    @Override
    public Map<String, String> asFilterMap() {
        return Collections.unmodifiableMap(filterMap);
    }

    public static DocflowFilter page(int skip, int take) {
        return new DocflowFilter()
                .skip(skip)
                .take(take);
    }
}
