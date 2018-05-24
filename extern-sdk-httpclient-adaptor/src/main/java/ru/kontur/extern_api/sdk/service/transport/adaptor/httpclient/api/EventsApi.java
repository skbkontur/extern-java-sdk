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

package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api;

import com.google.gson.reflect.TypeToken;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiResponse;

/**
 * @author Mikhail Pavlenko
 */

public class EventsApi extends RestApi {

    /**
     * Shows docflow events for all users and accounts connected with external service (e.g.: bank)
     *
     * @param fromId Event Id from which data is read out (required)
     * @param batchSize Max count of events to be returned (required)
     * @return ApiResponse&lt;EventsPage&gt;
     * @throws ApiException transport exception
     */
    @Path("/v1/events")
    @GET
    @Consumes("application/json; charset=utf-8")
    @Produces("application/json; charset=utf-8")
    public ApiResponse<EventsPage> getEvents(@QueryParam("fromId") String fromId, @QueryParam("batchSize") Integer batchSize) throws ApiException {
        return invoke("getEvents", null, new TypeToken<EventsPage>() {}.getType(), fromId, batchSize);
    }
}
