package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Application;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Demand;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Ion;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Letter;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Report;
import ru.kontur.extern_api.sdk.model.descriptions.Fns534Submission;
import ru.kontur.extern_api.sdk.model.descriptions.StatLetter;
import ru.kontur.extern_api.sdk.model.descriptions.StatReport;

public enum DocflowType implements Urn<DocflowType> {


    @SerializedName("urn:docflow:fns534-report")
    FNS_534_REPORT(Fns534Report.class),

    @SerializedName("urn:docflow:fns534-demand")
    FNS_534_DEMAND(Fns534Demand.class),

    @SerializedName("urn:docflow:fns534-submission")
    FNS_534_SUBMISSION(Fns534Submission.class),

    @SerializedName("urn:docflow:fns534-ion")
    FNS_534_ION(Fns534Ion.class),

    @SerializedName("urn:docflow:fns534-letter")
    FNS_534_LETTER(Fns534Letter.class),

    @SerializedName("urn:docflow:fns534-application")
    FNS_534_APPLICATION(Fns534Application.class),

    @SerializedName("urn:docflow:stat-report")
    STAT_REPORT(StatReport.class),

    @SerializedName("urn:docflow:stat-letter")
    STAT_LETTER(StatLetter.class);

    private final Class<? extends IDocflowDescription> type;

    DocflowType(Class<? extends IDocflowDescription> type) {
        this.type = type;
    }

    public Class<? extends IDocflowDescription> getType() {
        return type;
    }
}
