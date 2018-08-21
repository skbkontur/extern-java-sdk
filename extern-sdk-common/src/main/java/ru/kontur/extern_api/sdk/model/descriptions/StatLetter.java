package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class StatLetter extends DocflowDescription {

    private String cu;

    private String recipient;

    private String subject;

    private String okpo;

    private UUID originDocflowId;

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getOkpo() {
        return okpo;
    }

    public void setOkpo(String okpo) {
        this.okpo = okpo;
    }

    public UUID getOriginDocflowId() {
        return originDocflowId;
    }

    public void setOriginDocflowId(UUID originDocflowId) {
        this.originDocflowId = originDocflowId;
    }
}
