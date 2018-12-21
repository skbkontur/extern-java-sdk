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
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountInfo;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.Zip;


class BankCloudTestScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.LoadManually((cfg, builder) -> builder
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
                .build(Level.NONE)
        ).engine;

    }

    @Test
    void main() throws Exception {

        List<Account> accounts = engine.getAccountService()
                .acquireAccountsAsync()
                .get()
                .getOrThrow()
                .getAccounts();

        Account account = accounts.get(0);
        engine.setAccountProvider(account::getId);

        System.out.printf("Found %s accounts\n", accounts.size());
        System.out.printf("Using account: %s inn=%s kpp=%s\n",
                account.getOrganizationName(),
                account.getInn(),
                account.getKpp());

        Certificate workingCert = findWorkingCerts().get(0);
        senderCertificate = workingCert;

        System.out.printf("Using certificate: %s %s %s\n",
                workingCert.getFio(),
                workingCert.getInn(),
                workingCert.getKpp()
        );

        Docflow docflow = sendDraftWithUsn(account);

        System.out.println("Draft is sent. Long live the Docflow " + docflow.getId());

        finishDocflow(docflow);
    }

    private Docflow sendDraftWithUsn(Account senderAcc)
            throws Exception {

        Sender sender = new Sender();
        sender.setInn(senderAcc.getInn());
        sender.setKpp(senderAcc.getKpp());
        sender.setIpaddress("8.8.8.8");
        sender.setCertificate(senderCertificate.getContent());

        Recipient recipient = new FnsRecipient("0087");

        AccountInfo oPayer = new AccountInfo(senderAcc.getInn(), senderAcc.getKpp());

        UUID draftId = engine.getDraftService()
                .createAsync(sender, recipient, oPayer)
                .get()
                .getOrThrow();

        System.out.println("Draft created");

        UsnServiceContractInfo usn = PreparedTestData.usnV2();

        DraftDocument document = engine.getDraftService()
                .createAndBuildDeclarationAsync(draftId, 2, usn)
                .get()
                .getOrThrow();

        System.out.println("Usn document built and added to draft");

        openDraftDocumentAsPdf(draftId, document.getId());
        Assertions.assertTrue(cloudSignDraft(draftId));

        CheckResultData checkResult = engine.getDraftService()
                .checkAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(checkResult.hasNoErrors());
        System.out.println("Usn document has no errors");

        PrepareResult result = engine.getDraftService()
                .prepareAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(result.getStatus() == Status.OK ||
                result.getStatus() == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS
        );

        System.out.printf("Draft prepared to send: %s\n", result.getStatus());

        return engine.getDraftService()
                .sendAsync(draftId)
                .get()
                .getOrThrow();
    }

    /** Создаёт и отправляет ответные документы до завершения ДО. */
    private void finishDocflow(Docflow docflow) throws Exception {

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

    private boolean cloudSignDraft(UUID draftId) throws Exception {

        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        SignedDraft signedDraft = engine.getDraftService()
                .cloudSignAsync(draftId, cxt -> smsProvider.apply(cxt.get().getRequestId()))
                .get()
                .getOrThrow();

        System.out.printf("Draft signed in cloud, %s document(s) signed\n",
                signedDraft.getSignedDocuments().size()
        );

        return true;
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
                .getCertificateListAsync()
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
