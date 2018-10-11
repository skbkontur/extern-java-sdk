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

package ru.kontur.extern_api.sdk.scenario;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.crypt.CertificateWrapper;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.model.OrgFilter;


@Disabled
class BankScenario {

    private static ExternEngine engine;
    private static CryptoApi cryptoApi;

    @BeforeAll
    static void setUpClass() throws Exception {
        engine = TestSuite.Load().engine;
        cryptoApi = new CryptoApi();
    }

    @BeforeEach
    void setUp() {
        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @Test
    void main() throws Exception {
        SystemProperty.pop("httpclient.debug");

        List<Account> accounts = engine.getAccountService()
                .acquireAccountsAsync()
                .get()
                .getOrThrow()
                .getAccounts();

        Account account = accounts
                .get(0);

        engine.setAccountProvider(account::getId);

        System.out.printf("Found %s accounts\n", accounts.size());

        System.out.printf("Using account: %s inn=%s kpp=%s\n",
                account.getOrganizationName(),
                account.getInn(),
                account.getKpp());

        CertificateWrapper workingCert = findWorkingCerts().stream()
                .findFirst()
                .orElseThrow(AssertionError::new);

        System.out.printf("Using certificate: issuer %s and subject %s\n",
                workingCert.getIssuerFields().get("O"),
                workingCert.getSubjectFields().get("GIVENNAME")
        );

        List<Company> companies = engine.getOrganizationService()
                .searchAsync(OrgFilter.page(0, 1000))
                .get()
                .getOrThrow()
                .getCompanies();

        System.out.printf("Found %s organizations\n", companies.size());

        Company org = companies.get(1);
        CompanyGeneral general = org.getGeneral();

        System.out.printf("Using organization: %s inn=%s kpp=%s\n",
                general.getName(),
                general.getInn(),
                general.getKpp()
        );

        // todo hz
    }


    private List<CertificateWrapper> findWorkingCerts() throws Exception {

        List<CertificateWrapper> locals = cryptoApi.getCertificatesInstalledLocally();

        List<Certificate> remotes = engine
                .getCertificateService()
                .getCertificateListAsync()
                .get()
                .getOrThrow()
                .getCertificates();

        Set<String> remoteThumbprints = remotes.stream()
                .map(cert -> cryptoApi.getThumbprint(cert.getContent()))
                .collect(Collectors.toSet());

        List<CertificateWrapper> commonCerts = locals.stream()
                .filter(wrapper -> remoteThumbprints.contains(wrapper.getThumbprint()))
                .collect(Collectors.toList());

        System.out.printf(
                "Found %s certificates both registered in Kontur and installed locally\n",
                commonCerts.size()
        );

        return commonCerts;
    }
}
