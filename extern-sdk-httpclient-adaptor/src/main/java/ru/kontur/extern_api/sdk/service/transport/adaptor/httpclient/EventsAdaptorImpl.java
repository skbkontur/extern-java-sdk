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

package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import java.util.function.Supplier;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.EVENTS_PAGE;

import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.service.transport.adaptor.ApiException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient.api.EventsApi;

/**
 * @author Mikhail Pavlenko
 */

public class EventsAdaptorImpl extends BaseAdaptor implements EventsAdaptor {

    private final EventsApi api;

    public EventsAdaptorImpl() {
        api = new EventsApi();
    }

    @Override
    public HttpClient getHttpClient() {
        return api.getHttpClient();
    }

    @Override
    public void setHttpClient(Supplier<HttpClient> httpClient) {
        super.httpClientSupplier = httpClient;
    }
    
    @Override
    public QueryContext<EventsPage> getEvents(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            return new QueryContext<EventsPage>(cxt, cxt.getEntityName()).setResult(
                transport(cxt)
                    .getEvents(
                        cxt.getFromId(),
                        cxt.getSize())
                    .getData(),
                EVENTS_PAGE
            );
        } catch (ApiException x) {
            return new QueryContext<EventsPage>(cxt, cxt.getEntityName()).setServiceError(x);
        }
    }

    private EventsApi transport(QueryContext<?> cxt) {
        api.setHttpClient(configureTransport(cxt));
        return api;
    }
}
