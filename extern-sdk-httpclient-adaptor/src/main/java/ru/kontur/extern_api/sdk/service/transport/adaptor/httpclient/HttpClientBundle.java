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
 *
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.service.transport.adaptor.CertificatesAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.OrganizationsAdaptor;

public class HttpClientBundle implements AdaptorBundle {

    private final UserAgentProvider userAgentProvider;

    public HttpClientBundle(UserAgentProvider userAgentProvider) {
        this.userAgentProvider = userAgentProvider;
    }

    @Override
    public AccountsAdaptor getAccountsAdaptor() {
        AccountsAdaptorImpl accountsAdaptor = new AccountsAdaptorImpl();
        accountsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return accountsAdaptor;
    }

    @Override
    public CertificatesAdaptor getCertificatesAdaptor() {
        CertificatesAdaptorImpl certificatesAdaptor = new CertificatesAdaptorImpl();
        certificatesAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return certificatesAdaptor;
    }

    @Override
    public DocflowsAdaptor getDocflowsAdaptor() {
        DocflowsAdaptorImpl docflowsAdaptor = new DocflowsAdaptorImpl();
        docflowsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return docflowsAdaptor;
    }

    @Override
    public DraftsAdaptor getDraftsAdaptor() {
        DraftsAdaptorImpl draftsAdaptor = new DraftsAdaptorImpl();
        draftsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return draftsAdaptor;
    }

    @Override
    public EventsAdaptor getEventsAdaptor() {
        EventsAdaptorImpl eventsAdaptor = new EventsAdaptorImpl();
        eventsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return eventsAdaptor;
    }

    @Override
    public OrganizationsAdaptor getOrganizationsAdaptor() {
        OrganizationsAdaptorImpl organizationsAdaptor = new OrganizationsAdaptorImpl();
        organizationsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return organizationsAdaptor;
    }

    @Override
    public HttpClient getHttpClientAdaptor() {
        return new HttpClientImpl().setUserAgentProvider(userAgentProvider);
    }
}
