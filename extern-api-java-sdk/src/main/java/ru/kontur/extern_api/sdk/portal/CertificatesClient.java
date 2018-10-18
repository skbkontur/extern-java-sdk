package ru.kontur.extern_api.sdk.portal;

import java.util.HashMap;
import java.util.Map;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.httpclient.KonturHttpClient;
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

    private final ApiKeyProvider apiKeyProvider;
    private final UriProvider serviceBaseUrlProvider;
    private final HttpClient httpClient;

    public CertificatesClient(ApiKeyProvider apiKeyProvider, UriProvider serviceBaseUrlProvider) {
        this(apiKeyProvider, serviceBaseUrlProvider, Level.BASIC);
    }

    public CertificatesClient(ApiKeyProvider apiKey, UriProvider serviceBaseUrl, Level logLevel) {
        this.apiKeyProvider = apiKey;
        this.serviceBaseUrlProvider = serviceBaseUrl;

        ProviderSuite providerHolder = new ProviderSuite();
        providerHolder.setUserAgentProvider(new DefaultUserAgentProvider());
        this.httpClient = new KonturHttpClient(new KonturConfiguredClient(
                logLevel,
                serviceBaseUrl.getUri(),
                GsonProvider.getPortalCompatibleGson()
        ));
    }

    /**
     * Search certificates by a specified search query
     *
     * @param skip pagination skip
     * @param take pagination take
     * @param query search query. If you don't know syntax see
     *         {@link CertificatesClient#searchCertificates(int, int, SearchQuery)}
     * @return paginated certificate search result (thumbprints only)
     */
    public CertificateSearchResult searchCertificates(int skip, int take, String query) {
        String url = serviceBaseUrlProvider.getUri() + "/certificates/search";

        take = Math.max(0, Math.min(take, 50000));

        Map<String, Object> queryParams = new HashMap<>();
        queryParams.put("api-key", apiKeyProvider.getApiKey());
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
