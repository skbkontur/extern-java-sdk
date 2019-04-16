package ru.kontur.extern_api.sdk.utils;

import ru.kontur.extern_api.sdk.model.ClientInfo;

class DemandRequestPayer {
    private String inn;
    private String name;
    private DemandRequestOrganisationInfo organization;

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DemandRequestOrganisationInfo getOrganisationInfo() {
        return organization;
    }

    public void setOrganisationInfo(DemandRequestOrganisationInfo organisationInfo) {
        this.organization = organisationInfo;
    }

    static DemandRequestPayer fromClientInfo(ClientInfo clientInfo, String orgName){
        DemandRequestPayer demandRequestPayer = new DemandRequestPayer();
        demandRequestPayer.setInn(clientInfo.getOrganization().getInn());
        demandRequestPayer.setOrganisationInfo(new DemandRequestOrganisationInfo(clientInfo.getOrganization().getKpp()));
        demandRequestPayer.setName(orgName);
        return demandRequestPayer;
    }
}
