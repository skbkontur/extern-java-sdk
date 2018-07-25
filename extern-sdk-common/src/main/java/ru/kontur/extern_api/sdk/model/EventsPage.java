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

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @author AlexS
 * <p>
 *     Класс содержит список событий документооборотов
 * </p>
 * @see ApiEvent
 */
public class EventsPage {

    @SerializedName("first-event-id")
    private String firstEventId = null;
    @SerializedName("last-event-id")
    private String lastEventId = null;
    @SerializedName("requested-count")
    private Integer requestedCount = null;
    @SerializedName("returned-count")
    private Integer returnedCount = null;
    @SerializedName("api-events")
    private List<ApiEvent> apiEvents = new ArrayList<>();

    /**
     * <p>Возвращает идентификатор первого события в списке.</p>
     * @return идентификатор первого события в списке.
     */
    public String getFirstEventId() {
        return firstEventId;
    }

    /**
     * <p>Устанавливает идентификатор первого события в списке.</p>
     * @param firstEventId идентификатор первого события в списке
     */
    public void setFirstEventId(String firstEventId) {
        this.firstEventId = firstEventId;
    }

    /**
     * <p>Возвращает идентификатор последнего события в списке.</p>
     * @return идентификатор последнего события в списке
     */
    public String getLastEventId() {
        return lastEventId;
    }

    /**
     * <p>Устанавливает идентификатор последнего события в списке.</p>
     * @param lastEventId идентификатор последнего события в списке
     */
    public void setLastEventId(String lastEventId) {
        this.lastEventId = lastEventId;
    }

    /**
     * <p>Возвращает запрошенное количество событий в запросе</p>
     * @return запрошенное количество событий в запросе
     */
    public Integer getRequestedCount() {
        return requestedCount;
    }

    /**
     * <p>Устанавливает максимаоьное количество событий, которое должен вернуть запрос.</p>
     * @param requestedCount максимаоьное количество событий, которое должен вернуть запрос
     */
    public void setRequestedCount(Integer requestedCount) {
        this.requestedCount = requestedCount;
    }

    /**
     * <p>Возвращает количество событий, которое вернул запрос.</p>
     * @return количество событий, которое вернул запрос
     */
    public Integer getReturnedCount() {
        return returnedCount;
    }

    /**
     * <p>Устанавливает количество событий, которое вернул запрос.</p>
     * @param returnedCount количество событий, которое вернул запрос
     */
    public void setReturnedCount(Integer returnedCount) {
        this.returnedCount = returnedCount;
    }

    /**
     * <p>Возвращает список событий</p>
     * @return список событий
     * @see ApiEvent
     */
    public List<ApiEvent> getApiEvents() {
        return apiEvents;
    }

    /**
     * <p>Устанавливает список событий</p>
     * @param apiEvents список событий
     * @see ApiEvent
     */
    public void setApiEvents(List<ApiEvent> apiEvents) {
        this.apiEvents = apiEvents;
    }

    /**
     * <p>Добавляет событие {@link ApiEvent} в список</p>
     * @param apiEventsItem событие
     * @return страница событий
     */
    public EventsPage addApiEventsItem(ApiEvent apiEventsItem) {
        if (this.apiEvents == null) {
            this.apiEvents = new ArrayList<>();
        }
        this.apiEvents.add(apiEventsItem);
        return this;
    }

    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        EventsPage eventsPage = (EventsPage) o;
        return Objects.equals(this.firstEventId, eventsPage.firstEventId)
            && Objects.equals(this.lastEventId, eventsPage.lastEventId)
            && Objects.equals(this.requestedCount, eventsPage.requestedCount)
            && Objects.equals(this.returnedCount, eventsPage.returnedCount)
            && Objects.equals(this.apiEvents, eventsPage.apiEvents);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstEventId, lastEventId, requestedCount, returnedCount, apiEvents);
    }

}
