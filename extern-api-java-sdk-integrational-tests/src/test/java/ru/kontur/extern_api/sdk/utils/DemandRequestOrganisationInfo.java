package ru.kontur.extern_api.sdk.utils;

public class DemandRequestOrganisationInfo {

    public DemandRequestOrganisationInfo(String kpp) {
        this.kpp = kpp;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    private String kpp;
}
