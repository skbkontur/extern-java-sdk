package ru.kontur.extern_api.sdk.model.descriptions;

import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Application extends DocflowDescription {

    private String recepient;

    private String finalRecepient;

    private String documentNumber;

    private FormVersion formVersion;

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

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public FormVersion getFormVersion() {
        return formVersion;
    }

    public void setFormVersion(FormVersion formVersion) {
        this.formVersion = formVersion;
    }
}
