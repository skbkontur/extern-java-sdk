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

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.KonturHttpClient;
import ru.kontur.extern_api.sdk.httpclient.api.*;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;
import ru.kontur.extern_api.sdk.service.*;
import ru.kontur.extern_api.sdk.service.builders.DraftsBuilderServiceFactory;
import ru.kontur.extern_api.sdk.service.impl.builders.CommonRetrofitDraftsBuilderFactory;
import ru.kontur.extern_api.sdk.service.impl.builders.DraftsBuilderServiceFactoryImpl;
import ru.kontur.extern_api.sdk.service.impl.builders.fns_inventory.FnsInventoryDraftsBuilderApiFactory;
import ru.kontur.extern_api.sdk.service.impl.builders.pfr_report.PfrReportDraftsBuilderApiFactory;

import java.util.UUID;
import java.util.function.Supplier;


public class DefaultServicesFactory implements ServicesFactory {

    private final ProviderHolder providerHolder;
    private final KonturConfiguredClient configuredClient;

    public DefaultServicesFactory(KonturConfiguredClient client, ProviderHolder providerHolder) {
        this.providerHolder = providerHolder;
        this.configuredClient = client;
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
    public WarrantService getWarrantService() {
        return new WarrantServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(WarrantsApi.class)
        );
    }

    @Override
    public DocflowService getDocflowService() {
        return new DocflowServiceImpl(
                providerHolder.getAccountProvider(),
                providerHolder.getUserIPProvider(),
                createApi(DocflowsApi.class)
        );
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
    public ContentService getContentService() {
        return new ContentServiceImpl(createApi(ContentApi.class), providerHolder.getAccountProvider());
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
        return new KonturHttpClient(postConfigure(configuredClient).copy(), GsonProvider.LIBAPI);
    }

    @Override
    public DraftTaskService getTaskService(@NotNull UUID draftId) {
        return new DraftTaskServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(DraftsApi.class),
                draftId
        );
    }

    @Override
    public DraftsBuilderServiceFactory getDraftsBuilderService() {
        KonturConfiguredClient client = postConfigure(configuredClient);

        CommonRetrofitDraftsBuilderFactory commonFactory = new CommonRetrofitDraftsBuilderFactory(client);
        FnsInventoryDraftsBuilderApiFactory fnsInventoryFactory = new FnsInventoryDraftsBuilderApiFactory(
                client,
                commonFactory
        );
        PfrReportDraftsBuilderApiFactory pfrReportFactory = new PfrReportDraftsBuilderApiFactory(
                client,
                commonFactory
        );
        return new DraftsBuilderServiceFactoryImpl(
                providerHolder.getAccountProvider(),
                fnsInventoryFactory,
                pfrReportFactory
        );
    }

    @Override
    public RelatedDocumentsService getRelatedDocumentsService(
            UUID relatedDocflowId,
            UUID relatedDocumentId
    ) {
        return new RelatedDocumentsServiceImpl(
                providerHolder.getAccountProvider(),
                providerHolder.getUserIPProvider(),
                createApi(RelatedDocflowApi.class),
                relatedDocflowId,
                relatedDocumentId
        );
    }

    @Override
    public RelatedDocumentsService getRelatedDocumentsService(
            Docflow relatedDocflow,
            Document relatedDocumentId
    ) {
        return new RelatedDocumentsServiceImpl(
                providerHolder.getAccountProvider(),
                providerHolder.getUserIPProvider(),
                createApi(RelatedDocflowApi.class),
                relatedDocflow.getId(),
                relatedDocumentId.getId()
        );
    }

    @Override
    public ReplyTaskService getReplyTaskService(UUID docflowId, UUID documentId, UUID replyId) {

        return new ReplyTaskServiceImpl(
                providerHolder.getAccountProvider(),
                createApi(RepliesApi.class),
                docflowId,
                documentId,
                replyId
        );
    }

    private KonturConfiguredClient postConfigure(KonturConfiguredClient client) {
        Supplier<String> authSidProvider = () -> providerHolder.getAuthenticationProvider()
                .sessionId()
                .getOrThrow();

        return client
                .setAuthSidSupplier(authSidProvider)
                .setServiceBaseUrl(providerHolder.getServiceBaseUriProvider().getUri())
                .setApiKeySupplier(providerHolder.getApiKeyProvider()::getApiKey)
                .setUserAgentSupplier(providerHolder.getUserAgentProvider()::getUserAgent);
    }

    private <T> T createApi(Class<T> apiType) {
        return postConfigure(configuredClient).createApi(apiType);
    }
}
