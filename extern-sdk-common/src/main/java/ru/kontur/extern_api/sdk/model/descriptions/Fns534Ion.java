package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Ion extends DocflowDescription {

    private String knd;

    private String recepient;

    private String serviceCode;

    private String finalRecepient;

    private String formFullname;

    private String formShortname;

    private UUID portalUserId;

    public String getKnd() {
        return knd;
    }

    public void setKnd(String knd) {
        this.knd = knd;
    }

    public String getRecepient() {
        return recepient;
    }

    public void setRecepient(String recepient) {
        this.recepient = recepient;
    }

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
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

    public UUID getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(UUID portalUserId) {
        this.portalUserId = portalUserId;
    }
}
