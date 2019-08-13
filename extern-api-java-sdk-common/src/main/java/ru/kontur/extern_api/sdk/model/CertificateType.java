package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum CertificateType {
    @SerializedName("iron")
    Iron,
    @SerializedName("dss")
    DSS,
    @SerializedName("cloud")
    Cloud
}