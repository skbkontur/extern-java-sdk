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
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.COMPANY;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.COMPANY_BATCH;

import java.util.Map;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.service.transport.adaptor.OrganizationsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.OrganizationBatchDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.OrganizationDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.OrganizationsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.CreateOrganizationRequestDto;
import ru.kontur.extern_api.sdk.service.transport.swagger.model.UpdateOrganizationRequestDto;

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

            return cxt.setResult(
                new OrganizationDto()
                    .fromDto(
                        transport(cxt).organizationsGet(cxt.getAccountProvider().accountId(),cxt.getCompanyId())
                    ),
                COMPANY
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryContext<Company> create(QueryContext<Company> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            CompanyGeneral g = cxt.getCompanyGeneral();
            CreateOrganizationRequestDto dto
                = new CreateOrganizationRequestDto()
                    .inn(g.getInn())
                    .kpp(g.getKpp())
                    .name(g.getName());

            return cxt.setResult(
                new OrganizationDto()
                    .fromDto(
                        (Map<String, Object>) transport(cxt).organizationsCreate(cxt.getAccountProvider().accountId(),dto)
                    ),
                COMPANY
            ).setCompanyId(cxt.getCompany().getId());
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<Company> update(QueryContext<Company> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new OrganizationDto()
                    .fromDto(
                        transport(cxt).organizationsUpdate(cxt.getAccountProvider().accountId(),cxt.getCompanyId(),new UpdateOrganizationRequestDto().name(cxt.getName()))
                    ),
                COMPANY
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<Void> delete(QueryContext<Void> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            transport(cxt).organizationsDelete(cxt.getAccountProvider().accountId(),cxt.getCompanyId());

            return cxt;
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<CompanyBatch> search(QueryContext<CompanyBatch> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            cxt.setResult(
                new OrganizationBatchDto()
                    .fromDto(
                        transport(cxt).organizationsSearch(
                            cxt.getAccountProvider().accountId(),
                            cxt.getInn(),
                            cxt.getKpp(),
                            cxt.getSkip() == null ? null : cxt.getSkip().intValue(), /* to do: after change Integer type to Long needa remove intValue*/
                            cxt.getTake()
                        )
                    )
                ,
                COMPANY_BATCH
            );

            return cxt;
        } catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @SuppressWarnings("unchecked")
    private OrganizationsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient)super.prepareTransport(cxt));
        return api;
    }
}
