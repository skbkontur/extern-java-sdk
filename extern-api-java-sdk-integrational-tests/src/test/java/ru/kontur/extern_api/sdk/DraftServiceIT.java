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

@DisplayName("Draft function checking")
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

    private static Stream<UUID> getNewDraftId() {
        return tests.stream().map(DraftTestPack::createDraft);
    }

    private static Stream<Draft> getEmptyDraft() {
        return tests.stream().map(DraftTestPack::emptyDraft);
    }

    private static Stream<Draft> getDraftWithDocument() {
        return tests.stream().map(DraftTestPack::draftWithDocument);
    }

    private static Stream<Draft> getNewDraftWithDocument() {
        return tests.stream().map(DraftTestPack::newDraftWithDocument);
    }

    private static Stream<Draft> getDraftWithSignedDocument() {
        return tests.stream().map(DraftTestPack::draftWithSignedDocument);
    }

    private static Stream<Pair<Draft, DraftDocument>> getAddDocumentPack() {
        return tests.stream().map(DraftTestPack::addDocumentPack);
    }

    private static Stream<Pair<Draft, DraftDocument>> getAddDocumentNoFnsPack() {
        return tests.stream()
                .map(DraftTestPack::addDocumentNoFnsPack)
                .filter(Objects::nonNull);
    }

    /**
     * Test of the createDraft method
     *
     *
     * POST /v1/{accountId}/drafts
     */
    @ParameterizedTest
    @DisplayName("create")
    @MethodSource({"getNewDraftId"})
    void testCreateDraft(UUID draftId) {

        assertNotNull(draftId);
    }

    /**
     * Test of the getDraftById method
     *
     * GET /v1/{accountId}/drafts/{draftId}
     */
    @ParameterizedTest
    @DisplayName("get")
    @MethodSource({
            "getEmptyDraft",
            "getDraftWithDocument",
            "getDraftWithSignedDocument"})
    void testGetDraft(Draft draft) {

        Draft found = engine.getDraftService()
                .lookupAsync(draft.getId())
                .join()
                .getOrThrow();

        assertEquals(draft.getId(), found.getId());
        assertEquals(
                draft.getMeta().getSender().getInn(),
                found.getMeta().getSender().getInn());
    }

    /**
     * DELETE /v1/{accountId}/drafts/{draftId}
     *
     * Test of the deleteDraft method
     */
    @ParameterizedTest
    @DisplayName("delete")
    @MethodSource({
            "getEmptyDraft",
            "getDraftWithDocument",
            "getDraftWithSignedDocument"})
    void testDeleteDraft(Draft draft) {

        UUID id = draft.getId();

        engine.getDraftService().deleteAsync(id).join().ensureSuccess();

        QueryContext<Draft> lookup = engine.getDraftService().lookupAsync(id).join();

        assertTrue(lookup.isFail());
        assertEquals(404, lookup.getServiceError().getResponseCode());
    }

    /**
     * GET /v1/{accountId}/drafts/{draftId}/meta
     *
     * Test of the getDraftMeta method
     */
    @ParameterizedTest
    @DisplayName("get meta")
    @MethodSource({
            "getEmptyDraft",
            "getDraftWithDocument",
            "getDraftWithSignedDocument"})
    void testGetDraftMeta(Draft draft) {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draft.getId())
                .join()
                .getOrThrow();

        assertEquals(
                draft.getMeta().getSender().getInn(),
                draftMeta.getSender().getInn());

    }

    /**
     * Test of the updateDraftMeta method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/meta
     */
    @ParameterizedTest
    @DisplayName("update meta")
    @MethodSource({
            "getEmptyDraft",
            "getDraftWithDocument",
            "getDraftWithSignedDocument"})
    void testUpdateDraftMeta(Draft draft) {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draft.getId())
                .join()
                .getOrThrow();

        String ip = "8.8.8.8";
        draftMeta.getSender().setIpaddress(ip);

        DraftMeta newDraftMeta = engine.getDraftService()
                .updateDraftMetaAsync(draft.getId(), draftMeta)
                .join()
                .getOrThrow();

        assertEquals(ip, newDraftMeta.getSender().getIpaddress());
    }

    /**
     * Test of the addDecryptedDocument method
     *
     * POST /v1/{accountId}/drafts/{draftId}/documents
     */
    @ParameterizedTest
    @DisplayName("add decrypted document")
    @MethodSource({"getAddDocumentPack"})
    void testAddDecryptedDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        assertNotNull(addDocumentPack.second);
    }


    /**
     * Test of the deleteDocument method
     *
     * delete /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("delete document")
    @MethodSource({"getAddDocumentPack"})
    void testDeleteDocument(
            Pair<Draft, DraftDocument> addDocumentPack) {

        QueryContext deleteDocument = engine.getDraftService()
                .deleteDocumentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNull(deleteDocument.getServiceError());
    }

    /**
     * Test of the getDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("get document")
    @MethodSource({"getAddDocumentPack"})
    void testGetDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        DraftDocument draftDocument = addDocumentPack.second;

        QueryContext<DraftDocument> getDocument = engine.getDraftService()
                .lookupDocumentAsync(
                        addDocumentPack.first.getId(),
                        draftDocument.getId())
                .join();

        assertEquals(draftDocument.getId(), getDocument.get().getId());
        assertEquals(draftDocument.getDecryptedContentLink(),
                getDocument.get().getDecryptedContentLink());

        assertEquals(draftDocument.getEncryptedContentLink(),
                getDocument.get().getEncryptedContentLink());

        assertEquals(draftDocument.getSignatureContentLink(),
                getDocument.get().getSignatureContentLink());

        assertEquals(draftDocument.getDescription(),
                getDocument.get().getDescription());

        assertNull(getDocument.getServiceError());
    }

    /**
     * Test of updateDocument method, of class ExternSDKDocument.
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}
     */
    @ParameterizedTest
    @DisplayName("update document")
    @MethodSource({"getAddDocumentNoFnsPack"})
    void testUpdateDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        DocumentContents newContents = new DocumentContents();
        newContents.setDocumentDescription(new DocumentDescription().filename("my favorite file"));

        QueryContext updateDocument = engine.getDraftService()
                .updateDocumentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId(),
                        newContents)
                .join();

        assertNull(updateDocument.getServiceError());
    }

    /**
     * Test of the printDocument method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/print
     */
    @ParameterizedTest
    @DisplayName("print document")
    @MethodSource({"getAddDocumentPack"})
    void testPrintDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        byte[] pdf = engine.getDraftService()
                .getDocumentAsPdfAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join()
                .getOrThrow();

        String pdfFileHeader = "%PDF";
        assertEquals(pdfFileHeader, new String(Arrays.copyOfRange(pdf, 0, 4)));
    }

    /**
     * Test of getDecryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @ParameterizedTest
    @DisplayName("get decrypted document content")
    @MethodSource({"getAddDocumentPack"})
    void testGetDecryptedDocumentContent(Pair<Draft, DraftDocument> addDocumentPack) {

        QueryContext<byte[]> decrypted = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNotNull(decrypted.get());
        assertNull(decrypted.getServiceError());
    }

    /**
     * Test of updateDecryptedDocumentContent method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/decrypted
     */
    @ParameterizedTest
    @DisplayName("update decrypted document content")
    @MethodSource({"getAddDocumentNoFnsPack"})
    void testUpdateDecryptedDocumentContent(Pair<Draft, DraftDocument> addDocumentPack) {

        QueryContext update = engine.getDraftService()
                .updateDecryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId(),
                        Base64.getEncoder().encode(new byte[0]))
                .join();

        assertNull(update.getServiceError());
    }

    /**
     * Test of getEncryptedDocumentContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/content/encrypted
     */
    @ParameterizedTest
    @DisplayName("get encrypted document content")
    @MethodSource({"getAddDocumentPack"})
    void testGetEncryptedDocumentContent(Pair<Draft, DraftDocument> addDocumentPack) {

        engine.getDraftService().prepareAsync(addDocumentPack.first.getId())
                .join()
                .getOrThrow();

        QueryContext<byte[]> content = engine.getDraftService()
                .getEncryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNull(content.getServiceError());
        assertNotNull(content.get());
    }

    /**
     * Test of getSignatureContent method
     *
     * GET /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @ParameterizedTest
    @DisplayName("get document signature")
    @MethodSource({"getAddDocumentPack"})
    void testGetSignatureContent(Pair<Draft, DraftDocument> addDocumentPack) {

        // after prepare?

        QueryContext<byte[]> content = engine.getDraftService()
                .getSignatureContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNull(content.getServiceError());
        assertNotNull(content.get());
    }

    /**
     * Test of updateSignature method
     *
     * PUT /v1/{accountId}/drafts/{draftId}/documents/{documentId}/signature
     */
    @ParameterizedTest
    @DisplayName("update document signature")
    @MethodSource({"getAddDocumentPack"})
    void testUpdateSignature(Pair<Draft, DraftDocument> addDocumentPack) {

        byte[] docContent = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join()
                .getOrThrow();

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), Zip.unzip(docContent));

        QueryContext update = engine.getDraftService()
                .updateSignatureAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId(),
                        signature)
                .join();

        assertNull(update.getServiceError());

    }

    /**
     * Test of check method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/check
     */
    @ParameterizedTest
    @DisplayName("command \"Check\"")
    @MethodSource({"getNewDraftWithDocument"})
    void testCheck(Draft draft) {

        QueryContext<CheckResultData> checkResult = engine.getDraftService()
                .checkAsync(draft.getId())
                .join();

        assertTrue(checkResult.get().hasNoErrors());
    }

    /**
     * Test of prepare method
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/prepare
     */
    @ParameterizedTest
    @DisplayName("command \"Prepare\"")
    @MethodSource({"getNewDraftWithDocument"})
    void testPrepare(Draft draft) {

        QueryContext<PrepareResult> prepareResult = engine.getDraftService()
                .prepareAsync(draft.getId())
                .join();

        assertTrue(prepareResult.get().getStatus() == Status.OK ||
                prepareResult.get().getStatus() == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);

    }


    /**
     * Test of send method.
     *
     * POST /v1/{accountId}/drafts/drafts/{draftId}/send
     */
    @ParameterizedTest
    @DisplayName("command \"Send\"")
    @MethodSource({"getDraftWithDocument"})
    void testSend(Draft draft) {

        QueryContext<Docflow> send = engine.getDraftService()
                .sendAsync(draft.getId())
                .join();

        assertNull(send.getServiceError());
        assertEquals(send.get().getStatus().getName(), "sent");
    }
}