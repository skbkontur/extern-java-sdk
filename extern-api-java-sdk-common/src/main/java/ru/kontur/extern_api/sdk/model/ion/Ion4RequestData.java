package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

import java.time.Year;
import java.util.ArrayList;
import java.util.Date;

public class Ion4RequestData extends IonRequestData {
    @SerializedName("ДатаНач")
    private String beginDate;

    @SerializedName("ДатаКон")
    private String finishDate;

    @SerializedName("ПрКБК")
    private int prKbk;

    @SerializedName("ЗапрКБК")
    @Nullable
    private ArrayList<RequestingTax> requestingTax;

    public Ion4RequestData(
            IonRequestContract.AnswerFormat answerFormat,
            Date beginDate,
            Date finishDate,
            int prKbk,
            @Nullable ArrayList<RequestingTax> requestingTax) {
        super(answerFormat);

        this.requestingTax = requestingTax;
        this.beginDate = formatter.format(beginDate);
        this.finishDate = formatter.format(finishDate);
        this.prKbk = prKbk;
    }

    public int getPrKbk() {
        return prKbk;
    }

    public String getBeginDate() {
        return beginDate;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public ArrayList<RequestingTax> getRequestingTax() {
        return requestingTax;
    }
}
