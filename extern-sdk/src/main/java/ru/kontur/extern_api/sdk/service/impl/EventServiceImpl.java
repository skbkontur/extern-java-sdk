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
import ru.kontur.extern_api.sdk.service.transport.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.annotation.Component;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;


/**
 * @author AlexS
 */
public class EventServiceImpl extends AbstractService<EventsAdaptor> implements EventService {

    private static final String EN_EVENT = "event";

    @Component("eventsAdaptor")
    private EventsAdaptor eventsAdaptor;
    
    @Override
	public EventService serviceBaseUriProvider(UriProvider serviceBaseUriProvider) {
		super.serviceBaseUriProvider = serviceBaseUriProvider;
        return this;
	}

    @Override
	public EventService authenticationProvider(AuthenticationProvider authenticationProvider) {
		super.authenticationProvider = authenticationProvider;
        return this;
	}

    @Override
	public EventService accountProvider(AccountProvider accountProvider) {
		super.accountProvider = accountProvider;
        return this;
	}

    @Override
	public EventService apiKeyProvider(ApiKeyProvider apiKeyProvider) {
		super.apiKeyProvider = apiKeyProvider;
        return this;
	}

    @Override
	public EventService cryptoProvider(CryptoProvider cryptoProvider) {
		super.cryptoProvider = cryptoProvider;
        return this;
	}

    @Override
    public CompletableFuture<QueryContext<EventsPage>> getEventsAsync(String fromId, int size) {
        QueryContext<EventsPage> cxt = createQueryContext(EN_EVENT);
        return cxt
                .setFromId(fromId)
                .setSize(size)
                .applyAsync(eventsAdaptor::getEvents);
    }

    @Override
    public QueryContext<EventsPage> getEvents(QueryContext<?> parent) {
        QueryContext<EventsPage> cxt = createQueryContext(parent, EN_EVENT);
        return cxt.apply(eventsAdaptor::getEvents);
    }
}
