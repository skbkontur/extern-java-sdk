/*
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.model;

/**
 * @author AlexS
 */
public class ClientInfo {

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

        private String fssCode;

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

        public String getFssCode() { return fssCode; }

        public void setFssCode(String fssCode) { this.fssCode = fssCode; }
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

}
