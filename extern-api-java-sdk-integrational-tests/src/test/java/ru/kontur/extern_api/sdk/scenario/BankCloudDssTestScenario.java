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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.utils.*;

import java.security.cert.CertificateException;
import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Disabled
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
                .build(Level.BASIC);

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

        finishDocflow(docflow);
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

        Document document = docflow.getDocuments().stream()
                .filter(d -> d.getDescription().getType() == DocumentType.Fns534Report)
                .findFirst()
                .orElse(null);

        try {
            openDocflowDocumentAsPdf(docflow.getId(), document.getId());
        } catch (ApiException e) {
            System.out.println("Cannot print document. " + e.getMessage());
        }

        while (true) {
            System.out.println("Docflow status: " + docflow.getStatus());

            if (docflow.getStatus() == DocflowStatus.FINISHED) {
                break;
            }

            document = docflow.getDocuments().stream()
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
                System.out.println("Cannot print document. " + e.getMessage());
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

    private void cloudSignReplyDocument(String docflowId, String documentId, ReplyDocument reply)
            throws Exception {

        SignInitiation signInitiation = engine.getDocflowService()
                .cloudSignReplyDocumentForceConfirmationAsync(
                        UUID.fromString(docflowId),
                        UUID.fromString(documentId),
                        UUID.fromString(reply.getId())
                )
                .get()
                .getOrThrow();

        if (!signInitiation.needToConfirmSigning()) {
            System.out.println("Wow! You shouldn't confirm this signing!");
        } else {
            Assertions.assertEquals(signInitiation.getConfirmType(), ConfirmType.MY_DSS);

            TaskState taskState;
            do {
                taskState = engine.getReplyTaskService(
                        UUID.fromString(docflowId),
                        UUID.fromString(documentId),
                        UUID.fromString(reply.getId())
                ).getTaskStatus(UUID.fromString(signInitiation.getTaskId())).get();

                Thread.sleep(2000);
                System.out.println("Waiting for user to confirm dss operation...");

            } while (taskState == TaskState.RUNNING);
        }

        System.out.println("Reply signed in cloud");
    }


    private byte[] cloudDecryptDocument(UUID docflowId, UUID documentId)
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

        DecryptDocumentResultContent documentResultContent = (DecryptDocumentResultContent) taskInfo.getTaskResult();
        UUID contentId = documentResultContent.getContentId();
        byte[] content = engine.getContentService().getContent(contentId).get();
        return content;
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
