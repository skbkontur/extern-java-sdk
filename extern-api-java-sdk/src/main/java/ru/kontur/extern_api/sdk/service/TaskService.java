package ru.kontur.extern_api.sdk.service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CheckResultData;
import ru.kontur.extern_api.sdk.model.Docflow;
import ru.kontur.extern_api.sdk.model.PrepareResult;
import ru.kontur.extern_api.sdk.model.TaskInfo;

public interface TaskService {

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<QueryContext<TaskInfo<Docflow>>> startSendAsync(UUID draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/send?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return список документооборотов
     * @see Docflow
     */
    CompletableFuture<QueryContext<TaskInfo<Docflow>>> startSendAsync(String draftId);


    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/check?deferred=true</p>
     * Асинхронный метод запускающий процес проверки черновика
     *
     * @param draftId идентификатор черновика
     * @return результат проверки
     */
    CompletableFuture<QueryContext<TaskInfo<CheckResultData>>> startCheckAsync(UUID draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/check?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return результат проверки
     */
    CompletableFuture<QueryContext<TaskInfo<CheckResultData>>> startCheckAsync(String draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return результат подготовки
     */
    CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepareAsync(UUID draftId);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/prepare?deferred=true</p>
     * Асинхронный метод запускающий процес отправки черновика в контролиоующий орган
     *
     * @param draftId идентификатор черновика
     * @return результат подготовки
     */
    CompletableFuture<QueryContext<TaskInfo<PrepareResult>>> startPrepareAsync(String draftId);



    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат проверки черновика в контролиоующий орган
     *
     * @param checkTaskInfo задача отправки черновика
     * @return результат проверки
     * @see CheckResultData
     */
    CompletableFuture<CheckResultData> getCheckResult(UUID draftId, TaskInfo<CheckResultData> checkTaskInfo);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат подготовки черновика в контролиоующий орган
     *
     * @param prepareTaskInfo задача отправки черновика
     * @return результат подготовки
     * @see PrepareResult
     */
    CompletableFuture<PrepareResult> getPrepareResult(UUID draftId, TaskInfo<PrepareResult> prepareTaskInfo);

    /**
     * <p>POST /v1/{accountId}/drafts/{draftId}/tasks/{taskId}</p>
     * Асинхронный метод получающий результат отправки черновика в контролиоующий орган
     *
     * @param sendTaskInfo задача отправки черновика
     * @return документооборот
     * @see Docflow
     */
    CompletableFuture<Docflow> getSendResult(UUID draftId, TaskInfo<Docflow> sendTaskInfo);
}
