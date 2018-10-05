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

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.it.utils.AbstractTest;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyBatch;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;

class OrganizationTest extends AbstractTest {

    private static final String INN_1 = "7810654318";
    private static final String INN_2 = "7810654325";
    private static final String INN_3 = "7810654332";
    private static final String INN_4 = "7810654340";
    private static final String INN_5 = "7810654357";
    private static final String KPP = "781001001";
    private static final String NAME = "ASBEST, LLC";
    private static final String NAME_NEW = "NEW ASBEST, LLC";

    @BeforeAll
    static void setUpClass() {
        AbstractTest.initEngine();
    }

    @BeforeEach
    void setUp() {
        organizationService = engine.getOrganizationService();
    }

    @Test
    void testCreate() throws Exception {
        String companyId = create(INN_1);
        organizationService.deleteAsync(companyId).get().ensureSuccess();
    }

    @Test
    void testLookup() throws Exception {
        String companyId = create(INN_2);

        QueryContext<Company> companyCxt = organizationService
                .lookupAsync(companyId)
                .get()
                .ensureSuccess();

        assertNotNull(companyCxt.getCompany());
        organizationService.deleteAsync(companyId).get().ensureSuccess();
    }

    @Test
    void testUpdate() throws Exception {
        System.out.println("update an organization: PUT /v1/{accountId}/organizations/{orgId}");

        String companyId = create(INN_3);

        QueryContext<Company> updateCxt = organizationService
                .updateAsync(companyId, NAME_NEW)
                .get()
                .ensureSuccess();

        Company c = updateCxt.getCompany();

        assertNotNull(c);
        assertNotNull(c.getGeneral());
        assertEquals(NAME_NEW, c.getGeneral().getName());

        organizationService.deleteAsync(companyId).get().ensureSuccess();
    }

    @Test
    void testDelete() throws Exception {
        String companyId = create(INN_4);

        organizationService.deleteAsync(companyId)
                .get()
                .ensureSuccess();
    }

    @Test
    void testSearch() throws Exception {
        String companyId = create(INN_5);
        assertNotNull(search(INN_5));
        organizationService.deleteAsync(companyId).get();
    }

    private String search(String inn) throws Exception {
        QueryContext<CompanyBatch> batchCxt = organizationService.searchAsync(inn, KPP, null, null)
                .get();

        batchCxt.ensureSuccess();

        if (!batchCxt.get().getCompanies().isEmpty()) {
            return batchCxt.get().getCompanies().get(0).getId().toString();
        }

        return null;
    }

    private String create(String inn) throws Exception {
        String companyId = search(inn);
        if (companyId == null) {
            CompanyGeneral c = new CompanyGeneral();
            c.setInn(inn);
            c.setKpp(KPP);
            c.setName(NAME);
            QueryContext<Company> companyCxt = organizationService.createAsync(c).get();
            companyCxt.ensureSuccess();
            companyId = companyCxt.get().getId().toString();
        }
        assertNotNull(companyId);
        return companyId;
    }
}
