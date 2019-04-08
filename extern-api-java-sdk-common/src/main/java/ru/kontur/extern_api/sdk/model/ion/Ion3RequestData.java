package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;

public class Ion3RequestData extends IonRequestData {
    @SerializedName("ДатаНач")
    private String beginDate;

    @SerializedName("ДатаНач")
    private String finishDate;

    @SerializedName("УслВыбОтч")
    private int reportSelectionCondition;

    Ion3RequestData(int requestType, int acceptType, String beginDate, String finishDate, int reportSelectionCondition) {
        super(requestType, acceptType);

        this.beginDate = beginDate;
        this.finishDate = finishDate;
        this.reportSelectionCondition = reportSelectionCondition;
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
