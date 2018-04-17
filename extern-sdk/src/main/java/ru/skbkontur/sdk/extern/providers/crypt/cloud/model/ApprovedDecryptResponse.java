/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.cloud.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;


/**
 * @author alexs
 */
public class ApprovedDecryptResponse {

    @SerializedName("Data")
    private List<Entry> data;

    public List<Entry> getData() {
        return data;
    }

    public void setData(List<Entry> data) {
        this.data = data;
    }
}
