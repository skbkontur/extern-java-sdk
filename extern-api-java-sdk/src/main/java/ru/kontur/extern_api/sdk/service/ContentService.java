package ru.kontur.extern_api.sdk.service;

import okhttp3.ResponseBody;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.model.DownloadedContent;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ContentService {
    /**
     * Метод инициализирует скачивание контента полностью.
     *
     * @param contentId Идентификатор контента
     * @return  контент
     */
    CompletableFuture<ResponseBody> downloadAllContent(UUID contentId
    );

    /**
     * Метод инициализирует скачивание контента по частям с указанием диапозона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param to        Номер байта, по который скачивать контент
     * @return контент
     */
    CompletableFuture<ResponseBody> downloadPathContent(UUID contentId, int from, int to);

    /**
     * Метод инициализирует скачивание контента по частям с указанием диапозона байт.
     *
     * @param contentId Идентификатор контента
     * @param from      Номер байта, с которого начать скачивание
     * @param length    Количество байт, уоторые нужно скачать
     * @return контент
     */
    CompletableFuture<ResponseBody> downloadPartContentByLength(UUID contentId, int from, int length);

}
