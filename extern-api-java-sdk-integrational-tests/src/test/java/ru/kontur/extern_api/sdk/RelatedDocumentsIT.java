/*
 * Copyright (c) 2019 SKB Kontur
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
 */

package ru.kontur.extern_api.sdk;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static ru.kontur.extern_api.sdk.utils.TestUtils.fromWin1251Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentType;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.InventoriesPage;
import ru.kontur.extern_api.sdk.model.Inventory;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DemandTestData;
import ru.kontur.extern_api.sdk.utils.DemandTestDataProvider;
import ru.kontur.extern_api.sdk.utils.RelatedDocflowProvider;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@DisplayName("RelatedDocuments service should be able to")
class RelatedDocumentsIT {

    private static final String IFNS_CODE = "0087";

    private static ExternEngine engine;
    private static List<DemandTestData> tests;
    private static int sentRelatedInventories;
    private static int sentRelatedLetters;
    private static RelatedDocflowProvider relatedDocflowProvider;

    @BeforeAll
    static void setUpClass() {
        TestSuite testSuite = TestSuite.Load();
        engine = testSuite.engine;
        relatedDocflowProvider = new RelatedDocflowProvider(engine, IFNS_CODE);
        CryptoUtils cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());

        String certificate = cryptoUtils.loadX509(engine.getConfiguration().getThumbprint());
        TestData[] testData = TestUtils.getTestData(certificate);

        tests = Arrays
                .stream(testData)
                .filter(data -> data.getClientInfo().getRecipient().getIfnsCode() != null && !data
                        .getClientInfo().getRecipient().getIfnsCode().contentEquals(""))
                .map((TestData data) -> DemandTestDataProvider.getTestDemand(data, testSuite))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @Contract(pure = true)
    private static Stream<DemandTestData> demandTestDataStream() {
        return tests.stream();
    }

    @ParameterizedTest
    @DisplayName("create Inventory draft")
    @MethodSource("demandTestDataStream")
    void testCreateInventory(DemandTestData testData) {
        Draft draft = engine.getRelatedDocumentsService(
                testData.getDemandId(),
                testData.getDemandAttachmentId()
        )
                .createRelatedDraft(TestUtils.toDraftMetaRequest(testData)).join();

        assertEquals(testData.getDemandId(), draft.getMeta().getRelatedDocument().getRelatedDocflowId());
        assertEquals(
                testData.getDemandAttachmentId(),
                draft.getMeta().getRelatedDocument().getRelatedDocumentId()
        );
    }

    @ParameterizedTest
    @DisplayName("test inventories page is returned")
    @MethodSource("demandTestDataStream")
    void testGetInventoryPage(DemandTestData testData) {
        sendRelatedInventory(testData).join();
        InventoriesPage inventoriesPage = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getRelatedInventories().join();

        assertEquals(sentRelatedInventories, (long) inventoriesPage.getTotalCount());
    }

    @ParameterizedTest
    @DisplayName("inventory ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventory(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Inventory inventory = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getInventory(sentInventory.getId()).join();

        assertEquals(sentInventory.getId(), inventory.getId());
        assertEquals(DocflowType.FNS534_INVENTORY, inventory.getType());
    }

    @ParameterizedTest
    @DisplayName("inventory decrypted content ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventoryDecryptedContent(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument = sentInventory
                .getDocuments()
                .stream()
                .filter(document -> document.getDescription().getType()
                        .equals(DocumentType.Fns534InventoryMessage))
                .findFirst()
                .get();

        engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getDecryptedContentAsync(sentInventory.getId(), messageDocument.getId())
                .handle((result, throwable) -> {
                    if (!(throwable instanceof ApiException)) {
                        fail();
                    }
                    assertEquals(404, ((ApiException) throwable).getCode());
                    assertEquals("urn:error:externapi:emptyContent", ((ApiException) throwable).getErrorId());
                    return null;
                }).join();
    }

    @ParameterizedTest
    @DisplayName("inventory encrypted content ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventoryEncryptedContent(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument =
                sentInventory.getDocuments().stream().filter(document -> document.getDescription().getType()
                        .equals(DocumentType.Fns534InventoryMessage)).findFirst().get();

        String documentEncryptedContent = fromWin1251Bytes(engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getEncryptedContentAsync(sentInventory.getId(), messageDocument.getId()).join());

        assertFalse(documentEncryptedContent
                .contains("ВерсПрог =\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.01\">"));
        assertTrue(documentEncryptedContent.length() > 100);
    }

    @ParameterizedTest
    @DisplayName("inventory signatures ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventorySignatures(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument =
                sentInventory.getDocuments().stream().filter(document -> document.getDescription().getType()
                        .equals(DocumentType.Fns534InventoryMessage)).findFirst().get();

        List<Signature> signatures = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getSignatures(sentInventory.getId(), messageDocument.getId()).join();

        assertTrue(signatures.size() >= 1);
    }

    @ParameterizedTest
    @DisplayName("inventory signature ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventorySignature(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument =
                sentInventory.getDocuments().stream().filter(document -> document.getDescription().getType()
                        .equals(DocumentType.Fns534InventoryMessage)).findFirst().get();
        Signature signature = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getSignatures(sentInventory.getId(), messageDocument.getId()).join().stream().findFirst()
                .get();

        byte[] checkedSignature = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getSignatureContent(sentInventory.getId(), messageDocument.getId(), signature.getId())
                .join();

        assertNotNull(checkedSignature);
        assertTrue(checkedSignature.length > 0);
    }

    @ParameterizedTest
    @DisplayName("inventory ought be received from api by link in related document")
    @MethodSource("demandTestDataStream")
    void testGetInventoryByLink(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        InventoriesPage inventoriesPage = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getRelatedInventories().join();
        String inventoriesHref = inventoriesPage.getDocflowsPageItem().stream()
                .filter(docflowPageItem -> docflowPageItem.getId().equals(sentInventory.getId())).findFirst()
                .get()
                .getLinks().stream().filter(link -> link.getRel().equals("self")).findFirst().get().getHref();

        Inventory checkInventory = engine.getAuthorizedHttpClient().followGetLink(
                inventoriesHref,
                Inventory.class
        );

        assertEquals(sentInventory.getId(), checkInventory.getId());
        assertEquals(sentInventory.getType(), checkInventory.getType());
    }

//    activate after ka-2677 released
//    @ParameterizedTest
//    @DisplayName("letter ought be received from api by link in related document")
//    @MethodSource("demandTestDataStream")
//    void testGetLetterByLink(DemandTestData testData) {
//        Docflow sentLetter = sendRelatedLetter(testData).join();
//
//////      temporary code to check test on staging with ka-2677
////        DocflowPage docflowPage = engine.getAuthorizedHttpClient()
////                .followGetLink("https://extern-api.staging2.testkontur.ru/v1/"
////                                + engine.getAccountProvider().accountId().toString()
////                                + "/docflows/"
////                                + testData.getDemandId().toString()
////                                + "/documents/"
////                                + testData.getDemandAttachmentId().toString()
////                                + "/related"
////                        , DocflowPage.class);
//
//        DocflowPage docflowPage = engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
//         .getRelatedDocflows().join();
//
//        String inventoriesHref = docflowPage.getDocflowsPageItem().stream()
//                .filter(docflowPageItem -> docflowPageItem.getId().equals(sentLetter.getId())).findFirst().get()
//                .getLinks().stream().filter(link -> link.getRel().equals("self")).findFirst().get().getHref();
//
//        Docflow checkLetter = engine.getAuthorizedHttpClient().followGetLink(inventoriesHref, Docflow.class);
//
//        assertNotNull(checkLetter);
//        assertEquals(sentLetter.getId(), checkLetter.getId());
//        assertEquals(sentLetter.getType(), checkLetter.getType());
//    }

    @ParameterizedTest
    @DisplayName("letter and inventories must be on related page")
    @MethodSource("demandTestDataStream")
    void testGetRelated(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Docflow sentLetter = sendRelatedLetter(testData).join();

        DocflowPage docflowPage = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getRelatedDocflows().join();

        assertTrue(docflowPage.getDocflowsPageItem().stream()
                .anyMatch(pageItem -> pageItem.getId().equals(sentInventory.getId())));
        assertTrue(docflowPage.getDocflowsPageItem().stream()
                .anyMatch(pageItem -> pageItem.getId().equals(sentLetter.getId())));
    }

    @ParameterizedTest
    @DisplayName("total count on DocflowPage ought be equal to acctual count")
    @MethodSource("demandTestDataStream")
    void testRelatedCountOnPage(DemandTestData testData) {
        sendRelatedInventory(testData).join();
        sendRelatedLetter(testData).join();

        DocflowPage docflowPage = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getRelatedDocflows().join();

        assertEquals(sentRelatedInventories + sentRelatedLetters, (long) docflowPage.getTotalCount());
    }

    @ParameterizedTest
    @DisplayName("related count in origin document ought be same as actually sent related documents count")
    @MethodSource("demandTestDataStream")
    void testRelatedCountEqualsActual(DemandTestData testData) {
        sendRelatedInventory(testData).join();
        sendRelatedLetter(testData).join();

        Document demandAttachment = engine
                .getDocflowService()
                .lookupDocumentAsync(testData.getDemandId(), testData.getDemandAttachmentId())
                .thenApply(QueryContext::getDocument)
                .join();

        assertEquals(
                sentRelatedInventories + sentRelatedLetters,
                demandAttachment.getDescription().getRelatedDocflowsCount()
        );
    }

    private CompletableFuture<Docflow> sendRelatedLetter(DemandTestData testData) {
        return relatedDocflowProvider.sendRelatedLetter(testData)
                .thenApply(docflow -> {
                    sentRelatedLetters++;
                    return docflow;
                });
    }

    private CompletableFuture<Inventory> sendRelatedInventory(DemandTestData testData) {
        return relatedDocflowProvider.sendRelatedInventory(testData)
                .thenApply(inventory -> {
                    sentRelatedInventories++;
                    return inventory;
                });
    }
}
