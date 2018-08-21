package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Demand extends DocflowDescription {

    private String cu;

    private String transitCu;

    private UUID portalUserId;

    public String getCu() {
        return cu;
    }

    public void setCu(String cu) {
        this.cu = cu;
    }

    public String getTransitCu() {
        return transitCu;
    }

    public void setTransitCu(String transitCu) {
        this.transitCu = transitCu;
    }

    public UUID getPortalUserId() {
        return portalUserId;
    }

    public void setPortalUserId(UUID portalUserId) {
        this.portalUserId = portalUserId;
    }
}
