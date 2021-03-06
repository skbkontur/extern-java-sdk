/*
 * Copyright (c) 2019 SKB Kontur
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
 */

package ru.kontur.extern_api.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.model.warrants.OrganizationWarrantInformation;
import ru.kontur.extern_api.sdk.model.warrants.Warrant;
import ru.kontur.extern_api.sdk.model.warrants.WarrantList;
import ru.kontur.extern_api.sdk.utils.TestSuite;

public class WarrantsIT {
    private static ExternEngine engine;
    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
    }

    @Test
    void testGetWarrants() {

        WarrantList warrantList = engine.getWarrantService()
                .getWarrantsForAllUsers(0, 10)
                .join();

        int warrantsCount = warrantList.getOrganizationWarrantInformations().size();
        assertTrue(warrantsCount > 0);
        assertEquals(0, warrantList.getSkip());
        assertEquals(warrantsCount, warrantList.getTake());

        for (OrganizationWarrantInformation warrantInfo : warrantList.getOrganizationWarrantInformations()) {
            assertNotNull(warrantInfo.getOrganizationId());
            assertNotNull(warrantInfo.getOrganizationName());
            if (warrantInfo.getWarrant() == null)
                continue;
            assertNotNull(warrantInfo.getWarrant().getIssuer());
            assertNotNull(warrantInfo.getWarrant().getIssuer().getIssuerOrganization());
            assertNotNull(warrantInfo.getWarrant().getIssuer().getIssuerOrganization().getName());
            assertNotNull(warrantInfo.getWarrant().getDateBegin());
        }
        assertTrue(warrantList.getOrganizationWarrantInformations().stream().anyMatch(w -> w.getWarrant() != null));
    }
}
