package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Ion1RequestData extends IonRequestData {
    @SerializedName("НаДату")
    private String onDate;

    public Ion1RequestData(
            IonRequestContract.RequestType requestType,
            IonRequestContract.AnswerFormat answerFormat,
            Date onDate) {
        super(requestType, answerFormat);
        this.onDate = formatter.format(onDate);
    }

    public String getOnDate() {
        return onDate;
    }
}
