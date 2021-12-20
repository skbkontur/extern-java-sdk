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

package ru.kontur.extern_api.sdk;

import okhttp3.logging.HttpLoggingInterceptor;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.*;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;


@Execution(ExecutionMode.SAME_THREAD)
class DocflowServiceIT {

    protected static ExternEngine engine;

    private static class TestPack {

        private final List<QueryContext<Docflow>> testDocflows;

        TestPack(List<QueryContext<Docflow>> testDocflows) {
            this.testDocflows = testDocflows;
        }
    }

    private static final Logger log = Logger.getLogger(DocflowServiceIT.class.getName());
    private static EngineUtils engineUtils;

    private static final Lazy<TestPack> testPack = Lazy.of(() -> getTestPack(engine));
    private static final Lazy<TestData[]> testData = Lazy.of(() -> getTestData(engine));


    private static HttpClient client;
    private static final Lazy<Certificate> cloudCert = Lazy.of(DocflowServiceIT::getCloudCertificate);
    private static final Lazy<ApproveCodeProvider> codeProvider = Lazy
            .of(() -> new ApproveCodeProvider(engine));

    private static DocflowService docflowService;

    private static TestPack getTestPack(ExternEngine ee) {
        List<QueryContext<Docflow>> qcs = createDocflows(ee, testData.get());
        return new TestPack(qcs);
    }

    private static TestData[] getTestData(ExternEngine ee) {
        String cert = engineUtils.crypto.loadX509(ee.getConfiguration().getThumbprint());
        return TestUtils.getTestData(cert);
    }

    private static Stream<DemandTestData> demandLazyFactory() {
        String cert = engineUtils.crypto.loadX509(engine.getConfiguration().getThumbprint());
        TestData[] testData = TestUtils.getTestData(cert);
        List<DemandTestData> result = new ArrayList<>();

        DemandTestData demand = UncheckedSupplier.get(() -> DemandTestDataProvider
                .getTestDemand(testData[0], TestSuite.Load()).get());
        result.add(demand);
        return result.stream();
    }

    private static Stream<QueryContext<Docflow>> docflowLazyFactory() {
        return docflowFactory();
//        return testPack.get().testDocflows.stream();
    }

    private static List<QueryContext<Docflow>> testDocflows;

    private static Stream<QueryContext<Docflow>> docflowFactory() {
        testDocflows = testDocflows == null
                ? getTestPack(engine).testDocflows
                : testDocflows;
        return testDocflows.stream();
    }

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engineUtils = EngineUtils.with(engine);
        docflowService = engine.getDocflowService();
    }

    @ParameterizedTest
    @DisplayName("get docflow by id")
    @MethodSource("docflowLazyFactory")
    void testGetDocflow(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();
        Docflow returned = docflowService
                .lookupDocflowAsync(docflow.getId())
                .join()
                .getOrThrow();

        Assertions.assertEquals(docflow.getId(), returned.getId());
        Assertions.assertEquals(docflow.getType(), returned.getType());
        Optional<Link> webDocflowLink = returned.getLinks()
                .stream()
                .filter(l -> l.getRel().equals("web-docflow"))
                .findAny();

        Assumptions.assumingThat(docflow.getType() != DocflowType.FSS_REPORT, () -> {
            assertTrue(
                    webDocflowLink.isPresent(),
                    "Web view docflow link should exists in docflow links."
            );
        });

        Assertions.assertNotEquals(
                DocflowStateTypes.FAILED,
                returned.getSuccessState(),
                "Should be not error success state"
        );
        Assertions.assertNotEquals(
                DocflowStateTypes.WARNING,
                returned.getSuccessState(),
                "Should be not warning success state"
        );
        Assertions.assertNotNull(returned.getOrganizationId());
    }

    @Disabled("KA-1871")
    @ParameterizedTest
    @DisplayName("get docflow documents")
    @MethodSource("docflowLazyFactory")
    void testGetDocuments(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        Assertions.assertNotEquals(0, docs.size());
        for (Document document : docs) {
            Assertions.assertNotNull(document.getDescription().getType());
        }

    }

    @ParameterizedTest
    @DisplayName("lookup single document")
    @MethodSource("docflowLazyFactory")
    void testLookupDocument(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        for (Document d : docs) {
            Document actual = docflowService
                    .lookupDocumentAsync(docflow.getId().toString(), d.getId().toString())
                    .join()
                    .getOrThrow();

            Assertions.assertNotNull(d.getSendDate());
            Assertions.assertEquals(d.getId(), actual.getId());
            Assertions.assertEquals(d.getDescription().getType(), actual.getDescription().getType());
            Assertions.assertEquals(d.getId(), actual.getId());
        }
    }

    @ParameterizedTest
    @DisplayName("lookup document description")
    @MethodSource("docflowLazyFactory")
    void testLookupDocumentDescription(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        for (Document d : docs) {
            DocflowDocumentDescription actual = docflowService
                    .lookupDescriptionAsync(docflow.getId().toString(), d.getId().toString())
                    .join()
                    .getOrThrow();

            DocflowDocumentDescription expected = d.getDescription();

            Assertions.assertEquals(expected.getType(), actual.getType());
            Assertions.assertEquals(expected.getFilename(), actual.getFilename());
            Assertions.assertEquals(expected.getContentType(), actual.getContentType());
            Assertions.assertEquals(expected.getRequisites(), actual.getRequisites());
        }
    }

    @ParameterizedTest
    @DisplayName("get encrypted document content")
    @MethodSource("docflowLazyFactory")
    void testGetEncryptedContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        int processedDocs = 0;
        for (Document d : docs) {

            if (!d.hasEncryptedContent()) {
                continue;
            }

            byte[] encrypted = docflowService
                    .getEncryptedContentAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            byte[] decrypt = engineUtils
                    .crypto
                    .decrypt(engine.getConfiguration().getThumbprint(), encrypted);

            if (d.getDescription().getCompressed()) {
                decrypt = Zip.unzip(decrypt);
            }

            byte[] first5letters = Arrays.copyOfRange(decrypt, 0, 5);
            assumingThat(docflow.getType() != DocflowType.FSS_REPORT, () ->
                    assertArrayEquals("<?xml".getBytes(), first5letters)
            );
            processedDocs++;
        }
        assertTrue(processedDocs > 0, "Should be at least 1 processed document");
    }

    @ParameterizedTest
    @DisplayName("get decrypted document content")
    @MethodSource("docflowLazyFactory")
    void testGetDecryptedContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        int processedDocs = 0;
        for (Document d : docs) {

            if (!d.hasDecryptedContent()) {
                continue;
            }

            byte[] decrypted = docflowService
                    .getDecryptedContentAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            boolean needDecompress = d.getDescription().getCompressed()
                    && docflow.getType() != DocflowType.PFR_REPORT
                    && !d.hasEncryptedContent(); //расшифрованный контент для зашифрованных документов всегда расжат
            if (needDecompress) {
                decrypted = Zip.unzip(decrypted);
            }

            byte[] first5letters = Arrays.copyOfRange(decrypted, 0, 5);

            assumingThat(docflow.getType() != DocflowType.FSS_REPORT, () ->
                    assertArrayEquals("<?xml".getBytes(), first5letters)
            );
            processedDocs++;
        }
        assertTrue(processedDocs > 0, "Should be at least 1 processed document");
    }

    @Disabled("@see DraftWithCloudCertIT")
    @ParameterizedTest
    @DisplayName("decrypt document content in cloud")
    @MethodSource("docflowLazyFactory")
    void testDecryptContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .ensureSuccess();

        int processedDocs = 0;
        for (Document d : docsCxt.getOrThrow()) {

            if (!d.hasEncryptedContent()) {
                continue;
            }

            byte[] decrypted = docflowService.cloudDecryptDocument(
                    docflow.getId().toString(),
                    d.getId().toString(),
                    cloudCert.get().getContent(),
                    init -> codeProvider.get().apply(init.getRequestId())
            )
                    .getOrThrow();

            byte[] first5letters = Arrays.copyOfRange(decrypted, 0, 5);

            assumingThat(docflow.getType() != DocflowType.FSS_REPORT, () ->
                    assertArrayEquals("<?xml".getBytes(), first5letters)
            );
            processedDocs++;
        }
        assertTrue(processedDocs > 0, "Should be at least 1 processed document");
    }

    @ParameterizedTest
    @DisplayName("get document signatures")
    @MethodSource("docflowLazyFactory")
    void testGetSignatures(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        for (Document d : docs) {
            List<Signature> signs = docflowService
                    .getSignaturesAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            for (Signature sign : signs) {
                Assertions.assertNotNull(sign.getTitle());
            }
        }
    }

    @ParameterizedTest
    @DisplayName("get document single signature")
    @MethodSource("docflowLazyFactory")
    void testGetSignature(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        for (Document d : docs) {
            List<Signature> signs = docflowService
                    .getSignaturesAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            for (Signature sign : signs) {
                Signature signature = docflowService.getSignatureAsync(
                        docflow.getId().toString(),
                        d.getId().toString(),
                        sign.getId().toString()
                ).join().getOrThrow();

                Assertions.assertEquals(sign.getId(), signature.getId());
                Assertions.assertEquals(sign.getTitle(), signature.getTitle());
            }
        }
    }

    @ParameterizedTest
    @DisplayName("get document signature content")
    @MethodSource("docflowLazyFactory")
    void testGetSignatureContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        List<Document> docs = docflowService
                .getDocumentsAsync(docflow.getId())
                .join()
                .getOrThrow();

        for (Document d : docs) {
            List<Signature> signs = docflowService
                    .getSignaturesAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            for (Signature sign : signs) {
                byte[] signatureContent = docflowService
                        .getSignatureContentAsync(docflow.getId(), d.getId(), sign.getId())
                        .join()
                        .getOrThrow();

                Assertions.assertNotNull(signatureContent);
            }
        }
    }

    @ParameterizedTest
    @DisplayName("send reply documents by choosing its type")
    @MethodSource("docflowFactory")
    void testSendOneReply(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.getDocflow();

        Document document = docflow.getDocuments()
                .stream()
                .filter(Document::isNeedToReply)
                .findFirst()
                .orElse(null);

        if (document == null) {
            log.warning("Docflow " + docflow.getId() + " has no reply options");
            return;
        }

        String thumbprint = engine.getConfiguration().getThumbprint();

        Link generateLink = document.getReplyLinks()[0];

        GenerateReplyDocumentRequestData cert = new GenerateReplyDocumentRequestData()
                .certificateBase64(engineUtils.crypto.loadX509(thumbprint));

        client = engine.getAuthorizedHttpClient();

        ReplyDocument reply = client.followPostLink(
                generateLink.getHref(),
                cert,
                ReplyDocument.class
        );

        byte[] signature = engineUtils.crypto.sign(thumbprint, reply.getContent());

        client.followPutLink(
                reply.getPutSignatureLink().getHref(),
                signature,
                ReplyDocument.class
        );

        client.followPostLink(
                reply.getSendLink().getHref(),
                new SenderIp(engine.getUserIPProvider().userIP()),
                Docflow.class
        );
    }


    @ParameterizedTest
    @DisplayName("search docflows filtered")
    @MethodSource("docflowFactory")
    void testGetDocflows(QueryContext<Docflow> df) {
        CompanyGeneral company = engine.getOrganizationService()
                .lookupAsync(df.getDocflow().getOrganizationId())
                .join()
                .getOrThrow()
                .getGeneral();

        DocflowPage docflowPage = docflowService.searchDocflows(DocflowFilter
                .page(0, 10)
                .finished(true)
                .incoming(false)
                .innKpp(
                        company.getInn(),
                        company.getKpp()
                )
                .type(DocflowType.FNS534_REPORT)
        ).getOrThrow();

        assertTrue(docflowPage.getDocflowsPageItem().size() <= 10);

        for (DocflowPageItem docflowPageItem : docflowPage.getDocflowsPageItem()) {
            Assertions.assertEquals(DocflowStatus.FINISHED, docflowPageItem.getStatus());
            Assertions.assertEquals(DocflowType.FNS534_REPORT, docflowPageItem.getType());
        }
    }

    @ParameterizedTest
    @DisplayName("search docflows filtered by date-time")
    @MethodSource("docflowFactory")
    void testGetDocflowsFilteredByDateTime(QueryContext<Docflow> df) {
        CompanyGeneral company = engine.getOrganizationService()
                .lookupAsync(df.getDocflow().getOrganizationId())
                .join()
                .getOrThrow()
                .getGeneral();

        DocflowPage docflowPage = docflowService.searchDocflows(
                DocflowFilter
                        .page(0, 1)
                        .orderBy(SortOrder.ASCENDING))
                .getOrThrow();

        Assertions.assertEquals(1, docflowPage.getDocflowsPageItem().size());

        DocflowPageItem oldestDocflow = docflowPage.getDocflowsPageItem().get(0);
        Instant lastChangeDate = Instant.ofEpochMilli(oldestDocflow.getLastChangeDate().getTime());
        Date laterDate = new Date(lastChangeDate.plusMillis(1).toEpochMilli());
        // TODO исправить с задачей , должно учитывать более мелкие единицы, чем миллисекунды
        //  lastChangeDate.plusNanos(~1)

        docflowPage = docflowService.searchDocflows(
                DocflowFilter
                        .page(0, 1000)
                        .orderBy(SortOrder.ASCENDING)
                        .updatedTo(laterDate))
                .getOrThrow();

        // TODO исправить с задачей
        //Assertions.assertEquals(1, docflowPage.getDocflowsPageItem().size());
        //Assertions.assertEquals(oldestDocflow.getId(), docflowPage.getDocflowsPageItem().get(0).getId());

        docflowPage = docflowService.searchDocflows(
                DocflowFilter
                        .page(0, 1000)
                        .orderBy(SortOrder.ASCENDING)
                        .updatedFrom(laterDate))
                .getOrThrow();

        assertTrue(docflowPage.getDocflowsPageItem().size() > 0);
        Assertions.assertNotEquals(oldestDocflow.getId(), docflowPage.getDocflowsPageItem().get(0).getId());
    }

    @Disabled("print it with decrypt")
    @ParameterizedTest
    @MethodSource("docflowFactory")
    void testPrintFromDocflows(QueryContext<Docflow> docflowCxt) {
        DocumentContents dc = docflowCxt.getDocumentContents();

        String base64 = dc.getBase64Content();

        Docflow docflow = docflowCxt.getDocflow();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocumentsAsync(docflow.getId())
                .join();

        for (Document d : docsCxt.getOrThrow()) {
            // печатаем только исходпую декларацию
            String name = ofNullable(d.getDescription())
                    .map(DocflowDocumentDescription::getFilename)
                    .orElse("");
            String dcName = ofNullable(dc.getDescription())
                    .map(DocumentDescription::getFilename)
                    .orElse("");

            if (name.equalsIgnoreCase(dcName)) {
                QueryContext<String> printCxt = docflowService
                        .printAsync(
                                docflowCxt.getDocflow().getId().toString(),
                                d.getId().toString(),
                                base64
                        )
                        .join();

                Assertions.assertNotNull(printCxt.get());
            }
        }
    }

    @Disabled
    @ParameterizedTest
    @MethodSource("demandLazyFactory")
    @DisplayName("recognize demand attachment")
    void testDemandRecognize(DemandTestData demandTestData) throws RuntimeException {
        DocflowService docflowService = engine.getDocflowService();

        Awaiter.waitForCondition(
                () -> docflowService.lookupDocumentAsync(
                        demandTestData.getDemandId(),
                        demandTestData.getDemandAttachmentId()
                ),
                cxt -> cxt.isSuccess() || cxt.getServiceError().getCode() != 404,
                1000 * 20,
                1000 * 60 * 5
        ).thenApply(QueryContext::getOrThrow);

        Document document = docflowService.lookupDocumentAsync(
                demandTestData.getDemandId(),
                demandTestData.getDemandAttachmentId()
        )
                .join().getOrThrow();

        DemandAttachmentRequisites requisites = (DemandAttachmentRequisites) document.getDescription()
                .getRequisites();
        Assertions.assertNull(requisites.getDemandDate());
        Assertions.assertNull(requisites.getDemandNumber());
        Assertions.assertEquals(requisites.getDemandInnList().size(), 0);

        RecognizedMeta recognizedMeta = docflowService
                .recognizeAsync(
                        demandTestData.getDemandId(),
                        demandTestData.getDemandAttachmentId(),
                        demandTestData.getDemandAttachmentDecryptedBytes()
                )
                .join()
                .getOrThrow();

        Assertions.assertNotNull(recognizedMeta);
        Assertions.assertNotNull(recognizedMeta.getDemandDate());
        Assertions.assertNotNull(recognizedMeta.getDemandNumber());
        Assertions.assertNotNull(recognizedMeta.getDemandInnList());

        document = docflowService.lookupDocumentAsync(
                demandTestData.getDemandId(),
                demandTestData.getDemandAttachmentId()
        ).join().getOrThrow();
        requisites = (DemandAttachmentRequisites) document.getDescription().getRequisites();

        Assertions.assertEquals(requisites.getDemandDate(), recognizedMeta.getDemandDate());
        Assertions.assertEquals(requisites.getDemandNumber(), recognizedMeta.getDemandNumber());
        Assertions.assertLinesMatch(requisites.getDemandInnList(), recognizedMeta.getDemandInnList());
    }

    @Test
    @DisplayName("check demand")
    void checkDemand() throws RuntimeException {
        DocflowService docflowService = engine.getDocflowService();

        UUID docflowId = UUID.fromString("d8daeb8f-fda9-45b6-a6e8-7990d056bf1c");
        UUID decryptedMainDocumentContentId = UUID.fromString("4e0632ee-d32b-4ec5-8042-2d28030a531e");
        UUID demandAttId = UUID.fromString("ddb3f232-62d1-4706-8683-b9d5f03f10ff");
        UUID demandAttDecryptedContentId = UUID.fromString("a7f865b8-82e0-4c5e-b8a9-17af42c7ac72");

        CheckDemandRequestData requestData = new CheckDemandRequestData();

        DecryptedDemandAttachmentData decryptedDemandAttachmentData = new DecryptedDemandAttachmentData();
        decryptedDemandAttachmentData.setId(demandAttId);
        decryptedDemandAttachmentData.setContentId(demandAttDecryptedContentId);

        ArrayList<DecryptedDemandAttachmentData> decryptedDemandAttachmentDataList = new ArrayList<>();
        decryptedDemandAttachmentDataList.add(decryptedDemandAttachmentData);

        requestData.setDecryptedAttachments(decryptedDemandAttachmentDataList);
        requestData.setDecryptedDemandContentId(decryptedMainDocumentContentId);


        CheckDemandResult checkDemandResult = docflowService.checkDemandAsync(docflowId, requestData).join();
        Assertions.assertFalse(checkDemandResult.hasErrors());
        assertEquals(0, checkDemandResult.getErrorCodes().size());
    }

    @Test
    @DisplayName("check demand when incorrect signature")
    void checkDemandWhenIncorrectSignature() throws RuntimeException {

        ExternEngine engine = GetExistingDocflowProviderEngine();
        DocflowService docflowService = engine.getDocflowService();

        UUID docflowWithIncorrectSignaturesId = UUID.fromString("ac4768ab-ee23-49d7-a817-373079a2fc38");
        UUID decryptedMainDocumentContentId = UUID.fromString("925ee6d9-5f9e-4d04-af1b-d5c3eb8f9d5f");
        UUID demandAttId = UUID.fromString("5eadf5da-c4ea-474b-9d75-d7f22bca6eb5");
        UUID demandAttDecryptedContentId = UUID.fromString("d5c53ac5-29a0-4912-9d9d-a10734d5951a");

        CheckDemandRequestData requestData = new CheckDemandRequestData();

        DecryptedDemandAttachmentData decryptedDemandAttachmentData = new DecryptedDemandAttachmentData();
        decryptedDemandAttachmentData.setId(demandAttId);
        decryptedDemandAttachmentData.setContentId(demandAttDecryptedContentId);

        ArrayList<DecryptedDemandAttachmentData> decryptedDemandAttachmentDataList = new ArrayList<>();
        decryptedDemandAttachmentDataList.add(decryptedDemandAttachmentData);

        requestData.setDecryptedAttachments(decryptedDemandAttachmentDataList);
        requestData.setDecryptedDemandContentId(decryptedMainDocumentContentId);


        CheckDemandResult checkDemandResult = docflowService.checkDemandAsync(docflowWithIncorrectSignaturesId, requestData).join();
        assertTrue(checkDemandResult.hasErrors());
        assertEquals(1, checkDemandResult.getErrorCodes().size());

        String signatureMismatchErrorCode = "0100100004";
        assertEquals(signatureMismatchErrorCode, checkDemandResult.getErrorCodes().get(0));
    }

    private ExternEngine GetExistingDocflowProviderEngine() {

        String configPath = "/secret/existing-docflow-provider-config.json";
        Configuration configuration = TestConfig.LoadConfigFromEnvironment(configPath);

        return ExternEngineBuilder.createExternEngine(configuration.getServiceBaseUri())
                .apiKey(configuration.getApiKey())
                .buildAuthentication(configuration.getAuthBaseUri(), ab -> ab
                        .withApiKey(configuration.getApiKey())
                        .passwordAuthentication(configuration.getLogin(), configuration.getPass())
                )
                .doNotUseCryptoProvider()
                .accountId(configuration.getAccountId())
                .build(HttpLoggingInterceptor.Level.BASIC);
    }

    private static List<QueryContext<Docflow>> createDocflows(
            ExternEngine engine,
            TestData[] testDatas
    ) {
        DraftService draftService = engine.getDraftService();

        List<CompletableFuture<QueryContext<Docflow>>> docflowCreateFutures = Arrays
                .stream(testDatas)
                .map(testData -> {
                    if (testData.getDocs() == null || testData.getDocs().length == 0) {
                        Assertions.fail("missing test documents");
                    }
                    return createDocflow(engine, draftService, testData);
                }).collect(Collectors.toList());

        CompletableFuture.allOf(docflowCreateFutures.toArray(new CompletableFuture[0])).join();

        return docflowCreateFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
    }

    private static CompletableFuture<QueryContext<Docflow>> createDocflow(
            ExternEngine engine,
            DraftService draftService,
            TestData testData
    ) {
        boolean isPfrTestData = testData.getClientInfo().getOrganization().getRegistrationNumberPfr() != null;
        if (isPfrTestData) {
            return createPfrDocflow(engine);
        } else {
            return createDefaultDocflow(engine, draftService, testData);
        }
    }

    private static CompletableFuture<QueryContext<Docflow>> createPfrDocflow(ExternEngine engine) {
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

        draftService
                .checkAsync(draftId)
                .join();

        draftService
                .prepareAsync(draftId)
                .join();

        return draftService
                .sendAsync(draftId)
                .thenApply(QueryContext::ensureSuccess)
                .whenComplete((cxt, throwable) -> {
                    awaitDocflowIndexed(engine, cxt.getDocflow());
                });
    }

    @NotNull
    private static DocumentContents updateSignaturesInDraftDocument(
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

        byte[] signature = engineUtils.crypto.sign(thumbprint, decryptedContent);

        DocumentContents contents = new DocumentContents();
        contents.setDescription(document.getDescription());
        contents.setBase64Content(data);
        contents.setSignature(Base64.getEncoder().encodeToString(signature));

        draftService.updateDocumentAsync(draftId, document.getId(), contents)
                .join().getOrThrow();
        return contents;
    }

    private static UUID buildPfrDraftViaBuilder(ExternEngine engine) {
        CryptoUtils cryptoUtils = CryptoUtils.with(engine.getCryptoProvider());
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

    private static CompletableFuture<QueryContext<Docflow>> createDefaultDocflow(
            ExternEngine engine,
            DraftService draftService,
            TestData testData
    ) {
        DraftMetaRequest draftMetaRequest = TestUtils.toDraftMetaRequest(testData);
        return draftService
                .createAsync(draftMetaRequest)
                .thenApply(QueryContext::getOrThrow)
                .thenCompose(draft -> addDocument(draftMetaRequest, testData, draft.getId())
                        .thenApply(QueryContext::getOrThrow)
                        .thenApply(o -> draft)
                )
                .thenCompose(draft -> draftService.sendAsync(draft.getId()))
                .thenApply(QueryContext::ensureSuccess)
                .whenComplete((cxt, throwable) -> {
                    awaitDocflowIndexed(engine, cxt.getDocflow());
                });
    }

    private static void awaitDocflowIndexed(ExternEngine engine, Docflow docflow) {
        Awaiter.waitForCondition(
                () -> engine.getDocflowService().lookupDocflowAsync(docflow.getId()),
                cxt -> cxt.isSuccess() || cxt.getServiceError().getCode() != 404,
                2000
        ).thenApply(QueryContext::getOrThrow);
    }

    private static CompletableFuture<QueryContext<DraftDocument>> addDocument(
            DraftMetaRequest meta,
            TestData data,
            UUID draftId
    ) {
        String path = data.getDocs()[0];
        DocType docType = DocType.getDocType(meta.getRecipient());
        DocumentContents dc = EngineUtils.with(engine)
                .createDocumentContents(path, docType);

        return engine
                .getDraftService()
                .addDecryptedDocumentAsync(draftId, dc);
    }


    private static Certificate getCloudCertificate() {
        return UncheckedSupplier.get(() -> engine
                .getCertificateService()
                .getCertificates(0, 100)
                .get()
                .getOrThrow()
                .getCertificates().stream()
                .filter(Certificate::getIsCloud)
                .filter(Certificate::getIsQualified)
                .filter(Certificate::getIsValid)
                .collect(Collectors.toList()).get(0));
    }
}