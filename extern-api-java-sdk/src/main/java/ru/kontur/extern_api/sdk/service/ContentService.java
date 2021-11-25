package ru.kontur.extern_api.sdk.service;


import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ContentService {

    public CompletableFuture<Integer> getTotalSizeInBytes(UUID contentId);

    /**
     * Метод скачивает контент полностью.
     *
     * @param contentId Идентификатор контента
     * @return контент
     */
    CompletableFuture<byte[]> getContent(UUID contentId);

    /**
     * Метод загружает контент полностью.
     *
     * @param content контент
     * @return Идентификатор контента
     */
    CompletableFuture<UUID> uploadContent(byte[] content);

    /**
     * Метод скачивает контент по частям с указанием диапазона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param to        Номер байта, по который скачивать контент
     * @return контент
     */
    CompletableFuture<byte[]> getPartialContent(UUID contentId, int from, int to);

    /**
     * Метод скачивает контент по частям с указанием начального байта и длины кусочка
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param length    Количество байт, которые нужно скачать
     * @return контент
     */
    CompletableFuture<byte[]> getPartialContentByLength(UUID contentId, int from, int length);

}
