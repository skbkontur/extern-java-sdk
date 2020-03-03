package ru.kontur.extern_api.sdk.service;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ContentService {

    /**
     * Метод инициализирует скачивание контента полностью.
     *
     * @param contentId Идентификатор контента
     * @return контент
     */
    CompletableFuture<byte[]> downloadAllContent(UUID contentId
    );

    /**
     * Метод инициализирует скачивание контента по частям с указанием диапозона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param to        Номер байта, по который скачивать контент
     * @return контент
     */
    CompletableFuture<byte[]> downloadPartContent(UUID contentId, int from, int to);

    /**
     * Метод инициализирует скачивание контента по частям с указанием диапозона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param length    Количество байт, уоторые нужно скачать
     * @return контент
     */
    CompletableFuture<byte[]> downloadPartContentByLength(UUID contentId, int from, int length);

}
