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
package ru.kontur.extern_api.sdk;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.jetbrains.annotations.NotNull;
import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_LOAD;
import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_NOT_FOUND;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import static ru.kontur.extern_api.sdk.Messages.UNKNOWN;
import static ru.kontur.extern_api.sdk.utils.YAStringUtils.isNullOrEmpty;

import ru.kontur.extern_api.sdk.event.AuthenticationEvent;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.provider.*;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.auth.EngineAuthenticationProvider;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.BusinessDriver;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.OrganizationService;
import ru.kontur.extern_api.sdk.service.SDKException;
import ru.kontur.extern_api.sdk.service.ServicesFactory;
import ru.kontur.extern_api.sdk.service.impl.DefaultServicesFactory;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.utils.YAStringUtils;

/**
 * @author Aleksey Sukhorukov
 */
public class ExternEngine implements AuthenticationListener {

    private Environment env;

    private BusinessDriver businessDriver;

    private ServicesFactory servicesFactory;

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     */
    public ExternEngine() {
        this(new Configuration(), new DefaultServicesFactory());
    }

    public ExternEngine(@NotNull ServicesFactory servicesFactory) {
        this(new Configuration(), servicesFactory);
    }

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param config содержит конфигурационные параметры для инициализации нового ExternEngine объекта
     * @param servicesFactory ServicesFactory предоставляет проинициализированные сервисы, предоставляющие высокоуровневый доступ к Extern API
     * @see ru.kontur.extern_api.sdk.Configuration
     */
    public ExternEngine(@NotNull Configuration config, @NotNull ServicesFactory servicesFactory) {
        this.servicesFactory = servicesFactory;
        this.env = new Environment();
        this.env.configuration = config;
        setAccountProvider(config);
        setApiKeyProvider(config);
        if (!isNullOrEmpty(config.getLogin()) && !isNullOrEmpty(config.getPass())) {
            setAuthenticationProvider(
                new AuthenticationProviderByPass(
                    () -> ExternEngine.this.env.configuration.getAuthBaseUri(),
                    LoginAndPasswordProvider.form(config.getLogin(), config.getPass()),
                    config
                )
            );
        }
        setServiceBaseUriProvider(config::getServiceBaseUri);
        this.businessDriver = new BusinessDriver(this);
    }

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param configPath содержит путь к файлу, содержащий конфигурационные параметры
     * @throws SDKException непроверяемое исключение может возникнуть при загрузки данных из файла
     * @see ru.kontur.extern_api.sdk.Configuration
     * @see ru.kontur.extern_api.sdk.service.SDKException
     */
    public ExternEngine(@NotNull String configPath) throws SDKException {
        // loads config data from the resourse file: extern-sdk-config.json
        this(loadConfiguration(configPath), new DefaultServicesFactory());
    }

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param configuration содержит конфигурационные параметры для инициализации нового ExternEngine объекта
     * @throws SDKException непроверяемое исключение может возникнуть при загрузки данных из файла
     * @see ru.kontur.extern_api.sdk.Configuration
     * @see ru.kontur.extern_api.sdk.service.SDKException
     */
    public ExternEngine(@NotNull Configuration configuration) throws SDKException {
        // loads config data from the resourse file: extern-sdk-config.json
        this(configuration, new DefaultServicesFactory());
    }

    private static Configuration loadConfiguration(String path) throws SDKException {
        try (InputStream is = ExternEngine.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new SDKException(Messages.get(C_CONFIG_NOT_FOUND));
            }

            return new Gson()
                    .fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
        }
        catch (IOException x) {
            throw new SDKException(Messages.get(C_CONFIG_LOAD), x);
        }
        catch (SDKException x) {
            throw x;
        }
        catch (Throwable x) {
            throw new SDKException(Messages.get(UNKNOWN), x);
        }
    }

    /**
     * Возвращает окружение среды выполненения
     *
     * @return параметры среды выполнения
     * @see Environment
     */
    public Environment getEnvironment() {
        return env;
    }

    /**
     * Возвращает параметры, использующиеся для конфигурации среды выполнения
     *
     * @return Configuration параметры среды выполненя
     * @see Configuration
     */
    public Configuration getConfiguration() {
        return env.configuration;
    }

    /**
     * Возвращает экземпляр класса AccountService
     *
     * @return AccountService сервис предназначен для работы с учетными записями
     * @see ru.kontur.extern_api.sdk.service.AccountService
     */
    public AccountService getAccountService() {
        return servicesFactory.getAccountService();
    }

    /**
     * Возвращает экземпляр класса AccountService
     *
     * @return CertificateService сервис предназначен для работы с сертификатами пользователей
     * @see ru.kontur.extern_api.sdk.service.CertificateService
     */
    public CertificateService getCertificateService() {
        return servicesFactory.getCertificateService();
    }

    /**
     * Возвращает экземпляр класса DocflowService
     *
     * @return DocflowService сервис предназначен для работы с документоборотами
     * @see ru.kontur.extern_api.sdk.service.DocflowService
     */
    public DocflowService getDocflowService() {
        return servicesFactory.getDocflowService();
    }

    /**
     * Возвращает экземпляр класса DraftService
     *
     * @return DraftService сервис предназначен для работы с черновиками
     * @see ru.kontur.extern_api.sdk.service.DraftService
     */
    public DraftService getDraftService() {
        return servicesFactory.getDraftService();
    }

    /**
     * Возвращает экземпляр класса EventService
     *
     * @return EventService сервис предназначен для получения ленты соббытий документооборота
     * @see ru.kontur.extern_api.sdk.service.EventService
     */
    public EventService getEventService() {
        return servicesFactory.getEventService();
    }

    /**
     * Возвращает экземпляр класса OrganizationService
     *
     * @return OrganizationService сервис предназначен для управления организациями (CRUD)
     * @see ru.kontur.extern_api.sdk.service.OrganizationService
     */
    public OrganizationService getOrganizationService() {
        return servicesFactory.getOrganizationService();
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @return serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     */
    public UriProvider getServiceBaseUriProvider() {
        return servicesFactory.getServiceBaseUriProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @param serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     */
    public final void setServiceBaseUriProvider(UriProvider serviceBaseUriProvider) {
        this.servicesFactory.setServiceBaseUriProvider(serviceBaseUriProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @return AuthenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return this.servicesFactory.getAuthenticationProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @param authenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    public final void setAuthenticationProvider(@NotNull AuthenticationProvider authenticationProvider) {
        authenticationProvider.addAuthenticationListener(this);
        this.servicesFactory.setAuthenticationProvider(new EngineAuthenticationProvider(authenticationProvider, env));
        authenticationProvider.httpClient(this.getHttpClient());
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @return AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    public AccountProvider getAccountProvider() {
        return this.servicesFactory.getAccountProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @param accountProvider AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.provider.AuthenticationProvider
     */
    public final void setAccountProvider(AccountProvider accountProvider) {
        this.servicesFactory.setAccountProvider(accountProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @return ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.provider.ApiKeyProvider
     */
    public ApiKeyProvider getApiKeyProvider() {
        return this.servicesFactory.getApiKeyProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @param apiKeyProvider ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.provider.ApiKeyProvider
     */
    public final void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.servicesFactory.setApiKeyProvider(apiKeyProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @return CryptoProvider предназначен выполнения криптографических операций
     * @throws SDKException необрабатываемое исключение генерится в том случае, если криптопровайдер отсутствует
     * @see ru.kontur.extern_api.sdk.provider.CryptoProvider
     */
    public CryptoProvider getCryptoProvider() throws SDKException {
        if (this.servicesFactory.getCryptoProvider() == null) {
            throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
        }
        return this.servicesFactory.getCryptoProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @param cryptoProvider CryptoProvider предназначен выполнения криптографических операций
     * @see ru.kontur.extern_api.sdk.provider.CryptoProvider
     */
    public void setCryptoProvider(CryptoProvider cryptoProvider) {
        this.servicesFactory.setCryptoProvider(cryptoProvider);
    }

    public UserAgentProvider getUserAgentProvider() {
        return servicesFactory.getUserAgentProvider();
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс {@code UserIPProvider}
     * @return UserIPProvider предназначен для получения IP адреса отправителя
     * @see UserIPProvider
     */
    public UserIPProvider getUserIPProvider() {
        return servicesFactory.getUserIPProvider();
    }

    public void setUserIPProvider(UserIPProvider userIPProvider) {
        this.servicesFactory.setUserIPProvider(userIPProvider);
    }

    /**
     * Больше не используется
     */
    @Deprecated
    public void configureServices() {
    }

    @Override
    public void authenticate(AuthenticationEvent authEvent) {
        env.accessToken = authEvent.getAuthCxt().isSuccess() ? authEvent.getAuthCxt().get() : null;
    }

    /**
     * Возвращает экземпляр класса BusinessDriver.
     *
     * @return BusinessDriver класс, предназначенный для выполненения крупных операций. Например отправка документа выполняется с помощью следующих операций: 1)
     * создание черновика; 2) подпись документа; 3) отправка контента на сервер; 4) проверка; 5) запуск документооборота.
     */
    public BusinessDriver getBusinessDriver() {
        return businessDriver;
    }

    public HttpClient getHttpClient() {
        return servicesFactory.getHttpClient();
    }
}
