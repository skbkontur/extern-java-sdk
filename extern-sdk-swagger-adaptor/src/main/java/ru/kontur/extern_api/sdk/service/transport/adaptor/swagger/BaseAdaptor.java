/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import com.google.gson.GsonBuilder;
import java.util.Date;
import java.util.function.Supplier;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DateAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DateTimeTypeAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DocumentToSendAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.LocalDateTypeAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.SignatureToSendAdapter;

/**
 *
 * @author alexs
 */
public abstract class BaseAdaptor {
    
    protected Supplier<HttpClient> httpClientSupplier;
    
    protected HttpClient prepareTransport(QueryContext<?> cxt) {
        HttpClient httpClient = httpClientSupplier.get();
//        apiClient.setUserAgent(UserAgentService.USER_AGENT_STRING);
        httpClient.setGson(new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Date.class, new DateAdapter())
            .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend.class, new SignatureToSendAdapter())
            .registerTypeAdapter(ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend.class, new DocumentToSendAdapter())
            .create());
        // устанавливаем URI сервиса
        httpClient.setServiceBaseUri(cxt.getServiceBaseUriProvider().getUri());
        // устанавливаем таймаут соединения
        httpClient.setConnectWaiting(120 * 1000);
        // устанавливаем таймаут чтения
        httpClient.setReadTimeout(120 * 1000);
        // устанавливаем access tocken
        httpClient.acceptAccessToken(cxt.getAuthPrefix(), cxt.getSessionId());
        // устанавливаем api-key
        httpClient.acceptApiKey(cxt.getApiKeyProvider().getApiKey());
        
        return httpClient;
    }
}
