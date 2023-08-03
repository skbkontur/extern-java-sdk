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
import static ru.kontur.extern_api.sdk.utils.TestUtils.fromWin1251Bytes;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
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
import ru.kontur.extern_api.sdk.service.RelatedDocumentsService;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DemandTestData;
import ru.kontur.extern_api.sdk.utils.DemandTestDataProvider;
import ru.kontur.extern_api.sdk.utils.RelatedDocflowProvider;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@Execution(ExecutionMode.CONCURRENT)
@DisplayName("RelatedDocuments relatedDocumentService should be able to")
@Disabled
class RelatedDocumentsIT {

    private static final String IFNS_CODE = "0087";

    private static ExternEngine engine;
    private static List<DemandTestData> tests;
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
        Draft draft = relatedDocumentService(testData)
                .createRelatedDraft(TestUtils.toDraftMetaRequest(testData)).join();

        assertEquals(testData.getDemandId(), draft.getMeta().getRelatedDocument().getRelatedDocflowId());
        assertEquals(
                testData.getDemandAttachmentId(),
                draft.getMeta().getRelatedDocument().getRelatedDocumentId()
        );
    }

    @ParameterizedTest
    @DisplayName("inventory ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventory(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Inventory inventory = relatedDocumentService(testData)
                .getInventory(sentInventory.getId()).join();

        assertEquals(sentInventory.getId(), inventory.getId());
        assertEquals(DocflowType.FNS534_INVENTORY, inventory.getType());
    }

    @ParameterizedTest
    @DisplayName("inventory decrypted content ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventoryDecryptedContent(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument = getInventoryMessage(sentInventory);

        byte[] documentDecryptedContentBytes = engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getDecryptedContentAsync(sentInventory.getId(), messageDocument.getId()).join();

        String decryptedContent = fromWin1251Bytes(documentDecryptedContentBytes);
        
        assertTrue(decryptedContent.contains("ВерсПрог =\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.01\">"));
        assertTrue(decryptedContent.length() > 100);
    }

    @ParameterizedTest
    @DisplayName("inventory encrypted content ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventoryEncryptedContent(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument = getInventoryMessage(sentInventory);

        String documentEncryptedContent = fromWin1251Bytes(relatedDocumentService(testData)
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
        Document messageDocument = getInventoryMessage(sentInventory);

        List<Signature> signatures = relatedDocumentService(testData)
                .getSignatures(sentInventory.getId(), messageDocument.getId()).join();

        assertTrue(signatures.size() >= 1);
    }

    @ParameterizedTest
    @DisplayName("inventory signature ought be received from api")
    @MethodSource("demandTestDataStream")
    void testGetInventorySignature(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        Document messageDocument = getInventoryMessage(sentInventory);
        Signature signature = relatedDocumentService(testData)
                .getSignatures(sentInventory.getId(), messageDocument.getId()).join()
                .get(0);

        byte[] checkedSignature = relatedDocumentService(testData)
                .getSignatureContent(sentInventory.getId(), messageDocument.getId(), signature.getId())
                .join();

        assertNotNull(checkedSignature);
        assertTrue(checkedSignature.length > 0);
    }

    @NotNull
    private Document getInventoryMessage(Inventory sentInventory) {
        return sentInventory.getDocuments().stream()
                .filter(document -> Objects
                        .equals(document.getDescription().getType(), DocumentType.Fns534InventoryMessage))
                .findFirst()
                .orElseThrow(() -> new AssertionError("there is no Fns534InventoryMessage"));
    }

    @ParameterizedTest
    @DisplayName("inventory ought be received from api by link in related document")
    @MethodSource("demandTestDataStream")
    void testGetInventoryByLink(DemandTestData testData) {
        Inventory sentInventory = sendRelatedInventory(testData).join();
        InventoriesPage inventoriesPage = relatedDocumentService(testData)
                .getRelatedInventories().join();
        String inventoriesHref = inventoriesPage.getDocflowsPageItem().stream()
                .filter(docflowPageItem -> Objects.equals(docflowPageItem.getId(), sentInventory.getId()))
                .findFirst()
                .orElseThrow(AssertionError::new)
                .getLinks().stream()
                .filter(link -> link.getRel().equals("self")).findFirst()
                .orElseThrow(AssertionError::new)
                .getHref();

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

        DocflowPage docflowPage = relatedDocumentService(testData)
                .getRelatedDocflows().join();

        assertTrue(docflowPage.getDocflowsPageItem().stream()
                .anyMatch(pageItem -> pageItem.getId().equals(sentInventory.getId())));
        assertTrue(docflowPage.getDocflowsPageItem().stream()
                .anyMatch(pageItem -> pageItem.getId().equals(sentLetter.getId())));
    }

    private CompletableFuture<Docflow> sendRelatedLetter(DemandTestData testData) {
        return relatedDocflowProvider.sendRelatedLetter(testData);
    }

    private CompletableFuture<Inventory> sendRelatedInventory(DemandTestData testData) {
        return relatedDocflowProvider.sendRelatedInventory(testData);
    }

    private RelatedDocumentsService relatedDocumentService(DemandTestData data) {
        return engine.getRelatedDocumentsService(data.getDemandId(), data.getDemandAttachmentId());
    }
}
