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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.FnsRecipient;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DocType;
import ru.kontur.extern_api.sdk.utils.EngineUtils;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;
import org.apache.commons.lang3.tuple.Pair;
import ru.kontur.extern_api.sdk.utils.Zip;

class DraftServiceIT {

    private static ExternEngine engine;
    private static List<TestPack> tests;
    private static CryptoUtils cryptoUtils;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        tests = Arrays
                .stream(TestUtils
                        .getTestData(
                                cryptoUtils.loadX509(engine.getConfiguration().getThumbprint())))
                .map(TestPack::new)
                .collect(Collectors.toList()
                );
        tests.forEach(TestPack::createNewEmptyDraft);
    }

    @BeforeEach
    void setUp() {
        tests.forEach(TestPack::createNewEmptyDraftIfNecessary);
    }

    static class TestPack {

        final TestData data;
        final DraftMeta meta;

        private QueryContext<UUID> createDefaultDraftCxt;

        TestPack(TestData data) {

            this.data = data;
            meta = TestUtils.toDraftMeta(data);
        }

        private QueryContext<UUID> getCreateDraftCxt() {

            if (getDraft().get().getDocuments().size() > 0) {
                createNewEmptyDraft();
            }
            return createDefaultDraftCxt;
        }

        private void createNewEmptyDraftIfNecessary() {

            QueryContext<Draft> draft = getDraft();

            if (createDefaultDraftCxt == null
                    || draft.getServiceError() != null
                    || draft.get().getStatus().name().equals("sent")) {
                createNewEmptyDraft();
            }
        }

        private void createNewEmptyDraft() {

            createDefaultDraftCxt = UncheckedSupplier.get(() -> engine.getDraftService()
                    .createAsync(meta)
                    .get()
                    .ensureSuccess())
                    .map(QueryContext.DRAFT_ID, Draft::getId);
        }

        private QueryContext<Draft> getEmptyDraft() {

            QueryContext<Draft> getDraftCxt = getDraft();

            if (getDraftCxt.get().getDocuments().size() > 0) {
                createNewEmptyDraft();
                return getDraft();
            }
            return getDraftCxt;
        }

        private QueryContext<Draft> getDraft() {

            return UncheckedSupplier.get(() -> engine.getDraftService()
                    .lookupAsync(createDefaultDraftCxt.get())
                    .get());
        }

        private QueryContext<DraftDocument> addDocument() {

            String path = data.getDocs()[0];
            DocType docType = DocType.getDocType(meta.getRecipient());
            DocumentContents documentContents = EngineUtils.with(engine)
                    .createDocumentContents(path, docType);

            return UncheckedSupplier.get(() -> engine
                    .getDraftService()
                    .addDecryptedDocumentAsync(createDefaultDraftCxt.get(), documentContents)
                    .get()
                    .ensureSuccess());
        }

        private void signDocument(UUID documentId) {

            byte[] docContent = UncheckedSupplier.get(() -> engine.getDraftService()
                    .getDecryptedDocumentContentAsync(
                            createDefaultDraftCxt.get(),
                            documentId)
                    .get()
                    .ensureSuccess()
                    .get());

            byte[] signature = cryptoUtils
                    .sign(engine.getConfiguration().getThumbprint(), Zip.unzip(docContent));

            UncheckedSupplier.get(() -> engine.getDraftService()
                    .updateSignatureAsync(
                            createDefaultDraftCxt.get(),
                            documentId,
                            signature)
                    .get());
        }
    }


    private static Stream<QueryContext<UUID>> createDraftCxtFactory() {
        return tests.stream().map(TestPack::getCreateDraftCxt);
    }

    private static Stream<QueryContext<Draft>> getEmptyDraftCxtFactory() {
        return tests.stream().map(TestPack::getEmptyDraft);
    }

    private static Stream<QueryContext<Draft>> getDraftWithDocumentCxtFactory() {
        return tests.stream().map(testPack -> {
            testPack.addDocument();
            return testPack.getDraft();
        });
    }

    private static Stream<QueryContext<Draft>> getNewDraftWithDocumentCxtFactory() {
        return tests.stream().map(testPack -> {
            testPack.createNewEmptyDraft();
            testPack.addDocument();
            return testPack.getDraft();
        });
    }

    private static Stream<QueryContext<Draft>> getDraftWithSignedDocumentCxtFactory() {
        return tests.stream().map(testPack -> {
            DraftDocument document = testPack.addDocument().get();
            testPack.signDocument(document.getId());
            return testPack.getDraft();
        });
    }

    private static Stream<Pair<QueryContext<Draft>, QueryContext<DraftDocument>>> getAddDocumentPackFactory() {
        return tests.stream().map(testPack -> new ImmutablePair<>(
                testPack.getEmptyDraft(), testPack.addDocument()));
    }

    private static Stream<Pair<QueryContext<Draft>, QueryContext<DraftDocument>>> getAddDocumentNoFnsPackFactory() {
        return tests.stream()
                .filter(testPack -> !(testPack.meta.getRecipient() instanceof FnsRecipient))
                .map(testPack -> new ImmutablePair<>(
                        testPack.getEmptyDraft(), testPack.addDocument()));
    }

    /**
     * Test of the createDraft method
     *
     *
     * POST /v1/{accountId}/drafts
     */
    @ParameterizedTest
    @MethodSource({"createDraftCxtFactory"})
    void testCreateDraft(QueryContext<UUID> draftCxt) {

        assertNotNull(draftCxt.get());
        assertNull(draftCxt.getServiceError());
    }

    /**
     * Test of the getDraftById method
     *
     * GET /v1/{accountId}/drafts/{draftId}
     */
    @ParameterizedTest
    @MethodSource({
            "getEmptyDraftCxtFactory",
            "getDraftWithDocumentCxtFactory",
            "getDraftWithSignedDocumentCxtFactory"})
    void testGetDraft(QueryContext<Draft> draftCxt)
            throws ExecutionException, InterruptedException {

        Draft found = engine.getDraftService()
                .lookupAsync(draftCxt.get().getId())
                .get()
                .ensureSuccess()
                .get();

        assertEquals(draftCxt.get().getId(), found.getId());
        assertEquals(
                draftCxt.getDraft().getMeta().getSender().getInn(),
                found.getMeta().getSender().getInn());
    }

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * Test of the deleteDraft method
     */
    @ParameterizedTest
    @MethodSource({
            "getEmptyDraftCxtFactory",
            "getDraftWithDocumentCxtFactory",
            "getDraftWithSignedDocumentCxtFactory"})
    void testDeleteDraft(QueryContext<Draft> draftCxt)
            throws ExecutionException, InterruptedException {

        UUID id = draftCxt.get().getId();

        engine.getDraftService().deleteAsync(id).get().ensureSuccess();

        QueryContext<Draft> lookup = engine.getDraftService().lookupAsync(id).get();

        assertTrue(lookup.isFail());
        assertEquals(404, lookup.getServiceError().getResponseCode());
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/meta
     *
     * Test of the getDraftMeta method
     */
    @ParameterizedTest
    @MethodSource({
            "getEmptyDraftCxtFactory",
            "getDraftWithDocumentCxtFactory",
            "getDraftWithSignedDocumentCxtFactory"})
    void testGetDraftMeta(QueryContext<Draft> draftCxt)
            throws ExecutionException, InterruptedException {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draftCxt.get().getId())
                .get()
                .ensureSuccess()
                .get();

        assertEquals(
                draftCxt.getDraft().getMeta().getSender().getInn(),
                draftMeta.getSender().getInn());

    }

    /**
     * Test of the updateDraftMeta method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/meta
     */
    @ParameterizedTest
    @MethodSource({
            "getEmptyDraftCxtFactory",
            "getDraftWithDocumentCxtFactory",
            "getDraftWithSignedDocumentCxtFactory"})
    void testUpdateDraftMeta(QueryContext<Draft> draftCxt)
            throws ExecutionException, InterruptedException {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draftCxt.get().getId())
                .get()
                .ensureSuccess()
                .get();

        String ip = "8.8.8.8";
        draftMeta.getSender().setIpaddress(ip);

        DraftMeta newDraftMeta = engine.getDraftService()
                .updateDraftMetaAsync(draftCxt.get().getId(), draftMeta)
                .get()
                .ensureSuccess()
                .get();

        assertEquals(ip, newDraftMeta.getSender().getIpaddress());
    }

    /**
     * Test of the addDecryptedDocument method
     *
     * POST /v1/{accountId}/drafts/{draftId}/documents
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testAddDecryptedDocument(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack) {

        assertNotNull(addDocumentPack.getValue().get());
        assertNull(addDocumentPack.getValue().getServiceError());
    }


    /**
     * Test of the deleteDocument method
     *
     * delete /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testDeleteDocument(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext deleteDocument = engine.getDraftService()
                .deleteDocumentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get();

        assertNull(deleteDocument.getServiceError());
    }

    /**
     * Test of the getDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetDocument(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext<DraftDocument> getDocument = engine.getDraftService()
                .lookupDocumentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get();

        assertEquals(addDocumentPack.getValue().get().getId(), getDocument.get().getId());
        assertEquals(addDocumentPack.getValue().get().getDecryptedContentLink(),
                getDocument.get().getDecryptedContentLink());

        assertEquals(addDocumentPack.getValue().get().getEncryptedContentLink(),
                getDocument.get().getEncryptedContentLink());

        assertEquals(addDocumentPack.getValue().get().getSignatureContentLink(),
                getDocument.get().getSignatureContentLink());

        assertEquals(addDocumentPack.getValue().get().getDescription(),
                getDocument.get().getDescription());

        assertNull(getDocument.getServiceError());
    }

    /**
     * Test of updateDocument method, of class ExternSDKDocument.
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentNoFnsPackFactory"})
    void testUpdateDocument(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        DocumentContents newContents = new DocumentContents();
        newContents.setDocumentDescription(new DocumentDescription().filename("my favorite file"));

        QueryContext updateDocument = engine.getDraftService()
                .updateDocumentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId(),
                        newContents)
                .get();

        assertNull(updateDocument.getServiceError());
    }

    /**
     * Test of the printDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testPrintDocument(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        byte[] pdf = engine.getDraftService()
                .getDocumentAsPdfAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get()
                .ensureSuccess()
                .get();

        String pdfFileHeader = "%PDF";
        assertEquals(pdfFileHeader, new String(Arrays.copyOfRange(pdf, 0, 4)));
    }

    /**
     * Test of getDecryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetDecryptedDocumentContent(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext<byte[]> decrypted = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get();

        assertNotNull(decrypted.get());
        assertNull(decrypted.getServiceError());
    }

    /**
     * Test of updateDecryptedDocumentContent method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentNoFnsPackFactory"})
    void testUpdateDecryptedDocumentContent(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext update = engine.getDraftService()
                .updateDecryptedDocumentContentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId(),
                        Base64.encodeBase64(new byte[0]))
                .get();

        assertNull(update.getServiceError());
    }

    /**
     * Test of getEncryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetEncryptedDocumentContent(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        engine.getDraftService().prepareAsync(addDocumentPack.getKey().get().getId())
                .get()
                .ensureSuccess();

        QueryContext<byte[]> content = engine.getDraftService()
                .getEncryptedDocumentContentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get();

        assertNull(content.getServiceError());
        assertNotNull(content.get());
    }

    /**
     * Test of getSignatureContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetSignatureContent(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        // after prepare?

        QueryContext<byte[]> content = engine.getDraftService()
                .getSignatureContentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get();

        assertNull(content.getServiceError());
        assertNotNull(content.get());
    }

    /**
     * Test of updateSignature method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @ParameterizedTest
    @MethodSource({"getAddDocumentPackFactory"})
    void testUpdateSignature(
            ImmutablePair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        byte[] docContent = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId())
                .get()
                .ensureSuccess()
                .get();

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), Zip.unzip(docContent));

        QueryContext update = engine.getDraftService()
                .updateSignatureAsync(
                        addDocumentPack.getKey().get().getId(),
                        addDocumentPack.getValue().get().getId(),
                        signature)
                .get();

        assertNull(update.getServiceError());

    }

    /**
     * Test of check method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/check
     */
    @ParameterizedTest
    @MethodSource({"getNewDraftWithDocumentCxtFactory"})
    void testCheck(QueryContext<Draft> draftCxt) throws ExecutionException, InterruptedException {

        QueryContext<CheckResultData> checkResult = engine.getDraftService()
                .checkAsync(draftCxt.get().getId())
                .get()
                .ensureSuccess();

        assertTrue(checkResult.get().hasNoErrors());
    }

    /**
     * Test of prepare method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/prepare
     */
    @ParameterizedTest
    @MethodSource({"getNewDraftWithDocumentCxtFactory"})
    void testPrepare(QueryContext<Draft> draftCxt) throws ExecutionException, InterruptedException {

        QueryContext<PrepareResult> prepareResult = engine.getDraftService()
                .prepareAsync(draftCxt.get().getId())
                .get();

        assertTrue(prepareResult.get().getStatus() == Status.OK ||
                prepareResult.get().getStatus() == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);

    }


    /**
     * Test of send method.
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     */
    @ParameterizedTest
    @MethodSource({"getDraftWithDocumentCxtFactory"})
    void testSend(QueryContext<Draft> draftCxt) throws ExecutionException, InterruptedException {

        QueryContext<Docflow> send = engine.getDraftService()
                .sendAsync(draftCxt.get().getId())
                .get();

        assertNull(send.getServiceError());
        assertEquals(send.get().getStatus().getName(), "sent");
    }
}