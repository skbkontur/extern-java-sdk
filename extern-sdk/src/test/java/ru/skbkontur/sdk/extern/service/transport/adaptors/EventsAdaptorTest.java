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
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import ru.skbkontur.sdk.extern.model.ApiEvent;
import ru.skbkontur.sdk.extern.model.EventsPage;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.EventsPageDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.EventsPageDtoTest;
import ru.skbkontur.sdk.extern.service.transport.swagger.api.EventsApi;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;


/**
 *
 * @author AlexS
 */
public class EventsAdaptorTest {
    
    @Test
    public void testGetEvents() throws ApiException {
        System.out.println("getEvents");
        EventsPage expResult = EventsPageDtoTest.buildEventsPageSDK();
        ru.skbkontur.sdk.extern.service.transport.swagger.model.EventsPage eventsPage = new EventsPageDto().toDto(expResult);
        EventsApi eventsApi = mock(EventsApi.class);
        EventsAdaptor instance = new EventsAdaptor(eventsApi);
        when(eventsApi.eventsGetEvents("0", Integer.MAX_VALUE)).thenReturn(eventsPage);
        QueryContext<EventsPage> cxt = 
            new QueryContext<EventsPage>()
                .setFromId("0")
                .setSize(Integer.MAX_VALUE);
        EventsPage result = instance.getEvents(cxt).get();
        verify(eventsApi).eventsGetEvents("0", Integer.MAX_VALUE);
        assertEquals(expResult.getFirstEventId(), result.getFirstEventId());
        assertEquals(expResult.getLastEventId(), result.getLastEventId());
        assertEquals(expResult.getRequestedCount(), result.getRequestedCount());
        assertEquals(expResult.getReturnedCount(), result.getReturnedCount());
        assertEquals(expResult.getApiEvents().size(), result.getApiEvents().size());
        for (int i=0; i<expResult.getApiEvents().size(); i++) {
            ApiEvent expApiEvent = expResult.getApiEvents().get(i);
            ApiEvent resApiEvent = result.getApiEvents().get(i);
            assertTrue(expApiEvent.equals(resApiEvent));
        }
    }
}
