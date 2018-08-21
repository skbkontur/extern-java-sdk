package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.Date;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Submission extends DocflowDescription {

    private String knd;

    private Date periodBegin;

    private Date periodEnd;

    private String recepient;

    private String finalRecepient;

    private String formFullname;

    private String formShortname;

    private String inventoryDescription;

    private UUID originDocflowId;

    private UUID originDocumentId;

    private UUID portalUserId;

    private String ogrn;

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

    public String getInventoryDescription() {
        return inventoryDescription;
    }

    public void setInventoryDescription(String inventoryDescription) {
        this.inventoryDescription = inventoryDescription;
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

    public UUID getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(UUID portalUserId) {
        this.portalUserId = portalUserId;
    }

    public String getOgrn() {
        return ogrn;
    }

    public void setOgrn(String ogrn) {
        this.ogrn = ogrn;
    }
}
