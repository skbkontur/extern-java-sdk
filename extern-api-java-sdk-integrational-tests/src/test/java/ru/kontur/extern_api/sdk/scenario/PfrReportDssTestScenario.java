package ru.kontur.extern_api.sdk.scenario;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.security.cert.CertificateException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.Configuration;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.ConfirmType;
import ru.kontur.extern_api.sdk.model.DecryptDocumentResultContent;
import ru.kontur.extern_api.sdk.model.DecryptInitiation;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.TaskInfo;
import ru.kontur.extern_api.sdk.model.TaskState;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.pfr.PfrReply;
import ru.kontur.extern_api.sdk.model.pfr.PfrReplyDocument;
import ru.kontur.extern_api.sdk.model.pfr.PfrSignInitiation;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.TestConfig;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.Zip;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

@Disabled
public class PfrReportDssTestScenario {

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
            dssScenario();
        } catch (ApiException e) {
            System.err.println(e.prettyPrint());
            throw e;
        }
    }

    void dssScenario() throws Exception {
        senderCertificate = getDssCert();

        String thumbprint = cryptoApi.getThumbprint(senderCertificate.getContent());
        engine.getConfiguration().setThumbprint(thumbprint);

        System.out.printf(
                "Using certificate: %s %s %s\n",
                senderCertificate.getFio(),
                senderCertificate.getInn(),
                senderCertificate.getKpp()
        );

        Docflow pfrDocflow = sendPfrReport(engine).get().getOrThrow();
        System.out.println("Draft is sent. Long live the Docflow " + pfrDocflow.getId());

        finishDocflow(pfrDocflow);
    }

    private CompletableFuture<QueryContext<Docflow>> sendPfrReport(ExternEngine engine) throws Exception {
        UUID draftId = buildPfrDraftViaBuilder(engine);
        DraftService draftService = engine.getDraftService();

        HttpClient httpClient = engine.getHttpClient();
        cloudSignDraft(draftId);

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
                .thenApply(QueryContext::ensureSuccess)
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
        DraftService draftService = engine.getDraftService();
        SignInitiation signInitiation = draftService
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

        draftService.lookupAsync(draftId).join().getOrThrow().getDocuments()
                .stream()
                .map(link -> engine.getHttpClient().followGetLink(link.getHref(), DraftDocument.class))
                .map(draftDocument -> draftService.getSignatureContentAsync(
                        draftId,
                        draftDocument.getId()
                ).join().getOrThrow())
                .forEach(signature -> Assertions.assertTrue(signature.length > 0));
    }

    private UUID buildPfrDraftViaBuilder(ExternEngine engine)
            throws ExecutionException, InterruptedException {

        PfrReportDraftsBuilderService pfrReportDraftsBuilderService = engine
                .getDraftsBuilderService().pfrReport();

        PfrReportDraftsBuilder draftsBuilder = new DraftsBuilderCreator()
                .createPfrReportDraftsBuilder(
                        engine,
                        getAccount(),
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
                true
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

            // TODO - fix decrypting error || получать печатную форму после расшифровки, когда будут готовы ссылки на DecryptedContent
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
        PfrReply pfrReply = engine.getDocflowService()
                .generatePfrReplyAsync(docflowId, documentId, type, senderCertificate.getContent())
                .get()
                .getOrThrow();

        System.out.println("Reply generated");

        for (PfrReplyDocument replyDocument : pfrReply.getDocuments()) {
            prepareReplyDocument(documentId, replyDocument);
        }

        PfrSignInitiation signInitiation = engine.getDocflowService()
                .cloudSignPfrReplyDocumentAsync(docflowId, documentId, pfrReply.getId())
                .get()
                .getOrThrow();

        Assertions.assertEquals(signInitiation.getConfirmType(), ConfirmType.MY_DSS);

        TaskState taskState;
        do {
            taskState = engine.getReplyTaskService(
                    UUID.fromString(docflowId),
                    UUID.fromString(documentId),
                    UUID.fromString(pfrReply.getId())
            ).getPfrTaskStatus(UUID.fromString(signInitiation.getTaskId())).get();

            Thread.sleep(2000);
            System.out.println("Waiting for user to confirm dss operation...");

        } while (taskState == TaskState.RUNNING);

        System.out.println("Reply signed in cloud");

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

    private byte[] cloudDecryptDocument(UUID docflowId, UUID documentId)
            throws InterruptedException {

        DecryptInitiation initiation = engine.getDocflowService()
                .cloudDecryptDocumentInitAsync(
                        docflowId,
                        documentId,
                        senderCertificate.getContent().getBytes()
                ).join().getOrThrow();

        TaskInfo<DecryptDocumentResultContent> taskInfo;
        do {
            taskInfo = engine.getDocflowService().getDecryptTaskResult(
                    docflowId,
                    documentId,
                    UUID.fromString(initiation.getDecryptionTaskId())).join();

            Thread.sleep(2000);
            System.out.println("Waiting for user to confirm dss operation...");

        } while (taskInfo.getTaskState() == TaskState.RUNNING);

        return engine.getContentService()
                .getContent(taskInfo.getTaskResult().getContentId())
                .join();
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
