package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

public class RecognizeMetaRequest {

    private UUID contentId;

    public RecognizeMetaRequest(UUID contentId) {
        this.contentId = contentId;
    }

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }
}