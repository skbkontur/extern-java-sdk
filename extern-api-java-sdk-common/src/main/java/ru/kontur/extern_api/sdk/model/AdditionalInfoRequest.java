package ru.kontur.extern_api.sdk.model;

public class AdditionalInfoRequest {

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public AdditionalInfoRequest(String subject) {
        this.subject = subject;
    }

    public AdditionalInfoRequest() {
    }

    private String subject;
}
