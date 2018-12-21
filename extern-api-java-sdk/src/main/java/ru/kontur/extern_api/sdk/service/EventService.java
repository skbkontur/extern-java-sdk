/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;


/**
 * Группа методов предоставляет доступ к операциям для работы с событиями,
 * связанными с докуметооборотами (ДО):
 * <p>- получить список событий {@link EventService#getEventsAsync} | {@link EventService#getEvents};</p>
 */
public interface EventService {

    /**
     * <p>GET /v1/events</p>
     * Асинхронный метод возвращает список событий
     *
     * @param fromId идентификатор последнего обработанного события.
     *         Для первого обращения к ленте событий необходимо передать значение "0_0"
     * @param take максимальное количество возращаемых событий.
     * @return страница событий
     * @see EventsPage#getLastEventId() получение следующих fromId
     */
    CompletableFuture<QueryContext<EventsPage>> getEventsAsync(String fromId, int take);

    /**
     * <p>GET /v1/events</p>
     * Синхронный метод возвращает список событий
     *
     * @param cxt контекст. Должен содержать следующие данные:
     *         <p>-  идентификатор последнего обработанного события. см. {@link QueryContext#setFromId};</p>
     *         <p>-  максимальное количество возращаемых событий. см. {@link QueryContext#setSize(int)}.</p>
     * @return страница событий
     * @deprecated use {@link EventService#getEventsAsync(String, int)} instead
     */
    @Deprecated
    QueryContext<EventsPage> getEvents(QueryContext<?> cxt);
}
