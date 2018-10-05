/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;


class CertificateServiceTest {

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
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
    void getCertificatesTest() throws Exception {

        CertificateList certificateList = engine
                .getCertificateService()
                .getCertificateListAsync()
                .get()
                .ensureSuccess()
                .get();

        Assert.assertNotNull(certificateList);

        certificateList.getCertificates().forEach(certificate -> {
            Assert.assertNotNull(certificate.getInn());
            Assert.assertNotNull(certificate.getKpp());
        });

    }
}
