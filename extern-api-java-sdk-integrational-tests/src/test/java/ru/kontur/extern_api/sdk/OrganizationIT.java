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
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.OrganizationBatch;
import ru.kontur.extern_api.sdk.model.OrganizationGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.utils.TestSuite;

class OrganizationIT {

    private static final Organization ORGANIZATION = new Organization();

    private UUID companyId;

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {

        engine = TestSuite.Load().engine;
        OrganizationGeneral general = new OrganizationGeneral();
        general.setInn("7810654318");
        general.setKpp("781001001");
        general.setName("ASBEST, LLC");
        ORGANIZATION.setGeneral(general);
    }

    @BeforeEach
    void setUp() {

        this.companyId = createOrFindOrganisation();
        assertNotNull(companyId);
    }

    @AfterEach
    void tearDown() throws Exception {

        engine.getOrganizationService().deleteAsync(companyId).get();
        // please don't hurt me .^.
        //                       -
        Thread.sleep(5000);
    }

    @Test
    void testLookup() {

        QueryContext<Organization> companyCxt = engine.getOrganizationService()
                .lookupAsync(companyId)
                .join()
                .ensureSuccess();

        assertCompanyEquals(companyCxt.get());
    }

    @Test
    void testUpdate() {

        String newName = "Emerald";
        Organization organization = engine.getOrganizationService()
                .updateAsync(companyId, newName)
                .join()
                .getOrThrow();

        Assertions.assertEquals(organization.getGeneral().getName(), newName);
    }

    @Test
    void testSearch() {

        Organization organization = searchOrganisations(likeGiven())
                .getOrganizations()
                .get(0);

        assertNotNull(organization);
        assertCompanyEquals(organization);
    }

    @Test
    void testSearchAll() {

        OrgFilter innFilter = OrgFilter.maxPossibleBatch().inn(ORGANIZATION.getGeneral().getInn());
        OrganizationBatch organizationBatch = searchOrganisations(innFilter);

        for (Organization organization : organizationBatch.getOrganizations()) {
            assertNotNull(organization.getGeneral().getName());
        }
    }

    private OrganizationBatch searchOrganisations(OrgFilter filter) {

        return engine.getOrganizationService()
                .searchAsync(filter)
                .join()
                .getOrThrow();
    }

    private UUID createOrFindOrganisation() {
        List<Organization> companies = searchOrganisations(likeGiven())
                .getOrganizations();

        if (companies != null && !companies.isEmpty()) {
            return companies.get(0).getId();
        }

        Organization organization = engine.getOrganizationService()
                .createAsync(ORGANIZATION.getGeneral())
                .join()
                .getOrThrow();

        assertNotNull(organization);
        return organization.getId();
    }

    private static OrgFilter likeGiven() {
        return OrgFilter.maxPossibleBatch()
                .inn(ORGANIZATION.getGeneral().getInn())
                .kpp(ORGANIZATION.getGeneral().getKpp());
    }

    private static void assertCompanyEquals(Organization actual) {
        assertEquals(actual.getGeneral().getInn(), ORGANIZATION.getGeneral().getInn());
        assertEquals(actual.getGeneral().getKpp(), ORGANIZATION.getGeneral().getKpp());
        assertEquals(actual.getGeneral().getName(), ORGANIZATION.getGeneral().getName());
        assertNotNull(actual.getId());
    }
}
