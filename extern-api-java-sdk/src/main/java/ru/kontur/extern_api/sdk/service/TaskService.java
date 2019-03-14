package ru.kontur.extern_api.sdk.service;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.model.*;

public interface TaskService {

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<SendTaskInfo> startSendAsync();

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/check?deferred=true</p>
     * Асинхронный метод запускающий процес проверки черновика
     *
     * @return результат проверки
     */
    CompletableFuture<CheckTaskInfo> startCheckAsync();

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @return результат подготовки
     */
    CompletableFuture<PrepareTaskInfo> startPrepareAsync();

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат проверки черновика в контролиоующий орган
     *
     * @param checkTaskInfo задача отправки черновика
     * @return результат проверки
     * @see CheckResultData
     */
    CompletableFuture<CheckResultData> getCheckResult(CheckTaskInfo checkTaskInfo);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат подготовки черновика в контролиоующий орган
     *
     * @param prepareTaskInfo задача отправки черновика
     * @return результат подготовки
     * @see PrepareResult
     */
    CompletableFuture<PrepareResult> getPrepareResult(PrepareTaskInfo prepareTaskInfo);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат отправки черновика в контролиоующий орган
     *
     * @param sendTaskInfo задача отправки черновика
     * @return документооборот
     * @see Docflow
     */
    CompletableFuture<Docflow> getSendResult(SendTaskInfo sendTaskInfo);
}
