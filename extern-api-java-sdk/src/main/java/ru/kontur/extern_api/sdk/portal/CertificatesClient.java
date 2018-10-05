package ru.kontur.extern_api.sdk.portal;

import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.HttpClientBundle;
import java.util.HashMap;
import java.util.Map;
import ru.kontur.extern_api.sdk.portal.model.CertificateSearchResult;
import ru.kontur.extern_api.sdk.portal.model.SearchQuery;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.ProviderSuite;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.provider.useragent.DefaultUserAgentProvider;


/**
 * Kontur certificates api.
 */
public class CertificatesClient {

    private final ApiKeyProvider apiKeyPorvider;
    private final UriProvider serviceBaseUrlProvider;
    private final HttpClient httpClient;

    public CertificatesClient(ApiKeyProvider apiKeyPorvider, UriProvider serviceBaseUrlProvider) {
        this.apiKeyPorvider = apiKeyPorvider;
        this.serviceBaseUrlProvider = serviceBaseUrlProvider;

        ProviderSuite providerHolder = new ProviderSuite();
        providerHolder.setUserAgentProvider(new DefaultUserAgentProvider());
        this.httpClient = new HttpClientBundle(providerHolder).getHttpClientAdaptor();
    }

    /**
     * @deprecated typo :c.
     *         use {@link CertificatesClient#searchCertificates(int, int, String)} instead
     */
    @Deprecated
    public CertificateSearchResult searchCeritficates(int skip, int take, String query) {
        return searchCertificates(skip, take, query);
    }

    /**
     * @deprecated typo :c.
     *         use {@link CertificatesClient#searchCertificates(int, int, SearchQuery)} instead
     */
    @Deprecated
    public CertificateSearchResult searchCeritficates(int skip, int take, SearchQuery query) {
        return searchCertificates(skip, take, query);
    }

    /**
     * Search certificates by a specified search query
     *
     * @param skip pagination skip
     * @param take pagination take
     * @param query search query. If you don't know syntax see
     *         {@link CertificatesClient#searchCeritficates(int, int, SearchQuery)}
     * @return paginated certificate search result (thumbprints only)
     */
    public CertificateSearchResult searchCertificates(int skip, int take, String query) {
        String url = serviceBaseUrlProvider.getUri() + "/certificates/search";

        take = Math.min(take, 50000);

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("api-key", apiKeyPorvider.getApiKey());
        queryParams.put("skip", skip);
        queryParams.put("take", take);
        queryParams.put("filter", query);
        queryParams.put("fields", "thumbprint");

        return httpClient.followGetLink(url, queryParams, CertificateSearchResult.class);
    }

    /**
     * Search certificates  by specified search query
     *
     * @param skip pagination skip
     * @param take pagination take
     * @param query search query. See {@link SearchQuery}
     * @return paginated certificate search result (thumbprints only)
     */
    public CertificateSearchResult searchCertificates(int skip, int take, SearchQuery query) {
        return searchCertificates(skip, take, query.translate());
    }

}
