package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;

import java.net.ConnectException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.CheckResultData;
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
    public CompletableFuture<QueryContext<TaskInfo<Docflow>>> startSendAsync(UUID draftId) {
        return api.startSend(acc.accountId(),draftId,false)
                .thenApply(contextAdaptor(QueryContext.TASK_INFO_DOCFLOW));
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<Docflow>>> startSendAsync(String draftId) {
        return startSendAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<CheckResultData>>> startCheckAsync(UUID draftId) {
        return api.startCheck(acc.accountId(),draftId)
                .thenApply(contextAdaptor(QueryContext.TASK_INFO_CHECK_RESULT_DATA));
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<CheckResultData>>> startCheckAsync(String draftId) {
        return startCheckAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepareAsync(UUID draftId) {
        return api.startPrepare(acc.accountId(),draftId)
                .thenApply(contextAdaptor(QueryContext.TASK_INFO_PREPARE_RESULT));
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepareAsync(String draftId) {
        return startPrepareAsync(UUID.fromString(draftId));
    }

    @Override
    public CompletableFuture<QueryContext<CheckResultData>> getCheckResult(UUID draftId, TaskInfo<Docflow> checkTaskInfo) {
        return null;
    }

    @Override
    public CompletableFuture<QueryContext<PrepareResult>> getPrepareResult(UUID draftId, TaskInfo<Docflow> prepareTaskInfo) {
        return null;
    }

    @Override
    public Docflow getSendResult(UUID draftId, TaskInfo<Docflow> sendTaskInfo) throws InterruptedException, ExecutionException {
 //      if(sendTaskInfo.getTaskType() != TaskType.SEND)
           //TODO throw error here
//           return null;

       while (sendTaskInfo.getTaskState() == TaskState.RUNNING) {
           Thread.sleep(2000);
           sendTaskInfo = api.getSendResult(acc.accountId(), draftId, sendTaskInfo.getId()).get().getData();
           if(sendTaskInfo.getTaskState() == TaskState.SUCCEED)
               return sendTaskInfo.getTaskResult();
       }

       //TODO throw error here
       return null;
    }
}
