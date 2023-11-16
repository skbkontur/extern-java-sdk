package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public enum PaperDocumentsDeliveryType {
    @SerializedName("no")
    NO,
    @SerializedName("toApplicant")
    TO_APPLICANT,
    @SerializedName("byPost")
    BY_POST
}
