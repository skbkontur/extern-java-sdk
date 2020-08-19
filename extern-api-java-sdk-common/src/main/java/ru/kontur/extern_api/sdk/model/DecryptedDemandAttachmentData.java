package ru.kontur.extern_api.sdk.model;

import java.util.UUID;

/**
 * Класс содержит идентификаторы приложения и его расшифрованного контента для проверки требования
 */
public class DecryptedDemandAttachmentData {

    private UUID Id = null;

    private UUID ContentId = null;


    /**
     * Возвращает идентификатор приложения
     *
     * @return Идентификатор приложения
     */
    public UUID getId() {
        return Id;
    }

    /**
     * Устанавливает идентификатор приложения
     *
     * @param id идентификатор приложения
     */
    public void setId(UUID id) {
        Id = id;
    }

    /**
     * Возвращает идентификатор контента приложения
     *
     * @return Идентификатор контента приложения
     */
    public UUID getContentId() {
        return ContentId;
    }

    /**
     * Устанавливает идентификатор контента приложения
     *
     * @param contentId идентификатор контента приложения
     */
    public void setContentId(UUID contentId) {
        ContentId = contentId;
    }
}
