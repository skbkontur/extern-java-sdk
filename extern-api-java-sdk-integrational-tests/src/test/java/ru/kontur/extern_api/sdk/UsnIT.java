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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Stream;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.DraftMetaRequest;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.CertificateResource;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.Resources;
import ru.kontur.extern_api.sdk.utils.SystemProperty;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@DisplayName("Draft service should")
@Execution(ExecutionMode.CONCURRENT)
class UsnIT {

    private static DraftService draftService;
    private static DraftMetaRequest draftMeta;

    @BeforeAll
    static void init() {
        Configuration config = TestConfig.LoadConfigFromEnvironment();
        ExternEngine ee = ExternEngineBuilder
                .createExternEngine(config.getServiceBaseUri())
                .apiKey(config.getApiKey())
                .buildAuthentication(config.getAuthBaseUri(), builder -> builder.
                        passwordAuthentication(config.getLogin(), config.getPass())
                )
                .cryptoProvider(new CryptoProviderMSCapi())
                .accountId(config.getAccountId())
                .build(Level.BODY);

        draftService = ee.getDraftService();
        String cert = CertificateResource.readBase64(config.getThumbprint());
        draftMeta = Arrays
                .stream(TestUtils.getTestData(cert))
                .filter(td -> td.getClientInfo().getRecipient().getIfnsCode() != null)
                .findAny()
                .map(TestUtils::toDraftMetaRequest)
                .orElseThrow(() -> new RuntimeException("no suitable data for usn tests"));
    }

    @BeforeEach
    void setUp() {
        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @TestFactory
    @DisplayName("allow to create a valid usn")
    Stream<DynamicTest> usnTests() {
        return Stream.of(
                dynamicTest("V1 from file", () -> checkUsn(1, loadUsn("/docs/USN/usn.json"))),
                dynamicTest("V1 from dto NOT_SUPPORTED ???", System.out::println),
                dynamicTest("V2 from file", () -> checkUsn(2, loadUsn("/docs/USN/usnV2.json"))),
                dynamicTest("V2 from dto", () -> checkUsn(2, PreparedTestData.usnV2()))
        );
    }

    private void checkUsn(int version, UsnServiceContractInfo usn) {

        UUID draftId = draftService
                .createAsync(draftMeta)
                .join()
                .getOrThrow()
                .getId();

        draftService.createAndBuildDeclarationAsync(draftId, version, usn)
                .thenApply(QueryContext::getOrThrow)
                .join();

        draftService.checkAsync(draftId)
                .thenApply(QueryContext::getOrThrow)
                .thenAccept(UsnIT::assertCheckHasNoErrors)
                .join();
    }


    private static void assertCheckHasNoErrors(CheckResultData checkResult) {
        assertTrue(checkResult.hasNoErrors());
    }

    private static UsnServiceContractInfo loadUsn(String path) {
        return Resources.loadFromJson(path, UsnServiceContractInfo.class);
    }

}
