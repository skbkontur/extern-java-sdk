package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Application extends DocflowDescription {

    private Date periodBegin;

    private Date periodEnd;

    private int periodCode;

    private String recepient;

    private int correctionNumber;

    private String payerInn;

    private String okud;

    private String okpo;

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

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public int getCorrectionNumber() {
        return correctionNumber;
    }

    public void setCorrectionNumber(int correctionNumber) {
        this.correctionNumber = correctionNumber;
    }

    public String getPayerInn() {
        return payerInn;
    }

    public void setPayerInn(String payerInn) {
        this.payerInn = payerInn;
    }

    public String getOkud() {
        return okud;
    }

    public void setOkud(String okud) {
        this.okud = okud;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }
}
