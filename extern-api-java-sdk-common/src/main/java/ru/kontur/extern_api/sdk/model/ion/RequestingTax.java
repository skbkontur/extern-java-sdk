package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class RequestingTax {

    @SerializedName("КБК")
    private String kbk;

    public RequestingTax(String kbk) {

        this.kbk = kbk;
    }
}
