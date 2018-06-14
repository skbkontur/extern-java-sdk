/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import java.util.function.Supplier;
import ru.kontur.extern_api.sdk.model.EventsPage;
import ru.kontur.extern_api.sdk.service.transport.adaptor.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.EVENTS_PAGE;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.EventsPageDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.EventsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;

/**
 *
 * @author alexs
 */
public class EventsAdaptorImpl extends BaseAdaptor implements EventsAdaptor {

    private final EventsApi api;

    public EventsAdaptorImpl(EventsApi eventsApi) {
        this.api = eventsApi;
    }

    public EventsAdaptorImpl() {
        this(new EventsApi());
    }

    @Override
    public QueryContext<EventsPage> getEvents(QueryContext<EventsPage> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            return cxt.setResult(
                new EventsPageDto()
                    .fromDto(
                        transport(cxt)
                            .eventsGetEvents(
                                cxt.getFromId(),
                                cxt.getSize()
                            )
                    ),
                 EVENTS_PAGE
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private EventsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient)super.prepareTransport(cxt));
        return api;
    }
}
