package ru.kontur.extern_api.sdk.model;

import java.net.HttpURLConnection;
import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.ErrorInfo;

import java.util.Collections;
import java.util.UUID;

/**
 * Класс предоставляет информацию о выполняемой на сервере задаче.
 * Используется в сервисах: {@code TaskService} и {@code DraftBuilderTaskService}.
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
    public void setId(UUID id) {
        this.id = id;
    }


    /**
     * Возвращает тип задачи:
     * {@link TaskType}
     *
     * @return {@link TaskType} тип задачи
     */
    public TaskType getTaskType() {
        return taskType;
    }

    /**
     * Возвращает статус задачи:
     * {@link TaskState}
     *
     * @return {@link TaskState} статус задачи
     */
    public TaskState getTaskState() {
        return taskState;
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
     * Возвращает ошибку возникшую при выполнении задачи
     *
     * @return {@link ErrorInfo}
     */
    public ErrorInfo getError() {
        return error;
    }

    /**
     * Устанавливает ошибку возникшую при выполнении задачи
     *
     * @param error {@link ErrorInfo} ошибка
     */
    void setError(ErrorInfo error) {
        this.error = error;
    }

    /**
     * @return ApiException with info from {@link ApiResponse#getErrorInfo()} or null if {@link
     *         ApiResponse#isSuccessful()}
     */
    public ApiException asApiException() {

        if (isNotFailed()) {
            throw new IllegalStateException("Task was not in failed state.");
        }

        if (error == null) {
            return new ApiException(HttpURLConnection.HTTP_OK, "no-error-info");
        }

        return new ApiException(
                error.getStatusCode(),
                error.getId(),
                error.getMessage(),
                Collections.emptyMap(),
                null
        );
    }

    public <TOut> TaskInfo<TOut> map(Function<TResult, TOut> mapper) {
        return map(TaskInfo::new, mapper);
    }

    public <TOut, TaskOut extends TaskInfo<TOut>> TaskOut map(
            Supplier<TaskOut> constructor,
            Function<TResult, TOut> mapper
    ) {
        TaskOut task = constructor.get();
        TaskInfo<TOut> taskOut = task;
        taskOut.error = error;
        taskOut.id = id;
        taskOut.taskState = taskState;
        taskOut.taskType = taskType;
        if (isNotFailed() && taskResult != null) {
            taskOut.taskResult = mapper.apply(taskResult);
        }
        return task;
    }


    @Contract(pure = true)
    private boolean isNotFailed() {
        return taskState != TaskState.FAILED;
    }

    public boolean isFailed() {
        return taskState == TaskState.FAILED;
    }
}


