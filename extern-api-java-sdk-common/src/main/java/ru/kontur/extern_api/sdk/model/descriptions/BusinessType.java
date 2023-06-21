package ru.kontur.extern_api.sdk.model.descriptions;

import com.google.gson.annotations.SerializedName;

public enum BusinessType {
    @SerializedName("ip")
    IP,
    @SerializedName("ul")
    UL,

    @SerializedName("unknown")
    UNKNOWN
}
