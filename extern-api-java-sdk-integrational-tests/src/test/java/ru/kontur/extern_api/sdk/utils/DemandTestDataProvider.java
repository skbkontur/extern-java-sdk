package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.DemandRequestDto;
import ru.kontur.extern_api.sdk.model.Document;
import ru.kontur.extern_api.sdk.model.DocumentType;
import ru.kontur.extern_api.sdk.model.TestData;

public class DemandTestDataProvider extends TestData {

    private final static String ORG_NAME = "Ромашка";
    private final static String DEFAULT_KND = "1160001";//"1165013";

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, TestSuite testSuite) {
        return getTestDemand(testData, testSuite, testSuite.getConfig().getServiceBaseUri(),
                testSuite.getConfig().getAuthBaseUri());
    }

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, TestSuite testSuite,
            String serviceBaseUri) {
        return getTestDemand(testData, testSuite, serviceBaseUri, testSuite.getConfig().getAuthBaseUri());
    }

    public static CompletableFuture<DemandTestData> getTestDemand(TestData testData, @NotNull TestSuite testSuite,
            String serviceBaseUri, String serviceAuthUri) {
        return testSuite.GetEasyDocflowApi(serviceBaseUri, serviceAuthUri)
                .thenCompose(
                        api -> {
                            DemandRequestDto requestDto = DemandRequestDtoProvider.getDemandRequest(
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
                .waitForCondition(() -> testSuite.engine.getDocflowService().getDocumentsAsync(testData.getDemandId())
                        .thenApply(QueryContext::get), result -> result != null && result.size() > 0, 2000)
                .thenApply(documents -> {
                    UUID attachmentId = documents
                            .stream()
                            .filter(document -> document.getDescription().getType()
                                    == DocumentType.Fns534DemandAttachment)
                            .map(Document::getId).findFirst().orElse(null);
                    testData.setDemandAttachmentId(attachmentId);
                    return testData;
                });
    }
}
