package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Report extends DocflowDescription {

    private String knd;

    private Date periodBegin;

    private Date periodEnd;

    private int periodCode;

    private String recepient;

    private String finalRecepient;

    private String version;

    private int correctionNumber;

    private String formFullname;

    private String formShortname;

    private String payerInn;

    private UUID portalUserId;

    public String getKnd() {
        return knd;
    }

    public void setKnd(String knd) {
        this.knd = knd;
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getCorrectionNumber() {
        return correctionNumber;
    }

    public void setCorrectionNumber(int correctionNumber) {
        this.correctionNumber = correctionNumber;
    }

    public String getFormFullname() {
        return formFullname;
    }

    public void setFormFullname(String formFullname) {
        this.formFullname = formFullname;
    }

    public String getFormShortname() {
        return formShortname;
    }

    public void setFormShortname(String formShortname) {
        this.formShortname = formShortname;
    }

    public String getPayerInn() {
        return payerInn;
    }

    public void setPayerInn(String payerInn) {
        this.payerInn = payerInn;
    }

    public UUID getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(UUID portalUserId) {
        this.portalUserId = portalUserId;
    }
}
