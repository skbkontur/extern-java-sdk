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

import java.awt.Desktop;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import org.jetbrains.annotations.Nullable;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Company;
import ru.kontur.extern_api.sdk.model.CompanyGeneral;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.Recipient;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.SignedDraft;
import ru.kontur.extern_api.sdk.model.SortOrder;
import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.auth.TrustedAuthentication;
import ru.kontur.extern_api.sdk.provider.crypt.rsa.CryptoProviderRSA;
import ru.kontur.extern_api.sdk.it.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.it.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.Zip;


@Disabled
class BankCloudScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;

        CryptoProvider cryptoProvider = new CryptoProviderRSA(
                engine.getConfiguration().getJksPass(),
                engine.getConfiguration().getRsaKeyPass()
        );

        engine.setAuthenticationProvider(
                new TrustedAuthentication()
                        .apiKeyProvider(() -> engine.getConfiguration().getApiKey())
                        .serviceUserIdProvider(() -> engine.getConfiguration().getServiceUserId())
                        .authBaseUriProvider(() -> engine.getConfiguration().getAuthBaseUri())
                        .credentialProvider(() -> engine.getConfiguration().getCredential())
                        .signatureKeyProvider(() -> engine.getConfiguration().getThumbprintRsa())
                        .cryptoProvider(cryptoProvider)
                        .httpClient(engine.getHttpClient())
        );
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
    void playAroundWithDocflow() throws Exception {
        SystemProperty.pop("httpclient.debug");

        senderCertificate = findWorkingCerts().get(0);

        DocflowPage page = engine.getDocflowService().searchDocflows(DocflowFilter.page(0, 1)
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
        SystemProperty.pop("httpclient.debug");

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

        Company org = findOrganizations().get(0);
        CompanyGeneral general = org.getGeneral();
        System.out.printf("Using organization: %s inn=%s kpp=%s\n",
                general.getName(),
                general.getInn(),
                general.getKpp()
        );

        Docflow docflow = sendDraftWithUsn(account, org);

        System.out.println("Draft is sent. Long live the Docflow " + docflow.getId());

        playWithDocflow(docflow);

    }

    private Docflow sendDraftWithUsn(Account senderAccount, Company payer)
            throws Exception {

        Sender sender = new Sender();
        sender.setInn(senderAccount.getInn());
        sender.setKpp(senderAccount.getKpp());
        sender.setIpaddress("8.8.8.8");
        sender.setCertificate(senderCertificate.getContent());

        Recipient recipient = new FnsRecipient("0087");

        CompanyGeneral org = payer.getGeneral();
        Organization oPayer = new Organization(org.getInn(), org.getKpp());

        String draftId = engine.getDraftService()
                .createAsync(sender, recipient, oPayer)
                .get()
                .getOrThrow()
                .toString();

        System.out.println("Draft created");

        UsnServiceContractInfo usn = PreparedTestData.usnV2();

        DraftDocument document = engine.getDraftService()
                .createAndBuildDeclarationAsync(draftId, 2, usn)
                .get()
                .getOrThrow();

        System.out.println("Usn document built and added to draft");

        openDraftDocumentAsPdf(draftId, document.getId().toString());

        CheckResultData checkResult = engine.getDraftService()
                .checkAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(checkResult.hasNoErrors());
        System.out.println("Usn document has no errors");

        Assertions.assertTrue(cloudSignDraft(draftId));

        PrepareResult result = engine.getDraftService()
                .prepareAsync(draftId)
                .get()
                .getOrThrow();

        Assertions.assertTrue(
                result.getStatus() == Status.OK ||
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
                System.out.println("You don't want to reply now?");
                continue;
            }

            System.out.println("Reply on " + document.getDescription().getType());
            System.out.println("Open target document");
            try {
                openDocflowDocumentAsPdf(docflow.getId().toString(), document.getId().toString());
            } catch (ServiceException e) {
                System.out.println("Ok, Cannot print document. " + e.getMessage());
            }

            String type = ChooseInDialog.replyType(document);
            if (type == null) {
                System.out.println("You don't know how to reply now?");
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

    private void openDraftDocumentAsPdf(String draftId, String documentId) throws Exception {

        byte[] pdf = engine.getDraftService()
                .getDocumentAsPdfAsync(draftId, documentId)
                .get()
                .getOrThrow();

        Path tmpPdf = Files.write(Files.createTempFile(documentId + "-", ".pdf"), pdf);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(tmpPdf.toFile());
            System.out.println("Draft document printed. Pdf opened.");
        } else {
            System.out.println("Draft document printed. Trust me. Check it here: " + tmpPdf.toString());
        }

    }

    private void openDocflowDocumentAsPdf(String docflowId, String documentId) throws Exception {

        byte[] document = downloadDocumentContent(docflowId, documentId);

        if (document == null) {
            System.out.println("Cannot show the document");
            return;
        }

        byte[] pdf = engine.getDocflowService()
                .getDocumentAsPdfAsync(docflowId, documentId, document)
                .get()
                .getOrThrow();

        Path tmpPdf = Files.write(Files.createTempFile(documentId + "-", ".pdf"), pdf);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(tmpPdf.toFile());
            System.out.println("Docflow document printed. Pdf opened.");
        } else {
            System.out.println("Docflow document printed. Trust me. Check it here: " + tmpPdf.toString());
        }

    }

    private void openReplyAsPdf(ReplyDocument reply) throws Exception {
        byte[] replyPdf = reply.getPrintContent();

        Path tmpPdf = Files.write(Files.createTempFile(reply.getId() + "-", ".pdf"), replyPdf);

        if (Desktop.isDesktopSupported()) {
            Desktop.getDesktop().open(tmpPdf.toFile());
            System.out.println("Reply document printed. Pdf opened.");
        } else {
            System.out.println("Reply document printed. Trust me. Check it here: " + tmpPdf.toString());
        }

    }

    private byte[] downloadDocumentContent(String docflowId, String documentId) throws Exception {
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

    private boolean cloudSignDraft(String draftId) throws Exception {

        ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);

        SignedDraft signedDraft = engine.getDraftService()
                .cloudSignAsync(draftId, cxt -> smsProvider.apply(cxt.getRequestId()))
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


    private byte[] cloudDecryptDocument(String docflowId, String documentId) {
        return engine.getDocflowService()
                .cloudDecryptDocument(docflowId, documentId, senderCertificate.getContent(), init -> {
                    ApproveCodeProvider smsProvider = new ApproveCodeProvider(engine);
                    return smsProvider.apply(init.getRequestId());
                }).getOrThrow();
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

    private List<Company> findOrganizations() throws Exception {
        List<Company> companies = engine.getOrganizationService()
                .searchAsync(null , null, null, null)
                .get()
                .getOrThrow()
                .getCompanies();

        System.out.printf("Found %s organizations\n", companies.size());
        return companies;
    }

    static class ChooseInDialog {

        @Nullable
        private static Document replyForDocflow(Docflow docflow) {

            Document[] optDocs = docflow
                    .getDocuments()
                    .stream()
                    .filter(Document::isNeedToReply)
                    .toArray(Document[]::new);

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
                    "Select document to reply...",
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
                    "Choose reply type...",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    null);
            return opt == JOptionPane.CLOSED_OPTION ? null : options[opt];
        }

        static class ChosenReply {

            final Document document;
            final String type;

            public ChosenReply(Document document, String type) {
                this.document = document;
                this.type = type;
            }
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
