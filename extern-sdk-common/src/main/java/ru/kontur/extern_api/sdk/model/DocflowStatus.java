package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum DocflowStatus {

    @SerializedName("urn:docflow-common-status:sent")
    SENT,
    @SerializedName("urn:docflow-common-status:delivered")
    DELIVERED,
    @SerializedName("urn:docflow-common-status:response-arrived")
    RESPONSE_ARRIVED,
    @SerializedName("urn:docflow-common-status:response-processed")
    RESPONSE_PROCESSED,
    @SerializedName("urn:docflow-common-status:received")
    RECEIVED,
    @SerializedName("urn:docflow-common-status:arrived")
    ARRIVED,
    @SerializedName("urn:docflow-common-status:processed")
    PROCESSED,
    @SerializedName("urn:docflow-common-status:finished")
    FINISHED;

}
