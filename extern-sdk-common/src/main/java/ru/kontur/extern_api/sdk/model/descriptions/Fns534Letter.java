package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Letter extends DocflowDescription {

    private String cu;

    private String recepient;

    private String subject;

    private UUID originDocflowId;

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
}
