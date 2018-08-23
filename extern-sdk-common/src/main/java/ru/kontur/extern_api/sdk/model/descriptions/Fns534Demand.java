package ru.kontur.extern_api.sdk.model.descriptions;

import java.util.List;
import ru.kontur.extern_api.sdk.model.DocflowDescription;

public class Fns534Demand extends DocflowDescription {

    private String cu;

    private String transitCu;

    private int attachmentsCount;

    private List<FormVersion> formVersions;

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

    public int getAttachmentsCount() {
        return attachmentsCount;
    }

    public void setAttachmentsCount(int attachmentsCount) {
        this.attachmentsCount = attachmentsCount;
    }

    public List<FormVersion> getFormVersions() {
        return formVersions;
    }

    public void setFormVersions(
            List<FormVersion> formVersions) {
        this.formVersions = formVersions;
    }
}
