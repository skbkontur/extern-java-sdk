package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Submission extends DocflowDescription {

    private Date periodBegin;

    private Date periodEnd;

    private String recepient;

    private String finalRecepient;

    private UUID originDocflowId;

    private UUID originDocumentId;

    private String ogrn;

    private FormVersion formVersion;

    public Date getPeriodBegin() {
        return periodBegin;
    }

    public void setPeriodBegin(Date periodBegin) {
        this.periodBegin = periodBegin;
    }

    public Date getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(Date periodEnd) {
        this.periodEnd = periodEnd;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getFinalRecepient() {
        return finalRecepient;
    }

    public void setFinalRecepient(String finalRecepient) {
        this.finalRecepient = finalRecepient;
    }

    public UUID getOriginDocflowId() {
        return originDocflowId;
    }

    public void setOriginDocflowId(UUID originDocflowId) {
        this.originDocflowId = originDocflowId;
    }

    public UUID getOriginDocumentId() {
        return originDocumentId;
    }

    public void setOriginDocumentId(UUID originDocumentId) {
        this.originDocumentId = originDocumentId;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }

    public FormVersion getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersion formVersion) {
        this.formVersion = formVersion;
    }
}
