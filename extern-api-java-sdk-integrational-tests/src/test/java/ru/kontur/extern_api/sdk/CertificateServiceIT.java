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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk;

import static org.hamcrest.MatcherAssert.assertThat;

import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.utils.TestSuite;

@Execution(ExecutionMode.CONCURRENT)
class CertificateServiceIT {

    private static CertificateService certificateService;

    @BeforeAll
    static void setUpClass() {
        certificateService = TestSuite.Load().engine.getCertificateService();
    }

    @Test
    void getCertificatesTest() {

        CertificateList certificateList = certificateService
                .getCertificates(0, 100)
                .join()
                .ensureSuccess()
                .get();

        Assertions.assertNotNull(certificateList);

        certificateList.getCertificates().forEach(certificate -> {
            Assertions.assertNotNull(certificate.getInn());
            Assertions.assertNotNull(certificate.getKpp());
        });

    }

    @Test
    void getCertificatesPageTest() {

        List<Certificate> l12 = certificateService
                .getCertificates(0, 2).join().getOrThrow().getCertificates();

        List<Certificate> l2 = certificateService
                .getCertificates(1, 1).join().getOrThrow().getCertificates();

        Assertions.assertEquals(2, l12.size());
        Assertions.assertEquals(1, l2.size());
        Assertions.assertEquals(l2.get(0).getContent(), l12.get(1).getContent());
    }

    @Test
    @Disabled
    void getCertificateForAllUsersTest() {
        List<Certificate> forAllUsers = certificateService
                .getCertificatesForAllUsers(0, 100)
                .join()
                .getOrThrow()
                .getCertificates();

        Certificate[] localCertificates = certificateService
                .getCertificatesForAllUsers(0, 100)
                .join()
                .getOrThrow()
                .getCertificates()
                .toArray(new Certificate[0]);

        assertThat(forAllUsers, Matchers.contains(localCertificates));
    }
}
