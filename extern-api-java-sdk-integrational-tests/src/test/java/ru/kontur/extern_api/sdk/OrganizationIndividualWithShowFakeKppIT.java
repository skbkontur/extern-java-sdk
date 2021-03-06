package ru.kontur.extern_api.sdk;/*
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.UUID;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.OrgFilter;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;


@Execution(ExecutionMode.SAME_THREAD)
class OrganizationIndividualWithShowFakeKppIT {

    private static final Company COMPANY = new Company();

    private UUID companyId;

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        Configuration config = TestConfig.LoadConfigFromEnvironment();
        engine = TestSuite.LoadManually((cfg, builder) -> builder
                .buildAuthentication(cfg.getAuthBaseUri(), authBuilder -> authBuilder
                        .passwordAuthentication(config.getLoginSecond(), config.getPassSecond())
                )
                .doNotUseCryptoProvider()
                .doNotSetupAccount()
                .build(Level.BODY)
        ).engine;

        CompanyGeneral general = new CompanyGeneral();
        general.setInn("266061768316"); // Org with fake INN-KPP
        general.setName("TEST Individual Person Petrov OrganizationIndividualIT, LLC");
        COMPANY.setGeneral(general);
    }

    @BeforeEach
    void setUp() throws Exception {
        try {
            this.companyId = createOrFindOrganization();
        } catch (Exception ex) {
            throw new Exception("SetUp failed! Cant' create or find organization!", ex);
        }
        assertNotNull(companyId);
    }

    @Test
    void testLookup() {

        QueryContext<Company> companyCxt = engine.getOrganizationService()
                .lookupAsync(companyId)
                .join()
                .ensureSuccess();

        assertCompanyIsCorrect(companyCxt.get());
    }

    @Test
    void testSearch() {
        Company company = searchOrganisations(likeGiven(COMPANY)).get(0);
        assertNotNull(company);
        assertCompanyIsCorrect(company);
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

    private UUID createOrFindOrganization() throws Exception {
        List<Company> companies = searchOrganisations(likeGiven(COMPANY));
        if (companies != null && !companies.isEmpty()) {
            return companies.get(0).getId();
        }
        throw new Exception("Cant't create or find test org");
    }

    private static OrgFilter likeGiven(Company company) {
        return OrgFilter.maxPossibleBatch()
                .inn(company.getGeneral().getInn())
                .kpp(company.getGeneral().getKpp());
    }

    private static void assertCompanyIsCorrect(Company actual) {
        assertEquals("266061768316", actual.getGeneral().getInn());
        assertEquals("266000000",
                     actual.getGeneral().getKpp(),
                     "Т.к. включен показ фейковых КПП для ИП в Adjustment eUserId==gO a5e12a0d-6e15-46fc-9080-ba5b27c66137 куда входит тестовый Михайлов Николай Богданович"
        );
        assertEquals(
                "TEST Individual Person Petrov OrganizationIndividualIT, LLC",
                actual.getGeneral().getName()
        );
        assertNotNull(actual.getId());
    }
}
