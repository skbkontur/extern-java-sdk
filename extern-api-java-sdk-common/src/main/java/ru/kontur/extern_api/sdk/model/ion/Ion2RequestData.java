package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Ion2RequestData extends IonRequestData {

    @SerializedName("УслФорВып")
    private int reportGenerationCondition;

    @SerializedName("Год")
    private String year;

    @SerializedName("ЗапрашиваемыйНалог")
    @Nullable
    private ArrayList<RequestingTax> requestingTax;

    Ion2RequestData(int requestType, int answerFormat, int reportGenerationCondition, String year, @Nullable ArrayList<RequestingTax> requestingTax) {
        super(requestType, answerFormat);

        this.reportGenerationCondition = reportGenerationCondition;
        this.year = year;
        this.requestingTax = requestingTax;
    }

    public String getYear() {
        return year;
    }

    public int getReportGenerationCondition() {
        return reportGenerationCondition;
    }

    public ArrayList<RequestingTax> getRequestingTax() {
        return requestingTax;
    }
}
