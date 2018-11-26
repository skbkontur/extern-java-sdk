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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.utils.TestSuite;

class OrganizationIT {

    private static final Company COMPANY = new Company();

    private UUID companyId;

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        CompanyGeneral general = new CompanyGeneral();
        general.setInn("7810654318");
        general.setKpp("781001001");
        general.setName("ASBEST, LLC");
        COMPANY.setGeneral(general);
    }

    @BeforeEach
    void setUp() throws Exception {
        this.companyId = createOrFindOrganisation();
        assertNotNull(companyId);
    }

    @AfterEach
    void tearDown() throws Exception {
        engine.getOrganizationService().deleteAsync(companyId).get();
        Thread.sleep(100);
    }

    @Test
    void testLookup() throws Exception {

        QueryContext<Company> companyCxt = engine.getOrganizationService()
                .lookupAsync(companyId)
                .get()
                .ensureSuccess();

        assertCompanyEquals(companyCxt.get(), COMPANY);
    }

    @Test
    void testUpdate() {
        String newName = "Emerald";
        Company company = engine.getOrganizationService()
                .updateAsync(companyId, newName)
                .join().getOrThrow();

        Assertions.assertEquals(company.getGeneral().getName(), newName);
    }

    @Test
    void testSearch() {
        Company company = searchOrganisations(likeGiven(COMPANY)).get(0);
        assertNotNull(company);
        assertCompanyEquals(company, COMPANY);
    }

    @Test
    void testSearchAll() {
        OrgFilter innFilter = OrgFilter.maxPossibleBatch().inn(COMPANY.getGeneral().getInn());
        for (Company company : searchOrganisations(innFilter)) {
            assertNotNull(company.getGeneral().getName());
        }
    }

    private List<Company> searchOrganisations(OrgFilter filter) {
        return engine.getOrganizationService()
                .searchAsync(filter)
                .join()
                .getOrThrow()
                .getCompanies();
    }

    private UUID createOrFindOrganisation() throws Exception {
        List<Company> companies = searchOrganisations(likeGiven(COMPANY));

        if (companies != null && !companies.isEmpty()) {
            return companies.get(0).getId();
        }

        Company company = engine.getOrganizationService()
                .createAsync(COMPANY.getGeneral())
                .join()
                .getOrThrow();

        assertNotNull(company);
        return company.getId();
    }

    private static OrgFilter likeGiven(Company company) {
        return OrgFilter.maxPossibleBatch()
                .inn(company.getGeneral().getInn())
                .kpp(company.getGeneral().getKpp());
    }

    private static void assertCompanyEquals(Company actual, Company expected) {
        assertEquals(actual.getGeneral().getInn(), expected.getGeneral().getInn());
        assertEquals(actual.getGeneral().getKpp(), expected.getGeneral().getKpp());
        assertEquals(actual.getGeneral().getName(), expected.getGeneral().getName());
        assertNotNull(actual.getId());
    }
}
