package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.List;
import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class BusinessRegistrationDescription extends DocflowDescription {

    private String recipient;

    private String senderInn;

    private List<String> svdRegCodes;

    private RegistrationInfo registrationInfo;

    private UUID originDraftId;

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getSenderInn() {
        return senderInn;
    }

    public void setSenderInn(String senderInn) {
        this.senderInn = senderInn;
    }

    public List<String> getSvdRegCodes() {
        return svdRegCodes;
    }

    public void setSvdRegCodes(List<String> svdRegCodes) {
        this.svdRegCodes = svdRegCodes;
    }

    public RegistrationInfo getRegistrationInfo() {
        return registrationInfo;
    }

    public void setRegistrationInfo(RegistrationInfo registrationInfo) {
        this.registrationInfo = registrationInfo;
    }

    public UUID getOriginDraftId() {
        return originDraftId;
    }

    public void setOriginDraftId(UUID originDraftId) {
        this.originDraftId = originDraftId;
    }
}
