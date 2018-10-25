package ru.kontur.extern_api.sdk.it;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestConfig;
import ru.kontur.extern_api.sdk.portal.CertificatesClient;
import ru.kontur.extern_api.sdk.portal.model.CertificateSearchResult;
import ru.kontur.extern_api.sdk.portal.model.SearchQuery;

@DisplayName("portal services tests")
class PortalPackageIT {

    private static Configuration config;

    @BeforeAll
    static void setUp() {
        SystemProperty.push("httpclient.debug");
        config = TestConfig.LoadConfigFromEnvironment();
    }

    @AfterAll
    static void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @Nested
    @DisplayName("Certificates client should")
    class CertificatesClientTest {

        CertificatesClient certClient = new CertificatesClient(
                config::getApiKey,
                () -> "http://api.testkontur.ru/certapi/");

        @Test
        @DisplayName("search certificates")
        void searchCertificates() throws Exception {

            SearchQuery query = SearchQuery.equal("thumbprint", config.getThumbprint());
            CertificateSearchResult search = certClient.searchCertificates(0, 10, query).get();

            Assertions.assertEquals(1, search.getTotalHits());
            Assertions.assertEquals(config.getThumbprint(),
                    search.getResults().get(0).getThumbprint());
        }

    }

}
