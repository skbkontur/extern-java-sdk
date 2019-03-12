package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;

import java.net.ConnectException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.DataWrapper;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.TaskInfo;
import ru.kontur.extern_api.sdk.model.TaskState;
import ru.kontur.extern_api.sdk.model.TaskType;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.TaskService;

public class TaskServiceImpl implements TaskService {

    private final AccountProvider acc;
    private final DraftsApi api;

    TaskServiceImpl(AccountProvider accountProvider, DraftsApi api) {
        this.acc = accountProvider;
        this.api = api;
    }

    @Override
    public CompletableFuture<TaskInfo<Docflow>> startSendAsync(UUID draftId) {
        return api.startSend(acc.accountId(), draftId, false)
                .thenApply(ApiResponse::getData);
    }

    @Override
    public CompletableFuture<TaskInfo<Docflow>> startSendAsync(String draftId) {
        return startSendAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<TaskInfo<CheckResultData>> startCheckAsync(UUID draftId) {
        return api.startCheck(acc.accountId(), draftId)
                .thenApply(ApiResponse::getData);
    }

    @Override
    public CompletableFuture<TaskInfo<CheckResultData>> startCheckAsync(String draftId) {
        return startCheckAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<TaskInfo<PrepareResult>> startPrepareAsync(UUID draftId) {
        return api.startPrepare(acc.accountId(), draftId)
                .thenApply(ApiResponse::getData);
    }

    @Override
    public CompletableFuture<TaskInfo<PrepareResult>> startPrepareAsync(String draftId) {
        return startPrepareAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<CheckResultData> getCheckResult(UUID draftId,
            TaskInfo<CheckResultData> checkTaskInfo) {
        ensureCorrectType(checkTaskInfo, TaskType.CHECK);

        return WaitForCompletion(
                () -> api.getCheckResult(acc.accountId(), draftId, checkTaskInfo.getId()),
                this::checkStateRunning)
                .thenApply(DataWrapper::getData);
    }

    @Override
    public CompletableFuture<PrepareResult> getPrepareResult(UUID draftId,
            TaskInfo<PrepareResult> prepareTaskInfo) {
        ensureCorrectType(prepareTaskInfo, TaskType.PREPARE);

        return WaitForCompletion(
                () -> api.getPrepareResult(acc.accountId(), draftId, prepareTaskInfo.getId()),
                this::checkStateRunning);
    }

    @Override
    public CompletableFuture<Docflow> getSendResult(UUID draftId, TaskInfo<Docflow> sendTaskInfo) {
        ensureCorrectType(sendTaskInfo, TaskType.SEND);
        return WaitForCompletion(
                () -> api.getSendResult(acc.accountId(), draftId, sendTaskInfo.getId()),
                this::checkStateRunning);
    }

    private <T> boolean checkStateRunning(ApiResponse<TaskInfo<T>> callResult){
        return callResult.getData().getTaskState() != TaskState.RUNNING;
    }

    private <T> void ensureCorrectType(TaskInfo<T> taskInfo, TaskType requiredType){
        if (taskInfo.getTaskType() != requiredType)
        {
            //TODO throw error here
        }
    }

    private <T> CompletableFuture<T> WaitForCompletion(Supplier<CompletableFuture<ApiResponse<TaskInfo<T>>>> supplier,
            Predicate<ApiResponse<TaskInfo<T>>> predicate) {
        return supplier.get().thenCompose(result -> {
            if (predicate.test(result)) {
                return CompletableFuture.completedFuture(result.getData().getTaskResult());
            }
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException ex) {

            }
            return WaitForCompletion(supplier, predicate);
        });
    }

//
//    private Docflow WaitForSendComplete(UUID draftId, TaskInfo<Docflow> taskInfo) {
//        while (taskInfo.getTaskState() == TaskState.RUNNING) {
//            Thread.sleep(2000);
//            taskInfo = api
//                    .getSendResult(acc.accountId(), draftId, taskInfo.getId()).get().getData();
//            switch (taskInfo.getTaskState()) {
//                case RUNNING:
//                    continue;
//                case SUCCEED:
//                    return taskInfo.getTaskResult();
//                case FAILED:
//                    break;
//            }
//        }
//    }
}
