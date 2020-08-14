package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Класс предназначен для отправки информации на сервер для проверки требования
 */
public class CheckDemandRequestData {

    private UUID DecryptedDemandContentId = null;

    private List<DecryptedDemandAttachmentData> DecryptedAttachments = new ArrayList<>();


    /**
     * Возвращает значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     *
     * @return значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     */
    public UUID getDecryptedDemandContentId() {
        return DecryptedDemandContentId;
    }

    /**
     * Устанавливает значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     */
    public void setDecryptedDemandContentId(UUID decryptedDemandContentId) {
        DecryptedDemandContentId = decryptedDemandContentId;
    }

    /**
     * Возвращает список объектов класса DemandAttachmentsContentData
     *
     * @return список объектов класса DemandAttachmentsContentData
     * @see DecryptedDemandAttachmentData
     */
    public List<DecryptedDemandAttachmentData> getDecryptedAttachments() {
        return DecryptedAttachments;
    }

    /**
     * Устанавливает список объектов класса DemandAttachmentsContentData
     *
     * @see DecryptedDemandAttachmentData
     */
    public void setDecryptedAttachments(List<DecryptedDemandAttachmentData> decryptedAttachments) {
        DecryptedAttachments = decryptedAttachments;
    }
}