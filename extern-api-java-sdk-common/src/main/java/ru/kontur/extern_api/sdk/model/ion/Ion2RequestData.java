package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.time.Year;
import java.util.ArrayList;

public class Ion2RequestData extends IonRequestData {

    @SerializedName("УслФорВып")
    private int reportGenerationCondition;

    @SerializedName("Год")
    private int year;

    @SerializedName("ЗапрашиваемыйНалог")
    @Nullable
    private ArrayList<RequestingTax> requestingTax;

    public Ion2RequestData(
            IonRequestContract.RequestType requestType,
            IonRequestContract.AnswerFormat answerFormat,
            IonRequestContract.ReportGenerationCondition reportGenerationCondition,
            Year year,
            @Nullable ArrayList<RequestingTax> requestingTax) {
        super(requestType, answerFormat);

        this.reportGenerationCondition = reportGenerationCondition.index();
        this.year = year.getValue();
        this.requestingTax = requestingTax;
    }

    public int getYear() {
        return year;
    }

    public int getReportGenerationCondition() {
        return reportGenerationCondition;
    }

    public ArrayList<RequestingTax> getRequestingTax() {
        return requestingTax;
    }
}
