/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.it;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.model.ApiEvent;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.it.utils.SystemProperty;
import ru.kontur.extern_api.sdk.it.utils.TestSuite;


class EventTest {

    private static ExternEngine engine;

    @BeforeAll
    static void setUpClass() {
        engine = TestSuite.Load().engine;
    }

    @BeforeEach
    void setUp() {
        SystemProperty.push("httpclient.debug");
    }

    @AfterEach
    void tearDown() {
        SystemProperty.pop("httpclient.debug");
    }


    @Test
    void testGetEvents() throws Exception {
        EventsPage eventsPage = engine
                .getEventService()
                .getEventsAsync("0_0", 20)
                .get()
                .getOrThrow();

        for (ApiEvent apiEvent : eventsPage.getApiEvents()) {
            Assertions.assertNotNull(apiEvent.getDocflowType());
            Assertions.assertNotNull(apiEvent.getNewState());
        }

    }
}
