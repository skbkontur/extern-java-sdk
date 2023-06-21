package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

public class TaskStatus {

    private UUID id;
    private TaskState taskState;
    private TaskType taskType;

    private TaskResultLink taskResultLink;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public TaskState getTaskState() {
        return taskState;
    }

    public void setTaskState(TaskState taskState) {
        this.taskState = taskState;
    }

    public TaskType getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskType taskType) {
        this.taskType = taskType;
    }

    public TaskResultLink getTaskResultLink() {
        return taskResultLink;
    }

    public void setTaskResultLink(TaskResultLink taskResultLink) {
        this.taskResultLink = taskResultLink;
    }
}
