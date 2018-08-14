package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

public class SendReplyDocumentRequestData {

    @SerializedName("senderIp")
    private String senderIp = null;

    public SendReplyDocumentRequestData senderIp(String senderIp) {
        this.senderIp = senderIp;
        return this;
    }

    public String getSenderIp() {
        return senderIp;
    }

    public void setSenderIp(String senderIp) {
        this.senderIp = senderIp;
    }
}
