package ru.kontur.extern_api.sdk.model;

import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.ErrorInfo;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Класс предоставляет информацию о выполняемой на сервере задаче.
 * Используется в сервисах: {@code DraftService} и {@code DraftBuilderService}.
 *
 * @author Anton Kufko
 */
public class TaskInfo<TResult> {

    private UUID id = null;
    private TaskState taskState = null;
    private TaskType taskType = null;

    private TResult taskResult = null;
    private ErrorInfo error = null;

    /**
     * Возвращает идентификатор ДО
     *
     * @return id идентификатор ДО
     */
    public UUID getId() {
        return id;
    }

    /**
     * Устанавливает идентификатор ДО
     *
     * @param id идентификатор ДО
     */
    @SuppressWarnings("unused")
    public void setId(UUID id) {
        this.id = id;
    }


    /**
     * Возвращает тип задачи:
     * {@link TaskType}
     *
     * @return TaskType тип задачи
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Устанавливает тип задачи
     *
     * @param taskType тип задачи:
     * {@link TaskType}
     */
    @SuppressWarnings("unused")
    void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * Возвращает статус задачи:
     * {@link TaskState}
     *
     * @return TaskState статус задачи
     */
    public TaskState getTaskState() {
        return taskState;
    }

    /**
     * Устанавливает статус задачи
     *
     * @param taskState статус задачи:
     *                  {@link TaskState}
     */
    @SuppressWarnings("unused")
    void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    /**
     * Возвращает результат выполнения задачи задачи
     *
     * @return TResult @see T
     */
    public TResult getTaskResult() {
        return taskResult;
    }

    /**
     * Устанавливает результат выполнения задачи
     *
     * @param taskResult результат выполнения задачи:
     */
    @SuppressWarnings("unused")
    void setTaskResult(TResult taskResult) {
        this.taskResult = taskResult;
    }

    /**
     * Возвращает ошибку возникшую при выполнении задачи
     *
     * @return Error
     */
    public ErrorInfo getError() {
        return error;
    }

    /**
     * Устанавливает ошибку возникшую при выполнении задачи
     *
     * @param error ошибка
     */
    @SuppressWarnings("unused")
    void setError(ErrorInfo error) {
        this.error = error;
    }


    /**
     * @return ApiException with info from {@link ApiResponse#getErrorInfo()} or null if {@link
     *         ApiResponse#isSuccessful()}
     */
    public ApiException asApiException() {

        if (isSuccessful()) {
            return null;
        }

        ErrorInfo e = error;

        if (e == null) {
            return new ApiException(error.getStatusCode(), "no-error-info");
        }

        return new ApiException(
                error.getStatusCode(),
                e.getId(),
                e.getMessage(),
                Collections.emptyMap(),
                null
        );
    }

    protected boolean isSuccessful() {
        return taskState == TaskState.RUNNING || taskState == TaskState.SUCCEED;
    }
}


