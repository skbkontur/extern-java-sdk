package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Ion3RequestData extends IonRequestData {
    @SerializedName("ДатаНач")
    private String beginDate;

    @SerializedName("ДатаКон")
    private String finishDate;

    @SerializedName("УслВыбОтч")
    private int reportSelectionCondition;

    public Ion3RequestData(
            IonRequestContract.RequestType requestType,
            IonRequestContract.AnswerFormat answerFormat,
            Date beginDate,
            Date finishDate,
            IonRequestContract.ReportSelectionCondition reportSelectionCondition) {
        super(requestType, answerFormat);

        this.beginDate = formatter.format(beginDate);
        this.finishDate = formatter.format(finishDate);
        this.reportSelectionCondition = reportSelectionCondition.index();
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public int getReportSelectionCondition() {
        return reportSelectionCondition;
    }
}
