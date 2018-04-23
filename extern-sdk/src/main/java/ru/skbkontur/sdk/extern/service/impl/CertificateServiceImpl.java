/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.CertificateList;
import ru.skbkontur.sdk.extern.service.CertificateService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.CertificatesAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
public class CertificateServiceImpl extends BaseService<CertificatesAdaptor> implements CertificateService {
	private static final String EN_CER = "certificate";

	@Override
	public CompletableFuture<QueryContext<CertificateList>> getCertificateListAsync() {
		QueryContext<CertificateList> cxt = createQueryContext(EN_CER);
		return cxt.applyAsync(api::getCertificates);
	}

	@Override
	public QueryContext<CertificateList> getCertificateList(QueryContext<?> parent) {
		QueryContext<CertificateList> cxt = createQueryContext(parent, EN_CER);
		return cxt.apply(api::getCertificates);
	}
}
