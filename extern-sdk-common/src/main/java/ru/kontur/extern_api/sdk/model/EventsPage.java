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

    public String getFirstEventId() {
        return firstEventId;
    }

    public void setFirstEventId(String firstEventId) {
        this.firstEventId = firstEventId;
    }

    public String getLastEventId() {
        return lastEventId;
    }

    public void setLastEventId(String lastEventId) {
        this.lastEventId = lastEventId;
    }

    public Integer getRequestedCount() {
        return requestedCount;
    }

    public void setRequestedCount(Integer requestedCount) {
        this.requestedCount = requestedCount;
    }

    public Integer getReturnedCount() {
        return returnedCount;
    }

    public void setReturnedCount(Integer returnedCount) {
        this.returnedCount = returnedCount;
    }

    public List<ApiEvent> getApiEvents() {
        return apiEvents;
    }

    public void setApiEvents(List<ApiEvent> apiEvents) {
        this.apiEvents = apiEvents;
    }

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
