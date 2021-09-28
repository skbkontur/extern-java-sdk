package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

/**
 * <p>
 * Класс содержит описание контента документа черновика.
 * </p>
 */
public class DraftDocumentContent {

    /**
     * Идентификатор контента
     */
    private UUID contentId;

    /**
     * Флаг зашифрованности контента
     */
    private boolean encrypted;

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
}
