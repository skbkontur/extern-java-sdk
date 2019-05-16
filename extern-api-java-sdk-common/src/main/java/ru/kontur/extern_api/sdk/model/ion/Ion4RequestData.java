package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

public class Ion4RequestData extends IonRequestData {
    @SerializedName("НаДату")
    private String onDate;

    @SerializedName("Год")
    private int year;

    @SerializedName("ЗапрашиваемыйНалог")
    @Nullable
    private ArrayList<RequestingTax> requestingTax;

    public Ion4RequestData(
            IonRequestContract.RequestType requestType,
            IonRequestContract.AnswerFormat answerFormat,
            Date onDate,
            Year year,
            @Nullable ArrayList<RequestingTax> requestingTax) {
        super(requestType, answerFormat);
        this.onDate = formatter.format(onDate);
        this.year = year.getValue();
        this.requestingTax = requestingTax;
    }

    public String getOnDate() {
        return onDate;
    }

    public int getYear() {
        return year;
    }

    public ArrayList<RequestingTax> getRequestingTax() {
        return requestingTax;
    }
}
