/*
 * MIT License
 *
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
 */

package ru.kontur.extern_api.sdk.service.transport.adaptors;

import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.providers.ServiceError;
import ru.kontur.extern_api.sdk.service.transport.adaptors.dto.CertificateListDto;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.CertificatesApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;

import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.CERTIFICATE_LIST;


/**
 * @author alexs
 */
public class CertificatesAdaptor extends BaseAdaptor {

    private final CertificatesApi api;

    public CertificatesAdaptor(CertificatesApi certificatesApi) {
        this.api = certificatesApi;
    }

    public CertificatesAdaptor() {
        this(new CertificatesApi());
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
        api.setApiClient(apiClient);
    }

    public QueryContext<CertificateList> getCertificates(QueryContext<CertificateList> cxt) {
        try {
            if (cxt.isFail()) return cxt;

            return cxt.setResult(
                    new CertificateListDto()
                            .fromDto(
                                    transport(cxt)
                                            .certificatesGetCertificatesAsync(cxt.getAccountProvider().accountId())
                            ),
                    CERTIFICATE_LIST
            );
        } catch (ApiException x) {
            return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
        }
    }

    private CertificatesApi transport(QueryContext<?> cxt) {
        super.prepareTransport(cxt);
        return api;
    }
}
