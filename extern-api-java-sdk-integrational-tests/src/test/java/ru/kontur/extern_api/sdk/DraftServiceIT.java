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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.DraftDocument;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.PrepareResult.Status;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DraftTestPack;
import ru.kontur.extern_api.sdk.utils.Pair;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;
import ru.kontur.extern_api.sdk.utils.Zip;


@Execution(ExecutionMode.SAME_THREAD)
@DisplayName("Draft service should be able to")
class DraftServiceIT {

    private static ExternEngine engine;
    private static List<DraftTestPack> tests;
    private static CryptoUtils cryptoUtils;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        String certificate = cryptoUtils.loadX509(engine.getConfiguration().getThumbprint());
        TestData[] testData = TestUtils.getTestData(certificate);
        tests = Arrays
                .stream(testData)
                .map((TestData data) -> new DraftTestPack(data, engine, cryptoUtils))
                .collect(Collectors.toList());

        tests.forEach(DraftTestPack::createNewEmptyDraft);
    }

    @BeforeEach
    void setUp() {
        tests.forEach(DraftTestPack::createNewEmptyDraftIfNecessary);
    }

    private static Stream<UUID> newDraftIdFactory() {
        return tests.stream().map(DraftTestPack::createDraft);
    }

    private static Stream<Draft> emptyDraftFactory() {
        return tests.stream().map(DraftTestPack::emptyDraft);
    }

    private static Stream<Draft> draftWithDocumentFactory() {
        return tests.stream().map(DraftTestPack::draftWithDocument);
    }

    private static Stream<Draft> newDraftWithDocumentFactory() {
        return tests.stream().map(DraftTestPack::newDraftWithDocument);
    }

    private static Stream<Draft> draftWithSignedDocumentFactory() {
        return tests.stream().map(DraftTestPack::draftWithSignedDocument);
    }

    private static Stream<Pair<Draft, DraftDocument>> draftWithNewDocumentFactory() {
        return tests.stream().map(DraftTestPack::addDocumentPack);
    }

    private static Stream<Pair<Draft, DraftDocument>> draftWithNewFnsDocumentFactory() {
        return tests.stream()
                .map(DraftTestPack::addDocumentFnsPack)
                .filter(Objects::nonNull);
    }

    @ParameterizedTest
    @DisplayName("create draft")
    @MethodSource({"newDraftIdFactory"})
    void testCreateDraft(UUID draftId) {
        assertNotNull(draftId);
    }

    @ParameterizedTest
    @DisplayName("get draft")
    @MethodSource({
            "emptyDraftFactory",
            "draftWithDocumentFactory",
            "draftWithSignedDocumentFactory"})
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

    @ParameterizedTest
    @DisplayName("delete")
    @MethodSource({
            "emptyDraftFactory",
            "draftWithDocumentFactory",
            "draftWithSignedDocumentFactory"})
    void testDeleteDraft(Draft draft) {

        UUID id = draft.getId();

        engine.getDraftService().deleteAsync(id).join().ensureSuccess();

        QueryContext<Draft> lookup = engine.getDraftService().lookupAsync(id).join();

        assertTrue(lookup.isFail());
        assertEquals(404, lookup.getServiceError().getResponseCode());
    }

    /**
     *
     */
    @ParameterizedTest
    @DisplayName("get meta")
    @MethodSource({
            "emptyDraftFactory",
            "draftWithDocumentFactory",
            "draftWithSignedDocumentFactory"})
    void testGetDraftMeta(Draft draft) {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draft.getId())
                .join()
                .getOrThrow();

        assertEquals(
                draft.getMeta().getSender().getInn(),
                draftMeta.getSender().getInn());

    }

    @ParameterizedTest
    @DisplayName("update meta")
    @MethodSource({
            "emptyDraftFactory",
            "draftWithDocumentFactory",
            "draftWithSignedDocumentFactory"})
    void testUpdateDraftMeta(Draft draft) {

        DraftMeta draftMeta = engine.getDraftService()
                .lookupDraftMetaAsync(draft.getId())
                .join()
                .getOrThrow();

        String ip = "8.8.8.8";
        draftMeta.getSender().setIpaddress(ip);

        DraftMeta newDraftMeta = engine.getDraftService()
                .updateDraftMetaAsync(draft.getId(), draftMeta.asRequest())
                .join()
                .getOrThrow();

        assertEquals(ip, newDraftMeta.getSender().getIpaddress());
    }

    @ParameterizedTest
    @DisplayName("add decrypted document")
    @MethodSource({"draftWithNewDocumentFactory"})
    void testAddDecryptedDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        assertNotNull(addDocumentPack.second);
    }


    @ParameterizedTest
    @DisplayName("delete document")
    @MethodSource({"draftWithNewDocumentFactory"})
    void testDeleteDocument(
            Pair<Draft, DraftDocument> addDocumentPack) {

        QueryContext<Void> deleteDocument = engine.getDraftService()
                .deleteDocumentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNull(deleteDocument.getServiceError());
    }

    @ParameterizedTest
    @DisplayName("get document")
    @MethodSource({"draftWithNewDocumentFactory"})
    void testGetDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        DraftDocument draftDocument = addDocumentPack.second;

        QueryContext<DraftDocument> getDocument = engine.getDraftService()
                .lookupDocumentAsync(
                        addDocumentPack.first.getId(),
                        draftDocument.getId())
                .join();

        DraftDocument document = getDocument.getOrThrow();

        assertEquals(draftDocument.getId(), document.getId());
        assertEquals(draftDocument.getDecryptedContentLink(),
                document.getDecryptedContentLink());

        assertEquals(draftDocument.getEncryptedContentLink(),
                document.getEncryptedContentLink());

        assertEquals(draftDocument.getSignatureContentLink(),
                document.getSignatureContentLink());

        assertEquals(draftDocument.getDescription().getType(),
                document.getDescription().getType());

        assertEquals(1, document.getContents().size());

        assertNull(getDocument.getServiceError());
    }

    @ParameterizedTest
    @DisplayName("update document")
    @MethodSource({"draftWithNewFnsDocumentFactory"})
    void testUpdateDocument(Pair<Draft, DraftDocument> addDocumentPack) {

        DocumentContents newContents = new DocumentContents();
        newContents.setDescription(new DocumentDescription().filename("my favorite file"));

        QueryContext<DraftDocument> updateDocument = engine.getDraftService()
                .updateDocumentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId(),
                        newContents)
                .join();

        assertNull(updateDocument.getServiceError());
    }

    @ParameterizedTest
    @DisplayName("print document")
    @MethodSource({"draftWithNewDocumentFactory"})
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

    @ParameterizedTest
    @DisplayName("get decrypted document content")
    @MethodSource({"draftWithNewDocumentFactory"})
    void testGetDecryptedDocumentContent(Pair<Draft, DraftDocument> addDocumentPack) {

        QueryContext<byte[]> decrypted = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join();

        assertNotNull(decrypted.get());
        assertNull(decrypted.getServiceError());
    }

    @ParameterizedTest
    @DisplayName("get encrypted document content")
    @MethodSource({"draftWithNewDocumentFactory"})
    @Disabled
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

    @ParameterizedTest
    @DisplayName("get document signature")
    @MethodSource({"draftWithNewDocumentFactory"})
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

    @ParameterizedTest
    @DisplayName("update document signature")
    @MethodSource({"draftWithNewDocumentFactory"})
    void testUpdateSignature(Pair<Draft, DraftDocument> addDocumentPack) {

        byte[] docContent = engine.getDraftService()
                .getDecryptedDocumentContentAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId())
                .join()
                .getOrThrow();


        if (Zip.isZip(docContent))
            docContent = Zip.unzip(docContent);

        byte[] signature = cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), docContent);

        QueryContext<Void> update = engine.getDraftService()
                .updateSignatureAsync(
                        addDocumentPack.first.getId(),
                        addDocumentPack.second.getId(),
                        signature)
                .join();

        assertNull(update.getServiceError());

    }

    @ParameterizedTest
    @DisplayName("command \"Check\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    void testCheck(Draft draft) {

        QueryContext<CheckResultData> checkResult = engine.getDraftService()
                .checkAsync(draft.getId())
                .join();

        assertTrue(checkResult.getOrThrow().hasNoErrors());
    }

    @ParameterizedTest
    @DisplayName("command \"Prepare\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    @Disabled
    void testPrepare(Draft draft) {

        PrepareResult prepareResult = engine.getDraftService()
                .prepareAsync(draft.getId())
                .join()
                .getOrThrow();

        Status status = prepareResult.getStatus();
        assertTrue(status == Status.OK || status == Status.CHECK_PROTOCOL_HAS_ONLY_WARNINGS);

    }

    @ParameterizedTest
    @DisplayName("command \"Send\"")
    @MethodSource({"newDraftWithDocumentFactory"})
    @Disabled
    void testSend(Draft draft) {

        QueryContext<Docflow> send = engine.getDraftService()
                .sendAsync(draft.getId())
                .join();

        assertNull(send.getServiceError());
        assertEquals(send.getOrThrow().getStatus().getName(), "sent");
    }
}
