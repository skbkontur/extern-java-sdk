/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;


/**
 * @author AlexS
 */
public interface ApiClientAware {

    ApiClient getApiClient();

    void setApiClient(ApiClient apiClient);
}
