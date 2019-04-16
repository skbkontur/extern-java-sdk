package ru.kontur.extern_api.sdk.model;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.utils.Awaiter;
import ru.kontur.extern_api.sdk.utils.DemandRequestDto;
import ru.kontur.extern_api.sdk.utils.TestSuite;

public class DemandTestData extends TestData {

    private final static String ORG_NAME = "Ромашка";
    private final static String DEFAULT_KND = "1160001";//"1165013";

    public UUID getDemandId() {
        return demandId;
    }

    public void setDemandId(UUID demandId) {
        this.demandId = demandId;
    }

    private UUID demandId;
    private UUID demandAttachmentId;

    public UUID getDemandAttachmentId() {
        return demandAttachmentId;
    }

    public void setDemandAttachmentId(UUID demandAttachmentId) {
        this.demandAttachmentId = demandAttachmentId;
    }

    public DemandTestData(TestData testData, UUID demandId) {
        this.demandId = demandId;
        this.setClientInfo(testData.getClientInfo());
        this.setDocs(testData.getDocs());
    }

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, TestSuite testSuite) {
        return getTestDemand(testData, testSuite, testSuite.getConfig().getServiceBaseUri(),
                testSuite.getConfig().getAuthBaseUri());
    }

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, TestSuite testSuite,
            String serviceBaseUri) {
        return getTestDemand(testData, testSuite, serviceBaseUri, testSuite.getConfig().getAuthBaseUri());
    }

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, TestSuite testSuite,
            String serviceBaseUri, String serviceAuthUri) {
        return testSuite.GetEasyDocflowApi(serviceBaseUri, serviceAuthUri)
                .thenCompose(
                        api -> {
                            DemandRequestDto requestDto = new DemandRequestDto(
                                    testSuite.engine.getAccountProvider().accountId(),
                                    testData.getClientInfo(), ORG_NAME,
                                    new String[]{DEFAULT_KND});
                            return api.getDemand(requestDto);
                        })
                .thenApply(responseDto -> new DemandTestData(testData, UUID.fromString(responseDto.getDocflowId())))
                .thenCompose(result -> waitForApi(result, testSuite));
    }

    private static CompletableFuture<DemandTestData> waitForApi(DemandTestData testData, TestSuite testSuite) {
        return Awaiter
                .waitForCondition(() -> {
                    return testSuite.engine.getDocflowService().getDocumentsAsync(testData.demandId)
                            .thenApply(QueryContext::get);
                }, result -> result != null && result.size() > 0, 2000)
                .thenApply(documents -> {
                    UUID atachmentId = documents
                            .stream()
                            .filter(document -> document.getDescription().getType()
                                    == DocumentType.Fns534DemandAttachment)
                            .map(Document::getId).findFirst().orElse(null);
                    testData.setDemandAttachmentId(atachmentId);
                    return testData;
                });
    }
}
