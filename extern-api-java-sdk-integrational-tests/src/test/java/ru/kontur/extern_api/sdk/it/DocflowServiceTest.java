package ru.kontur.extern_api.sdk.it;

import static java.util.Optional.ofNullable;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.DocflowDocumentDescription;
import ru.kontur.extern_api.sdk.model.DocflowFilter;
import ru.kontur.extern_api.sdk.model.DocflowPage;
import ru.kontur.extern_api.sdk.model.DocflowPageItem;
import ru.kontur.extern_api.sdk.model.DocflowStatus;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentContents;
import ru.kontur.extern_api.sdk.model.DocumentDescription;
import ru.kontur.extern_api.sdk.model.DraftMeta;
import ru.kontur.extern_api.sdk.model.GenerateReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.model.Organization;
import ru.kontur.extern_api.sdk.model.ReplyDocument;
import ru.kontur.extern_api.sdk.model.SendReplyDocumentRequestData;
import ru.kontur.extern_api.sdk.model.SignConfirmResultData;
import ru.kontur.extern_api.sdk.model.SignInitiation;
import ru.kontur.extern_api.sdk.model.Signature;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.it.model.TestData;
import ru.kontur.extern_api.sdk.it.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.it.utils.DocType;
import ru.kontur.extern_api.sdk.it.utils.EngineUtils;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;
import ru.kontur.extern_api.sdk.it.utils.TestUtils;
import ru.kontur.extern_api.sdk.utils.UncheckedRunnable;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;
import ru.kontur.extern_api.sdk.utils.Zip;
import ru.kontur.extern_api.sdk.utils.Lazy;


@DisplayName("Docflow service should be able to")
@Execution(ExecutionMode.CONCURRENT)
class DocflowServiceTest {

    private static class TestPack {

        private final List<QueryContext<Docflow>> testDocflows;

        TestPack(List<QueryContext<Docflow>> testDocflows) {
            this.testDocflows = testDocflows;
        }
    }

    private static Logger log = Logger.getLogger(DocflowServiceTest.class.getName());
    private static ExternEngine engine;
    private static EngineUtils engineUtils;

    private static Lazy<TestPack> testPack = Lazy.of(() -> getTestPack(engine));
    private static HttpClient client;
    private static Lazy<Certificate> cloudCert = Lazy.of(DocflowServiceTest::getCloudCertificate);
    private static Lazy<ApproveCodeProvider> codeProvider = Lazy
            .of(() -> new ApproveCodeProvider(engine));

    private DocflowService docflowService;

    private static TestPack getTestPack(ExternEngine ee) {
        String cert = engineUtils.crypto.loadX509(ee.getConfiguration().getThumbprint());
        TestData[] tds = TestUtils.getTestData(cert);
        List<QueryContext<Docflow>> qcs = UncheckedSupplier.get(() -> createDocflows(ee, tds));
        return new TestPack(qcs);
    }

    private static Stream<QueryContext<Docflow>> docflowsLazyFactory() {
        return testPack.get().testDocflows.stream();
    }

    private static Stream<QueryContext<Docflow>> docflowsFactory() {
        return getTestPack(engine).testDocflows.stream();
    }

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
        engineUtils = EngineUtils.with(engine);
    }

    @BeforeEach
    void setUp() {
        docflowService = engine.getDocflowService();

        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }

    @ParameterizedTest
    @DisplayName("get docflow by id")
    @MethodSource("docflowsLazyFactory")
    void testGetDocflow(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();
        Docflow returned = docflowService
                .lookupDocflow(docflowCxt.setDocflowId(docflow.getId()))
                .getOrThrow();

        assertEquals(docflow.getId(), returned.getId());
        assertEquals(docflow.getType(), returned.getType());
        assertEquals(docflow.getStatus(), returned.getStatus());

    }

    @Disabled("KA-1871")
    @ParameterizedTest
    @DisplayName("get docflow documents")
    @MethodSource("docflowsLazyFactory")
    void testGetDocuments(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        List<Document> docs = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .getOrThrow();

        Assertions.assertNotEquals(0, docs.size());
        for (Document document : docs) {
            assertNotNull(document.getDescription().getType());
        }

    }

    @ParameterizedTest
    @DisplayName("lookup single document")
    @MethodSource("docflowsLazyFactory")
    void testLookupDocument(QueryContext<Docflow> docflowCxt) throws Exception {
        Docflow docflow = docflowCxt.get();

        List<Document> docs = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .getOrThrow();

        for (Document d : docs) {
            Document actual = docflowService
                    .lookupDocumentAsync(docflow.getId().toString(), d.getId().toString())
                    .get()
                    .getOrThrow();

            assertNotNull(d.getSendDate());
            assertEquals(d.getId(), actual.getId());
            assertEquals(d.getDescription().getType(), actual.getDescription().getType());
            assertEquals(d.getId(), actual.getId());
        }
    }

    @ParameterizedTest
    @DisplayName("lookup document description")
    @MethodSource("docflowsLazyFactory")
    void testLookupDocumentDescription(QueryContext<Docflow> docflowCxt) throws Exception {
        Docflow docflow = docflowCxt.get();

        List<Document> docs = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .getOrThrow();

        for (Document d : docs) {
            DocflowDocumentDescription actual = docflowService
                    .lookupDescriptionAsync(docflow.getId().toString(), d.getId().toString())
                    .get()
                    .getOrThrow();

            DocflowDocumentDescription expected = d.getDescription();

            assertEquals(expected.getType(), actual.getType());
            assertEquals(expected.getFilename(), actual.getFilename());
            assertEquals(expected.getContentType(), actual.getContentType());
        }
    }

    @ParameterizedTest
    @DisplayName("get encrypted document content")
    @MethodSource("docflowsLazyFactory")
    void testGetEncryptedContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        assertNotNull(docsCxt.get());

        for (Document d : docsCxt.get()) {

            if (!d.hasEncryptedContent()) {
                continue;
            }

            byte[] encrypted = docflowService
                    .getEncryptedContent(docsCxt.setDocumentId(d.getId()))
                    .getOrThrow();

            byte[] decrypt = engineUtils
                    .crypto
                    .decrypt(engine.getConfiguration().getThumbprint(), encrypted);

            if (d.getDescription().getCompressed()) {
                decrypt = Zip.unzip(decrypt);
            }

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypt, 0, 5));
        }
    }

    @ParameterizedTest
    @DisplayName("get decrypted document content")
    @MethodSource("docflowsLazyFactory")
    void testGetDecryptedContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        assertNotNull(docsCxt.get());

        for (Document d : docsCxt.get()) {

            if (!d.hasDecryptedContent()) {
                continue;
            }

            byte[] decrypted = docflowService
                    .getDecryptedContent(docsCxt.setDocumentId(d.getId()))
                    .getOrThrow();

            if (d.getDescription().getCompressed()) {
                decrypted = Zip.unzip(decrypted);
            }

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
        }
    }

    @Disabled("@see DraftWithCloudCertTest")
    @ParameterizedTest
    @DisplayName("decrypt document content in cloud")
    @MethodSource("docflowsLazyFactory")
    void testDecryptContent(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        assertNotNull(docsCxt.get());

        for (Document d : docsCxt.get()) {

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

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
        }
    }

    @ParameterizedTest
    @DisplayName("get document signatures")
    @MethodSource("docflowsLazyFactory")
    void testGetSignatures(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        for (Document d : docsCxt.get()) {
            List<Signature> signs = docflowService
                    .getSignatures(docsCxt.setDocumentId(d.getId()))
                    .getOrThrow();

            for (Signature sign : signs) {
                assertNotNull(sign.getTitle());
            }
        }
    }

    @ParameterizedTest
    @DisplayName("get document single signature")
    @MethodSource("docflowsLazyFactory")
    void testGetSignature(QueryContext<Docflow> docflowCxt) throws Exception {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        for (Document d : docsCxt.get()) {
            List<Signature> signs = docflowService
                    .getSignatures(docsCxt.setDocumentId(d.getId()))
                    .getOrThrow();

            for (Signature sign : signs) {
                Signature signature = docflowService.getSignatureAsync(
                        docflow.getId().toString(),
                        d.getId().toString(),
                        sign.getId().toString()
                ).get().getOrThrow();

                Assertions.assertEquals(sign.getId(), signature.getId());
                Assertions.assertEquals(sign.getTitle(), signature.getTitle());
            }
        }
    }

    @ParameterizedTest
    @DisplayName("get document signature content")
    @MethodSource("docflowsLazyFactory")
    void testGetSignatureContent(QueryContext<Docflow> docflowCxt) throws Exception {
        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        for (Document d : docsCxt.get()) {
            List<Signature> signs = docflowService
                    .getSignatures(docsCxt.setDocumentId(d.getId()))
                    .getOrThrow();

            for (Signature sign : signs) {
                byte[] signatureContent = docflowService.getSignatureContentAsync(
                        docflow.getId().toString(),
                        d.getId().toString(),
                        sign.getId().toString()
                ).get().getOrThrow();

                assertNotNull(signatureContent);
            }
        }
    }

    @ParameterizedTest
    @DisplayName("two options for generate reply works")
    @MethodSource("docflowsLazyFactory")
    void testGenerateReply(QueryContext<Docflow> docflowCxt) throws Exception {
        Docflow docflow = docflowCxt.get();

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

        String certificateBase64 = engineUtils.crypto.loadX509(thumbprint);
        GenerateReplyDocumentRequestData cert = new GenerateReplyDocumentRequestData()
                .certificateBase64(certificateBase64);

        ReplyDocument linkReply = engine.getAuthorizedHttpClient()
                .followPostLink(generateLink.getHref(), cert, ReplyDocument.class);

        ReplyDocument funcReply = engine.getDocflowService()
                .generateReplyAsync(
                        docflow.getId().toString(),
                        document.getId().toString(),
                        document.getReplyOptions()[0],
                        certificateBase64
                ).get().getOrThrow();

        String lName = linkReply.getFilename();
        lName = lName.substring(0, lName.lastIndexOf("_"));

        String rName = funcReply.getFilename();
        rName = rName.substring(0, rName.lastIndexOf("_"));

        Assertions.assertEquals(lName, rName);
    }

    @ParameterizedTest
    @DisplayName("send reply documents by choosing its type")
    @MethodSource("docflowsFactory")
    void testSendOneReply(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

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
                ReplyDocument.class);

        byte[] signature = engineUtils.crypto.sign(thumbprint, reply.getContent());

        client.followPutLink(
                reply.getPutSignatureLink().getHref(),
                signature,
                ReplyDocument.class);

        client.followPostLink(
                reply.getSendLink().getHref(),
                new SendReplyDocumentRequestData().senderIp(engine.getUserIPProvider().userIP()),
                Docflow.class);
    }

    @ParameterizedTest
    @DisplayName("send reply document by choosing its type with cloud cert")
    @MethodSource("docflowsFactory")
    void testSendOneReplyWithCloudSign(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        Document document = docflow.getDocuments()
                .stream()
                .filter(Document::isNeedToReply)
                .findFirst()
                .orElse(null);

        if (document == null) {
            log.warning("Docflow " + docflow.getId() + " has no reply options");
            return;
        }

        Link generateLink = document.getReplyLinks()[0];

        GenerateReplyDocumentRequestData certificateBase64 = new GenerateReplyDocumentRequestData()
                .certificateBase64(cloudCert.get().getContent());

        client = engine.getAuthorizedHttpClient();

        ReplyDocument reply = client.followPostLink(
                generateLink.getHref(),
                certificateBase64,
                ReplyDocument.class);

        SignInitiation signInitiation = client.followPostLink(
                reply.getCloudSignLink().getHref(),
                SignInitiation.class);

        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("code", codeProvider.get().apply(signInitiation.getRequestId()));
        queryParams.put("requestId", signInitiation.getRequestId());

        client.followPostLink(
                reply.getCloudSignConfirmLink().getHref(),
                queryParams,
                null,
                SignConfirmResultData.class);

        client.followPostLink(
                reply.getSendLink().getHref(),
                new SendReplyDocumentRequestData().senderIp(engine.getUserIPProvider().userIP()),
                Docflow.class);
    }


    @ParameterizedTest
    @DisplayName("send all replies for document by choosing type")
    @MethodSource("docflowsFactory")
    void testSendOneReplyWithCloudSignWithoutConfirmation(QueryContext<Docflow> docflowCxt) {
        Docflow docflow = docflowCxt.get();

        Document document = docflow.getDocuments()
                .stream()
                .filter(Document::isNeedToReply)
                .findFirst()
                .orElse(null);

        if (document == null) {
            log.warning("Docflow " + docflow.getId() + " has no reply options");
            return;
        }

        Link generateLink = document.getReplyLinks()[0];

        // если есть ссылка на генерацию ИОПа
        Assertions.assertEquals("fns534-report-receipt", generateLink.getName());

        GenerateReplyDocumentRequestData certificateBase64 = new GenerateReplyDocumentRequestData()
                .certificateBase64(cloudCert.get().getContent());

        client = engine.getAuthorizedHttpClient();

        ReplyDocument reply = client.followPostLink(
                generateLink.getHref(),
                certificateBase64,
                ReplyDocument.class);

        SignInitiation signInitiation = client.followPostLink(
                reply.getCloudSignLink().getHref(),
                // включить возможность подписи без подтверждения
                Collections.singletonMap("forceConfirmation", false),
                null,
                SignInitiation.class);

        // если requestId == null -- то сервер подписал документ и не требует подтверждения
        Assertions.assertNull(signInitiation.getRequestId());

        client.followPostLink(
                reply.getSendLink().getHref(),
                new SendReplyDocumentRequestData().senderIp(engine.getUserIPProvider().userIP()),
                Docflow.class);
    }


    @ParameterizedTest
    @DisplayName("search docflows filtered")
    @MethodSource("docflowsLazyFactory")
    void testGetDocflows(QueryContext<Docflow> df) {
        Organization company = df.getDraftMeta().getPayer();

        DocflowPage docflowPage = docflowService.searchDocflows(DocflowFilter
                .page(0, 10)
                .finished(true)
                .incoming(false)
                .innKpp(company.getInn(), company.getKpp())
                .type(DocflowType.FNS534_REPORT)
        ).getOrThrow();

        Assertions.assertTrue(docflowPage.getDocflowsPageItem().size() <= 10);

        for (DocflowPageItem docflowPageItem : docflowPage.getDocflowsPageItem()) {
            assertEquals(DocflowStatus.FINISHED, docflowPageItem.getStatus());
            assertEquals(DocflowType.FNS534_REPORT, docflowPageItem.getType());
        }
    }


    @ParameterizedTest
    @MethodSource("docflowsLazyFactory")
    void testPrintFromDocflows(QueryContext<Docflow> docflowCxt) {
        DocumentContents dc = docflowCxt.getDocumentContents();

        String base64 = dc.getBase64Content();

        Docflow docflow = docflowCxt.get();

        // загружаем список документов
        QueryContext<List<Document>> docsCxt = docflowService
                .getDocuments(docflowCxt.setDocflowId(docflow.getId()))
                .ensureSuccess();

        assertNotNull(docsCxt.get());

        for (Document d : docsCxt.get()) {
            // печатаем только исходпую декларацию
            String name = ofNullable(d.getDescription())
                    .map(DocflowDocumentDescription::getFilename)
                    .orElse("");
            String dcName = ofNullable(dc.getDocumentDescription())
                    .map(DocumentDescription::getFilename)
                    .orElse("");

            if (name.equalsIgnoreCase(dcName)) {
                QueryContext<String> printCxt = docflowService
                        .print(docsCxt.setDocumentId(d.getId())
                                .setContentString(base64))
                        .ensureSuccess();

                assertNotNull(printCxt.get());
            }
        }
    }

    private static List<QueryContext<Docflow>> createDocflows(ExternEngine engine, TestData[] testData) {

        DraftService draftService = engine.getDraftService();

        List<QueryContext<Docflow>> testCtxs = Arrays.stream(testData).parallel().map(td -> {
            if (td.getDocs() == null || td.getDocs().length == 0) {
                Assertions.fail("No a test document");
            }

            DraftMeta dm = TestUtils.toDraftMeta(td);
            String path = td.getDocs()[0];
            DocType docType = DocType.getDocType(dm.getRecipient());

            CompletableFuture<QueryContext<Docflow>> future = draftService
                    .createAsync(dm.getSender(), dm.getRecipient(), dm.getPayer())
                    .thenApply(cxt -> engineUtils.addDecryptedDocument(cxt, path, docType))
                    .thenApply(draftService::send)
                    .thenApply(QueryContext::ensureSuccess);

            return UncheckedSupplier.get(future::get);

        }).collect(Collectors.toList());

        UncheckedRunnable.run(() -> Thread.sleep(3000));

        return testCtxs;
    }


    private static Certificate getCloudCertificate() {
        return UncheckedSupplier.get(() -> engine
                .getCertificateService()
                .getCertificateListAsync()
                .get()
                .getOrThrow()
                .getCertificates().stream()
                .filter(Certificate::getIsCloud)
                .filter(Certificate::getIsQualified)
                .filter(Certificate::getIsValid)
                .collect(Collectors.toList()).get(0));
    }


}
