package ru.kontur.extern_api.sdk.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
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
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.pfr.PfrReply;
import ru.kontur.extern_api.sdk.model.pfr.PfrReplyDocument;
import ru.kontur.extern_api.sdk.model.pfr.PfrSignInitiation;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.Zip;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;


@Execution(ExecutionMode.CONCURRENT)
public class PfrReportCloudTestScenario {

    private static ExternEngine engine;
    private static Certificate senderCertificate;
    private static TestSuite test;
    private static ApproveCodeProvider smsProvider;

    @BeforeAll
    static void setUpClass() {
        test = TestSuite.Load();
        engine = test.engine;
        smsProvider = new ApproveCodeProvider(engine);
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

        Docflow pfrDocflow = sendPfrReport(engine).get().getOrThrow();
        System.out.println("Draft is sent. Long live the Docflow " + pfrDocflow.getId());

        finishDocflow(pfrDocflow);
    }

    private CompletableFuture<QueryContext<Docflow>> sendPfrReport(ExternEngine engine) throws Exception {
        UUID draftId = buildPfrDraftViaBuilder(engine);
        cloudSignDraft(draftId);

        DraftService draftService = engine.getDraftService();
        HttpClient httpClient = engine.getHttpClient();

        // check pdf printing
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

        // prepare
        draftService
                .prepareAsync(draftId)
                .join();

        // send
        return draftService
                .sendAsync(draftId)
                .thenApply(QueryContext::ensureSuccess)
                .whenComplete((cxt, throwable) -> {
                    awaitDocflowIndexed(engine, cxt.getDocflow());
                });
    }

    private void cloudSignDraft(UUID draftId) throws ExecutionException, InterruptedException {
        SignInitiation init = engine.getDraftService()
                .cloudSignInitAsync(draftId)
                .get()
                .getOrThrow();

        engine.getDraftService()
                .cloudSignConfirmAsync(
                        draftId,
                        init.getRequestId(),
                        smsProvider.apply(init.getRequestId())
                )
                .get()
                .getOrThrow();

        System.out.println("Draft signed in cloud");
    }

    private UUID buildPfrDraftViaBuilder(ExternEngine engine){
        PfrReportDraftsBuilderService pfrReportDraftsBuilderService = engine
                .getDraftsBuilderService().pfrReport();

        PfrReportDraftsBuilder draftsBuilder = new DraftsBuilderCreator()
                .createPfrReportDraftsBuilder(
                        engine,
                        senderCertificate.getContent());

        PfrReportDraftsBuilderDocument draftsBuilderDocument = new DraftsBuilderDocumentCreator()
                .createPfrReportDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );

        new DraftsBuilderDocumentFileCreator().createPfrReportDraftsBuilderDocumentFile(
                engine,
                null,
                draftsBuilder,
                draftsBuilderDocument,
                false
        );

        BuildDraftsBuilderResult draftsBuilderResult = pfrReportDraftsBuilderService.buildAsync(
                draftsBuilder
                        .getId())
                .join();
        assertEquals(1, draftsBuilderResult.getDraftIds().length);
        return draftsBuilderResult.getDraftIds()[0];
    }

    private static void awaitDocflowIndexed(ExternEngine engine, Docflow docflow) {
        Awaiter.waitForCondition(
                () -> engine.getDocflowService().lookupDocflowAsync(docflow.getId()),
                cxt -> cxt.isSuccess() || cxt.getServiceError().getCode() != 404,
                2000
        ).thenApply(QueryContext::getOrThrow);
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

            if (docflow.getStatus() == DocflowStatus.RESPONSE_ARRIVED) {
                System.out.println("Docflow status: " + docflow.getStatus());
            }

            Document document = docflow.getDocuments().stream()
                    .filter(Document::isNeedToReply)
                    .findFirst()
                    .orElse(null);

            if (document == null) {

                int timeout = 30;
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
                System.out.println(
                        "Ok, Cannot print document " + document.getDescription().getType() + ". " + e
                                .getMessage());
            }

            String type = document.getReplyOptions()[0];
            System.out.println("Reply with " + type);
            docflow = sendReply(docflow.getId().toString(), document, type);
        }
    }

    private Docflow sendReply(String docflowId, Document document, String type)
            throws Exception {

        String documentId = document.getId().toString();
        byte[] certContent = senderCertificate.getContent().getBytes();
        PfrReply pfrReply = engine.getDocflowService()
                .generatePfrReplyAsync(docflowId, documentId, type, certContent)
                .get()
                .getOrThrow();

        System.out.println("Reply generated");

        for (PfrReplyDocument replyDocument : pfrReply.getDocuments()) {
           prepareReplyDocument(documentId, replyDocument);
        }

        PfrSignInitiation signInitiation = engine.getDocflowService()
                .cloudSignPfrReplyAsync(docflowId, documentId, pfrReply.getId())
                .get()
                .getOrThrow();

        engine.getDocflowService()
                .cloudSignConfirmPfrReplyAsync(
                        docflowId,
                        documentId,
                        pfrReply.getId(),
                        signInitiation.getRequestId(),
                        smsProvider.apply(signInitiation.getRequestId())
                )
                .get()
                .getOrThrow();

        Docflow docflow = engine.getDocflowService()
                .sendPfrReplyAsync(
                        docflowId,
                        documentId,
                        pfrReply.getId()
                )
                .get()
                .getOrThrow();

        System.out.println("PFR Reply sent!");
        return docflow;
    }

    private void prepareReplyDocument(String documentId, PfrReplyDocument reply) {
        engine.getDocflowService()
                .updatePfrReplyDocumentDecryptedContentAsync(
                        reply.getDocflowId(),
                        documentId,
                        reply.getReplyId(),
                        reply.getId(),
                        GetRandomBytes()
                )
                .join().getOrThrow();
        System.out.println("Reply document decrypted");
    }

    private byte[] GetRandomBytes(){
        Random rd = new Random();
        byte[] arr = new byte[100];
        rd.nextBytes(arr);
        return arr;
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

    private byte[] cloudDecryptDocument(UUID docflowId, UUID documentId) {
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

    private Account getAccount() throws ExecutionException, InterruptedException {

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
}
