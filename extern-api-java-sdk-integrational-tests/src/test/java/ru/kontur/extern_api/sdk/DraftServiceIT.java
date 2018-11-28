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
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DraftTestPack;
import ru.kontur.extern_api.sdk.utils.Pair;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.Zip;

class DraftServiceIT {

    private static ExternEngine engine;
    private static List<DraftTestPack> tests;
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
                .map((TestData data) -> new DraftTestPack(data, engine, cryptoUtils))
                .collect(Collectors.toList()
                );
        tests.forEach(DraftTestPack::createNewEmptyDraft);
    }

    @BeforeEach
    void setUp() {
        tests.forEach(DraftTestPack::createNewEmptyDraftIfNecessary);
    }

    private static Stream<QueryContext<UUID>> createDraftCxtFactory() {
        return tests.stream().map(DraftTestPack::createDraftCxtFactory);
    }

    private static Stream<QueryContext<Draft>> getEmptyDraftCxtFactory() {
        return tests.stream().map(DraftTestPack::emptyDraftCxtFactory);
    }

    private static Stream<QueryContext<Draft>> getDraftWithDocumentCxtFactory() {
        return tests.stream().map(DraftTestPack::draftWithDocumentCxtFactory);
    }

    private static Stream<QueryContext<Draft>> getNewDraftWithDocumentCxtFactory() {
        return tests.stream().map(DraftTestPack::newDraftWithDocumentCxtFactory);
    }

    private static Stream<QueryContext<Draft>> getDraftWithSignedDocumentCxtFactory() {
        return tests.stream().map(DraftTestPack::draftWithSignedDocumentCxtFactory);
    }

    private static Stream<Pair<QueryContext<Draft>, QueryContext<DraftDocument>>> getAddDocumentPackFactory() {
        return tests.stream().map(DraftTestPack::addDocumentPackFactory);
    }

    private static Stream<Pair<QueryContext<Draft>, QueryContext<DraftDocument>>> getAddDocumentNoFnsPackFactory() {
        return tests.stream()
                .map(DraftTestPack::addDocumentNoFnsPackFactory)
                .filter(Objects::nonNull);
    }

    /**
     * Test of the createDraft method
     *
     *
     * POST /v1/{accountId}/drafts
     */
    @ParameterizedTest
    @DisplayName("Create draft checking")
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
    @DisplayName("Get draft checking")
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
    @DisplayName("Delete draft checking")
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
    @DisplayName("Get draft meta checking")
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
    @DisplayName("Update draft meta checking")
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
    @DisplayName("Add decrypted draft document checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testAddDecryptedDocument(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack) {

        assertNotNull(addDocumentPack.second.get());
        assertNull(addDocumentPack.second.getServiceError());
    }


    /**
     * Test of the deleteDocument method
     *
     * delete /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("Delete draft document checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testDeleteDocument(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext deleteDocument = engine.getDraftService()
                .deleteDocumentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
                .get();

        assertNull(deleteDocument.getServiceError());
    }

    /**
     * Test of the getDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("Get draft document checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetDocument(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext<DraftDocument> getDocument = engine.getDraftService()
                .lookupDocumentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
                .get();

        assertEquals(addDocumentPack.second.get().getId(), getDocument.get().getId());
        assertEquals(addDocumentPack.second.get().getDecryptedContentLink(),
                getDocument.get().getDecryptedContentLink());

        assertEquals(addDocumentPack.second.get().getEncryptedContentLink(),
                getDocument.get().getEncryptedContentLink());

        assertEquals(addDocumentPack.second.get().getSignatureContentLink(),
                getDocument.get().getSignatureContentLink());

        assertEquals(addDocumentPack.second.get().getDescription(),
                getDocument.get().getDescription());

        assertNull(getDocument.getServiceError());
    }

    /**
     * Test of updateDocument method, of class ExternSDKDocument.
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("Update draft document checking")
    @MethodSource({"getAddDocumentNoFnsPackFactory"})
    void testUpdateDocument(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        DocumentContents newContents = new DocumentContents();
        newContents.setDocumentDescription(new DocumentDescription().filename("my favorite file"));

        QueryContext updateDocument = engine.getDraftService()
                .updateDocumentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId(),
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
    @DisplayName("Print draft document checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testPrintDocument(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        byte[] pdf = engine.getDraftService()
                .getDocumentAsPdfAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
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
    @DisplayName("Get decrypted draft document content checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetDecryptedDocumentContent(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext<byte[]> decrypted = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
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
    @DisplayName("Update decrypted draft document content checking")
    @MethodSource({"getAddDocumentNoFnsPackFactory"})
    void testUpdateDecryptedDocumentContent(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        QueryContext update = engine.getDraftService()
                .updateDecryptedDocumentContentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId(),
                        Base64.getEncoder().encode(new byte[0]))
                .get();

        assertNull(update.getServiceError());
    }

    /**
     * Test of getEncryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     */
    @ParameterizedTest
    @DisplayName("Get encrypted draft document content checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetEncryptedDocumentContent(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        engine.getDraftService().prepareAsync(addDocumentPack.first.get().getId())
                .get()
                .ensureSuccess();

        QueryContext<byte[]> content = engine.getDraftService()
                .getEncryptedDocumentContentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
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
    @DisplayName("Get draft document signature checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testGetSignatureContent(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        // after prepare?

        QueryContext<byte[]> content = engine.getDraftService()
                .getSignatureContentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
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
    @DisplayName("Update draft document signature checking")
    @MethodSource({"getAddDocumentPackFactory"})
    void testUpdateSignature(
            Pair<QueryContext<Draft>, QueryContext<DraftDocument>> addDocumentPack)
            throws ExecutionException, InterruptedException {

        byte[] docContent = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId())
                .get()
                .ensureSuccess()
                .get();

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), Zip.unzip(docContent));

        QueryContext update = engine.getDraftService()
                .updateSignatureAsync(
                        addDocumentPack.first.get().getId(),
                        addDocumentPack.second.get().getId(),
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
    @DisplayName("Command \"Check\" checking")
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
    @DisplayName("Command \"Prepare\" checking")
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
    @DisplayName("Command \"Send\" checking")
    @MethodSource({"getDraftWithDocumentCxtFactory"})
    void testSend(QueryContext<Draft> draftCxt) throws ExecutionException, InterruptedException {

        QueryContext<Docflow> send = engine.getDraftService()
                .sendAsync(draftCxt.get().getId())
                .get();

        assertNull(send.getServiceError());
        assertEquals(send.get().getStatus().getName(), "sent");
    }
}