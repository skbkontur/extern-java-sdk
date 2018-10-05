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

import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.adaptor.CertificatesAdaptor;
import ru.kontur.extern_api.sdk.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.DraftsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.OrganizationsAdaptor;
import ru.kontur.extern_api.sdk.httpclient.retrofit.RetrofitClient;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.EventsApi;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;

public class HttpClientBundle implements AdaptorBundle {

    private final ProviderHolder providerHolder;
    private final RetrofitClient retrofitClient;

    public HttpClientBundle(ProviderHolder providerHolder) {
        this.providerHolder = providerHolder;
        this.retrofitClient = new RetrofitClient(Level.BODY);
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
        String authSid = providerHolder.getAuthenticationProvider()
                .sessionId()
                .ensureSuccess()
                .get();

        EventsApi service = retrofitClient
                .setAuthSid(authSid)
                .setServiceBaseUrl(providerHolder.getServiceBaseUriProvider().getUri())
                .setApiKey(providerHolder.getApiKeyProvider().getApiKey())
                .setUserAgent(providerHolder.getUserAgentProvider().getUserAgent())
                .createService(EventsApi.class);

        return new EventsAdaptorImpl(service);
    }

    @Override
    public OrganizationsAdaptor getOrganizationsAdaptor() {
        OrganizationsAdaptorImpl organizationsAdaptor = new OrganizationsAdaptorImpl();
        organizationsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return organizationsAdaptor;
    }

    @Override
    public HttpClient getHttpClientAdaptor() {
        return new HttpClientImpl()
                .setUserAgentProvider(providerHolder.getUserAgentProvider());
    }
}
