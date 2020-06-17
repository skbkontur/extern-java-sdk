package ru.kontur.extern_api.sdk.model.pfr.Zped;

import ru.kontur.extern_api.sdk.model.BuildDocumentContract;

public class ZpedRequestContract implements BuildDocumentContract {
    private Boolean ForUl = null;
    private InsurerUlDto InsurerUl = null;
    private InsurerIpDto InsurerIp = null;
    private RepresentativeDto Representative = null;

    public Boolean getForUl() {
        return ForUl;
    }

    public void setForUl(Boolean forUl) {
        ForUl = forUl;
    }

    public InsurerUlDto getInsurerUl() {
        return InsurerUl;
    }

    public void setInsurerUl(InsurerUlDto insurerUl) {
        InsurerUl = insurerUl;
    }

    public InsurerIpDto getInsurerIp() {
        return InsurerIp;
    }

    public void setInsurerIp(InsurerIpDto insurerIp) {
        InsurerIp = insurerIp;
    }

    public RepresentativeDto getRepresentative() {
        return Representative;
    }

    public void setRepresentative(RepresentativeDto representative) {
        Representative = representative;
    }
}