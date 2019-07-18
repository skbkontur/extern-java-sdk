package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum ConfirmType {
    @SerializedName("none")
    NONE,
    @SerializedName("sms")
    SMS,
    @SerializedName("my-dss")
    MY_DSS,
    @SerializedName("applet")
    APPLET
}