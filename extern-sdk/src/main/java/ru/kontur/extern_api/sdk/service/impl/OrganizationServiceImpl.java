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

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.transport.adaptor.OrganizationsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationServiceImpl extends AbstractService  implements OrganizationService {

    private static final String EN_ORG = "organization";

    private final OrganizationsAdaptor organizationsAdaptor;

    public OrganizationServiceImpl(OrganizationsAdaptor organizationsAdaptor) {
        this.organizationsAdaptor = organizationsAdaptor;
    }

    @Override
    public CompletableFuture<QueryContext<Company>> lookupAsync(String companyId) {
        QueryContext<Company> cxt = createQueryContext(EN_ORG);
        return cxt
            .setCompanyId(companyId)
            .applyAsync(organizationsAdaptor::lookup);
    }

    @Override
    public QueryContext<Company> lookup(QueryContext<?> parent) {
        QueryContext<Company> cxt = createQueryContext(parent,EN_ORG);
        return cxt.apply(organizationsAdaptor::lookup);
    }

    @Override
    public CompletableFuture<QueryContext<Company>> createAsync(CompanyGeneral companyGeneral) {
        QueryContext<Company> cxt = createQueryContext(EN_ORG);
        return cxt
            .setCompanyGeneral(companyGeneral)
            .applyAsync(organizationsAdaptor::create);
    }

    @Override
    public QueryContext<Company> create(QueryContext<?> parent) {
        QueryContext<Company> cxt = createQueryContext(parent,EN_ORG);
        return cxt.apply(organizationsAdaptor::create);
    }

    @Override
    public CompletableFuture<QueryContext<Company>> updateAsync(String companyId, String name) {
        QueryContext<Company> cxt = createQueryContext(EN_ORG);
        return cxt
            .setCompanyId(companyId)
            .setName(name)
            .applyAsync(organizationsAdaptor::update);
    }

    @Override
    public QueryContext<Company> update(QueryContext<?> parent) {
        QueryContext<Company> cxt = createQueryContext(parent,EN_ORG);
        return cxt.apply(organizationsAdaptor::update);
    }

    @Override
    public CompletableFuture<QueryContext<Void>> deleteAsync(String companyId) {
        QueryContext<Void> cxt = createQueryContext(EN_ORG);
        return cxt
            .setCompanyId(companyId)
            .applyAsync(organizationsAdaptor::delete);
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> parent) {
        QueryContext<Void> cxt = createQueryContext(parent,EN_ORG);
        return cxt.apply(organizationsAdaptor::delete);
    }
}
