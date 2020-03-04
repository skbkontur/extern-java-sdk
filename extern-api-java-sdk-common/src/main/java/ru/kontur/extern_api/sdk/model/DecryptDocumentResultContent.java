package ru.kontur.extern_api.sdk.model;

import com.google.gson.annotations.SerializedName;

import java.util.UUID;

public class DecryptDocumentResultContent {

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }

    @SerializedName("content-id")
    private UUID contentId;
}
