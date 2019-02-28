package ru.kontur.extern_api.sdk.model;
import com.google.gson.annotations.SerializedName;

public enum TaskType implements Urn<TaskType> {
    @SerializedName("urn:taskType:send")
    SEND,
    @SerializedName("urn:taskType:sent:prepare")
    PREPARE,
    @SerializedName("urn:taskType:check")
    CHECK,
    @SerializedName("urn:taskType:print")
    PRINT,
    @SerializedName("urn:taskType:changeDocument")
    CHANGE_DOCUMENT,
    @SerializedName("urn:taskType:draftBuilderBuild")
    DRAFT_BUILDER_BUILD,
    @SerializedName("urn:taskType:draftBuilderChange")
    DRAFT_BUILDER_CHANGE

}
