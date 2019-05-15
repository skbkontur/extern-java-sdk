package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class Ion4RequestData extends IonRequestData {
    @SerializedName("НаДату")
    private String onDate;

    @SerializedName("Год")
    private String year;

    @SerializedName("ЗапрашиваемыйНалог")
    @Nullable
    private ArrayList<RequestingTax> requestingTax;

    public Ion4RequestData(int requestType, int answerFormat, String onDate, String year, @Nullable ArrayList<RequestingTax> requestingTax) {
        super(requestType, answerFormat);
        this.onDate = onDate;
        this.year = year;
        this.requestingTax = requestingTax;
    }

    public String getOnDate() {
        return onDate;
    }

    public String getYear() {
        return year;
    }

    public ArrayList<RequestingTax> getRequestingTax() {
        return requestingTax;
    }
}
