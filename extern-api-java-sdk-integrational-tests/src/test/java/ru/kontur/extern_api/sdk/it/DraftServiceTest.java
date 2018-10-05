/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.Sender;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.it.model.TestData;
import ru.kontur.extern_api.sdk.it.utils.AbstractTest;
import ru.kontur.extern_api.sdk.it.utils.DocType;
import ru.kontur.extern_api.sdk.it.utils.OrganizationServiceUtils;
import ru.kontur.extern_api.sdk.it.utils.TestUtils;
import ru.kontur.extern_api.sdk.utils.Lazy;


class DraftServiceTest extends AbstractTest {

    private static OrganizationServiceUtils orgUtils;

    static class TestPack {

        final TestData data;

        final Lazy<QueryContext<UUID>> draft = Lazy.of(this::newDraft);

        TestPack(TestData data) {
            this.data = data;
        }

        private QueryContext<UUID> newDraft() {
            return engine.getDraftService()
                    .create(new QueryContext<>().setDraftMeta(TestUtils.toDraftMeta(data)))
                    .ensureSuccess();
        }
    }

    private final Lazy<List<TestPack>> tests = Lazy.of(() -> Arrays
            .stream(getTestData(loadX509(engine)))
            .map(TestPack::new)
            .collect(Collectors.toList())
    );

    @BeforeAll
    static void setUpClass() {
        AbstractTest.initEngine();
        engine.setCryptoProvider(new CryptoProviderMSCapi());

        orgUtils = new OrganizationServiceUtils(
                engine.getOrganizationService(),
                engine.getAccountProvider().accountId().toString()
        );
    }

    @BeforeEach
    void setUp() {
        draftService = engine.getDraftService();
        docflowService = engine.getDocflowService();
    }

    /**
     * Test of the createDraft method
     *
     *
     * POST /v1/{accountId}/drafts
     */
    @Test
    void testCreateDraft() {
        for (TestPack test : tests.get()) {
            Assert.assertNotNull(test.newDraft().get());
        }
    }

    /**
     * Test of the getDraftById method
     *
     * GET /v1/{accountId}/drafts/{draftId}
     */
    @Test
    void testGetDraft() {
        for (TestPack testPack : tests.get()) {
            Draft found = draftService
                    .lookup(testPack.draft.get())
                    .ensureSuccess()
                    .get();

            QueryContext<UUID> draftCxt = testPack.draft.get();
            Assert.assertEquals(draftCxt.getDraftId(), found.getId());

            String inn = testPack.data.getClientInfo().getSender().getInn();

            Assert.assertEquals(inn, found.getMeta().getSender().getInn());
        }
    }

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * Test of the deleteDraft method
     */
    @Test
    void testDeleteDraft() {

        for (TestPack testPack : tests.get()) {
            QueryContext<UUID> cxt = testPack.newDraft();
            draftService.delete(cxt).ensureSuccess();

            QueryContext<Draft> lookup = draftService.lookup(cxt);
            assertTrue(lookup.isFail());
            assertEquals(404, lookup.getServiceError().getResponseCode());
        }
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/meta
     *
     * Test of the getDraftMeta method
     */
    @Test
    void testGetDraftMeta() {
        for (TestPack testPack : tests.get()) {
            DraftMeta found = draftService
                    .lookupDraftMeta(testPack.draft.get())
                    .ensureSuccess()
                    .get();

            String inn = testPack.data.getClientInfo().getSender().getInn();
            Assert.assertEquals(inn, found.getSender().getInn());
        }
    }

    /**
     * Test of the updateDraftMeta method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/meta
     */
    @Test
    void testUpdateDraftMeta() {
        for (TestPack testPack : tests.get()) {
            QueryContext<DraftMeta> draftMetaCxt = draftService
                    .lookupDraftMeta(testPack.draft.get())
                    .ensureSuccess();

            DraftMeta draftMeta = draftMetaCxt.getDraftMeta();
            String ip = "8.8.8.8";
            draftMeta.getSender().setIpaddress(ip);

            DraftMeta newDraftMeta = draftService
                    .updateDraftMeta(draftMetaCxt.setDraftMeta(draftMeta))
                    .ensureSuccess()
                    .get();

            assertEquals(ip, newDraftMeta.getSender().getIpaddress());
        }

    }

    /**
     * Test of the addDecryptedDocument method
     *
     * POST /v1/{accountId}/drafts/{draftId}/documents
     */
    @Test
    void testAddDecryptedDocument() {
        for (TestPack testPack : tests.get()) {
            QueryContext<UUID> draftCxt = testPack.draft.get();

            DocType docType = DocType.getDocType(draftCxt.getDraftMeta().getRecipient());
            QueryContext<DraftDocument> draftDocCxt = addDecryptedDocument(
                    engine,
                    draftCxt,
                    testPack.data.getDocs()[0],
                    docType);

            draftDocCxt.ensureSuccess();
            assertNotNull(draftDocCxt.get());
        }

    }

    /**
     * Test of the deleteDocument method
     *
     * delete /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testDeleteDocument() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            if (td.getDocs() == null || td.getDocs().length == 0) {
                fail("No a test document");
            }

            DocType docType = DocType
                    .getDocType(draftCxt.getDraftMeta().getRecipient());
            String doc = td.getDocs()[0];
            QueryContext<Void> deletedDocCxt = CompletableFuture
                    .supplyAsync(() -> addDecryptedDocument(engine, draftCxt, doc, docType))
                    .thenApply(draftService::deleteDocument)
                    .get();

            deletedDocCxt.ensureSuccess();

            assertNull(deletedDocCxt.get());
        }

    }

    /**
     * Test of the getDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testGetDocument() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            if (td.getDocs() == null || td.getDocs().length == 0) {
                fail("No a test document");
            }

            QueryContext<DraftDocument> draftDocumentCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::lookupDocument)
                    .get();

            draftDocumentCxt.ensureSuccess();

            assertNotNull(draftDocumentCxt.get());
        }

    }

    /**
     * Test of updateDocument method, of class ExternSDKDocument.
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testUpdateDocument() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            if (!(draftCxt.getDraftMeta().getRecipient() instanceof FnsRecipient)) {
                continue;
            }

            String path = td.getDocs()[0];

            QueryContext<DraftDocument> draftDocumentCxt
                    = CompletableFuture
                    .supplyAsync(() -> addDecryptedDocument(engine, draftCxt, path,
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(
                            cxt -> draftService.updateDocument(
                                    cxt.setDocumentContents(
                                            DraftServiceTest.createDocumentContents(engine,
                                                    this.getUpdatedDocumentPath(path),
                                                    DocType.FNS)
                                    )
                            )
                    ).get();

            draftDocumentCxt.ensureSuccess();

            assertNotNull(draftDocumentCxt.get());
        }

    }

    /**
     * Test of the printDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     */
    @Test
    void testPrintDocument() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;
            DocType docType = DocType.getDocType(draftCxt.getDraftMeta().getRecipient());

            String pdfBase64 = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0], docType))
                    .thenApply(draftService::printDocument)
                    .get()
                    .ensureSuccess()
                    .get();

            byte[] pdf = Base64.getDecoder().decode(pdfBase64);
            String pdfFileHeader = "%PDF";
            assertEquals(pdfFileHeader, new String(Arrays.copyOfRange(pdf, 0, 4)));

        }

    }

    /**
     * Test of getDecryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @Test
    void testGetDecryptedDocumentContent() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            QueryContext<String> draftDocumentContentCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::getDecryptedDocumentContent)
                    .get();

            draftDocumentContentCxt.ensureSuccess();

            assertNotNull(draftDocumentContentCxt.get());
        }

    }

    /**
     * Test of updateDecryptedDocumentContent method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @Test
    void testUpdateDecryptedDocumentContent() throws Exception {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            if (!(draftCxt.getDraftMeta().getRecipient() instanceof FnsRecipient)) {
                continue;
            }

            String path = td.getDocs()[0];

            byte[] updatedContent = this.loadUpdateDocument(path);

            CompletableFuture
                    .supplyAsync(() -> addDecryptedDocument(engine, draftCxt, path,
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(cxt -> draftService
                            .updateDecryptedDocumentContent(cxt.setContent(updatedContent)))
                    .thenApply(QueryContext::ensureSuccess)
                    .get();
        }
    }

    /**
     * Test of getEncryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     */
    @Test
    void testGetEncryptedDocumentContent() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            QueryContext<String> draftDocumentContentCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::prepare)
                    .thenApply(draftService::getEncryptedDocumentContent)
                    .get();

            draftDocumentContentCxt.ensureSuccess();

            assertNotNull(draftDocumentContentCxt.get());
        }

    }

    /**
     * Test of getSignatureContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @Test
    void testGetSignatureContent() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            QueryContext<String> signatureContentCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::getSignatureContent)
                    .get();

            signatureContentCxt.ensureSuccess();

            assertNotNull(signatureContentCxt.get());
        }

    }

    /**
     * Test of updateSignature method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @Test
    void testUpdateSignature() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            String thumbprint = engine.getConfiguration().getThumbprint();
            Objects.requireNonNull(thumbprint);

            String path = td.getDocs()[0];

            byte[] signature = sign(engine, loadDocument(path));

            QueryContext<Void> signatureContentCxt
                    = CompletableFuture
                    .supplyAsync(() -> addDecryptedDocument(engine, draftCxt, path,
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(
                            cxt -> draftService.updateSignature(cxt.setContent(signature)))
                    .get();

            signatureContentCxt.ensureSuccess();
        }

    }

    /**
     * Test of check method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/check
     */
    @Test
    void testCheck() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            QueryContext<CheckResultData> checkCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::check)
                    .get();

            checkCxt.ensureSuccess();

            assertNotNull(checkCxt.get());
        }
    }

    /**
     * Test of prepare method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/prepare
     */
    @Test
    void testPrepare() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;

            QueryContext<PrepareResult> prepareCxt
                    = CompletableFuture.supplyAsync(
                    () -> addDecryptedDocument(engine, draftCxt, td.getDocs()[0],
                            DocType.getDocType(draftCxt.getDraftMeta().getRecipient())))
                    .thenApply(draftService::prepare)
                    .get();

            prepareCxt.ensureSuccess();

            assertNotNull(prepareCxt.get());
        }
    }


    /**
     * Test of send method.
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     */
    @Test
    void testSend() throws InterruptedException, ExecutionException {
        for (TestPack test : tests.get()) {

            System.setProperty("httpclient.debug", "");
            QueryContext<UUID> draftCxt = test.draft.get();
            TestData td = test.data;
            String path = td.getDocs()[0];
            DraftMeta draftMeta = draftCxt.getDraftMeta();
            DocType docType = DocType.getDocType(draftMeta.getRecipient());
            Organization payer = draftMeta.getPayer();
            Sender sender = draftMeta.getSender();

            orgUtils.createIfNotExist(payer.getInn(), payer.getKpp());
            orgUtils.createIfNotExist(sender.getInn(), sender.getKpp());

            Docflow docflow = CompletableFuture
                    .completedFuture(draftCxt)
                    .thenApply(cxt -> addDecryptedDocument(engine, cxt, path, docType))
                    .thenApply(cxt -> draftService.send(cxt))
                    .thenApply(QueryContext::ensureSuccess)
                    .thenApply(QueryContext::get)
                    .get();

            assertEquals(DocflowStatus.SENT, docflow.getStatus());
        }
    }

}
