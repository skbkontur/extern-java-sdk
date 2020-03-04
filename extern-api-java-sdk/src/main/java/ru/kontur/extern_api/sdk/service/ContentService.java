package ru.kontur.extern_api.sdk.service;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ContentService {

    /**
     * Метод скачивает контент полностью.
     *
     * @param contentId Идентификатор контента
     * @return контент
     */
    CompletableFuture<byte[]> downloadAllContent(UUID contentId
    );

    /**
     * Метод скачивает контент по частям с указанием диапзона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param to        Номер байта, по который скачивать контент
     * @return контент
     */
    CompletableFuture<byte[]> downloadPartContent(UUID contentId, int from, int to);

    /**
     * Метод скачивает контент по частям с указанием начального байта и длины кусочка
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param length    Количество байт, которые нужно скачать
     * @return контент
     */
    CompletableFuture<byte[]> downloadPartContentByLength(UUID contentId, int from, int length);

}
