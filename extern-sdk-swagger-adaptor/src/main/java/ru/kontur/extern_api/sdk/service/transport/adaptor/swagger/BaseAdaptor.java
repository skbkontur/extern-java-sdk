/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import com.google.gson.GsonBuilder;
import java.util.Date;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DateAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DateTimeTypeAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.DocumentToSendAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.LocalDateTypeAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.SignatureToSendAdapter;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.UserAgentService;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.auth.ApiKeyAuth;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.auth.Authentication;

/**
 *
 * @author alexs
 */
public abstract class BaseAdaptor implements ApiClientAware {
    void prepareTransport(QueryContext<?> cxt) {
        ApiClient apiClient = new ApiClient();
        apiClient.setUserAgent(UserAgentService.USER_AGENT_STRING);
        apiClient.getJSON().setGson(new GsonBuilder()
            .disableHtmlEscaping()
            .registerTypeAdapter(Date.class, new DateAdapter(apiClient))
            .registerTypeAdapter(DateTime.class, new DateTimeTypeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateTypeAdapter())
            .registerTypeAdapter(ru.kontur.extern_api.sdk.service.transport.swagger.model.SignatureToSend.class, new SignatureToSendAdapter())
            .registerTypeAdapter(ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend.class, new DocumentToSendAdapter())
            .create());
        // устанавливаем URI сервиса
        apiClient.setBasePath(cxt.getServiceBaseUriProvider().getUri());
        // устанавливаем таймаут соединения
        apiClient.setConnectTimeout(60 * 1000);
        // устанавливаем таймаут чтения
        apiClient.setReadTimeout(60 * 1000);
        // устанавливаем сконфигуренного клиента
        setApiClient(apiClient);
        // устанавливаем access tocken
        acceptAccessToken(cxt.getAuthPrefix(),cxt.getSessionId());
        // устанавливаем api-key
        acceptApiKey(cxt.getApiKeyProvider().getApiKey());
    }
    
    private void acceptApiKey(String apiKey) {
        if (apiKey != null && !apiKey.isEmpty()) {
            Authentication apiKeyAuth = getApiClient().getAuthentication("apiKey");
            if (apiKeyAuth != null && apiKeyAuth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) apiKeyAuth).setApiKey(apiKey);
            }
        }
    }

    private void acceptAccessToken(String authPrefix, String sessionId) {
        if (sessionId != null && !sessionId.isEmpty()) {
            Authentication apiKeyAuth = getApiClient().getAuthentication("auth.sid");
            if (apiKeyAuth != null && apiKeyAuth instanceof ApiKeyAuth) {
                ((ApiKeyAuth) apiKeyAuth).setApiKey(sessionId);
                ((ApiKeyAuth) apiKeyAuth).setApiKeyPrefix(authPrefix);
            }
        }
    }
}
