package ru.kontur.extern_api.sdk.service.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.TaskService;
import ru.kontur.extern_api.sdk.utils.ConditionAwaiter;

public class TaskServiceImpl implements TaskService {

    private final AccountProvider acc;
    private final DraftsApi api;
    private final static int DELAY_TIMEOUT = 2000;
    private final UUID draftId;

    TaskServiceImpl(AccountProvider accountProvider, DraftsApi api, UUID draftId) {
        this.acc = accountProvider;
        this.api = api;
        this.draftId = draftId;
    }

    @Override
    public CompletableFuture<SendTaskInfo> startSendAsync() {
        return api.startSend(acc.accountId(), draftId, false);
    }

    @Override
    public CompletableFuture<CheckTaskInfo> startCheckAsync() {
        return api.startCheck(acc.accountId(), draftId)
                .thenApply(WrappedCheckDataTaskInfo::unwrap);
    }

    @Override
    public CompletableFuture<PrepareTaskInfo> startPrepareAsync() {
        return api.startPrepare(acc.accountId(), draftId);
    }


    @Override
    public CompletableFuture<CheckResultData> getCheckResult(CheckTaskInfo checkTaskInfo) {

        return waitWhileRunning(() -> api.getCheckResult(acc.accountId(), draftId, checkTaskInfo.getId())
                .thenApply(result -> (TaskInfo<DataWrapper<CheckResultData>>) result))
                .thenApply(DataWrapper::getData);
    }

    @Override
    public CompletableFuture<PrepareResult> getPrepareResult(PrepareTaskInfo prepareTaskInfo) {

        return waitWhileRunning(
                () -> api.getPrepareResult(acc.accountId(), draftId, prepareTaskInfo.getId())
                        .thenApply(result -> (TaskInfo<PrepareResult>) result));
    }

    @Override
    public CompletableFuture<Docflow> getSendResult(SendTaskInfo sendTaskInfo) {
        return waitWhileRunning(
                () -> api.getSendResult(acc.accountId(), draftId, sendTaskInfo.getId())
                        .thenApply(result -> (TaskInfo<Docflow>) result));
    }

    private <T> boolean checkStateRunning(TaskInfo<T> callResult) {
        return callResult.getTaskState() != TaskState.RUNNING;
    }

    private <T> CompletableFuture<T> waitWhileRunning(Supplier<CompletableFuture<TaskInfo<T>>> supplier) {
        return ConditionAwaiter.waitForCondition(supplier, this::checkStateRunning, DELAY_TIMEOUT)
                .thenCompose(result -> {
                            CompletableFuture<T> future = new CompletableFuture<>();
                            if (result.getError() != null) {
                                future.completeExceptionally(result.asApiException());
                            } else {
                                future.complete(result.getTaskResult());
                            }
                            return future;
                        }
                );
    }

}
