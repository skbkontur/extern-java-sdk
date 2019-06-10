package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum ExternUserRole {
    @SerializedName("user")
    USER,

    @SerializedName("admin")
    ADMIN,

    @SerializedName("director")
    DIRECTOR
}
