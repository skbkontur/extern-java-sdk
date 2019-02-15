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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.jupiter.api.*;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.crypt.CertificateWrapper;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.utils.PreparedTestData;
import ru.kontur.extern_api.sdk.utils.SystemProperty;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;


class BankScenario {

    private static ExternEngine engine;
    private static CryptoApi cryptoApi;

    @BeforeAll
    static void setUpClass() throws Exception {
        engine = TestSuite.Load().engine;
        cryptoApi = new CryptoApi();
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

        CertificateWrapper workingCert = findWorkingCerts().stream()
                .findFirst()
                .orElseThrow(AssertionError::new);

        System.out.printf("Using certificate: issuer %s and subject %s\n",
                workingCert.getIssuerFields().get("O"),
                workingCert.getSubjectFields().get("GIVENNAME")
        );

        List<Company> companies = engine.getOrganizationService()
                .searchAsync(OrgFilter.page(0, 1000))
                .get()
                .getOrThrow()
                .getCompanies();

        System.out.printf("Found %s organizations\n", companies.size());

        Company org = companies.get(0);
        CompanyGeneral general = org.getGeneral();

        System.out.printf("Using organization: %s inn=%s kpp=%s\n",
                general.getName(),
                general.getInn(),
                general.getKpp()
        );

        Docflow docflow = sendDraftWithUsn(account, workingCert);

        finishDocflow(docflow);
    }

    private Docflow sendDraftWithUsn(Account senderAcc, CertificateWrapper certificate)
            throws Exception {

        SenderRequest sender = new SenderRequest(
                senderAcc.getInn(),
                senderAcc.getKpp(),
                certificate.g,
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
                result.getStatus() == PrepareResult.Status.OK ||
                        result.getStatus() == PrepareResult.Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS,
                test.serialize(result)
        );

        System.out.printf("Draft prepared to send: %s\n", result.getStatus());

        return engine.getDraftService()
                .sendAsync(draftId)
                .get()
                .getOrThrow();
    }

    /** Создаёт и отправляет ответные документы до завершения ДО. */
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

            String type = document.getReplyOptions()[0];
            System.out.println("Reply with " + type);
            docflow = sendReply(docflow.getId().toString(), document, type);
        }

    }

    private Docflow sendReply(String docflowId, Document document, String type, String certificateContent )
            throws Exception {

        String documentId = document.getId().toString();
        ReplyDocument replyDocument = engine.getDocflowService()
                .generateReplyAsync(docflowId, documentId, type, certificateContent)
                .get()
                .getOrThrow();

        System.out.println("Reply generated");

        byte[] signature = engine.;
        replyDocument = engine.getDocflowService()
                .uploadReplyDocumentSignatureAsync(docflowId, documentId, replyDocument.getId(), signature)
                .get()
                .getOrThrow();

        Docflow docflow = engine.getDocflowService()
                .sendReplyAsync(docflowId, documentId, replyDocument.getId())
                .get()
                .getOrThrow();

        System.out.println("Reply sent!");
        return docflow;
    }

    private List<CertificateWrapper> findWorkingCerts() throws Exception {

        List<CertificateWrapper> locals = cryptoApi.getCertificatesInstalledLocally();

        List<Certificate> remotes = engine
                .getCertificateService()
                .getCertificateListAsync()
                .get()
                .getOrThrow()
                .getCertificates();

        Set<String> remoteThumbprints = remotes.stream()
                .map(cert -> cryptoApi.getThumbprint(cert.getContent()))
                .collect(Collectors.toSet());

        List<CertificateWrapper> commonCerts = locals.stream()
                .filter(wrapper -> remoteThumbprints.contains(wrapper.getThumbprint()))
                .collect(Collectors.toList());

        System.out.printf(
                "Found %s certificates both registered in Kontur and installed locally\n",
                commonCerts.size()
        );

        return commonCerts;
    }
}
