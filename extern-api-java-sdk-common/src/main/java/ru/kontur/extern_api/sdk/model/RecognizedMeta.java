package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecognizedMeta {
    private String demandNumber;
    private String demandKnd;
    private Date demandDate = null;
    private Date receiptDeadlineDate = null;
    private Date replyDeadlineDate = null;
    private List<String> demandInnList = new ArrayList<>();

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public String getDemandKnd() {
        return demandKnd;
    }

    public void setDemandKnd(String demandKnd) {
        this.demandKnd = demandKnd;
    }

    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date demandDate) {
        this.demandDate = demandDate;
    }

    public Date getReceiptDeadlineDate() {
        return receiptDeadlineDate;
    }

    public void setReceiptDeadlineDate(Date receiptDeadlineDate) {
        this.receiptDeadlineDate = receiptDeadlineDate;
    }

    public Date getReplyDeadlineDate() {
        return replyDeadlineDate;
    }

    public void setReplyDeadlineDate(Date replyDeadlineDate) {
        this.replyDeadlineDate = replyDeadlineDate;
    }

    public List<String> getDemandInnList() {
        return demandInnList;
    }

    public void setDemandInnList(List<String> demandInnList) {
        this.demandInnList = demandInnList;
    }
}