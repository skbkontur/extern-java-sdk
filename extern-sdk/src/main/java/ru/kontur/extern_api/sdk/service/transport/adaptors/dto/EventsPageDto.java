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

package ru.kontur.extern_api.sdk.service.transport.adaptors.dto;

import ru.kontur.extern_api.sdk.model.EventsPage;

import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class EventsPageDto {

    public EventsPage fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.EventsPage dto) {
        if (dto == null) return null;

        EventsPage ep = new EventsPage();

        ApiEventDto apiEventsDto = new ApiEventDto();

        ep.setApiEvents(dto.getApiEvents().stream().map(apiEventsDto::fromDto).collect(Collectors.toList()));
        ep.setFirstEventId(dto.getFirstEventId());
        ep.setLastEventId(dto.getLastEventId());
        ep.setRequestedCount(dto.getRequestedCount());
        ep.setReturnedCount(dto.getReturnedCount());

        return ep;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.EventsPage toDto(EventsPage ep) {
        if (ep == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.EventsPage dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.EventsPage();

        ApiEventDto apiEventsDto = new ApiEventDto();

        dto.setApiEvents(ep.getApiEvents().stream().map(apiEventsDto::toDto).collect(Collectors.toList()));
        dto.setFirstEventId(ep.getFirstEventId());
        dto.setLastEventId(ep.getLastEventId());
        dto.setRequestedCount(ep.getRequestedCount());
        dto.setReturnedCount(ep.getReturnedCount());

        return dto;
    }
}
