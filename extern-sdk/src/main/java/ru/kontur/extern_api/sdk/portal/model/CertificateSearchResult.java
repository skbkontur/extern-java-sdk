package ru.kontur.extern_api.sdk.portal.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CertificateSearchResult {

    @SerializedName("totalHits")
    private int totalHits;

    @SerializedName("results")
    private List<CertificateFields> results;

    public int getTotalHits() {
        return totalHits;
    }

    public List<CertificateFields> getResults() {
        return results;
    }
}
