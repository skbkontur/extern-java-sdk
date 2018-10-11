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

import org.jetbrains.annotations.Nullable;

public class OrgFilter {

    private long skip;
    private int take;

    @Nullable
    private String inn;

    @Nullable
    private String kpp;

    private OrgFilter(){}

    private OrgFilter skip(long skip) {
        this.skip = skip;
        return this;
    }

    private OrgFilter take(int take) {
        this.take = take;
        return this;
    }

    public OrgFilter inn(String inn) {
        this.inn = inn;
        return this;
    }

    public OrgFilter kpp(String kpp) {
        this.kpp = kpp;
        return this;
    }

    public static OrgFilter page(long skip, int take) {
        return new OrgFilter()
                .skip(skip)
                .take(take);
    }

    public static OrgFilter page(int skip, int take) {
        return page((long) skip, take);
    }

    public static OrgFilter maxPossibleBatch(){return page(0, 1000 );}

    public long getSkip() { return skip; }

    public int getTake() { return take; }

    @Nullable
    public String getInn() {
        return inn;
    }

    @Nullable
    public String getKpp() {
        return kpp;
    }
}
