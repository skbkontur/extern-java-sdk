package ru.kontur.extern_api.sdk.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.TaskState;

public interface ReplyTaskService {

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/tasks/{apiTaskId}</p>
     * Асинхронный метод, получающий текущий статус задачи
     *
     * @param taskId ИД задачи
     * @return статус задачи
     */
    CompletableFuture<TaskState> getTaskStatus(UUID taskId);

}
