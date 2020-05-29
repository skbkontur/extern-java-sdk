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
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.pfr.PfrRecipient;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.DocType;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.TestUtils;

/**
 * Тест на сценарий отправки отчета PfrV2 СЗВ-ТД c Dss
 */
@Execution(ExecutionMode.CONCURRENT)
class PfrReportV2DssTestScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
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
                .build(Level.BASIC);

        cryptoApi = new CryptoApi();
    }

    @Test
    void main() throws Exception {
        try {
            scenario();
        } catch (ApiException e) {
            System.err.println(e.prettyPrint());
            throw e;
        }
    }

    void scenario() throws Exception {

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

        Docflow pfrDocflow = sendPfrReport(account, engine).get().getOrThrow();
        System.out.println("Draft is sent. Long live the Docflow " + pfrDocflow.getId());

        finishDocflow(pfrDocflow);

        Docflow pfrDocflowFinished = engine.getDocflowService()
                .lookupDocflowAsync(pfrDocflow.getId().toString())
                .get()
                .getOrThrow();

        for (Document document : pfrDocflowFinished.getDocuments()) {
            System.out.println("Will process contents " + document.getDescription().getType());
            if (document.hasEncryptedContent()) {
                byte[] pfrServiceDocumentContent = engine.getDocflowService().getEncryptedContentAsync(
                        pfrDocflowFinished.getId(),
                        document.getId()
                ).join().getOrThrow();
                System.out.println(
                        "Service Document " + document.getId() + " encrypted content received, len bytes ="
                                + pfrServiceDocumentContent.length);

                decryptDocumentWithDss(pfrDocflow.getId(), document.getId());
            }

            if (document.hasDecryptedContent()) {
                byte[] decryptedContent = engine.getDocflowService().getDecryptedContentAsync(
                        pfrDocflowFinished.getId(),
                        document.getId()
                ).join().getOrThrow();

                System.out.println("Document " + document.getId() + "content received, len bytes ="
                                           + decryptedContent.length);
            }
        }
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

    private byte[] decryptDocumentWithDss(UUID docflowId, UUID documentId)
            throws Exception {

        DecryptInitiation decryptInitiation = engine.getDocflowService().cloudDecryptDocumentInitAsync(
                docflowId,
                documentId,
                Base64.getDecoder().decode(senderCertificate.getContent()),
                true
        )
                .get().getOrThrow();

        Assertions.assertEquals(ConfirmType.MY_DSS, decryptInitiation.getConfirmType());
        TaskInfo<DecryptDocumentResultContent> taskInfo;
        do {
            taskInfo = engine.getDocflowService().getDecryptTaskResult(
                    docflowId,
                    documentId,
                    UUID.fromString(decryptInitiation.getDecryptionTaskId())
            ).get();

            Thread.sleep(2000);
            System.out.println("Waiting for user to confirm dss operation...");
        } while (taskInfo.getTaskState() == TaskState.RUNNING);

        if (taskInfo.getTaskResult() == null) {
            throw new Exception(
                    "Crypt operation completed, but does not have a result. TaskId = " + taskInfo.getId());
        }

        DecryptDocumentResultContent documentResultContent = (DecryptDocumentResultContent) taskInfo
                .getTaskResult();
        UUID contentId = documentResultContent.getContentId();
        byte[] content = engine.getContentService().getContent(contentId).get();
        return content;
    }

    private CompletableFuture<QueryContext<Docflow>> sendPfrReport(
            Account account,
            ExternEngine engine
    ) throws Exception {
        UUID draftId = createDraftWithDocument(account, engine);
        DraftService draftService = engine.getDraftService();

        // check pdf printing
        HttpClient httpClient = engine.getHttpClient();
        Draft updatedDraft = draftService
                .lookupAsync(draftId)
                .join()
                .getOrThrow();
        for (Link link : updatedDraft.getDocuments()) {
            DraftDocument draftDocument = httpClient.followGetLink(
                    link.getHref(),
                    DraftDocument.class
            );
            openDraftDocumentAsPdf(draftId, draftDocument);
        }

        // check
        draftService
                .checkAsync(draftId)
                .join();

        cloudSignDraftWithDssCert(draftId);

        // prepare
        draftService
                .prepareAsync(draftId)
                .join();

        // send
        return draftService
                .sendAsync(draftId)
                .thenApply(QueryContext::ensureSuccess);
    }

    private void cloudSignDraftWithDssCert(UUID draftId) throws Exception {

        SignInitiation signInitiation = engine.getDraftService()
                .cloudSignInitAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertEquals(signInitiation.getConfirmType(), ConfirmType.MY_DSS);

        TaskState taskState;
        TaskInfo ti = new TaskInfo();
        ti.setId(UUID.fromString(signInitiation.getTaskId()));
        do {
            taskState = engine.getTaskService(draftId).getTaskStatus(ti).get();
            Thread.sleep(2000);
            System.out.println("Waiting for user to confirm dss operation...");

        } while (taskState == TaskState.RUNNING);

        engine.getDraftService().lookupAsync(draftId).join().getOrThrow().getDocuments()
                .stream()
                .map(link -> engine.getHttpClient().followGetLink(link.getHref(), DraftDocument.class))
                .map(draftDocument -> engine.getDraftService().getSignatureContentAsync(
                        draftId,
                        draftDocument.getId()
                ).join().getOrThrow())
                .forEach(signature -> Assertions.assertTrue(signature.length > 0));
    }

    private static UUID createDraftWithDocument(
            Account senderAcc,
            ExternEngine engine
    ) throws ExecutionException, InterruptedException {
        SenderRequest sender = new SenderRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                senderCertificate.getContent(),
                "8.8.8.8"
        );

        Recipient recipient = new PfrRecipient("666-666");

        OrganizationRequest oPayer = new OrganizationRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                senderAcc.getOrganizationName()
        );
        oPayer.setRegistrationNumberPfr("666-666-223456");

        UUID draftId = engine.getDraftService()
                .createAsync(sender, recipient, oPayer)
                .get()
                .getOrThrow();

        System.out.println("Draft created");

        String testDocPath = "/docs/pfr/SomePfrV2ReportDss.xml";
        DocumentContents docs = TestUtils.loadDocumentContents(testDocPath, DocType.PFR);
        docs.setDescription((new DocumentDescription().filename("ReportForDss1.xml")));

        engine.getDraftService()
                .addDecryptedDocumentAsync(draftId, docs)
                .join().ensureSuccess();

        return draftId;
    }

    private void finishDocflow(Docflow docflow) throws Exception {

        int budget = 60 * 5_000;
        Docflow updated = null;
        while (budget > 0) {
            QueryContext<Docflow> ctx = engine.getDocflowService().lookupDocflowAsync(docflow.getId()).join();
            if (ctx.isSuccess()) {
                updated = ctx.get();
                break;
            }
            Thread.sleep(1000);
            budget -= 1000;
        }

        docflow = updated;
        Assertions.assertNotNull(docflow, "Cannot get docflow in 5 minutes");

        System.out.println("Docflow status: " + docflow.getStatus());

        if (docflow.getStatus() == DocflowStatus.SENT) {
            System.out.println("Docflow " + docflow.getId() + " status: " + docflow.getStatus());
        }

        if (docflow.getStatus() == DocflowStatus.FINISHED) {
            System.out.println("Docflow " + docflow.getId() + " status: " + docflow.getStatus());
        }

        if (docflow.getStatus() == DocflowStatus.RESPONSE_ARRIVED) {
            System.out.println("Docflow " + docflow.getId() + "status: " + docflow.getStatus());
        }
    }

    private void openDraftDocumentAsPdf(UUID draftId, DraftDocument draftDocument) throws Exception {
        UUID draftDocumentId = draftDocument.getId();
        String type = draftDocument.getDescription().getType();
        try {
            byte[] pdfPrintingForm = engine.getDraftService()
                    .getDocumentAsPdfAsync(draftId, draftDocumentId)
                    .get()
                    .getOrThrow();
            System.out.println(
                    "Draft document " + type + " " + draftDocumentId + " printed, pdf size is "
                            + pdfPrintingForm.length);
        } catch (Exception ex) {
            System.out.println(
                    "Draft document " + type + " " + draftDocumentId + " can't be printed, error : " + ex);
        }
    }

    private List<Certificate> findWorkingCerts() throws Exception {

        List<Certificate> remotes = engine
                .getCertificateService()
                .getCertificates(0, 100)
                .get()
                .getOrThrow()
                .getCertificates();

        List<Certificate> cloudCerts = remotes.stream()
                .filter(Certificate::getIsCloud)
                .filter(Certificate::getIsValid)
                .filter(Certificate::getIsQualified)
                .collect(Collectors.toList());

        System.out.printf(
                "Found %s valid qualified cloud certificates\n",
                cloudCerts.size()
        );

        return cloudCerts;
    }
}
