package ru.kontur.extern_api.sdk.model;
import com.google.gson.annotations.SerializedName;

public enum TaskType implements Urn<TaskType> {
    @SerializedName("urn:task-type:send")
    SEND,
    @SerializedName("urn:task-type:prepare")
    PREPARE,
    @SerializedName("urn:task-type:check")
    CHECK,
    @SerializedName("urn:task-type:print")
    PRINT,
    @SerializedName("urn:task-type:changeDocument")
    CHANGE_DOCUMENT,
    @SerializedName("urn:task-type:draftBuilderBuild")
    DRAFT_BUILDER_BUILD,
    @SerializedName("urn:task-type:draftBuilderChange")
    DRAFT_BUILDER_CHANGE

}
