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

package ru.kontur.extern_api.sdk.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * <p>
 * Класс содержит список событий документооборотов
 * </p>
 *
 * @see ApiEvent
 */
public class EventsPage {

    private String firstEventId;
    private String lastEventId;
    private Integer requestedCount;
    private Integer returnedCount;

    private List<ApiEvent> apiEvents = new ArrayList<>();

    /**
     * <p>Возвращает идентификатор первого события в списке.</p>
     *
     * @return идентификатор первого события в списке.
     */
    public String getFirstEventId() {
        return firstEventId;
    }

    /**
     * <p>Возвращает идентификатор последнего события в списке.</p>
     *
     * @return идентификатор последнего события в списке
     */
    public String getLastEventId() {
        return lastEventId;
    }

    /**
     * <p>Возвращает запрошенное количество событий в запросе</p>
     *
     * @return запрошенное количество событий в запросе
     */
    public Integer getRequestedCount() {
        return requestedCount;
    }

    /**
     * <p>Возвращает количество событий, которое вернул запрос.</p>
     *
     * @return количество событий, которое вернул запрос
     */
    public Integer getReturnedCount() {
        return returnedCount;
    }

    /**
     * <p>Возвращает список событий</p>
     *
     * @return список событий
     * @see ApiEvent
     */
    public List<ApiEvent> getApiEvents() {
        return apiEvents;
    }

}
