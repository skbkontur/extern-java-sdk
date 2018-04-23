/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author alexs
 */
public class EventsPageDtoTest {
    
    @Test
    public void testFromDto() {
        System.out.println("fromDtoEventsPage");
        
        ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage dto = buildEventsPageDto();
        
        EventsPageDto instance = new EventsPageDto();
        
        ru.skbkontur.sdk.extern.model.EventsPage expEventsPage = buildEventsPageSDK();
        
        ru.skbkontur.sdk.extern.model.EventsPage resEventsPage = instance.fromDto(dto);
        
        assertTrue(expEventsPage.equals(resEventsPage));
    }

    @Test
    public void testToDto() {
        System.out.println("toDtoEventsPage");
        
        ru.skbkontur.sdk.extern.model.EventsPage sdk = buildEventsPageSDK();
        
        EventsPageDto instance = new EventsPageDto();
        
        ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage expEventsPage = buildEventsPageDto();
        
        ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage resEventsPage = instance.toDto(sdk);
        
        assertTrue(expEventsPage.equals(resEventsPage));
    }
    

    private static ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage buildEventsPageDto() {
        ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage eventsPage = new ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage();
        eventsPage.setFirstEventId("firstEventId");
        eventsPage.setLastEventId("lastEventId");
        eventsPage.setRequestedCount(Integer.MAX_VALUE);
        eventsPage.setReturnedCount(Integer.MIN_VALUE);
        eventsPage.addApiEventsItem(ApiEventDtoTest.buildApiEventDto());
        return eventsPage;
    }

    public static ru.skbkontur.sdk.extern.model.EventsPage buildEventsPageSDK() {
        ru.skbkontur.sdk.extern.model.EventsPage eventsPage = new ru.skbkontur.sdk.extern.model.EventsPage();
        eventsPage.setFirstEventId("firstEventId");
        eventsPage.setLastEventId("lastEventId");
        eventsPage.setRequestedCount(Integer.MAX_VALUE);
        eventsPage.setReturnedCount(Integer.MIN_VALUE);
        eventsPage.addApiEventsItem(ApiEventDtoTest.buildApiEventSDK());
        return eventsPage;
    }
}
