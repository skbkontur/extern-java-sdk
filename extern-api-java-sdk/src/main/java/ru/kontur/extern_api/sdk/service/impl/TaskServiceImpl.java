package ru.kontur.extern_api.sdk.service.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;

import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private final AccountProvider acc;
    private final DraftsApi api;
    private final static int delayTimeOut = 2000;
    private final UUID draftId;

    TaskServiceImpl(AccountProvider accountProvider, DraftsApi api, UUID draftId) {
        this.acc = accountProvider;
        this.api = api;
        this.draftId = draftId;
    }

    @Override
    public CompletableFuture<DocflowTaskInfo> startSendAsync() {
        return api.startSend(acc.accountId(), draftId, false);
    }

    @Override
    public CompletableFuture<CheckResultDataTaskInfo> startCheckAsync() {
        return api.startCheck(acc.accountId(), draftId)
                .thenApply(WrappedCheckResultDataTaskInfo::unwrap);
    }

    @Override
    public CompletableFuture<PrepareResultTaskInfo> startPrepareAsync() {
        return api.startPrepare(acc.accountId(), draftId);
    }


    @Override
    public CompletableFuture<CheckResultData> getCheckResult(CheckResultDataTaskInfo checkTaskInfo) {

        return waitWhileRunning(() -> api.getCheckResult(acc.accountId(), draftId, checkTaskInfo.getId())
                .thenApply(result -> (TaskInfo<DataWrapper<CheckResultData>>) result))
                .thenApply(DataWrapper::getData);
    }

    @Override
    public CompletableFuture<PrepareResult> getPrepareResult(PrepareResultTaskInfo prepareTaskInfo) {

        return waitWhileRunning(
                () -> api.getPrepareResult(acc.accountId(), draftId, prepareTaskInfo.getId())
                        .thenApply(result -> (TaskInfo<PrepareResult>) result));
    }

    @Override
    public CompletableFuture<Docflow> getSendResult(DocflowTaskInfo sendTaskInfo) {
        return waitWhileRunning(
                () -> api.getSendResult(acc.accountId(), draftId, sendTaskInfo.getId())
                        .thenApply(result -> (TaskInfo<Docflow>) result));
    }

    private <T> boolean checkStateRunning(TaskInfo<T> callResult) {
        return callResult.getTaskState() != TaskState.RUNNING;
    }

    private <T> CompletableFuture<T> waitWhileRunning(Supplier<CompletableFuture<TaskInfo<T>>> supplier) {
        return waitForCondition(supplier, this::checkStateRunning);
    }

    private <T> CompletableFuture<T> waitForCondition(Supplier<CompletableFuture<TaskInfo<T>>> supplier,
                                                      Predicate<TaskInfo<T>> predicate) {
        return supplier.get().thenCompose(result -> {
            if (predicate.test(result)) {
                CompletableFuture<T> future = new CompletableFuture<>();
                if (result.getError() != null) {
                    future.completeExceptionally(result.asApiException());
                } else {
                    future.complete(result.getTaskResult());
                }
                return future;
            }

            try {
                TimeUnit.MILLISECONDS.sleep(delayTimeOut);
            } catch (InterruptedException ignored) {
                Thread.currentThread().interrupt();
            }
            return waitForCondition(supplier, predicate);
        });
    }
}
