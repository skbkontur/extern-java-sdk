package ru.kontur.extern_api.sdk.service.impl;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.TaskService;
import ru.kontur.extern_api.sdk.utils.Awaiter;

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
                .thenApply(WrappedCheckTaskInfo::unwrap);
    }

    @Override
    public CompletableFuture<PrepareTaskInfo> startPrepareAsync() {
        return api.startPrepare(acc.accountId(), draftId);
    }

    @Override
    public CompletableFuture<TaskState> getTaskStatus(TaskInfo taskInfo) {
        return api.getTaskInfo(acc.accountId(), draftId, taskInfo.getId())
                .thenApply(TaskInfo::getTaskState);
    }

    @Override
    public CompletableFuture<CheckResultData> getCheckResult(CheckTaskInfo checkTaskInfo) {
        return waitWhileRunning(() -> api
                .getCheckResult(acc.accountId(), draftId, checkTaskInfo.getId())
                .thenApply(WrappedCheckTaskInfo::unwrap)
        );
    }

    @Override
    public CompletableFuture<PrepareResult> getPrepareResult(PrepareTaskInfo prepareTaskInfo) {
        return waitWhileRunning(() -> api
                .getPrepareResult(acc.accountId(), draftId, prepareTaskInfo.getId())
        );
    }

    @Override
    public CompletableFuture<Docflow> getSendResult(SendTaskInfo sendTaskInfo) {
        return waitWhileRunning(() -> api
                .getSendResult(acc.accountId(), draftId, sendTaskInfo.getId())
        );
    }

    private <T extends TaskInfo<?>> boolean checkStateRunning(T callResult) {
        return callResult.getTaskState() != TaskState.RUNNING;
    }

    private <T, Task extends TaskInfo<T>> CompletableFuture<T> waitWhileRunning(
            Supplier<CompletableFuture<Task>> supplier
    ) {
        return Awaiter.waitForCondition(supplier, this::checkStateRunning, DELAY_TIMEOUT)
                .thenApply(result -> {
                    if (result.isFailed()) {
                        throw result.asApiException();
                    }
                    return result.getTaskResult();
                });
    }
}
