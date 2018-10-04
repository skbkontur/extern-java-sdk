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
package ru.kontur.extern_api.sdk.httpclient;

import static ru.kontur.extern_api.sdk.adaptor.QueryContext.COMPANY;
import static ru.kontur.extern_api.sdk.adaptor.QueryContext.COMPANY_BATCH;
import static ru.kontur.extern_api.sdk.adaptor.QueryContext.NOTHING;

import ru.kontur.extern_api.sdk.httpclient.api.OrganizationsApi;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.OrganizationsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationsAdaptorImpl extends BaseAdaptor implements OrganizationsAdaptor {

    private final OrganizationsApi api;

    public OrganizationsAdaptorImpl() {
        this.api = new OrganizationsApi();
    }

    @Override
    public QueryContext<Company> lookup(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<Company> resultCxt = new QueryContext<Company>(cxt, cxt.getEntityName()).setResult(
                transport(cxt)
                    .lookup(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getCompanyId().toString()
                    ).getData(),
                COMPANY
            );
            return resultCxt.setCompanyId(resultCxt.getCompany().getId());
        } catch (ApiException x) {
            return new QueryContext<Company>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<Company> create(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            QueryContext<Company> resultCxt = new QueryContext<Company>(cxt, cxt.getEntityName())
                .setResult(
                    transport(cxt)
                        .create(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getCompanyGeneral()
                        ).getData(),
                    COMPANY
                );
            return resultCxt.setCompanyId(resultCxt.getCompany().getId());
        } catch (ApiException x) {
            return new QueryContext<Company>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<Company> update(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<Company>(cxt, cxt.getEntityName()).setResult(
                transport(cxt)
                    .update(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getCompanyId().toString(),
                        cxt.getName()
                    ).getData(),
                COMPANY
            );
        } catch (ApiException x) {
            return new QueryContext<Company>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<Void> delete(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            transport(cxt)
                .delete(
                    cxt.getAccountProvider().accountId().toString(),
                    cxt.getCompanyId().toString()
                );

            return new QueryContext<Void>(cxt, cxt.getEntityName()).setResult(null, NOTHING);
        } catch (ApiException x) {
            return new QueryContext<Void>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    @Override
    public QueryContext<CompanyBatch> search(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<CompanyBatch>(cxt, cxt.getEntityName()).setResult(
                transport(cxt)
                    .search(
                        cxt.getAccountProvider().accountId().toString(),
                        cxt.getInn(),
                        cxt.getKpp(),
                        cxt.getSkip(),
                        cxt.getTake()
                    ).getData()
                , COMPANY_BATCH
            );
        } catch (ApiException x) {
            return new QueryContext<CompanyBatch>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private OrganizationsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
