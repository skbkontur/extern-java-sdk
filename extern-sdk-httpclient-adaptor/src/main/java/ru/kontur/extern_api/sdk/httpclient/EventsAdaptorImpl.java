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

package ru.kontur.extern_api.sdk.httpclient;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.httpclient.retrofit.ApiUtils;
import ru.kontur.extern_api.sdk.httpclient.retrofit.api.EventsApi;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.utils.QueryContextUtils;


public class EventsAdaptorImpl implements EventsAdaptor {

    private final EventsApi api;
    private final ApiUtils utils;

    public EventsAdaptorImpl(EventsApi api) {
        this.api = api;
        this.utils = new ApiUtils(GsonProvider.getGson());
    }

    @Override
    public CompletableFuture<QueryContext<EventsPage>> getEvents(QueryContext<?> cxt) {
        String fromId = Objects.requireNonNull(cxt.getFromId(), "`fromId` should be in context");
        int batchSize = cxt.getSize();

        if (batchSize == 0) {
            throw new NullPointerException("`size` should be in context and greater than 0");
        }

        return api.getEvents(fromId, batchSize)
                .thenApply(utils::toApiResponse)
                .exceptionally(ApiResponse::error)
                .thenApply(rsp -> QueryContextUtils.join(cxt, rsp, QueryContext.EVENTS_PAGE));
    }

}
