package ru.kontur.extern_api.sdk.model.ion;

import com.google.gson.annotations.SerializedName;
import org.jetbrains.annotations.Nullable;

public class RequestingTax {

    @SerializedName("Налог")
    @Nullable
    private String taxName;

    @SerializedName("КБК")
    private String kbk;

    @SerializedName("ОКАТО")
    private String okato;

    public RequestingTax(@Nullable String taxName, String kbk, String okato) {

        this.taxName = taxName;
        this.kbk = kbk;
        this.okato = okato;
    }
}
