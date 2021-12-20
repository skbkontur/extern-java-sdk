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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

/**
 * Тест на сценарий отправки отчета PfrV2 СЗВ-ТД
 */
@Execution(ExecutionMode.CONCURRENT)
class PfrReportV2CloudTestScenarioIT {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private static TestSuite test;

    @BeforeAll
    static void setUpClass() {
        test = TestSuite.Load();
        engine = test.engine;
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

        Certificate workingCert = findWorkingCerts().get(0);
        senderCertificate = workingCert;

        System.out.printf(
                "Using certificate: %s %s %s\n",
                workingCert.getFio(),
                workingCert.getInn(),
                workingCert.getKpp()
        );

        List<Account> accounts = engine.getAccountService()
                .getAccountsAsync(0, 100)
                .get()
                .getOrThrow()
                .getAccounts();

        Account account = accounts.stream()
                .filter(
                        acc -> acc.getInn().equals(senderCertificate.getInn())
                                && acc.getKpp().equals(senderCertificate.getKpp()))
                .collect(Collectors.toList())
                .get(0);

        engine.setAccountProvider(account::getId);

        System.out.printf("Found %s accounts\n", accounts.size());
        System.out.printf(
                "Using account: %s inn=%s kpp=%s\n",
                account.getOrganizationName(),
                account.getInn(),
                account.getKpp()
        );

        Docflow pfrDocflow = sendPfrReport(engine, workingCert).get().getOrThrow();
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

                cloudDecryptDocument(pfrDocflow.getId(), document.getId());
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

    private void cloudDecryptDocument(UUID docflowId, UUID documentId) {
        ApproveCodeProvider backdoor = new ApproveCodeProvider(engine);
        byte[] decrypted = engine.getDocflowService().cloudDecryptDocument(
                docflowId.toString(),
                documentId.toString(),
                senderCertificate.getContent(),
                init -> backdoor.apply(init.getRequestId())
        )
                .getOrThrow();
        Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
    }

    private CompletableFuture<QueryContext<Docflow>> sendPfrReport(
            ExternEngine engine,
            Certificate workingCert
    ) throws Exception {
        UUID draftId = buildPfrDraftViaBuilder(engine, workingCert);
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

        cloudSignDraft(draftId);

        // prepare
        draftService
                .prepareAsync(draftId)
                .join();

        // send
        return draftService
                .sendAsync(draftId)
                .thenApply(QueryContext::ensureSuccess);
    }

    private static UUID buildPfrDraftViaBuilder(
            ExternEngine engine,
            Certificate workingCert
    ) {
        PfrReportDraftsBuilderService pfrReportDraftsBuilderService = engine
                .getDraftsBuilderService().pfrReport();

        PfrReportDraftsBuilder draftsBuilder = new DraftsBuilderCreator()
                .createPfrReportDraftsBuilder(
                        engine,
                        workingCert.getContent()
                );
        PfrReportDraftsBuilderDocument draftsBuilderDocument = new DraftsBuilderDocumentCreator()
                .createPfrReportDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );
        new DraftsBuilderDocumentFileCreator().createPfrReportV2DraftsBuilderDocumentFile(
                engine,
                draftsBuilder,
                draftsBuilderDocument
        );
        BuildDraftsBuilderResult draftsBuilderResult = pfrReportDraftsBuilderService.buildAsync(
                draftsBuilder
                        .getId())
                .join();
        assertEquals(1, draftsBuilderResult.getDraftIds().length);
        return draftsBuilderResult.getDraftIds()[0];
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

    private void cloudSignDraft(UUID draftId) throws Exception {

        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        SignedDraft signedDraft = engine.getDraftService()
                .cloudSignAsync(draftId, cxt -> smsProvider.apply(cxt.get().getRequestId()))
                .get()
                .getOrThrow();

        System.out.printf(
                "Draft signed in cloud, %s document(s) signed\n",
                signedDraft.getSignedDocuments().size()
        );
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
