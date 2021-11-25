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

import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.SortOrder;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.Zip;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;


@Disabled
class BankCloudInteractiveScenarioIT {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private List<Certificate> cloudCerts;

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
    void playAroundWithDocflow() throws Exception {

        senderCertificate = findWorkingCerts().get(0);

        DocflowPage page = engine.getDocflowService().searchDocflows(DocflowFilter
                .page(1, 1)
                .finished(false)
                .orderBy(SortOrder.DESCENDING)
        ).getOrThrow();

        String docflowId = page.getDocflowsPageItem().get(0).getId().toString();
        Docflow docflow = engine.getDocflowService()
                .lookupDocflowAsync(docflowId)
                .get()
                .getOrThrow();

        playWithDocflow(docflow);
    }

    @Test
    void main() throws Exception {

        List<Account> accounts = engine.getAccountService()
                .getAccountsAsync(0, 100)
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

        playWithDocflow(docflow);

    }

    private Docflow sendDraftWithUsn(Account senderAcc)
            throws Exception {

        SenderRequest sender = new SenderRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                senderCertificate.getContent(),
                "8.8.8.8"
        );

        Recipient recipient = new FnsRecipient("0087");

        OrganizationRequest oPayer = new OrganizationRequest(senderAcc.getInn(), senderAcc.getKpp(), senderAcc.getOrganizationName());

        Certificate workingCert = findWorkingCerts().get(0);

        UUID draftId = engine.getDraftService()
                .createAsync(sender, recipient, oPayer)
                .get()
                .getOrThrow();

        System.out.println("Draft created");

        UsnServiceContractInfo usn = PreparedTestData.usnV2(workingCert, oPayer);

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

    private void playWithDocflow(Docflow docflow) throws Exception {
        do {

            System.out.println("Docflow status: " + docflow.getStatus());

            if (docflow.getStatus() == DocflowStatus.FINISHED) {
                break;
            }

            System.out.println("Choose reply...");
            Document document = ChooseInDialog.replyForDocflow(docflow);
            if (document == null) {
                System.out.println("You don't want to reply now...");
                System.out.println("Refreshing docflow...");

                docflow = engine.getDocflowService()
                        .lookupDocflowAsync(docflow.getId().toString())
                        .get()
                        .getOrThrow();
                continue;
            }

            System.out.println("Reply on " + document.getDescription().getType());
            System.out.println("Open target document");
            try {
                openDocflowDocumentAsPdf(docflow.getId(), document.getId());
            } catch (ApiException e) {
                System.out.println("Ok, Cannot print document. " + e.getMessage());
            }

            String type = ChooseInDialog.replyType(document);
            if (type == null) {
                System.out.println("You don't know how to reply now...");
                System.out.println("Refreshing docflow...");

                docflow = engine.getDocflowService()
                        .lookupDocflowAsync(docflow.getId().toString())
                        .get()
                        .getOrThrow();
                continue;
            }

            System.out.println("Reply with " + type);
            docflow = sendReply(docflow.getId().toString(), document, type);

        } while (!ChooseInDialog.exit());

    }

    private Docflow sendReply(String docflowId, Document document, String type)
            throws Exception {

        String documentId = document.getId().toString();
        ReplyDocument replyDocument = engine.getDocflowService()
                .generateReplyAsync(docflowId, documentId, type, senderCertificate.getContent())
                .get()
                .getOrThrow();

        System.out.println("Reply generated");
        openReplyAsPdf(replyDocument);

        Assertions.assertTrue(cloudSignReplyDocument(docflowId, documentId, replyDocument));

        Docflow docflow = engine.getDocflowService()
                .sendReplyAsync(docflowId, documentId, replyDocument.getId())
                .get()
                .getOrThrow();

        System.out.println("Reply sent!");
        return docflow;
    }

    private void openDraftDocumentAsPdf(UUID draftId, UUID documentId) throws Exception {

        byte[] pdf = engine.getDraftService()
                .getDocumentAsPdfAsync(draftId, documentId)
                .get()
                .getOrThrow();

        openPrinted("draft document", pdf);
    }

    private void openDocflowDocumentAsPdf(UUID docflowId, UUID documentId) throws Exception {

        byte[] document = downloadDocumentContent(docflowId, documentId);

        if (document == null) {
            System.out.println("Cannot show the document");
            return;
        }

        byte[] pdf = engine.getDocflowService()
                .getDocumentAsPdfAsync(docflowId, documentId, document)
                .get()
                .getOrThrow();

        openPrinted("docflow document", pdf);
    }

    private void openReplyAsPdf(ReplyDocument reply) throws Exception {
        openPrinted("reply", reply.getPrintContent());
    }

    private byte[] downloadDocumentContent(UUID docflowId, UUID documentId) throws Exception {
        Document document = engine.getDocflowService()
                .lookupDocumentAsync(docflowId, documentId)
                .get()
                .getOrThrow();

//        TODO: Поправить тест под новый DocflowDocumentContents
//        if (document.hasDecryptedContent()) {
//            System.out.println("Document is already decrypted");
//            byte[] content = engine.getDocflowService()
//                    .getDecryptedContentAsync(docflowId, documentId)
//                    .get()
//                    .getOrThrow();
//
//            if (document.getDescription().getCompressed()) {
//                content = Zip.unzip(content);
//            }
//
//            return content;
//        }

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

    private boolean cloudSignReplyDocument(String docflowId, String documentId, ReplyDocument reply)
            throws Exception {

        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        SignInitiation init = engine.getDocflowService()
                .cloudSignReplyDocumentAsync(docflowId, documentId, reply.getId())
                .get()
                .getOrThrow();

        if (init.getRequestId() == null) {
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
        return true;
    }


    private byte[] cloudDecryptDocument(UUID docflowId, UUID documentId) {
        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

//        CompletableFuture<Stream<String>> serials = engine.getDocflowService()
//                .getEncryptedContentAsync(docflowId, documentId)
//                .thenApply(QueryContext::getOrThrow)
//                .thenApply(calm(CryptoApi::getSerialNumbers));
//
//        List<String> stringStream = get(serials::get).collect(Collectors.toList());
//
//        List<String> integers = cloudCerts.stream()
//                .map(Certificate::getContent)
//                .map(Base64.getDecoder()::decode)
//                .map(calm(get(X509CertificateFactory::new)::create))
//                .map(cw -> cw.getCert().getSerialNumber())
//                .map(BigInteger::toByteArray)
//                .map(IOUtil::bytesToHex)
//                .collect(Collectors.toList());

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

        this.cloudCerts = cloudCerts;
        return cloudCerts;
    }

    private void openPrinted(String label, byte[] pdf) throws IOException {
        Path tmpPdf = Files.write(Files.createTempFile(label + "-", ".pdf"), pdf);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(tmpPdf.toFile());
            System.out.println(label + " document printed. Pdf opened.");
        } else {
            System.out.println(label + " printed. Trust me. Check it here: " + tmpPdf.toString());
        }
    }

    private static class ChooseInDialog {

        @Nullable
        private static Document replyForDocflow(Docflow docflow) {

            Document[] optDocs = docflow
                    .getDocuments()
                    .stream()
                    .filter(Document::isNeedToReply)
                    .toArray(Document[]::new);

            if (optDocs.length == 0) {
                JOptionPane.showInternalMessageDialog(
                        null,
                        "There is no available documents to reply on. "
                                + "Wait for the inspection to answer. "
                                + "Click OK to refresh the docflow status.",
                        "Inspector thinking...",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return null;
            }

            String[] colNames = Arrays.stream(optDocs)
                    .map(d -> d.getDescription().getType())
                    .map(Objects::toString)
                    .toArray(String[]::new);

            String[][] cols = Arrays.stream(optDocs)
                    .map(Document::getReplyOptions)
                    .toArray(String[][]::new);

            JTable view = new JTable(transpose(cols), colNames);
            view.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
            JScrollPane pane = new JScrollPane(view);

            int result = JOptionPane.showOptionDialog(
                    null,
                    pane,
                    "Reply on...",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    colNames,
                    null
            );

            return result == JOptionPane.CLOSED_OPTION ? null : optDocs[result];
        }

        private static boolean exit() {
            int exit = JOptionPane.showConfirmDialog(
                    null,
                    "Stop send replies and exit?",
                    "exit",
                    JOptionPane.YES_NO_OPTION
            );

            return exit == JOptionPane.YES_OPTION;
        }

        @Nullable
        private static String replyType(Document document) {
            String[] options = document.getReplyOptions();

            int opt = JOptionPane.showOptionDialog(null,
                    "How do you want to reply on " + document.getDescription().getType() + "?",
                    "Reply with...",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    null);
            return opt == JOptionPane.CLOSED_OPTION ? null : options[opt];
        }

    }

    private static Object[][] transpose(Object[][] tss) {

        int nCols = tss.length;
        int nRows = Arrays.stream(tss)
                .mapToInt(value -> value.length)
                .max()
                .orElse(1);

        Object[][] rss = new Object[nRows][nCols];

        for (int i = 0; i < tss.length; i++) {
            for (int j = 0; j < tss[i].length; j++) {
                rss[j][i] = tss[i][j];
            }
        }

        return rss;
    }
}
