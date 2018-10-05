/*
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
 *
 */

package ru.kontur.extern_api.sdk.httpclient.retrofit;

import okhttp3.logging.HttpLoggingInterceptor.Level;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import retrofit2.Response;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;


@Disabled("IT")
class RetrofitClientTest {

    @Test
    @DisplayName("client should log body and/or headers depends on specified log level")
    void loggingTest() throws Exception {

        RetrofitClient rc = new RetrofitClient(Level.BODY, "https://extern-api.testkontur.ru")
                .setAuthSid("***")
                .setApiKey("***");

        TestEventsApi service = rc.createService(TestEventsApi.class);
        Response<EventsPage> execute = service.getEvents("0_0", 1).get();

        Assertions.assertTrue(execute.isSuccessful());

    }

    @Nested
    @DisplayName("client with adaptor should be integrated with adaptor ")
    class AdaptorTest {

        @Test
        @DisplayName("on success")
        void adaptorTestSuccess() {

            RetrofitClient rc = new RetrofitClient(Level.BODY, "https://extern-api.testkontur.ru")
                    .setAuthSid("***")
                    .setApiKey("***");

            EventsAdaptorTest ea = new EventsAdaptorTest(rc.createService(TestEventsApi.class));

            EventsPage events = QueryContextUtils
                    .join(ea.getEvents(new QueryContext<>().setFromId("0_0").setSize(1)))
                    .ensureSuccess().get();

            Assertions.assertNotNull(events.getApiEvents());
        }

        @Test
        @DisplayName("on error")
        void adaptorTestFail() {

            RetrofitClient rc = new RetrofitClient(Level.BODY, "https://extern-api.testkontur.ru")
                    .setAuthSid("***")
                    .setApiKey("***");

            EventsAdaptorTest ea = new EventsAdaptorTest(rc.createService(TestEventsApi.class));

            QueryContext<EventsPage> events = QueryContextUtils
                    .join(ea.getEvents(new QueryContext<>().setFromId("0_0").setSize(1)));

            Assertions.assertTrue(events.isFail());
            Assertions.assertEquals(401, events.getServiceError().getResponseCode());
        }

    }

}
