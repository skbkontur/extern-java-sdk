/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package ru.kontur.extern_api.sdk.events;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

import ru.kontur.extern_api.sdk.common.StandardObjectsValidator;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.model.ApiEvent;
import ru.kontur.extern_api.sdk.model.DocflowType;
import ru.kontur.extern_api.sdk.model.EventsPage;

/**
 * @author Mikhail Pavlenko
 */

public class EventsValidator {

    public static void validateEventsPage(EventsPage eventsPage, boolean withApiEvents) {
        assertNotNull("EventsPage must not be null!", eventsPage);
        assertEquals("FirstEventId is wrong!", "string", eventsPage.getFirstEventId());
        assertEquals("LastEventId is wrong!", "string", eventsPage.getLastEventId());
        assertEquals("RequestedCount is wrong!", 0, eventsPage.getRequestedCount().intValue());
        assertEquals("ReturnedCount is wrong!", 0, eventsPage.getReturnedCount().intValue());

        if (withApiEvents) {
            StandardObjectsValidator
                .validateNotEmptyList(eventsPage.getApiEvents(), "ApiEvents");
            validateApiEvents(eventsPage.getApiEvents().get(0));
        } else {
            StandardObjectsValidator
                .validateEmptyList(eventsPage.getApiEvents(), "ApiEvents");
        }
    }

    public static void validateApiEvents(ApiEvent apiEvent) {
        assertNotNull("ApiEvent must not be null!", apiEvent);

        assertEquals("Inn is wrong!", "string", apiEvent.getInn());
        assertEquals("Kpp is wrong!", "string", apiEvent.getKpp());
        assertEquals("Type is wrong!", DocflowType.FNS534_REPORT, apiEvent.getDocflowType());
        assertEquals("NewState is wrong!", "urn:nss:nid", apiEvent.getNewState());
        assertEquals("EventDateTime is wrong!", StandardValues.standardDate(),
            apiEvent.getEventDateTime());
        assertEquals("Id is wrong!", "string", apiEvent.getId());

        StandardObjectsValidator.validateLink(apiEvent.getDocflowLink());


    }
}
