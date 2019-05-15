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

import static java.util.Optional.ofNullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.crypt.mscapi.CryptoProviderMSCapi;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.DemandTestData;
import ru.kontur.extern_api.sdk.utils.DemandTestDataProvider;
import ru.kontur.extern_api.sdk.utils.DocType;
import ru.kontur.extern_api.sdk.utils.EngineUtils;
import ru.kontur.extern_api.sdk.utils.Lazy;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;
import ru.kontur.extern_api.sdk.utils.Zip;


@Execution(ExecutionMode.CONCURRENT)
@DisplayName("Docflow service should be able to")
class DocflowServiceIT {

    protected static ExternEngine engine;

    private static class TestPack {

        private final List<QueryContext<Docflow>> testDocflows;

        TestPack(List<QueryContext<Docflow>> testDocflows) {
            this.testDocflows = testDocflows;
        }
    }

    private static Logger log = Logger.getLogger(DocflowServiceIT.class.getName());
    private static EngineUtils engineUtils;

    private static Lazy<TestPack> testPack = Lazy.of(() -> getTestPack(engine));
    private static Lazy<TestData[]> testData = Lazy.of(() -> getTestData(engine));


    private static HttpClient client;
    private static Lazy<Certificate> cloudCert = Lazy.of(DocflowServiceIT::getCloudCertificate);
    private static Lazy<ApproveCodeProvider> codeProvider = Lazy
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

    private static Stream<QueryContext<Docflow>> docflowFactory() {
        return getTestPack(engine).testDocflows.stream();
    }

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engine.setCryptoProvider(new CryptoProviderMSCapi());
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
            Assertions.assertArrayEquals("<?xml".getBytes(), first5letters);
        }
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

        for (Document d : docs) {

            if (!d.hasDecryptedContent()) {
                continue;
            }

            byte[] decrypted = docflowService
                    .getDecryptedContentAsync(docflow.getId(), d.getId())
                    .join()
                    .getOrThrow();

            if (d.getDescription().getCompressed()) {
                decrypted = Zip.unzip(decrypted);
            }

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
        }
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

            Assertions.assertArrayEquals("<?xml".getBytes(), Arrays.copyOfRange(decrypted, 0, 5));
        }
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
    @DisplayName("two options for generate reply works")
    @MethodSource("docflowLazyFactory")
    void testGenerateReply(QueryContext<Docflow> docflowCxt) {
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

        String certificateBase64 = engineUtils.crypto.loadX509(thumbprint);
        GenerateReplyDocumentRequestData cert = new GenerateReplyDocumentRequestData()
                .certificateBase64(certificateBase64);

        ReplyDocument linkReply = engine.getAuthorizedHttpClient()
                .followPostLink(generateLink.getHref(), cert, ReplyDocument.class);

        ReplyDocument funcReply = engine.getDocflowService().generateReplyAsync(
                docflow.getId(),
                document.getId(),
                document.getReplyOptions()[0],
                Base64.getDecoder().decode(certificateBase64)
        ).join().getOrThrow();

        String lName = linkReply.getFilename();
        lName = lName.substring(0, lName.lastIndexOf("_"));

        String rName = funcReply.getFilename();
        rName = rName.substring(0, rName.lastIndexOf("_"));

        Assertions.assertEquals(lName, rName);
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
    @DisplayName("send reply document by choosing its type with cloud cert")
    @MethodSource("docflowFactory")
    void testSendOneReplyWithCloudSign(QueryContext<Docflow> docflowCxt) {
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

        Link generateLink = document.getReplyLinks()[0];

        GenerateReplyDocumentRequestData certificateBase64 = new GenerateReplyDocumentRequestData()
                .certificateBase64(cloudCert.get().getContent());

        client = engine.getAuthorizedHttpClient();

        ReplyDocument reply = client.followPostLink(
                generateLink.getHref(),
                certificateBase64,
                ReplyDocument.class
        );

        SignInitiation signInitiation = client.followPostLink(
                reply.getCloudSignLink().getHref(),
                SignInitiation.class
        );

        HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("code", codeProvider.get().apply(signInitiation.getRequestId()));
        queryParams.put("requestId", signInitiation.getRequestId());

        client.followPostLink(
                reply.getCloudSignConfirmLink().getHref(),
                queryParams,
                null,
                SignConfirmResultData.class
        );

        client.followPostLink(
                reply.getSendLink().getHref(),
                new SenderIp(engine.getUserIPProvider().userIP()),
                Docflow.class
        );
    }


    @ParameterizedTest
    @DisplayName("send all replies for document by choosing type")
    @MethodSource("docflowFactory")
    void testSendOneReplyWithCloudSignWithoutConfirmation(QueryContext<Docflow> docflowCxt) {
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

        Link generateLink = document.getReplyLinks()[0];

        // если есть ссылка на генерацию ИОПа
        Assertions.assertEquals("fns534-report-receipt", generateLink.getName());

        GenerateReplyDocumentRequestData certificateBase64 = new GenerateReplyDocumentRequestData()
                .certificateBase64(cloudCert.get().getContent());

        client = engine.getAuthorizedHttpClient();

        ReplyDocument reply = client.followPostLink(
                generateLink.getHref(),
                certificateBase64,
                ReplyDocument.class
        );

        SignInitiation signInitiation = client.followPostLink(
                reply.getCloudSignLink().getHref(),
                // включить возможность подписи без подтверждения
                Collections.singletonMap("forceConfirmation", false),
                null,
                SignInitiation.class
        );

        // если requestId == null -- то сервер подписал документ и не требует подтверждения
        Assertions.assertNull(signInitiation.getRequestId());

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
                .innKpp(company.getInn(), company.getKpp())
                .type(DocflowType.FNS534_REPORT)
        ).getOrThrow();

        Assertions.assertTrue(docflowPage.getDocflowsPageItem().size() <= 10);

        for (DocflowPageItem docflowPageItem : docflowPage.getDocflowsPageItem()) {
            Assertions.assertEquals(DocflowStatus.FINISHED, docflowPageItem.getStatus());
            Assertions.assertEquals(DocflowType.FNS534_REPORT, docflowPageItem.getType());
        }
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

    @ParameterizedTest
    @MethodSource("demandLazyFactory")
    @DisplayName("recognize demand attachment")
    void testDemandRecognize(DemandTestData demandTestData) throws RuntimeException {
        DocflowService docflowService = engine.getDocflowService();

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

    private static List<QueryContext<Docflow>> createDocflows(
            ExternEngine engine,
            TestData[] testData
    ) {

        DraftService draftService = engine.getDraftService();

        List<CompletableFuture<QueryContext<Docflow>>> docflowCreateFutures = Arrays
                .stream(testData)
                .map(td -> {
                    if (td.getDocs() == null || td.getDocs().length == 0) {
                        Assertions.fail("missing test documents");
                    }

                    DraftMetaRequest dm = TestUtils.toDraftMetaRequest(td);

                    return draftService
                            .createAsync(dm)
                            .thenApply(QueryContext::getOrThrow)
                            .thenCompose(draft -> addDocument(dm, td, draft.getId())
                                    .thenApply(QueryContext::getOrThrow)
                                    .thenApply(o -> draft)
                            )
                            .thenCompose(draft -> draftService.sendAsync(draft.getId()))
                            .thenApply(QueryContext::ensureSuccess)
                            .whenComplete((cxt, throwable) -> {
                                awaitDocflowIndexed(engine, cxt.getDocflow());
                            });

                }).collect(Collectors.toList());

        CompletableFuture.allOf(docflowCreateFutures.toArray(new CompletableFuture[0])).join();

        return docflowCreateFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());
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
