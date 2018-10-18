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
package ru.kontur.extern_api.sdk;

import java.util.Optional;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;
import ru.kontur.extern_api.sdk.provider.ProviderHolderParent;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.ServicesFactory;

public class ExternEngine implements ProviderHolderParent<ProviderHolder> {

    private final ServicesFactory servicesFactory;
    private final ProviderHolder providerHolder;
    private final Configuration configuration;

    public ExternEngine(
            Configuration configuration,
            ProviderHolder providerHolder,
            ServicesFactory servicesFactory) {
        this.servicesFactory = servicesFactory;
        this.providerHolder = providerHolder;
        this.configuration = configuration;
    }

    /**
     * @return Configuration параметры среды выполненя
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * @return AccountService сервис предназначен для работы с учетными записями
     */
    public AccountService getAccountService() {
        return servicesFactory.getAccountService();
    }

    /**
     * @return CertificateService сервис предназначен для работы с сертификатами пользователей
     * @see CertificateService
     */
    public CertificateService getCertificateService() {
        return servicesFactory.getCertificateService();
    }

    /**
     * @return DocflowService сервис предназначен для работы с документоборотами
     * @see DocflowService
     */
    public DocflowService getDocflowService() {
        return servicesFactory.getDocflowService();
    }

    /**
     * @return DraftService сервис предназначен для работы с черновиками
     * @see DraftService
     */
    public DraftService getDraftService() {
        return servicesFactory.getDraftService();
    }

    /**
     * @return EventService сервис предназначен для получения ленты событий документооборота
     * @see EventService
     */
    public EventService getEventService() {
        return servicesFactory.getEventService();
    }

    /**
     * @return OrganizationService сервис предназначен для управления организациями (CRUD)
     * @see OrganizationService
     */
    public OrganizationService getOrganizationService() {
        return servicesFactory.getOrganizationService();
    }

    @Override
    public ProviderHolder getChildProviderHolder() {
        return providerHolder;
    }

    public HttpClient getHttpClient() {
        AuthenticationProvider auth = getAuthenticationProvider();
        String sessionId = auth.sessionId().ensureSuccess().getSessionId();
        return servicesFactory
                .getHttpClient()
                .acceptAccessToken(sessionId)
                .acceptApiKey(getApiKeyProvider().getApiKey());
    }

    public HttpClient getAuthorizedHttpClient() {
        HttpClient httpClient = servicesFactory.getHttpClient();

        Optional.ofNullable(getApiKeyProvider())
                .map(ApiKeyProvider::getApiKey)
                .ifPresent(httpClient::acceptApiKey);

        Optional.ofNullable(getAuthenticationProvider())
                .map(AuthenticationProvider::sessionId)
                .map(QueryContext::ensureSuccess)
                .ifPresent(cxt -> httpClient
                        .acceptAccessToken(cxt.getAuthPrefix(), cxt.getSessionId()));

        return httpClient;
    }
}
