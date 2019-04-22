package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RecognizedMeta {
    private String demandNumber;
    private Date demandDate = null;
    private List<String> demandInnList = new ArrayList<>();

    public String getDemandNumber() {
        return demandNumber;
    }

    public void setDemandNumber(String demandNumber) {
        this.demandNumber = demandNumber;
    }

    public Date getDemandDate() {
        return demandDate;
    }

    public void setDemandDate(Date demandDate) {
        this.demandDate = demandDate;
    }

    public List<String> getDemandInnList() {
        return demandInnList;
    }

    public void setDemandInnList(List<String> demandInnList) {
        this.demandInnList = demandInnList;
    }
}