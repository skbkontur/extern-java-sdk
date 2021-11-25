package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

//TODO: Javadoc
public class DocflowDocumentContents {

    private UUID contentId;
    private boolean encrypted;
    private boolean compressed;

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }

    public boolean isEncrypted() {
        return encrypted;
    }

    public void setEncrypted(boolean encrypted) {
        this.encrypted = encrypted;
    }

    public boolean isCompressed() {
        return compressed;
    }

    public void setCompressed(boolean compressed) {
        this.compressed = compressed;
    }

}
