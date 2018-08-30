package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534CuLetter extends DocflowDescription {

    private String recipient;

    private String subject;

    private UUID originDocflowId;

    private UUID originDocumentId;

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
}
