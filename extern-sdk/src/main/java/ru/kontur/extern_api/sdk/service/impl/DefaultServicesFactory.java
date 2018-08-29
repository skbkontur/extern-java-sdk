/*
 * The MIT License
 *
 * Copyright 2018 alexs.
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

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.DefaultExtern;
import ru.kontur.extern_api.sdk.provider.AbstractProviderHolder;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.provider.useragent.DefaultUserAgentProvider;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.ServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.HttpClientBundle;

/**
 * @author alexs
 */
public class DefaultServicesFactory extends AbstractProviderHolder implements ServicesFactory {

    private final AdaptorBundle adaptorBundle;
    private final UserAgentProvider userAgentProvider;

    public DefaultServicesFactory(@NotNull AdaptorBundle adaptorBundle) {
        this.adaptorBundle = adaptorBundle;
        this.userAgentProvider = adaptorBundle.getHttpClientAdaptor().getUserAgentProvider();
        setServiceBaseUriProvider(() -> DefaultExtern.BASE_URL);
    }

    public DefaultServicesFactory() {
        this(new HttpClientBundle(new DefaultUserAgentProvider()));
    }

    @Override
    public AccountService getAccountService() {
        return this.copyProvidersTo(
                new AccountServiceImpl(this, adaptorBundle.getAccountsAdaptor()));
    }

    @Override
    public CertificateService getCertificateService() {
        return this.copyProvidersTo(
                new CertificateServiceImpl(this, adaptorBundle.getCertificatesAdaptor()));
    }

    @Override
    public DocflowService getDocflowService() {
        return this.copyProvidersTo(
                new DocflowServiceImpl(this, adaptorBundle.getDocflowsAdaptor()));
    }

    @Override
    public DraftService getDraftService() {
        return this.copyProvidersTo(
                new DraftServiceImpl(this, adaptorBundle.getDraftsAdaptor()));
    }

    @Override
    public EventService getEventService() {
        return this.copyProvidersTo(
                new EventServiceImpl(this, adaptorBundle.getEventsAdaptor()));
    }

    @Override
    public OrganizationService getOrganizationService() {
        return this.copyProvidersTo(
                new OrganizationServiceImpl(this, adaptorBundle.getOrganizationsAdaptor()));
    }

    @Override
    public HttpClient getHttpClient() {
        return adaptorBundle
                .getHttpClientAdaptor()
                .setServiceBaseUri(getServiceBaseUriProvider().getUri())
                .setUserAgentProvider(userAgentProvider);
    }

}
