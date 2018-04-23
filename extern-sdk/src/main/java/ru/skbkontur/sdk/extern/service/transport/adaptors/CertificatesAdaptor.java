/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.CERTIFICATE_LIST;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.CertificateListDto;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.api.CertificatesApi;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class CertificatesAdaptor extends BaseAdaptor {

	private final CertificatesApi api;
	
    @SuppressWarnings("unchecked")
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
			if (cxt.isFail())	return cxt;

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
			return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
		}
	}

	private CertificatesApi transport(QueryContext<?> cxt) {
		super.prepareTransport(cxt);
		return api;
	}
}
