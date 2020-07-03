package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Класс предназначен для отправки информации на сервер для проверки требования
 */
public class CheckDemandRequestData {

    private UUID MainDocumentContentId = null;

    private List<DemandAttachmentsContentData> DemandAttachmentsContentData = new ArrayList<>();


    /**
     * Возвращает значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     *
     * @return значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     */
    public UUID getMainDocumentContentId() {
        return MainDocumentContentId;
    }

    /**
     * Устанавливает значение идентификатора расшифрованного контента главного документа (urn:document:fns534-demand)
     */
    public void setMainDocumentContentId(UUID mainDocumentContentId) {
        MainDocumentContentId = mainDocumentContentId;
    }

    /**
     * Возвращает список объектов класса DemandAttachmentsContentData
     *
     * @return список объектов класса DemandAttachmentsContentData
     * @see DemandAttachmentsContentData
     */
    public List<DemandAttachmentsContentData> getDemandAttachmentsContentData() {
        return DemandAttachmentsContentData;
    }

    /**
     * Устанавливает список объектов класса DemandAttachmentsContentData
     *
     * @see DemandAttachmentsContentData
     */
    public void setDemandAttachmentsContentData(List<DemandAttachmentsContentData> demandAttachmentsContentData) {
        DemandAttachmentsContentData = demandAttachmentsContentData;
    }
}