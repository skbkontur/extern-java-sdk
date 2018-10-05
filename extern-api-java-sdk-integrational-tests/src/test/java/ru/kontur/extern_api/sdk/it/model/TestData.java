/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it.model;


import java.util.Optional;
import ru.kontur.extern_api.sdk.it.model.ClientInfo.Recipient;

public class TestData {

    private ClientInfo clientInfo;
    private String[] docs;

    public ClientInfo getClientInfo() {
        return clientInfo;
    }

    public void setClientInfo(ClientInfo clientInfo) {
        this.clientInfo = clientInfo;
    }

    public String[] getDocs() {
        return docs;
    }

    public void setDocs(String[] docs) {
        this.docs = docs;
    }

    @Override
    public String toString() {
        try {
            Recipient recipient = clientInfo.getRecipient();
            String sRecipient = Optional.ofNullable(recipient.getIfnsCode())
                    .map(ifns -> "ifns-code " + ifns)
                    .orElseGet(() -> "togs-code " + recipient.getTogsCode());

            return "TestData{"
                    + " sender=" + clientInfo.getSender().getFio()
                    + " recipient=" + sRecipient
                    + " }";

        } catch (Exception ignored) {
            return super.toString();
        }


    }
}
