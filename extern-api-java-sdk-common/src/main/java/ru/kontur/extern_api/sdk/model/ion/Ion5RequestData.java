package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

public class Ion5RequestData extends IonRequestData {
    @SerializedName("НаДату")
    private String onDate;

    public Ion5RequestData(int requestType, int answerFormat, String onDate) {
        super(requestType, answerFormat);
        this.onDate = onDate;
    }

    public String getOnDate() {
        return onDate;
    }
}
