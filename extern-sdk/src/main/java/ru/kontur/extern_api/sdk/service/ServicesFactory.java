/*
 * The MIT License
 *
 * Copyright 2018 alexs.
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
package ru.kontur.extern_api.sdk.service;

import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.provider.UserAgentProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;

/**
 *
 * @author alexs
 */
public interface ServicesFactory {
    UserAgentProvider getUserAgentProvider();
    
	AccountService getAccountService();
    
	CertificateService getCertificateService();
    
	DocflowService getDocflowService();
    
	DraftService getDraftService();
    
	EventService getEventService();

    OrganizationService getOrganizationService();

    HttpClient getHttpClient();
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @return serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.kontur.extern_api.sdk.provider.ServiceBaseUriProvider
     */
    UriProvider getServiceBaseUriProvider();
    
    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @param serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.kontur.extern_api.sdk.provider.ServiceBaseUriProvider
     */
    void setServiceBaseUriProvider(UriProvider serviceBaseUriProvider);
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @return AuthenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    AuthenticationProvider getAuthenticationProvider();
    
    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @param authenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    void setAuthenticationProvider(AuthenticationProvider authenticationProvider);
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @return AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    AccountProvider getAccountProvider();
    
    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @param accountProvider AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    void setAccountProvider(AccountProvider accountProvider);
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @return ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.provider.ApiKeyProvider
     */
    ApiKeyProvider getApiKeyProvider();
    
    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @param apiKeyProvider ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.provider.ApiKeyProvider
     */
    void setApiKeyProvider(ApiKeyProvider apiKeyProvider);
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @return CryptoProvider предназначен выполнения криптографических операций
     * @throws SDKException необрабатываемое исключение генерится в том случае, если криптопровайдер отсутствует
     * @see ru.kontur.extern_api.sdk.provider.CryptoProvider
     */
    CryptoProvider getCryptoProvider() throws SDKException;

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @param cryptoProvider CryptoProvider предназначен выполнения криптографических операций
     * @return CryptoProvider
     * @see ru.kontur.extern_api.sdk.provider.CryptoProvider
     */
    void setCryptoProvider(CryptoProvider cryptoProvider);
}
