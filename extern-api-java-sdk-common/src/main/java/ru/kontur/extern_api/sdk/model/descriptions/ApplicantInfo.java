package ru.kontur.extern_api.sdk.model.descriptions;

public class ApplicantInfo {

    private Fio fio;

    private String inn;

    private String email;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
