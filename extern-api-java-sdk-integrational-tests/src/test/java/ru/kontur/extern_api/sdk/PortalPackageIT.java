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

package ru.kontur.extern_api.sdk;


import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.utils.SystemProperty;
import ru.kontur.extern_api.sdk.utils.TestBaseIT;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.portal.CertificatesClient;
import ru.kontur.extern_api.sdk.portal.model.CertificateSearchResult;
import ru.kontur.extern_api.sdk.portal.model.SearchQuery;

@DisplayName("portal services tests")
class PortalPackageIT extends TestBaseIT {

    private static Configuration config = engine.getConfiguration();

    @BeforeAll
    static void init() {
        SystemProperty.push("httpclient.debug");
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
