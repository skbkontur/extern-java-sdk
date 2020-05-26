package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.httpclient.api.RepliesApi;
import ru.kontur.extern_api.sdk.model.TaskInfo;
import ru.kontur.extern_api.sdk.model.TaskState;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.service.ReplyTaskService;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ReplyTaskServiceImpl implements ReplyTaskService {

    private final AccountProvider acc;
    private final RepliesApi api;
    private final UUID docflowId;
    private final UUID documentId;
    private final UUID replyId;

    ReplyTaskServiceImpl(
            AccountProvider accountProvider,
            RepliesApi api,
            UUID docflowId,
            UUID documentId,
            UUID replyId
    ) {
        this.acc = accountProvider;
        this.api = api;
        this.docflowId = docflowId;
        this.documentId = documentId;
        this.replyId = replyId;
    }

    @Override
    public CompletableFuture<TaskState> getTaskStatus(UUID taskId) {
        return api.getTaskInfo(acc.accountId(), docflowId, documentId, replyId, taskId)
                .thenApply(TaskInfo::getTaskState);
    }

    @Override
    public CompletableFuture<TaskState> getPfrTaskStatus(UUID taskId) {
        return api.getPfrTaskInfo(acc.accountId(), docflowId, documentId, replyId, taskId)
                .thenApply(TaskInfo::getTaskState);
    }
}
