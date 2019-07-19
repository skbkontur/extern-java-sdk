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

import java.security.cert.CertificateException;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestSuite;


class BankCloudDssTestScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private static TestSuite test;
    private static Configuration configuration;
    private static CryptoApi cryptoApi;

    @BeforeAll
    static void setUpClass() throws CertificateException {

        String dssConfigPath = "/secret/extern-sdk-dss-config.json";

        configuration = TestConfig.LoadConfigFromEnvironment(dssConfigPath);

        engine = ExternEngineBuilder.createExternEngine(configuration.getServiceBaseUri())
                .apiKey(configuration.getApiKey())
                .buildAuthentication(configuration.getAuthBaseUri(), ab -> ab
                        .withApiKey(configuration.getApiKey())
                        .passwordAuthentication(configuration.getLogin(), configuration.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(configuration.getAccountId())
                .build(Level.BODY);

        cryptoApi = new CryptoApi();

        test = new TestSuite(engine, configuration);
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

    void dssScenario() throws Exception {

        senderCertificate = getDssCert();
        System.out.printf(
                "Using certificate: fio = %s, inn = %s, kpp = %s\n",
                senderCertificate.getFio(),
                senderCertificate.getInn(),
                senderCertificate.getKpp()
        );

        Account account = getAccount();
        engine.setAccountProvider(account::getId);
        System.out.printf(
                "Using account: %s inn = %s kpp = %s\n",
                account.getOrganizationName(),
                account.getInn(),
                account.getKpp()
        );

        Docflow docflow = sendDraftWithUsn(account, senderCertificate);

        System.out.println("Draft is sent. Long live the Docflow " + docflow.getId());

        //finishDocflow(docflow);

    }

    Account getAccount() throws ExecutionException, InterruptedException {

        return engine.getAccountService()
                .getAccountsAsync(0, 100)
                .get()
                .getOrThrow()
                .getAccounts()
                .stream()
                .filter(acc -> acc.getInn().equals(senderCertificate.getInn()))
                .collect(Collectors.toList())
                .get(0);
    }

    private Docflow sendDraftWithUsn(Account senderAcc, Certificate certificate)
            throws Exception {

        SenderRequest sender = new SenderRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                senderCertificate.getContent(),
                "8.8.8.8"
        );

        Recipient recipient = new FnsRecipient("0087");

        OrganizationRequest oPayer = new OrganizationRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                senderAcc.getOrganizationName()
        );

        UUID draftId = engine.getDraftService()
                .createAsync(sender, recipient, oPayer)
                .get()
                .getOrThrow();

        System.out.println("Draft created");

        UsnServiceContractInfo usn = PreparedTestData.usnV2(certificate, oPayer);

        DraftDocument document = engine.getDraftService()
                .createAndBuildDeclarationAsync(draftId, 2, usn)
                .get()
                .getOrThrow();

        System.out.println("Usn document built and added to draft");

        openDraftDocumentAsPdf(draftId, document.getId());
        cloudSignDraftWithDssCert(draftId);

        CheckResultData checkResult = engine.getDraftService()
                .checkAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(checkResult.hasNoErrors(), test.serialize(checkResult));
        System.out.println("Usn document has no errors");

        PrepareResult result = engine.getDraftService()
                .prepareAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(
                result.getStatus() == Status.OK ||
                        result.getStatus() == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS,
                test.serialize(result)
        );

        System.out.printf("Draft prepared to send: %s\n", result.getStatus());

        return engine.getDraftService()
                .sendAsync(draftId)
                .get()
                .getOrThrow();
    }

    private void openDraftDocumentAsPdf(UUID draftId, UUID documentId) throws Exception {
        engine.getDraftService()
                .getDocumentAsPdfAsync(draftId, documentId)
                .get()
                .getOrThrow();
    }

    private void cloudSignDraftWithDssCert(UUID draftId) throws Exception {

        SignInitiation signInitiation = engine.getDraftService()
                .cloudSignInitAsync(draftId)
                .get()
                .getOrThrow();

        assert (signInitiation.getConfirmType() == ConfirmType.MY_DSS);

        TaskState taskState;
        do {
            TaskInfo ti = new TaskInfo();
            ti.setId(UUID.fromString(signInitiation.getTaskId()));

            taskState = engine.getTaskService(draftId).getTaskStatus(ti).get();
            Thread.sleep(1000);

        } while (taskState == TaskState.RUNNING);

        engine.getDraftService().lookupAsync(draftId).join().getOrThrow().getDocuments()
                .stream()
                .map(link -> engine.getHttpClient().followGetLink(link.getHref(), DraftDocument.class))
                .map(draftDocument -> engine.getDraftService().getSignatureContentAsync(draftId, draftDocument.getId()).join().getOrThrow())
                .forEach(signature -> Assertions.assertTrue(signature.length > 0));
    }


    private Certificate getDssCert() throws Exception {

        return engine
                .getCertificateService()
                .getCertificates(0, 100)
                .get()
                .getOrThrow()
                .getCertificates()
                .stream()
                .filter(c -> cryptoApi.getThumbprint(c.getContent()).equals(configuration.getThumbprint()))
                .findFirst()
                .get();
    }
}
