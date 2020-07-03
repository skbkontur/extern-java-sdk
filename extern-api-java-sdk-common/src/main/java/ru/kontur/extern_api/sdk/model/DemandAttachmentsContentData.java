package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

/**
 * Класс содержит идентификаторы приложения и его расшифрованного контента для проверки требования
 */
public class DemandAttachmentsContentData {

    private UUID AttachmentId = null;

    private UUID AttachmentContentId = null;


    /**
     * Возвращает идентификатор приложения
     *
     * @return Идентификатор приложения
     */
    public UUID getAttachmentId() {
        return AttachmentId;
    }

    /**
     * Устанавливает идентификатор приложения
     *
     * @param attachmentId идентификатор приложения
     */
    public void setAttachmentId(UUID attachmentId) {
        AttachmentId = attachmentId;
    }

    /**
     * Возвращает идентификатор контента приложения
     *
     * @return Идентификатор контента приложения
     */
    public UUID getAttachmentContentId() {
        return AttachmentContentId;
    }

    /**
     * Устанавливает идентификатор контента приложения
     *
     * @param attachmentContentId идентификатор контента приложения
     */
    public void setAttachmentContentId(UUID attachmentContentId) {
        AttachmentContentId = attachmentContentId;
    }
}
