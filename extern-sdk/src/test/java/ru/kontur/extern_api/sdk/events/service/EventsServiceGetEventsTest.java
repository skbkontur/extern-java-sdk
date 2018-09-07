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

package ru.kontur.extern_api.sdk.events.service;

import static javax.servlet.http.HttpServletResponse.SC_OK;
import static junit.framework.TestCase.assertNotNull;
import static ru.kontur.extern_api.sdk.events.EventsValidator.validateEventsPage;

import java.util.UUID;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.kontur.extern_api.sdk.ExternEngine;
import ru.kontur.extern_api.sdk.ExternEngineBuilder;
import ru.kontur.extern_api.sdk.common.ResponseData;
import ru.kontur.extern_api.sdk.common.StandardValues;
import ru.kontur.extern_api.sdk.common.TestServlet;
import ru.kontur.extern_api.sdk.drafts.service.AuthenticationProviderAdaptor;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

/**
 * @author Mikhail Pavlenko
 */

public class EventsServiceGetEventsTest {

    private static ExternEngine engine;
    private static Server server;

    @BeforeClass
    public static void startJetty() throws Exception {
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        context.addServlet(TestServlet.class, "/events/*");
        server = new Server(8080);
        server.setHandler(context);
        server.start();
    }

    @AfterClass
    public static void stopJetty() {
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @BeforeClass
    public static void setUpClass() {
        engine = ExternEngineBuilder.createExternEngine()
                .apiKey(UUID.randomUUID().toString()).authProvider(new AuthenticationProviderAdaptor())
                .doNotUseCryptoProvider()
                .accountId(UUID.randomUUID().toString())
                .serviceBaseUrl("http://localhost:8080/events")
                .build();
    }

    @Test
    public void testGetEvents_Empty() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{}");

        QueryContext<EventsPage> queryContext = new QueryContext<>();
        queryContext.setFromId(UUID.randomUUID().toString());
        queryContext.setSize(42);

        assertNotNull("EventsPage must not be null!",
            engine.getEventService().getEvents(queryContext).get());
    }

    @Test
    public void testGetEvents_EventsPage() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{\"first-event-id\": \"string\","
            + "\"last-event-id\": \"string\","
            + "\"requested-count\": 0,"
            + "\"returned-count\": 0}");

        QueryContext<EventsPage> queryContext = new QueryContext<>();
        queryContext.setFromId(UUID.randomUUID().toString());
        queryContext.setSize(42);
        validateEventsPage(engine.getEventService().getEvents(queryContext).get(), false);
    }

    @Test
    public void testGetEvents_EventsPage_ApiEvent() {
        ResponseData.INSTANCE.setResponseCode(SC_OK); // 200
        ResponseData.INSTANCE.setResponseMessage("{"
            + "\"first-event-id\": \"string\","
            + "\"last-event-id\": \"string\","
            + "\"requested-count\": 0,"
            + "\"returned-count\": 0,"
            + "\"api-events\": [{"
            + "\"inn\": \"string\","
            + "\"kpp\": \"string\","
            + "\"docflow-type\": \"urn:nss:nid\","
            + "\"docflow-link\": {"
            + "\"href\": \"string\","
            + "\"rel\": \"string\","
            + "\"name\": \"string\","
            + "\"title\": \"string\","
            + "\"profile\": \"string\","
            + "\"templated\": true"
            + "},"
            + "\"new-state\": \"urn:nss:nid\","
            + "\"event-date-time\": \"" + StandardValues.DATE + "\","
            + "\"id\": \"string\""
            + "}]"
            + "}");

        QueryContext<EventsPage> queryContext = new QueryContext<>();
        queryContext.setFromId(UUID.randomUUID().toString());
        queryContext.setSize(42);
        validateEventsPage(engine.getEventService().getEvents(queryContext).get(), true);
    }

}
