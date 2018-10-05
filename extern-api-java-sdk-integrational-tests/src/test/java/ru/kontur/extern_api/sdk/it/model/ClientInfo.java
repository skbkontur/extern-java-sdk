/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it.model;

/**
 * @author AlexS
 */
public class ClientInfo {

    public static class Sender {

        private String inn;
        private String kpp;
        private String certificate;
        private String ipAddress;
        private Fio fio;

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

        public String getCertificate() {
            return certificate;
        }

        public void setCertificate(String certificate) {
            this.certificate = certificate;
        }

        public String getIpAddress() {
            return this.ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public Fio getFio() {
            return fio;
        }

        public void setFio(Fio fio) {
            this.fio = fio;
        }

        public static class Fio {

            private String surname;
            private String name;
            private String patronymic;

            public String getSurname() {
                return surname;
            }

            public void setSurname(String surname) {
                this.surname = surname;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getPatronymic() {
                return patronymic;
            }

            public void setPatronymic(String patronymic) {
                this.patronymic = patronymic;
            }

            @Override
            public String toString() {
                return "fio " + surname + " " + name + " " + patronymic;
            }
        }
    }

    public static class Recipient {

        private String ifnsCode;

        private String togsCode;

        public String getIfnsCode() {
            return ifnsCode;
        }

        public void setIfnsCode(String ifnsCode) {
            this.ifnsCode = ifnsCode;
        }

        public String getTogsCode() {
            return togsCode;
        }

        public void setTogsCode(String togsCode) {
            this.togsCode = togsCode;
        }
    }

    public static class Organization {

        private String inn;
        private String kpp;

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
    }

    private Sender sender;
    private Recipient recipient;
    private Organization organization;
    private String ipAddress;

    public Sender getSender() {
        return sender;
    }

    public void setSender(Sender sender) {
        this.sender = sender;
    }

    public Recipient getRecipient() {
        return recipient;
    }

    public void setRecipient(Recipient recipient) {
        this.recipient = recipient;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

}
