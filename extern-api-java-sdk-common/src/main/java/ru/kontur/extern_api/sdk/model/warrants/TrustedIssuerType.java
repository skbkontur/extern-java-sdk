package ru.kontur.extern_api.sdk.model.warrants;

import com.google.gson.annotations.SerializedName;

/**
 * Тип представителя
 */
public enum TrustedIssuerType {
    /** Законный представитель - тот, кто по закону по умолчанию имеет права (например, опекун или конкурсный управляющий) */
    @SerializedName("legal")
    LEGAL,

    /** Уполномоченный представитель - лицо, на которое дали доверенность (в свою очередь, может выдать доверенность другому лицу) */
    @SerializedName("authorized")
    AUTHORIZED
}
