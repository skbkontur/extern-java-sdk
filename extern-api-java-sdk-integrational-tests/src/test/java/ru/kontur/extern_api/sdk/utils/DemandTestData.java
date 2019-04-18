package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.TestData;

public class DemandTestData extends TestData {

    public UUID getDemandId() {
        return demandId;
    }

    public void setDemandId(UUID demandId) {
        this.demandId = demandId;
    }

    private UUID demandId;
    private UUID demandAttachmentId;

    public UUID getDemandAttachmentId() {
        return demandAttachmentId;
    }

    public void setDemandAttachmentId(UUID demandAttachmentId) {
        this.demandAttachmentId = demandAttachmentId;
    }

    public DemandTestData(TestData testData, UUID demandId) {
        this.demandId = demandId;
        this.setClientInfo(testData.getClientInfo());
        this.setDocs(testData.getDocs());
    }

}
