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

import ru.skbkontur.sdk.extern.model.EventsPage;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.EVENTS_PAGE;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.EventsPageDto;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.api.EventsApi;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author AlexS
 */
public class EventsAdaptor extends BaseAdaptor {

    private EventsApi api;

    public EventsAdaptor(EventsApi eventsApi) {
        this.api = eventsApi;
    }

    public EventsAdaptor() {
        this(new EventsApi());
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
		api.setApiClient(apiClient);
    }

	public QueryContext<EventsPage> getEvents(QueryContext<EventsPage> cxt) {
		try {
			if (cxt.isFail()) return cxt;

			return cxt.setResult(
				new EventsPageDto()
					.fromDto(
						transport(cxt)
                            .eventsGetEvents(
                                cxt.getFromId(),
                                cxt.getSize()
                            )
					)
				,
				EVENTS_PAGE
			);
		}
		catch (ApiException x) {
			return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
		}
	}
	
	private EventsApi transport(QueryContext<?> cxt) {
		super.prepareTransport(cxt);
		return api;
	}
    
}
