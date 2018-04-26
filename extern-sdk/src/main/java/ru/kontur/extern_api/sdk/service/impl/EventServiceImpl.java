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

package ru.kontur.extern_api.sdk.service.impl;

import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.transport.adaptors.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;

import java.util.concurrent.CompletableFuture;


/**
 * @author AlexS
 */
public class EventServiceImpl extends BaseService<EventsAdaptor> implements EventService {

    private static final String EN_EVENT = "event";

    @Override
    public CompletableFuture<QueryContext<EventsPage>> getEventsAsync(String fromId, int size) {
        QueryContext<EventsPage> cxt = createQueryContext(EN_EVENT);
        return cxt
                .setFromId(fromId)
                .setSize(size)
                .applyAsync(api::getEvents);
    }

    @Override
    public QueryContext<EventsPage> getEvents(QueryContext<?> parent) {
        QueryContext<EventsPage> cxt = createQueryContext(parent, EN_EVENT);
        return cxt.apply(api::getEvents);
    }

}
