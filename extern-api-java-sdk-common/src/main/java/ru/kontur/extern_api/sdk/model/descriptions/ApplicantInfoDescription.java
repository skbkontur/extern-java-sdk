package ru.kontur.extern_api.sdk.model.descriptions;

public class ApplicantInfoDescription {

    private Fio fio;

    private String inn;

    public Fio getFio() {
        return fio;
    }

    public void setFio(Fio fio) {
        this.fio = fio;
    }

    public String getInn() {
        return inn;
    }

    public void setInn(String inn) {
        this.inn = inn;
    }
}
