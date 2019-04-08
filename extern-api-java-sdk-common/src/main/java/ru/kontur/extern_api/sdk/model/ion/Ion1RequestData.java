package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

public class Ion1RequestData extends IonRequestData {
    @SerializedName("НаДату")
    private String onDate;

    Ion1RequestData(int requestType, int acceptType, String onDate) {
        super(requestType, acceptType);
        this.onDate = onDate;
    }

    public String getOnDate() {
        return onDate;
    }
}
