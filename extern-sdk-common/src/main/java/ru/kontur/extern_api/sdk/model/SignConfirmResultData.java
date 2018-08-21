package ru.kontur.extern_api.sdk.model;

import java.util.List;

public class SignConfirmResultData {

    private List<Link> signedDocuments;

    public List<Link> getSignedDocuments() {
        return signedDocuments;
    }

    public void setSignedDocuments(List<Link> signedDocuments){
        this.signedDocuments = signedDocuments;
    }

}
