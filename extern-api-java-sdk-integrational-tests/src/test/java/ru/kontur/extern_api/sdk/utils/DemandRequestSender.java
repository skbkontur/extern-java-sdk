package ru.kontur.extern_api.sdk.utils;

import ru.kontur.extern_api.sdk.model.ClientInfo;
import ru.kontur.extern_api.sdk.model.ClientInfo.Sender;
import ru.kontur.extern_api.sdk.model.Sender.Certificate;

class DemandRequestSender {

    private String inn;
    private String kpp;
    private String name;

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }

    public String getKpp() {
        return kpp;
    }

    public void setKpp(String kpp) {
        this.kpp = kpp;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Certificate getCertificate() {
        return certificate;
    }

    public void setCertificate(Certificate certificate) {
        this.certificate = certificate;
    }

    private Certificate certificate;

    static DemandRequestSender fromClientInfo(ClientInfo clientInfo, String ogrName){
        DemandRequestSender demandRequestSender =  new DemandRequestSender();
        Sender sender = clientInfo.getSender();
        demandRequestSender.setInn(sender.getInn());
        demandRequestSender.setKpp(sender.getKpp());
        demandRequestSender.setName(ogrName);
        demandRequestSender.setCertificate(new Certificate(sender.getCertificate()));
        return demandRequestSender;
    }
}
