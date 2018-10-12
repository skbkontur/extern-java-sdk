/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.it.model.TestData;
import ru.kontur.extern_api.sdk.it.utils.AbstractTest;
import ru.kontur.extern_api.sdk.it.utils.DocType;
import ru.kontur.extern_api.sdk.it.utils.OrganizationServiceUtils;
import ru.kontur.extern_api.sdk.it.utils.TestUtils;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.Lazy;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;
import ru.kontur.extern_api.sdk.utils.Zip;


class DraftServiceTest extends AbstractTest {

    private static OrganizationServiceUtils orgUtils;

    static class TestPack {

        final TestData data;

        final DraftMeta meta;

        final Lazy<QueryContext<UUID>> draft = Lazy.of(this::newDraft);

        final Lazy<QueryContext<UUID>> withDocument = Lazy.of(this::newDraftWithDoc);

        TestPack(TestData data) {
            this.data = data;
            this.meta = TestUtils.toDraftMeta(data);
        }

        private QueryContext<UUID> newDraft() {
            return engine.getDraftService()
                    .create(new QueryContext<>().setDraftMeta(meta))
                    .ensureSuccess();
        }

        private QueryContext<UUID> newDraftWithDoc() {
            UUID id = newDraft().getOrThrow();
            return new QueryContext<>(QueryContext.DRAFT_ID, id)
                    .setDocumentId(addDocument(id));

        }

        private UUID addDocument(UUID draftId) {
            String path = data.getDocs()[0];
            DocType docType = DocType.getDocType(meta.getRecipient());
            DocumentContents dc = createDocumentContents(engine, path, docType);

            DraftDocument document = UncheckedSupplier.get(() -> engine
                    .getDraftService()
                    .addDecryptedDocumentAsync(draftId, dc)
                    .get()
                    .getOrThrow());

            return document.getId();
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
    void testUpdateDraftMeta() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            DraftMeta draftMeta = draftService
                    .lookupDraftMetaAsync(draftId)
                    .get()
                    .getOrThrow();

            String ip = "8.8.8.8";
            draftMeta.getSender().setIpaddress(ip);

            DraftMeta newDraftMeta = draftService
                    .updateDraftMetaAsync(draftId, draftMeta)
                    .get()
                    .getOrThrow();

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
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);
        }

    }

    /**
     * Test of the deleteDocument method
     *
     * delete /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testDeleteDocument() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);
            draftService.deleteDocumentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();
        }

    }

    /**
     * Test of the getDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testGetDocument() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);
            draftService.lookupDocumentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();
        }

    }

    /**
     * Test of updateDocument method, of class ExternSDKDocument.
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @Test
    void testUpdateDocument() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            if (!(test.meta.getRecipient() instanceof FnsRecipient)) {
                continue;
            }

            String path = test.data.getDocs()[0];

            DraftDocument newDoc = draftService.updateDocumentAsync(
                    draftId,
                    documentId,
                    DraftServiceTest.createDocumentContents(
                            engine,
                            this.getUpdatedDocumentPath(path),
                            DocType.FNS
                    )
            ).get().getOrThrow();
        }
    }

    /**
     * Test of the printDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     */
    @Test
    void testPrintDocument() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            byte[] pdf = draftService
                    .getDocumentAsPdfAsync(draftId, documentId)
                    .get()
                    .getOrThrow();

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
    void testGetDecryptedDocumentContent() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            byte[] decrypted = draftService
                    .getDecryptedDocumentContentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();

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
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            if (!(test.meta.getRecipient() instanceof FnsRecipient)) {
                continue;
            }

            String path = test.data.getDocs()[0];
            byte[] updatedContent = this.loadUpdateDocument(path);

            draftService
                    .updateDecryptedDocumentContentAsync(draftId, documentId, updatedContent)
                    .get()
                    .getOrThrow();

        }
    }

    /**
     * Test of getEncryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     */
    @Test
    void testGetEncryptedDocumentContent() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.newDraft().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            draftService.prepareAsync(draftId).get().getOrThrow();

            byte[] content = draftService
                    .getEncryptedDocumentContentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();
        }
    }

    /**
     * Test of getSignatureContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @Test
    void testGetSignatureContent() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            // after prepare?

            byte[] content = draftService
                    .getSignatureContentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();
        }

    }

    /**
     * Test of updateSignature method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @Test
    void testUpdateSignature() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.draft.get().getOrThrow();
            UUID documentId = test.addDocument(draftId);

            byte[] docContent = draftService
                    .getDecryptedDocumentContentAsync(draftId, documentId)
                    .get()
                    .getOrThrow();

            byte[] signature = sign(engine, Zip.unzip(docContent));

            draftService
                    .updateSignatureAsync(draftId, documentId, signature)
                    .get()
                    .getOrThrow();
        }

    }

    /**
     * Test of check method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/check
     */
    @Test
    void testCheck() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.withDocument.get().getOrThrow();

            CheckResultData checkResult = draftService.checkAsync(draftId)
                    .get()
                    .getOrThrow();

            Assertions.assertTrue(checkResult.hasNoErrors());
        }
    }

    /**
     * Test of prepare method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/prepare
     */
    @Test
    void testPrepare() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.withDocument.get().getOrThrow();

            // after sign

            PrepareResult prepareResult = draftService.prepareAsync(draftId)
                    .get()
                    .getOrThrow();

            Assertions.assertTrue(prepareResult.getStatus() == Status.OK ||
                    prepareResult.getStatus() == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);
        }
    }


    /**
     * Test of send method.
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     */
    @Test
    void testSend() throws Exception {
        for (TestPack test : tests.get()) {
            UUID draftId = test.newDraftWithDoc().getOrThrow();

            // sign !

            draftService.sendAsync(draftId)
                    .get()
                    .getOrThrow();
        }
    }

}
