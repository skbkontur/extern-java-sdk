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

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.Zip;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

@Disabled("Not implemented")
class BankCloudTestScenarioPfr {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private static TestSuite test;
    private static PfrReportDraftsBuilderService pfrReportDraftsBuilderService;
    private PfrReportDraftsBuilder pfrReportDraftsBuilder;
    private static CryptoUtils cryptoUtils;

    @BeforeAll
    static void setUpClass() {
        ExternEngine engine2 = TestSuite.Load().engine;
        test = TestSuite.LoadManually((cfg, builder) -> builder
                .buildAuthentication(cfg.getAuthBaseUri(), authBuilder -> authBuilder
                        .trustedAuthentication(UUID.fromString(cfg.getServiceUserId()))
                        .configureEncryption(
                                cfg.getJksPass(),
                                cfg.getRsaKeyPass(),
                                cfg.getThumbprintRsa()
                        )
                )
                .doNotUseCryptoProvider()
                .doNotSetupAccount()
                .build(Level.BASIC)
        );
        engine = test.engine;
        pfrReportDraftsBuilderService = engine.getDraftsBuilderService().pfrReport();
        cryptoUtils = CryptoUtils.with(engine2.getCryptoProvider());
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

        Docflow docflow = sendDraftWithUsn(account, workingCert);

        System.out.println("Draft is sent. Long live the Docflow " + docflow.getId());

        finishDocflow(docflow);
    }

    private Docflow sendDraftWithUsn(Account senderAcc, Certificate certificate)
            throws Exception {

        pfrReportDraftsBuilder = new DraftsBuilderCreator().createPfrReportDraftsBuilder(
                engine,
                cryptoUtils
        );
        PfrReportDraftsBuilderDocument draftsBuilderDocument = new DraftsBuilderDocumentCreator()
                .createPfrReportDraftsBuilderDocument(
                        engine,
                        pfrReportDraftsBuilder
                );
        new DraftsBuilderDocumentFileCreator().createPfrReportDraftsBuilderDocumentFile(
                engine,
                cryptoUtils,
                pfrReportDraftsBuilder,
                draftsBuilderDocument
        );

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

//        UUID draftId = engine.getDraftService()
//                .createAsync(sender, recipient, oPayer)
//                .get()
//                .getOrThrow();

        System.out.println("Draft created");

        BuildDraftsBuilderResult buildDraftsBuilderResult = pfrReportDraftsBuilderService.buildAsync(pfrReportDraftsBuilder.getId()).join();
        assertEquals(1, buildDraftsBuilderResult.getDraftIds().length);

        UUID draftId = buildDraftsBuilderResult.getDraftIds()[0];

        // UsnServiceContractInfo usn = PreparedTestData.usnV2(certificate, oPayer);

        Draft draft = engine.getDraftService()
                .lookupAsync(draftId.toString())
                .get()
                .getOrThrow();

        System.out.println("Usn document built and added to draft");

        //openDraftDocumentAsPdf(draftId, document.getId());
        cloudSignDraft(draftId);

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

    /**
     * Создаёт и отправляет ответные документы до завершения ДО.
     */
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

        while (true) {
            System.out.println("Docflow status: " + docflow.getStatus());

            if (docflow.getStatus() == DocflowStatus.FINISHED) {
                break;
            }

            Document document = docflow.getDocuments().stream()
                    .filter(Document::isNeedToReply)
                    .findFirst()
                    .orElse(null);

            if (document == null) {

                int timeout = 20;
                System.out.println("No reply available yet. Waiting " + timeout + " seconds");
                UncheckedRunnable.run(() -> Thread.sleep(timeout * 1000));
                docflow = engine.getDocflowService()
                        .lookupDocflowAsync(docflow.getId().toString())
                        .get()
                        .getOrThrow();
                continue;
            }

            System.out.println("Reply on " + document.getDescription().getType());
            try {
                openDocflowDocumentAsPdf(docflow.getId(), document.getId());
            } catch (ApiException e) {
                System.out.println("Ok, Cannot print document. " + e.getMessage());
            }

            String type = document.getReplyOptions()[0];
            System.out.println("Reply with " + type);
            docflow = sendReply(docflow.getId().toString(), document, type);
        }

    }

    private Docflow sendReply(String docflowId, Document document, String type)
            throws Exception {

        String documentId = document.getId().toString();
        ReplyDocument replyDocument = engine.getDocflowService()
                .generateReplyAsync(docflowId, documentId, type, senderCertificate.getContent())
                .get()
                .getOrThrow();

        System.out.println("Reply generated");

        cloudSignReplyDocument(docflowId, documentId, replyDocument);

        Docflow docflow = engine.getDocflowService()
                .sendReplyAsync(docflowId, documentId, replyDocument.getId())
                .get()
                .getOrThrow();

        System.out.println("Reply sent!");
        return docflow;
    }

    private void openDraftDocumentAsPdf(UUID draftId, UUID documentId) throws Exception {
        engine.getDraftService()
                .getDocumentAsPdfAsync(draftId, documentId)
                .get()
                .getOrThrow();
    }

    private void openDocflowDocumentAsPdf(UUID docflowId, UUID documentId) throws Exception {

        byte[] document = downloadDocumentContent(docflowId, documentId);

        if (document == null) {
            System.out.println("Cannot show the document");
            return;
        }

        engine.getDocflowService()
                .getDocumentAsPdfAsync(docflowId, documentId, document)
                .get()
                .getOrThrow();
    }

    private byte[] downloadDocumentContent(UUID docflowId, UUID documentId) throws Exception {
        Document document = engine.getDocflowService()
                .lookupDocumentAsync(docflowId, documentId)
                .get()
                .getOrThrow();

        if (document.hasDecryptedContent()) {
            System.out.println("Document is already decrypted");
            byte[] content = engine.getDocflowService()
                    .getDecryptedContentAsync(docflowId, documentId)
                    .get()
                    .getOrThrow();

            if (document.getDescription().getCompressed()) {
                content = Zip.unzip(content);
            }

            return content;
        }

        System.out.println("Decrypting document...");
        return cloudDecryptDocument(docflowId, documentId);
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

    private void cloudSignReplyDocument(String docflowId, String documentId, ReplyDocument reply)
            throws Exception {

        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        SignInitiation init = engine.getDocflowService()
                .cloudSignReplyDocumentAsync(docflowId, documentId, reply.getId())
                .get()
                .getOrThrow();

        if (!init.needToConfirmSigning()) {
            System.out.println("Wow! You shouldn't confirm this signing!");
        } else {
            engine.getDocflowService()
                    .cloudSignConfirmReplyDocumentAsync(
                            docflowId,
                            documentId,
                            reply.getId(),
                            init.getRequestId(),
                            smsProvider.apply(init.getRequestId())
                    )
                    .get()
                    .getOrThrow();
        }

        System.out.println("Reply signed in cloud");
    }


    private byte[] cloudDecryptDocument(UUID docflowId, UUID documentId) {
        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        return engine.getDocflowService()
                .cloudDecryptDocument(
                        docflowId.toString(),
                        documentId.toString(),
                        senderCertificate.getContent(),
                        init -> smsProvider.apply(init.getRequestId())
                ).getOrThrow();
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
