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
import java.util.UUID;
import java.util.stream.Collectors;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.Zip;


class BankCloudDssTestScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private static TestSuite test;

    @BeforeAll
    static void setUpClass() {

        String dssConfigPath = "/secret/extern-sdk-dss-config.json";

        Configuration configuration = TestConfig.LoadConfigFromEnvironment(dssConfigPath);

        engine = ExternEngineBuilder.createExternEngine(configuration.getServiceBaseUri())
                .apiKey(configuration.getApiKey())
                .buildAuthentication(configuration.getAuthBaseUri(), ab -> ab
                        .withApiKey(configuration.getApiKey())
                        .passwordAuthentication(configuration.getLogin(), configuration.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(configuration.getAccountId())
                .build(Level.BASIC);
    }

    @Test
    void main() throws Exception {
        try {
            dssScenario();
        } catch (ApiException e) {
            System.err.println(e.prettyPrint());
            throw e;
        }
    }

    void dssScenario() {

        List<Certificate> cert = engine.getCertificateService().getCertificates(0, 10).join().getOrThrow()
                .getCertificates();
        System.out.println(cert.get(0).getFio());
    }
}
