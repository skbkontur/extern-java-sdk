package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

public class DownloadedContent {

    private String contentType;
    private byte[] content;

    /**
     * Возвращает contentType
     *
     * @return contentType
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Устанавливает contentType
     *
     * @param contentType String
     * @see UUID
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Возвращает content
     *
     * @return content
     */
    public String getContent() {
        return contentType;
    }

    /**
     * Устанавливает content
     *
     * @param content byte[]
     * @see UUID
     */
    public void setContent(byte[] content) {
        this.content = content;
    }
}
