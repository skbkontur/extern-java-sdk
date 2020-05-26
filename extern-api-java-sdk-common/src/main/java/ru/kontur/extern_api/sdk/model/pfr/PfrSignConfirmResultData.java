package ru.kontur.extern_api.sdk.model.pfr;

import java.util.List;
import ru.kontur.extern_api.sdk.model.Link;

public class PfrSignConfirmResultData {

    private List<Link> links;

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> signedDocuments){
        this.links = signedDocuments;
    }
}
