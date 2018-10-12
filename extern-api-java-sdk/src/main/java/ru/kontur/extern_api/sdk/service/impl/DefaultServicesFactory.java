/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur.
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
package ru.kontur.extern_api.sdk.service.impl;

import java.util.concurrent.TimeUnit;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.retrofit.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.AccountsApi;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.CertificatesApi;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.DraftsApi;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.EventsApi;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.OrganizationsApi;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.ServicesFactory;


public class DefaultServicesFactory implements ServicesFactory {

    private final ProviderHolder providerHolder;
    private final AdaptorBundle adaptorBundle;
    private final KonturConfiguredClient configuredClient;

    public DefaultServicesFactory(
            ProviderHolder providerHolder,
            AdaptorBundle adaptorBundle) {
        this.providerHolder = providerHolder;
        this.adaptorBundle = adaptorBundle;
        // todo: inject
        this.configuredClient = new KonturConfiguredClient(Level.NONE)
            .setReadTimeout(60, TimeUnit.SECONDS);
    }


    @Override
    public AccountService getAccountService() {
        return new AccountServiceImpl(createApi(AccountsApi.class));
    }

    @Override
    public CertificateService getCertificateService() {
        return new CertificateServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(CertificatesApi.class)
        );
    }

    @Override
    public DocflowService getDocflowService() {
        return providerHolder.copyProvidersTo(new DocflowServiceImpl(
                providerHolder,
                adaptorBundle.getDocflowsAdaptor()
        ));
    }

    @Override
    public DraftService getDraftService() {
        return new DraftServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(DraftsApi.class)
        );
    }

    @Override
    public EventService getEventService() {
        return new EventServiceImpl(createApi(EventsApi.class));
    }

    @Override
    public OrganizationService getOrganizationService() {
        return new OrganizationServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(OrganizationsApi.class)
        );
    }

    @Override
    public HttpClient getHttpClient() {
        return adaptorBundle
                .getHttpClientAdaptor()
                .setServiceBaseUri(providerHolder.getServiceBaseUriProvider().getUri())
                .setUserAgentProvider(providerHolder.getUserAgentProvider());
    }

    private <T> T createApi(Class<T> apiType) {
        String authSid = providerHolder.getAuthenticationProvider()
                .sessionId()
                .ensureSuccess()
                .get();

        return configuredClient
                .setAuthSid(authSid)
                .setServiceBaseUrl(providerHolder.getServiceBaseUriProvider().getUri())
                .setApiKey(providerHolder.getApiKeyProvider().getApiKey())
                .setUserAgent(providerHolder.getUserAgentProvider().getUserAgent())
                .createService(apiType);
    }
}
