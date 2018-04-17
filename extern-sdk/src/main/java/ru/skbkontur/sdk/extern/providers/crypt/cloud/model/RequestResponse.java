/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author AlexS
 */
public class RequestResponse {

    @SerializedName("ResultId")
    private String resultId;
    @SerializedName("FileIds")
    private List<File> fileIds;
    @SerializedName("Links")
    private List<Link> links;

    public String getResultId() {
        return resultId;
    }

    public void setResultId(String resultId) {
        this.resultId = resultId;
    }

    public List<File> getFileIds() {
        return fileIds;
    }

    public void setFileIds(List<File> fileIds) {
        this.fileIds = fileIds;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
