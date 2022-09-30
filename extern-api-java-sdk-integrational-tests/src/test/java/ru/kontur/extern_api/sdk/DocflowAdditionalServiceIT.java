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
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumingThat;

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
import org.jetbrains.annotations.NotNull;
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
import ru.kontur.extern_api.sdk.model.builders.BuildDraftsBuilderResult;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilder;
import ru.kontur.extern_api.sdk.model.builders.pfr_report.PfrReportDraftsBuilderDocument;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.builders.pfr_report.PfrReportDraftsBuilderService;
import ru.kontur.extern_api.sdk.utils.ApproveCodeProvider;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.CryptoUtils;
import ru.kontur.extern_api.sdk.utils.DemandTestData;
import ru.kontur.extern_api.sdk.utils.DemandTestDataProvider;
import ru.kontur.extern_api.sdk.utils.DocType;
import ru.kontur.extern_api.sdk.utils.EngineUtils;
import ru.kontur.extern_api.sdk.utils.Lazy;
import ru.kontur.extern_api.sdk.utils.TestSuite;
import ru.kontur.extern_api.sdk.utils.TestUtils;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentCreator;
import ru.kontur.extern_api.sdk.utils.builders.DraftsBuilderDocumentFileCreator;

@Execution(ExecutionMode.SAME_THREAD)
class DocflowAdditionalServiceIT {

    protected static ExternEngine engine;

    private static class TestPack {

        private final List<QueryContext<Docflow>> testDocflows;

        TestPack(List<QueryContext<Docflow>> testDocflows) {
            this.testDocflows = testDocflows;
        }
    }

    private static Logger log = Logger.getLogger(DocflowAdditionalServiceIT.class.getName());
    private static EngineUtils engineUtils;

    private static Lazy<TestPack> testPack = Lazy.of(() -> getTestPack(engine));
    private static Lazy<TestData[]> testData = Lazy.of(() -> getTestData(engine));


    private static HttpClient client;
    private static Lazy<Certificate> cloudCert = Lazy.of(DocflowAdditionalServiceIT::getCloudCertificate);
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

    private static List<QueryContext<Docflow>> testDocflows;

    private static Stream<QueryContext<Docflow>> docflowFactory() {
        testDocflows = getTestPack(engine).testDocflows;
        return testDocflows.stream();
    }

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
        engineUtils = EngineUtils.with(engine);
        docflowService = engine.getDocflowService();
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