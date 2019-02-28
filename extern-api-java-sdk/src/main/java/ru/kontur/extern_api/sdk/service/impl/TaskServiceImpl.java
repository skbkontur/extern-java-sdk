package ru.kontur.extern_api.sdk.service.impl;

import static ru.kontur.extern_api.sdk.utils.QueryContextUtils.contextAdaptor;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.api.DraftsApi;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.TaskInfo;
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
        return null;
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<CheckResultData>>> startCheckAsync(String draftId) {
        return null;
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepeareAsync(UUID draftId) {
        return null;
    }

    @Override
    public CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepeareAsync(String draftId) {
        return null;
    }
}
