package ru.kontur.extern_api.sdk.model;
import com.google.gson.annotations.SerializedName;

public enum  TaskState  {
    @SerializedName("running")
    RUNNING,

    @SerializedName("succeed")
    SUCCEED,

    @SerializedName("failed")
    FAILED;

}


