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
package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.CertificatesApi;
import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;


public class CertificateServiceImpl implements CertificateService {

    private final AccountProvider accountProvider;
    private final CertificatesApi api;

    CertificateServiceImpl(AccountProvider accountProvider, CertificatesApi api) {
        this.accountProvider = accountProvider;
        this.api = api;
    }

    @Override
    public CompletableFuture<QueryContext<CertificateList>> getCertificates(int skip, int take) {
        return api.getCertificates(accountProvider.accountId(), skip, take, false)
                .thenApply(contextAdaptor(QueryContext.CERTIFICATE_LIST));
    }

    @Override
    public CompletableFuture<QueryContext<CertificateList>> getCertificatesForAllUsers(int skip, int take) {
        return api.getCertificates(accountProvider.accountId(), skip, take, true)
                .thenApply(contextAdaptor(QueryContext.CERTIFICATE_LIST));
    }

    @Override
    public CompletableFuture<QueryContext<CertificateList>> getCertificateListAsync() {
        return api.get100Certificates(accountProvider.accountId())
                .thenApply(contextAdaptor(QueryContext.CERTIFICATE_LIST));
    }

    @Override
    public QueryContext<CertificateList> getCertificateList(QueryContext<?> parent) {
        return QueryContextUtils.join(getCertificateListAsync());
    }
}
