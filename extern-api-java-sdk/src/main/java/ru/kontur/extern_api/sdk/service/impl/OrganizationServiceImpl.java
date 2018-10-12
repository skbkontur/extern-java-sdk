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
package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;
import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.join;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.OrganizationsApi;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.CompanyName;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.OrganizationService;


public class OrganizationServiceImpl implements OrganizationService {

    private final AccountProvider acc;
    private final OrganizationsApi api;

    OrganizationServiceImpl(AccountProvider accountProvider, OrganizationsApi api) {
        this.acc = accountProvider;
        this.api = api;
    }

    @Override
    public CompletableFuture<QueryContext<Company>> lookupAsync(UUID companyId) {
        return api.lookup(acc.accountId(), companyId)
                .thenApply(contextAdaptor(QueryContext.COMPANY));
    }

    @Override
    public CompletableFuture<QueryContext<Company>> lookupAsync(String companyId) {
        return lookupAsync(UUID.fromString(companyId));
    }

    @Override
    @Deprecated
    public QueryContext<Company> lookup(QueryContext<?> parent) {
        return join(lookupAsync(parent.<UUID>require(QueryContext.COMPANY_ID).toString()));
    }

    @Override
    public CompletableFuture<QueryContext<Company>> createAsync(CompanyGeneral companyGeneral) {
        return api.create(acc.accountId(), companyGeneral)
                .thenApply(contextAdaptor(QueryContext.COMPANY));
    }

    @Override
    @Deprecated
    public QueryContext<Company> create(QueryContext<?> parent) {
        return join(createAsync(parent.require(QueryContext.COMPANY_GENERAL)));
    }

    @Override
    public CompletableFuture<QueryContext<Company>> updateAsync(UUID companyId, String name) {
        return api.update(acc.accountId(), companyId, new CompanyName(name))
                .thenApply(contextAdaptor(QueryContext.COMPANY));
    }

    @Override
    public CompletableFuture<QueryContext<Company>> updateAsync(String companyId, String name) {
        return updateAsync(UUID.fromString(companyId), name);
    }

    @Override
    @Deprecated
    public QueryContext<Company> update(QueryContext<?> parent) {
        return join(updateAsync(
                parent.<UUID>require(QueryContext.COMPANY_ID).toString(),
                parent.require(QueryContext.NAME)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(UUID companyId) {
        return api.delete(acc.accountId(), companyId)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String companyId) {
        return deleteAsync(UUID.fromString(companyId));
    }

    @Override
    @Deprecated
    public QueryContext<Void> delete(QueryContext<?> parent) {
        return join(deleteAsync(parent.<UUID>require(QueryContext.COMPANY_ID).toString()));
    }

    @Override
    public CompletableFuture<QueryContext<CompanyBatch>> searchAsync(OrgFilter filter) {
        return api.search(
                acc.accountId(),
                filter.getInn(),
                filter.getKpp(),
                filter.getSkip(),
                filter.getTake()
        ).thenApply(contextAdaptor(QueryContext.COMPANY_BATCH));
    }

    @Override
    @Deprecated
    public CompletableFuture<QueryContext<CompanyBatch>> searchAsync(String inn, String kpp, Long skip, Integer take) {
        return searchAsync(OrgFilter
                .page(skip == null ? 0 : skip, take == null ? 0 : take)
                .inn(inn)
                .kpp(kpp)
        );
    }

    @Override
    @Deprecated
    public QueryContext<CompanyBatch> search(QueryContext<?> parent) {
        return join(searchAsync(
                parent.require(QueryContext.INN),
                parent.require(QueryContext.KPP),
                parent.require(QueryContext.SKIP),
                parent.require(QueryContext.TAKE)
        ));
    }

}
