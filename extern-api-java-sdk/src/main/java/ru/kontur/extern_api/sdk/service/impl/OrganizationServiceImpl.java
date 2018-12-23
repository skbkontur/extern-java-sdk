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
import org.jetbrains.annotations.Nullable;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.OrganizationsApi;
import ru.kontur.extern_api.sdk.model.CompanyName;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.OrganizationBatch;
import ru.kontur.extern_api.sdk.model.OrganizationGeneral;
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
    public CompletableFuture<QueryContext<Organization>> lookupAsync(UUID organizationId) {
        return api.lookup(acc.accountId(), organizationId)
                .thenApply(contextAdaptor(QueryContext.ORGANIZATION));
    }

    @Override
    public CompletableFuture<QueryContext<Organization>> lookupAsync(String organizationId) {
        return lookupAsync(UUID.fromString(organizationId));
    }

    @Override
    public QueryContext<Organization> lookup(QueryContext<?> parent) {
        return join(lookupAsync(parent.<UUID>require(QueryContext.ORGANIZATION_ID).toString()));
    }

    @Override
    public CompletableFuture<QueryContext<Organization>> createAsync(
            OrganizationGeneral organizationGeneral) {
        return api.create(acc.accountId(), organizationGeneral)
                .thenApply(contextAdaptor(QueryContext.ORGANIZATION));
    }

    @Override
    public QueryContext<Organization> create(QueryContext<?> parent) {
        return join(createAsync(parent.require(QueryContext.ORGANIZATION_GENERAL)));
    }

    @Override
    public CompletableFuture<QueryContext<Organization>> updateAsync(UUID organizationId, String name) {
        return api.update(acc.accountId(), organizationId, new CompanyName(name))
                .thenApply(contextAdaptor(QueryContext.ORGANIZATION));
    }

    @Override
    public CompletableFuture<QueryContext<Organization>> updateAsync(String organizationId, String name) {
        return updateAsync(UUID.fromString(organizationId), name);
    }

    @Override
    public QueryContext<Organization> update(QueryContext<?> parent) {
        return join(updateAsync(
                parent.<UUID>require(QueryContext.ORGANIZATION_ID).toString(),
                parent.require(QueryContext.NAME)
        ));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(UUID organizationId) {
        return api.delete(acc.accountId(),organizationId)
                .thenApply(contextAdaptor(QueryContext.NOTHING));
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String organizationId) {
        return deleteAsync(UUID.fromString(organizationId));
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> parent) {
        return join(deleteAsync(parent.<UUID>require(QueryContext.ORGANIZATION_ID).toString()));
    }

    @Override
    public CompletableFuture<QueryContext<OrganizationBatch>> searchAsync(OrgFilter filter) {
        return api.search(
                acc.accountId(),
                filter.getSkip(),
                filter.getTake(),
                filter.asFilterMap()
        ).thenApply(contextAdaptor(QueryContext.ORGANIZATION_BATCH));
    }

    @Override
    @Deprecated
    public CompletableFuture<QueryContext<OrganizationBatch>> searchAsync(
            @Nullable String inn,
            @Nullable String kpp,
            @Nullable Integer skip,
            @Nullable Integer take
    ) {
        return searchAsync(OrgFilter
                .page(skip == null ? 0 : skip, take == null ? 0 : take)
                .inn(inn)
                .kpp(kpp)
        );
    }

    @Override
    public QueryContext<OrganizationBatch> search(QueryContext<?> parent) {
        return join(searchAsync(
                parent.require(QueryContext.INN),
                parent.require(QueryContext.KPP),
                parent.require(QueryContext.SKIP),
                parent.require(QueryContext.TAKE)
        ));
    }
}
