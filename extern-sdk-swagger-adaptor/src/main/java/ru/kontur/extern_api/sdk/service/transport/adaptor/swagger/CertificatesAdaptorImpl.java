/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.service.transport.adaptor.CertificatesAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CERTIFICATE_LIST;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.CertificateListDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.CertificatesApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author alexs
 */
public class CertificatesAdaptorImpl extends BaseAdaptor implements CertificatesAdaptor {

    private final CertificatesApi api;

    public CertificatesAdaptorImpl() {
        this.api = new CertificatesApi();
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
        api.setApiClient(apiClient);
    }

    @Override
    public QueryContext<CertificateList> getCertificates(QueryContext<CertificateList> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new CertificateListDto()
                    .fromDto(
                        transport(cxt)
                            .certificatesGetCertificatesAsync(cxt.getAccountProvider().accountId())
                    ),
                CERTIFICATE_LIST
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private CertificatesApi transport(QueryContext<?> cxt) {
        super.prepareTransport(cxt);
        return api;
    }
}
