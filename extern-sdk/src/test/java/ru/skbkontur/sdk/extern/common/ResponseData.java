package ru.skbkontur.sdk.extern.common;

public enum ResponseData {
    INSTANCE;

    private int responseCode;
    private String responseMessage;
    private String requestMessage;


    public void setResponseCode(int code) {
        responseCode = code;
    }

    public int getResponseCode() {
        return responseCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public void setResponseMessage(String responseMessage) {
        this.responseMessage = responseMessage;
    }

    public String getRequestMessage() {
        return requestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        this.requestMessage = requestMessage;
    }
}
