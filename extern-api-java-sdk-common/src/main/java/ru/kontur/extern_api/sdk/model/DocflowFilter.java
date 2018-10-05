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

import java.util.Date;
import java.util.Optional;
import java.util.UUID;
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

public class DocflowFilter {

    private long skip;
    private int take = 1000;

    @Nullable
    private Boolean finished;
    @Nullable
    private Boolean incoming;
    @Nullable
    private String innKpp;
    @Nullable
    private UUID orgId;
    @Nullable
    private SortOrder orderBy;
    @Nullable
    private Date updatedFrom;
    @Nullable
    private Date updatedTo;
    @Nullable
    private Date createdFrom;
    @Nullable
    private Date createdTo;
    @Nullable
    private DocflowType type;

    @Deprecated
    private String stringType;

    private DocflowFilter() {

    }

    private DocflowFilter skip(long skip) {
        this.skip = skip;
        return this;
    }

    private DocflowFilter take(int take) {
        this.take = take;
        return this;
    }

    public DocflowFilter finished(boolean finished) {
        this.finished = finished;
        return this;
    }

    public DocflowFilter incoming(boolean incoming) {
        this.incoming = incoming;
        return this;
    }

    public DocflowFilter innKpp(String inn, String kpp) {
        this.innKpp = YAStringUtils.joinIfExists("-", inn, kpp);
        return this;
    }

    public DocflowFilter inn(String inn) {
        this.innKpp = inn;
        return this;
    }

    public DocflowFilter orgId(UUID orgId) {
        this.orgId = orgId;
        return this;
    }

    public DocflowFilter orgId(String orgId) {
        this.orgId = UUID.fromString(orgId);
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
        this.updatedFrom = updatedFrom;
        return this;
    }

    public DocflowFilter updatedTo(Date updatedTo) {
        this.updatedTo = updatedTo;
        return this;
    }

    public DocflowFilter createdFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
        return this;
    }

    public DocflowFilter createdTo(Date createdTo) {
        this.createdTo = createdTo;
        return this;
    }

    public DocflowFilter type(DocflowType type) {
        this.type = type;
        return this;
    }

    /**
     * @deprecated it's not recommended use raw string types in a search query
     */
    @Deprecated
    public DocflowFilter type(String rawType) {
        this.stringType = rawType;
        return this;
    }

    public long getSkip() {
        return skip;
    }

    public int getTake() {
        return take;
    }

    @Nullable
    public Boolean getFinished() {
        return finished;
    }

    @Nullable
    public Boolean getIncoming() {
        return incoming;
    }

    @Nullable
    public String getInnKpp() {
        return innKpp;
    }

    @Nullable
    public String getOrgId() {
        return Optional.ofNullable(orgId).map(UUID::toString).orElse(null);
    }

    @Nullable
    public SortOrder getOrderBy() {
        return orderBy;
    }

    @Nullable
    public Date getUpdatedFrom() {
        return updatedFrom;
    }

    @Nullable
    public Date getUpdatedTo() {
        return updatedTo;
    }

    @Nullable
    public Date getCreatedFrom() {
        return createdFrom;
    }

    @Nullable
    public Date getCreatedTo() {
        return createdTo;
    }

    @Nullable
    public DocflowType getType() {
        return type;
    }

    /**
     * @deprecated it's not recommended use raw string types in a search query
     */
    @Deprecated
    public String getRawType() {
        return stringType;
    }

    public String getTypeAsString() {
        return Optional
                .ofNullable(getType())
                .map(DocflowType::getName)
                .orElse(getRawType());
    }

    public static DocflowFilter page(long skip, int take) {
        return new DocflowFilter()
                .skip(skip)
                .take(take);
    }

    public static DocflowFilter page(int skip, int take) {
        return page((long) skip, take);
    }
}
