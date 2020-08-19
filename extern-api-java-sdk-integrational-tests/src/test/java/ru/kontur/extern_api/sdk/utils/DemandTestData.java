package ru.kontur.extern_api.sdk.utils;

import java.util.UUID;
import ru.kontur.extern_api.sdk.model.TestData;

public class DemandTestData extends TestData {

    private UUID demandId;
    private UUID demandAttachmentId;
    private byte[] demandAttachmentDecryptedBytes;
    private UUID demandMainDocumentId;
    private byte[] demandMainDocumentDecryptedBytes;

    public UUID getDemandId() {
        return demandId;
    }

    public void setDemandId(UUID demandId) {
        this.demandId = demandId;
    }

    public UUID getDemandAttachmentId() {
        return demandAttachmentId;
    }

    void setDemandAttachmentId(UUID demandAttachmentId) {
        this.demandAttachmentId = demandAttachmentId;
    }

    DemandTestData(TestData testData, UUID demandId) {
        this.demandId = demandId;
        this.setClientInfo(testData.getClientInfo());
        this.setDocs(testData.getDocs());
    }

    public byte[] getDemandAttachmentDecryptedBytes() {
        return demandAttachmentDecryptedBytes;
    }

    public void setDemandAttachmentDecryptedBytes(byte[] demandAttachmentDecryptedBytes) {
        this.demandAttachmentDecryptedBytes = demandAttachmentDecryptedBytes;
    }

    public byte[] getDemandMainDocumentDecryptedBytes() {
        return demandMainDocumentDecryptedBytes;
    }

    public void setDemandMainDocumentDecryptedBytes(byte[] demandMainDocumentDecryptedBytes) {
        this.demandMainDocumentDecryptedBytes = demandMainDocumentDecryptedBytes;
    }

    public UUID getDemandMainDocumentId() {
        return demandMainDocumentId;
    }

    public void setDemandMainDocumentId(UUID demandMainDocumentId) {
        this.demandMainDocumentId = demandMainDocumentId;
    }
}
