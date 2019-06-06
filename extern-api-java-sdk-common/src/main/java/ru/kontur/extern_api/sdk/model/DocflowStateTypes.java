package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum DocflowStateTypes implements Urn<DocflowStateTypes> {


    @SerializedName("urn:docflow-state:neutral")
    NEUTRAL,

    @SerializedName("urn:docflow-state:successful")
    SUCCESSFUL,

    @SerializedName("urn:docflow-state:failed")
    FAILED,

    @SerializedName("urn:docflow-state:warning")
    WARNING
}
