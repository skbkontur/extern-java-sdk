package ru.kontur.extern_api.sdk.portal;

import java.util.concurrent.CompletableFuture;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.httpclient.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.portal.model.CertificateFields.Names;
import ru.kontur.extern_api.sdk.portal.model.CertificateSearchResult;
import ru.kontur.extern_api.sdk.portal.model.SearchQuery;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;


/**
 * Kontur certificates api.
 */
public class CertificatesClient {

    private final CertificatesApi api;

    public CertificatesClient(ApiKeyProvider apiKeyProvider, UriProvider serviceBaseUrlProvider) {
        this(apiKeyProvider, serviceBaseUrlProvider, Level.BASIC);
    }

    public CertificatesClient(ApiKeyProvider apiKey, UriProvider serviceBaseUrl, Level logLevel) {

        KonturConfiguredClient client = new KonturConfiguredClient(
                logLevel,
                serviceBaseUrl.getUri()
        ).setApiKeySupplier(apiKey::getApiKey);

        api = client.createApi(CertificatesApi.class);

    }

    /**
     * Search certificates  by specified search query
     *
     * @param skip pagination skip
     * @param take pagination take
     * @param query search query. See {@link SearchQuery}
     * @return paginated certificate search result (thumbprints only)
     */
    public CompletableFuture<CertificateSearchResult> searchCertificates(int skip, int take, SearchQuery query) {
        return api.search(skip, take, query, Names.of("thumbprint"));
    }

}
