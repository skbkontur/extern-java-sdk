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
package ru.kontur.extern_api.sdk.it;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;

class OrganizationIT {

    private static final String INN = "7810654318";
    private static final String KPP = "781001001";
    private static final String NAME = "ASBEST, LLC";
    private static final String NAME_NEW = "NEW ASBEST, LLC";

    private String companyId = null;

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
    }

    @BeforeEach
    void setUp() throws Exception {

        this.companyId = createOrFindOrganisation();
        assertNotNull(companyId);
    }

    @AfterEach
    void tearDown() throws Exception {
        engine.getOrganizationService().deleteAsync(companyId).get();
    }

    @Test
    void testLookup() throws Exception {

        QueryContext<Company> companyCxt = engine.getOrganizationService()
                .lookupAsync(companyId)
                .get()
                .ensureSuccess();

        checkFields(companyCxt.get(), NAME);
    }

    @Test
    void testUpdate() throws Exception {

        QueryContext<Company> updateCxt = engine.getOrganizationService()
                .updateAsync(companyId, NAME_NEW)
                .get()
                .ensureSuccess();

        checkFields(updateCxt.getCompany(), NAME_NEW);
    }

    @Test
    void testSearch() throws Exception {

        Company company = searchOrganisations(OrgFilter.maxPossibleBatch().inn(INN).kpp(KPP))
                .get(0);
        assertNotNull(company);
        checkFields(company, NAME);
    }

    @Test
    void testSearchAll() throws Exception {

        for (Company company : searchOrganisations(OrgFilter.maxPossibleBatch().inn(INN))) {
            assertNotNull(company.getGeneral().getName());
        }
    }

    private List<Company> searchOrganisations(OrgFilter filter) throws Exception {

        QueryContext<CompanyBatch> batchCxt = engine.getOrganizationService()
                .searchAsync(filter)
                .get()
                .ensureSuccess();

        return !batchCxt.get().getCompanies().isEmpty()
                ? batchCxt.get().getCompanies()
                : Collections.emptyList();
    }


    private String createOrFindOrganisation() throws Exception {

        List<Company> companies = searchOrganisations(
                OrgFilter.maxPossibleBatch().inn(INN).kpp(KPP));

        if (companies != null && !companies.isEmpty()) {
            return companies.get(0).getId().toString();
        }

        CompanyGeneral companyGeneral = new CompanyGeneral();
        companyGeneral.setInn(INN);
        companyGeneral.setKpp(KPP);
        companyGeneral.setName(NAME);
        QueryContext<Company> companyCxt = engine.getOrganizationService()
                .createAsync(companyGeneral)
                .get()
                .ensureSuccess();

        Company company = companyCxt.get();

        assertNotNull(company);
        return company.getId().toString();
    }

    private void checkFields(Company company, String name) {

        assertEquals(company.getGeneral().getInn(), INN);
        assertEquals(company.getGeneral().getKpp(), KPP);
        assertEquals(company.getGeneral().getName(), name);
        assertNotNull(company.getId());
    }
}
