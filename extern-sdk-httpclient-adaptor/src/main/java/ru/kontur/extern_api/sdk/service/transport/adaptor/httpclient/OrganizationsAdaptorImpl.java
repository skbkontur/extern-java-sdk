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
package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.COMPANY;

import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.OrganizationsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.OrganizationsApi;

/**
 * @author Aleksey Sukhorukov
 */
public class OrganizationsAdaptorImpl extends BaseAdaptor implements OrganizationsAdaptor {

    private OrganizationsApi api;

    public OrganizationsAdaptorImpl() {
        this.api = new OrganizationsApi();
    }

    @Override
    public QueryContext<Company> lookup(QueryContext<Company> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt).lookup(cxt.getAccountProvider().accountId().toString(), cxt.getCompanyId().toString()).getData(), COMPANY);
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<Company> create(QueryContext<Company> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt
                .setResult(
                    transport(cxt)
                        .create(
                            cxt.getAccountProvider().accountId().toString(),
                            cxt.getCompanyGeneral()
                        ).getData(),
                    COMPANY
                ).setCompanyId(cxt.getCompany().getId());
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<Company> update(QueryContext<Company> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(transport(cxt).update(cxt.getAccountProvider().accountId().toString(), cxt.getCompanyId().toString(), cxt.getName()).getData(),
                COMPANY);
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    @Override
    public QueryContext<Void> delete(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt).delete(cxt.getAccountProvider().accountId().toString(), cxt.getCompanyId().toString());

            return cxt;
        } catch (ApiException x) {
            return cxt.setServiceError(x);
        }
    }

    private OrganizationsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
