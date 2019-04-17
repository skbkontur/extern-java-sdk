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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.io.IOUtils;
import org.jetbrains.annotations.Contract;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.utils.DemandTestData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DocumentType;
import ru.kontur.extern_api.sdk.model.Draft;
import ru.kontur.extern_api.sdk.model.ExtendedDraftMetaRequest;
import ru.kontur.extern_api.sdk.model.InventoriesPage;
import ru.kontur.extern_api.sdk.model.Inventory;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.model.TestData;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.Resources;
import ru.kontur.extern_api.sdk.utils.SystemProperty;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;

@DisplayName("Inventories service should be able to")
class InventoriesIT {

    private static final String WINDOWS_1251 = "windows-1251";
    private static final String VALID_INVENTORY_JPG_DOCUMENT = "/inventories/zapiska_valid.jpg";
    private static final String IFNS_CODE = "0087";
    private static ExternEngine engine;
    private static List<DemandTestData> tests;
    private static CryptoUtils cryptoUtils;
    private static int sentRelatedInventories;
    private static int sentRelatedLetters;

    @BeforeAll
    static void setUpClass() {
        TestSuite testSuite = TestSuite.Load();
        engine = testSuite.engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
        String certificate = cryptoUtils.loadX509(engine.getConfiguration().getThumbprint());
        TestData[] testData = TestUtils.getTestData(certificate);

        tests = Arrays
                .stream(testData)
                .filter(data -> data.getClientInfo().getRecipient().getIfnsCode() != null && !data
                        .getClientInfo().getRecipient().getIfnsCode().contentEquals(""))
                .map((TestData data) -> DemandTestData.getTestDemand(data, testSuite))
                .collect(Collectors.toList())
                .stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
    }

    @BeforeEach
    void setUp() {
        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @Contract(pure = true)
    private static Stream<DemandTestData> demandTestDataStream() {
        return tests.stream();
    }

    @ParameterizedTest
    @DisplayName("create Inventory draft")
    @MethodSource("demandTestDataStream")
    void testCreateInventory(DemandTestData testData) {
        Draft draft = engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .createRelatedDraft(TestUtils.toDraftMetaRequest(testData)).join();

        assertEquals(testData.getDemandId(), draft.getMeta().getRelatedDocument().getRelatedDocflowId());
        assertEquals(testData.getDemandAttachmentId(), draft.getMeta().getRelatedDocument().getRelatedDocumentId());
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
                .filter(document -> document.getDescription().getType().equals(DocumentType.Fns534InventoryMessage))
                .findFirst()
                .get();
        try {
            engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                    .getDecryptedContentAsync(sentInventory.getId(), messageDocument.getId())
                    .whenComplete((bytes, throwable) -> {
                        if (!(throwable instanceof ApiException)) {
                            fail();
                        }
                        assertEquals(404, ((ApiException) throwable).getCode());
                        assertEquals("urn:error:externapi:emptyContent", ((ApiException) throwable).getErrorId());
                    })
                    .join();
        } catch (Throwable throwable) {

        }
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

        assertFalse(documentEncryptedContent.contains("ВерсПрог =\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.01\">"));
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
                .getSignatures(sentInventory.getId(), messageDocument.getId()).join().stream().findFirst().get();

        byte[] checkedSignature = engine
                .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .getSignatureContent(sentInventory.getId(), messageDocument.getId(), signature.getId()).join();

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
                .filter(docflowPageItem -> docflowPageItem.getId().equals(sentInventory.getId())).findFirst().get()
                .getLinks().stream().filter(link -> link.getRel().equals("self")).findFirst().get().getHref();

        Inventory checkInventory = engine.getAuthorizedHttpClient().followGetLink(inventoriesHref, Inventory.class);

        assertEquals(sentInventory.getId(), checkInventory.getId());
        assertEquals(sentInventory.getType(), checkInventory.getType());
    }

    @ParameterizedTest
    @DisplayName("letter ought be received from api by link in related document")
    @MethodSource("demandTestDataStream")
    void testGetLetterByLink(DemandTestData testData) {
        Docflow sentLetter = sendRelatedLetter(testData).join();

        DocflowPage docflowPage = engine.getAuthorizedHttpClient()
                .followGetLink("https://extern-api.staging2.testkontur.ru/v1/"
                                + engine.getAccountProvider().accountId().toString()
                                + "/docflows/"
                                + testData.getDemandId().toString()
                                + "/documents/"
                                + testData.getDemandAttachmentId().toString()
                                + "/related"
                        , DocflowPage.class);
        // .getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
        //  .getRelatedDocflows().join();

        String inventoriesHref = docflowPage.getDocflowsPageItem().stream()
                .filter(docflowPageItem -> docflowPageItem.getId().equals(sentLetter.getId())).findFirst().get()
                .getLinks().stream().filter(link -> link.getRel().equals("self")).findFirst().get().getHref();

        Docflow checkLetter = engine.getAuthorizedHttpClient().followGetLink(inventoriesHref, Inventory.class);

        assertNotNull(checkLetter);
        assertEquals(sentLetter.getId(), checkLetter.getId());
        assertEquals(sentLetter.getType(), checkLetter.getType());
    }


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

        assertEquals(sentRelatedInventories + sentRelatedLetters,
                demandAttachment.getDescription().getRelatedDocflowsCount());
    }


    private CompletableFuture<Docflow> sendRelatedLetter(DemandTestData testData) {
        sentRelatedLetters++;
        return engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .createRelatedDraft(new ExtendedDraftMetaRequest(TestUtils.toDraftMetaRequest(testData), "Письмо"))
                .thenCompose(draft -> addLetterDocument(draft, testData))
                .thenCompose(this::sendDraft)
                .thenCompose(docflow -> waitForDocflow(docflow.getId()));
    }

    CompletionStage<Draft> addLetterDocument(Draft draft, DemandTestData testData) {
        byte[] docContent = getContentBytes(getLetterData(testData));
        return addAnyDocument(docContent, new DocumentDescription(), draft);
    }

    CompletableFuture<Inventory> sendRelatedInventory(DemandTestData testData) {
        UUID childFileId = UUID.randomUUID();
        UUID baseFileId = UUID.randomUUID();
        sentRelatedInventories++;
        return engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                .createRelatedDraft(TestUtils.toDraftMetaRequest(testData))
                .thenCompose(draft -> addInventoriesDocument(draft, testData, baseFileId, childFileId))
                .thenCompose(draft -> addInventoriesSubmission(draft, testData, baseFileId, childFileId))
                .thenCompose(this::sendDraft)
                .thenCompose(docflow -> waitForInventory(docflow.getId(), testData));
    }

    private static CompletableFuture<Docflow> waitForDocflow(UUID id) {
        return Awaiter.waitForCondition(() -> engine.getDocflowService()
                        .lookupDocflowAsync(id)
                        .thenApply(QueryContext::getDocflow),
                Objects::nonNull, 2000);
    }

    private static CompletableFuture<Inventory> waitForInventory(UUID id, DemandTestData testData) {
        return Awaiter.waitForCondition(
                () -> engine.getRelatedDocumentsService(testData.getDemandId(), testData.getDemandAttachmentId())
                        .getInventory(id),
                Objects::nonNull, 2000);
    }

    String getAttachmentFileName(DemandTestData testData, UUID baseFileId, UUID childFileId) {
        return "0510041_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_"
                + IFNS_CODE
                + "_"
                + baseFileId
                + "_"
                + childFileId
                + ".jpg";
    }

    CompletableFuture<Draft> addInventoriesDocument(Draft draft, DemandTestData testData, UUID baseFileId,
            UUID childFileId) {
        byte[] docContent = getFromResource(VALID_INVENTORY_JPG_DOCUMENT);
        DocumentDescription description = new DocumentDescription();
        description.setFilename(getAttachmentFileName(testData, baseFileId, childFileId));

        return addAnyDocument(docContent, description, draft);
    }

    CompletableFuture<Draft> addInventoriesSubmission(Draft draft, DemandTestData testData, UUID baseFileId,
            UUID childFileId) {
        byte[] docContent = getContentBytes(getSubmissionData(testData, baseFileId, childFileId));
        DocumentDescription description = new DocumentDescription();
        return addAnyDocument(docContent, description, draft);
    }

    private byte[] getContentBytes(String content) {
        try {
            return content.getBytes(WINDOWS_1251);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    CompletableFuture<Draft> addAnyDocument(byte[] docContent, DocumentDescription description, Draft draft) {
        DocumentContents contents = new DocumentContents();
        contents.setBase64Content(toBase64(docContent));
        contents.setSignature(getContentSignature(docContent));
        contents.setDescription(description);
        return engine.getDraftService().addDecryptedDocumentAsync(draft.getId(), contents)
                .thenApply(result -> draft);
    }

    byte[] getFromResource(String path) {
        try (InputStream is = Resources.class.getResourceAsStream(path)) {
            Objects.requireNonNull(is, "Resource not found: " + path);
            return IOUtils.toByteArray(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    String getContentSignature(byte[] docContent) {
        return toBase64(cryptoUtils
                .sign(engine.getConfiguration().getThumbprint(), docContent));
    }

    String toBase64(byte[] docContent) {
        return new String(Base64.getEncoder().encode(docContent));
    }

    CompletableFuture<Docflow> sendDraft(Draft draft) {
        return engine.getDraftService().sendAsync(draft.getId()).thenApply(QueryContext::get);
    }


    String getSubmissionData(DemandTestData testData, UUID baseFileId, UUID childFileId) {
        return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<Файл ИдФайл=\"ON_DOCNPNO_" + IFNS_CODE + "_" + IFNS_CODE + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_20180226_"
                + UUID.randomUUID().toString()
                + "\" ВерсПрог =\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.01\">"
                + "<Документ КНД=\"1184002\" ДатаДок=\"17.05.2018\">"
                + "<СвОтпрДок>"
                + "<ОтпрЮЛ НаимОрг=\"ООО «Первая Ижевская тестовая»\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\" />"
                + "</СвОтпрДок>"
                + "<СвПолДок>"
                + "<ОтпрНО КодНО=\"" + IFNS_CODE + "\" НаимНО=\"Тестовый КО (положительный ответ)\" />"
                + "</СвПолДок>"
                + "<СвНП>"
                + "<НПЮЛ НаимОрг=\"ООО «Первая Ижевская тестовая»\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\" />"
                + "</СвНП>"
                + "<Подписант ПрПодп=\"3\" Должн =\"Руководитель\" Тлф=\"8800\" E-mail=\"ruk@skbkontur.ru\" ИННФЛ=\"227632864503\">"
                + "<ФИО Фамилия=\""
                + testData.getClientInfo().getSender().getFio().getSurname()
                + "\" Имя=\""
                + testData.getClientInfo().getSender().getFio().getName()
                + "\" Отчество=\""
                + testData.getClientInfo().getSender().getFio().getPatronymic()
                + "\" />"
                + "</Подписант>"
                + "<ДокПредстНО КолФайл=\"1\">"
                + "<ИдФайлОсн>1165013_" + IFNS_CODE + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_"
                + UUID.randomUUID().toString()
                + "_20170807_"
                + UUID.randomUUID().toString()
                + "</ИдФайлОсн>"
                + "<ДокСкан ПорНомДок=\"1.01\" КодДок=\"0510041\" НаимДок=\"Фото_1\" >"
                + "<ИмяФайл>"
                + getAttachmentFileName(testData, baseFileId, childFileId)
                + "</ИмяФайл>"
                + "</ДокСкан>"
                + "</ДокПредстНО>"
                + "</Документ>"
                + "</Файл>";
    }


    String getLetterData(DemandTestData testData) {
        return "<?xml version=\"1.0\" encoding=\"windows-1251\"?>"
                + "<Файл ИдФайл=\"IU_OBRNP_"
                + IFNS_CODE
                + "_"
                + IFNS_CODE
                + "_"
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "_20180301_"
                + UUID.randomUUID().toString()
                + "\" ВерсПрог=\"КОНТУР-ЭКСТЕРН, ВЕРСИЯ 13.0\" ВерсФорм=\"5.03\">"
                + "<Документ КНД=\"1166102\" ДатаДок=\"01.03.2018\">"
                + "<СвНП>"
                + "<НПЮЛ НаимОрг=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getOrgName()
                + "\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\"/>"
                + "<АдрРФ КодРегион=\"66\"/>"
                + "</СвНП>"
                + "<СвОтпр>"
                + "<СвОтпрЮЛ НаимОрг=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getOrgName()
                + "\" ИННЮЛ=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getInn()
                + "\" КПП=\""
                + TestUtils.toDraftMetaRequest(testData).getPayer().getKpp()
                + "\"/>"
                + "</СвОтпр>"
                + "<Подписант ПрПодп=\"1\">"
                + "<ФИО Фамилия=\""
                + testData.getClientInfo().getSender().getFio().getSurname()
                + "\" Имя=\""
                + testData.getClientInfo().getSender().getFio().getName()
                + "\" Отчество=\""
                + testData.getClientInfo().getSender().getFio().getPatronymic()
                + "\" />"
                + "</Подписант>"
                + "<ОбращИнф ИФНС=\"" + IFNS_CODE + "\">"
                + "<ОбращТекст><![CDATA[с]]></ОбращТекст>"
                + "<Прил КолФайл=\"0\"/>"
                + "</ОбращИнф>"
                + "</Документ>"
                + "</Файл>";
    }

    String fromWin1251Bytes(byte[] content) {
        try {
            return new String(content, WINDOWS_1251);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Win-1251 no supported");
        }
    }
}
