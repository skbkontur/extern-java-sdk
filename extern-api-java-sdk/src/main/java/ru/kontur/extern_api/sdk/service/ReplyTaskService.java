package ru.kontur.extern_api.sdk.service;

import ru.kontur.extern_api.sdk.model.TaskState;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ReplyTaskService {

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/replies/{replyId}/tasks/{apiTaskId}</p>
     * Асинхронный метод, получающий текущий статус задачи
     *
     * @param taskId ИД задачи
     * @return статус задачи
     */
    CompletableFuture<TaskState> getTaskStatus(UUID taskId);

    /**
     * <p>GET /v1/{accountId}/docflows/{docflowId}/documents/{documentId}/pfr-replies/{replyId}/tasks/{apiTaskId}</p>
     * Асинхронный метод, получающий текущий статус задачи подписания ответного документа ПФР
     *
     * @param taskId ИД задачи
     * @return статус задачи
     */
    CompletableFuture<TaskState> getPfrTaskStatus(UUID taskId);
}
