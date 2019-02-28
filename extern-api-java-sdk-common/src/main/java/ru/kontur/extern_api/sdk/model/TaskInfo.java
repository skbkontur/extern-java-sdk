package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

/**
 * <p>
 * Класс предоставляет информацию о выполняемой на сервере задаче.
 * Используется в сервисах: {@code DraftService} и {@code DraftBuilderService}.
 * </p>
 *
 * @author Anton Kufko
 */
public class TaskInfo<TResult> {

    private UUID id = null;
    private TaskState taskState = null;
    private TaskType taskType = null;

    private TResult taskResult = null;
    private Error error = null;

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
     * <ul>
     * <li>urn:taskType:send</li>
     * </ul>
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
     *         <ul>
     *         <li>urn:taskType:send</li>
     *         </ul>
     */
    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    /**
     * Возвращает статус задачи:
     * <ul>
     * <li>RUNNING</li>
     * </ul>
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
     *         <ul>
     *         <li>RUNNING</li>
     *         </ul>
     */
    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    /**
     * Возвращает результат выполнения задачи задачи
     *
     * @return TResult
     */
    public TResult getTaskResult() {
        return taskResult;
    }

    /**
     * Устанавливает результат выполнения задачи
     *
     * @param taskResult результат выполнения задачи:
     */
    public void setTaskState(TResult taskResult) {
        this.taskResult = taskResult;
    }

    /**
     * Возвращает ошибку возникшую при выполнении задачи
     *
     * @return Error
     */
    public Error getError() {
        return error;
    }

    /**
     * Устанавливает ошибку возникшую при выполнении задачи
     *
     * @param error ошибка
     */
    public void setError(Error error) {
        this.error = error;
    }
}
