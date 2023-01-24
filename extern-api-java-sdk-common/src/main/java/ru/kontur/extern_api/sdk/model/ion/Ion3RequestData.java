package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Ion3RequestData extends IonRequestData {
    @SerializedName("ДатаНач")
    private String beginDate;

    @SerializedName("ДатаКон")
    private String finishDate;

    public Ion3RequestData(
            IonRequestContract.AnswerFormat answerFormat,
            Date beginDate,
            Date finishDate) {
        super(answerFormat);

        this.beginDate = formatter.format(beginDate);
        this.finishDate = formatter.format(finishDate);
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getFinishDate() {
        return finishDate;
    }
}
