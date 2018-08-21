package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Letter extends DocflowDescription {

    private String cu;

    private String recepient;

    private String finalRecepient;

    private String subject;

    private UUID originDocflowId;

    private UUID originDocumentId;

    private UUID orgid;

    private UUID portalUserId;

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
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

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
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

    public UUID getOrgid() {
        return orgid;
    }

    public void setOrgid(UUID orgid) {
        this.orgid = orgid;
    }

    public UUID getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(UUID portalUserId) {
        this.portalUserId = portalUserId;
    }
}
