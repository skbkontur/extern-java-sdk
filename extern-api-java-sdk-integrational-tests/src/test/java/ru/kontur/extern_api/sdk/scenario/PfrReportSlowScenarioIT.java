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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.model.pfr.PfrReply;
import ru.kontur.extern_api.sdk.model.pfr.PfrReplyDocument;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.*;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

import java.util.Base64;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Execution(ExecutionMode.CONCURRENT)
@Disabled("Unstable pfr service")
class PfrReportSlowScenarioIT {

    private static ExternEngine engine;
    private static TestSuite test;
    private static CryptoUtils cryptoUtils;

    @BeforeAll
    static void setUpClass() {
        test = TestSuite.Load();
        engine = test.engine;
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
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

        Docflow pfrDocflow = sendPfrReport(engine).get().getOrThrow();
        System.out.println("Draft is sent. Long live the Docflow " + pfrDocflow.getId());

        finishDocflow(pfrDocflow);

        Docflow pfrDocflowFinished = engine.getDocflowService()
                .lookupDocflowAsync(pfrDocflow.getId().toString())
                .get()
                .getOrThrow();

        for (Document document : pfrDocflowFinished.getDocuments()) {
            System.out.println("Will process contents " + document.getDescription().getType());
            if (document.getDescription().getType().toString() == "PfrReportProtocolAppendix") {
                // TODO fix condition when pfr content links will be done
                System.out.println("Skip " + document.getDescription().getType());
                continue;
            }

//        TODO: Поправить тест под новый DocflowDocumentContents
//
//            if (document.hasEncryptedContent()) {
//                byte[] pfrServiceDocumentContent = engine.getDocflowService().getEncryptedContentAsync(
//                        pfrDocflowFinished.getId(),
//                        document.getId()
//                ).join().getOrThrow();
//                System.out.println(
//                        "Service Document " + document.getId() + " encrypted content received, len bytes ="
//                                + pfrServiceDocumentContent.length);
//                byte[] pfrServiceDocumentContentDecrypted = cryptoUtils.decrypt(
//                        engine.getConfiguration().getThumbprint(),
//                        pfrServiceDocumentContent
//                );
//
//                System.out.println("DoService Document " + document.getId() + "content decrypted, len bytes ="
//                        + pfrServiceDocumentContentDecrypted.length);
//            }
//
//            if (document.hasDecryptedContent()) {
//                byte[] pfrSpecialDecryptedContent = engine.getDocflowService().getDecryptedContentAsync(
//                        pfrDocflowFinished.getId(),
//                        document.getId()
//                ).join().getOrThrow();
//
//                System.out.println("Document " + document.getId() + "content received, len bytes ="
//                        + pfrSpecialDecryptedContent.length);
//            }
        }
    }

    private CompletableFuture<QueryContext<Docflow>> sendPfrReport(ExternEngine engine) throws Exception {
        UUID draftId = buildPfrDraftViaBuilder(engine);
        DraftService draftService = engine.getDraftService();
        Draft draft = draftService
                .lookupAsync(draftId)
                .join()
                .getOrThrow();

        HttpClient httpClient = engine.getHttpClient();

        draft.getDocuments()
                .stream()
                .map(Link::getHref)
                .map(link -> httpClient.followGetLink(link, DraftDocument.class))
                .map(document -> {
                    DocumentContents contents = updateSignaturesInDraftDocument(
                            engine,
                            draftService,
                            draftId,
                            httpClient,
                            document
                    );

                    return contents;
                })
                .toArray();

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
                .thenApply(QueryContext::ensureSuccess);
    }

    private DocumentContents updateSignaturesInDraftDocument(
            ExternEngine engine,
            DraftService draftService,
            UUID draftId,
            HttpClient httpClient,
            DraftDocument document
    ) {
        Assertions.assertNotNull(document);

        String contentLink = document.getDecryptedContentLink().getHref();
        String data = httpClient.followGetLink(contentLink, String.class);

        String thumbprint = engine.getConfiguration().getThumbprint();
        byte[] decryptedContent = draftService.getDecryptedDocumentContentAsync(
                draftId,
                document.getId()
        ).join().getOrThrow();

        byte[] signature = cryptoUtils.sign(thumbprint, decryptedContent);

        DocumentContents contents = new DocumentContents();
        contents.setDescription(document.getDescription());
        contents.setBase64Content(data);
        contents.setSignature(Base64.getEncoder().encodeToString(signature));

        draftService.updateDocumentAsync(draftId, document.getId(), contents)
                .join().getOrThrow();
        return contents;
    }

    private static UUID buildPfrDraftViaBuilder(ExternEngine engine) {
        PfrReportDraftsBuilderService pfrReportDraftsBuilderService = engine
                .getDraftsBuilderService().pfrReport();

        PfrReportDraftsBuilder draftsBuilder = new DraftsBuilderCreator()
                .createPfrReportDraftsBuilder(
                        engine,
                        cryptoUtils
                );
        PfrReportDraftsBuilderDocument draftsBuilderDocument = new DraftsBuilderDocumentCreator()
                .createPfrReportDraftsBuilderDocument(
                        engine,
                        draftsBuilder
                );
        new DraftsBuilderDocumentFileCreator().createPfrReportDraftsBuilderDocumentFile(
                engine,
                cryptoUtils,
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
                System.out.println("Ok, Cannot print document " + document.getDescription().getType() + ". " + e.getMessage());
                if (!e.getErrorId().equals("urn:error:externapi:documentPrintUnsupported"))
                    throw e;
            }

            String type = document.getReplyOptions()[0];
            System.out.println("Reply with " + type);
            docflow = sendReply(docflow.getId().toString(), document, type);
        }
    }

    private Docflow sendReply(String docflowId, Document document, String type)
            throws Exception {

        String documentId = document.getId().toString();
        byte[] certContent = cryptoUtils.loadCertContent(engine.getConfiguration().getThumbprint());
        PfrReply pfrReply = engine.getDocflowService()
                .generatePfrReplyAsync(docflowId, documentId, type, certContent)
                .get()
                .getOrThrow();

        System.out.println("Reply generated");

        for (PfrReplyDocument replyDocument : pfrReply.getDocuments()) {
            signReplyDocumentAndUploadSignature(documentId, replyDocument);
        }

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
        return decryptDocument(docflowId, documentId);
    }

    private void signReplyDocumentAndUploadSignature(
            String documentId,
            PfrReplyDocument replyDocument
    ) {
        String thumbprint = engine.getConfiguration().getThumbprint();

        byte[] encryptedContent = replyDocument.getEncryptedContent();
        byte[] decryptedContent = cryptoUtils.decrypt(thumbprint, encryptedContent);

        engine.getDocflowService()
                .updatePfrReplyDocumentDecryptedContentAsync(
                        replyDocument.getDocflowId(),
                        documentId,
                        replyDocument.getReplyId(),
                        replyDocument.getId(),
                        decryptedContent
                )
                .join().getOrThrow();
        System.out.println("Reply document decrypted");

        byte[] signature = cryptoUtils.sign(thumbprint, decryptedContent);
        engine.getDocflowService()
                .updatePfrReplyDocumentSignatureAsync(
                        replyDocument.getDocflowId(),
                        replyDocument.getDocumentId(),
                        replyDocument.getReplyId(),
                        replyDocument.getId(),
                        signature
                )
                .join().getOrThrow();
        System.out.println(
                "Reply document " + replyDocument.getId() + " signature saved for docflow " + replyDocument
                        .getDocflowId()
                        + " and original doc " + replyDocument.getDocumentId()
        );
    }


    private byte[] decryptDocument(UUID docflowId, UUID documentId) throws ExecutionException, InterruptedException {
        byte[] encrypted = engine.getDocflowService()
                .getEncryptedContentAsync(docflowId, documentId)
                .get()
                .getOrThrow();

        return cryptoUtils.decrypt(engine.getConfiguration().getThumbprint(), encrypted);
    }
}