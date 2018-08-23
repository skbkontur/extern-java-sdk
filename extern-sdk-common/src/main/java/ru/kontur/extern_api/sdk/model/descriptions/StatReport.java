package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class StatReport extends DocflowDescription {

    private FormVersion formVersion;

    private String recipient;

    private String okpo;

    private String okud;

    private Date periodBegin;

    private Date periodEnd;

    private int periodCode;

    private int correctionNumber;

    public FormVersion getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersion formVersion) {
        this.formVersion = formVersion;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public String getOkud() {
        return okud;
    }

    public void setOkud(String okud) {
        this.okud = okud;
    }

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

    public int getPeriodCode() {
        return periodCode;
    }

    public void setPeriodCode(int periodCode) {
        this.periodCode = periodCode;
    }

    public int getCorrectionNumber() {
        return correctionNumber;
    }

    public void setCorrectionNumber(int correctionNumber) {
        this.correctionNumber = correctionNumber;
    }
}
