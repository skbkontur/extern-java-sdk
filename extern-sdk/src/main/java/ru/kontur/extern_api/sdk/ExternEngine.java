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
import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_LOAD;
import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_NOT_FOUND;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import static ru.kontur.extern_api.sdk.Messages.UNKNOWN;
import ru.kontur.extern_api.sdk.annotation.Component;
import ru.kontur.extern_api.sdk.event.AuthenticationEvent;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;
import ru.kontur.extern_api.sdk.provider.Providers;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.provider.auth.EngineAuthenticationProvider;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.BusinessDriver;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.SDKException;

/**
 *
 * @author alexs
 */
public class ExternEngine implements AuthenticationListener {

    private static final Gson GSON = new Gson();

    private Environment env;

    private UriProvider serviceBaseUriProvider;

    private EngineAuthenticationProvider authenticationProvider;

    private AccountProvider accountProvider;

    private ApiKeyProvider apiKeyProvider;

    private CryptoProvider cryptoProvider;

    private BusinessDriver businessDriver;
    
    @Component("accountService")
    private AccountService accountService;

    @Component("certificateService")
    private CertificateService certificateService;

    @Component("docflowService")
    private DocflowService docflowService;

    @Component("draftService")
    private DraftService draftService;

    @Component("eventService")
    private EventService eventService;

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     */
    public ExternEngine() {
        this(new Configuration());
    }

    private void init() {
        ExternContext.getInstance().bind(this);
        configureService(accountService);
        configureService(certificateService);
        configureService(eventService);
        configureService(docflowService);
        configureService(draftService);
    }

    private void configureService(Providers serviceProviders) {
        if (serviceProviders != null) {
            serviceProviders
                .accountProvider(accountProvider)
                .apiKeyProvider(apiKeyProvider)
                .authenticationProvider(authenticationProvider)
                .cryptoProvider(cryptoProvider)
                .serviceBaseUriProvider(serviceBaseUriProvider);
        }
    }
    
    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param configuration содержит конфигурационные параметры для инициализации нового ExternEngine объекта
     * @see ru.kontur.extern_api.sdk.Configuration
     */
    public ExternEngine(Configuration configuration) {
        init();
        this.env = new Environment();
        this.env.configuration = configuration;
        if (configuration != null) {
            setAccountProvider(configuration);
            setApiKeyProvider(configuration);
            setAuthenticationProvider(authenticationProvider);
            if (configuration.getLogin() != null && !configuration.getLogin().isEmpty() && configuration.getPass() != null && !configuration.getPass().isEmpty()) {
                setAuthenticationProvider(
                    new AuthenticationProviderByPass(
                        () -> ExternEngine.this.env.configuration.getAuthBaseUri(),
                        new LoginAndPasswordProvider() {
                            @Override
                            public String getLogin() {
                                return configuration.getLogin();
                            }

                            @Override
                                public String getPass() {
                                    return configuration.getPass();
                                }
                        },
                        () -> configuration.getApiKey()
                    )
                );
            }
            setServiceBaseUriProvider(() -> configuration.getServiceBaseUri());
        }
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
    public ExternEngine(String configPath) throws SDKException {
        // loads config data from the resourse file: extern-sdk-config.json
        this(loadConfiguration(configPath));
    }

    private static Configuration loadConfiguration(String path) throws SDKException {
        try (InputStream is = ExternEngine.class.getResourceAsStream(path)) {
            if (is == null) {
                throw new SDKException(Messages.get(C_CONFIG_NOT_FOUND));
            }

            return GSON.fromJson(new JsonReader(new InputStreamReader(is)), Configuration.class);
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
        return accountService;
    }

    /**
     * Устанавливает экземпляр класса AccountService
     *
     * @param accountService сервис предназначен для работы с учетными записями
     * @see ru.kontur.extern_api.sdk.service.AccountService
     */
    public void setAccountService(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * Возвращает экземпляр класса AccountService
     *
     * @return CertificateService сервис предназначен для работы с сертификатами пользователей
     * @see ru.kontur.extern_api.sdk.service.CertificateService
     */
    public CertificateService getCertificateService() {
        return certificateService;
    }

    /**
     * Устанавливает экземпляр класса CertificateService
     *
     * @param certificateService сервис предназначен для работы с сертификатами пользователей
     * @see ru.kontur.extern_api.sdk.service.CertificateService
     */
    public void setCertificateService(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /**
     * Возвращает экземпляр класса DocflowService
     *
     * @return DocflowService сервис предназначен для работы с документоборотами
     * @see ru.kontur.extern_api.sdk.service.DocflowService
     */
    public DocflowService getDocflowService() {
        return docflowService;
    }

    /**
     * Устанавливает экземпляр класса DocflowService
     *
     * @param docflowService сервис предназначен для работы с документоборотами
     * @see ru.kontur.extern_api.sdk.service.DocflowService
     */
    public void setDocflowService(DocflowService docflowService) {
        this.docflowService = docflowService;
    }

    /**
     * Возвращает экземпляр класса DraftService
     *
     * @return DraftService сервис предназначен для работы с черновиками
     * @see ru.kontur.extern_api.sdk.service.DraftService
     */
    public DraftService getDraftService() {
        return draftService;
    }

    /**
     * Устанавливает экземпляр класса DraftService
     *
     * @param draftService сервис предназначен для работы с черновиками
     * @see ru.kontur.extern_api.sdk.service.DraftService
     */
    public void setDraftService(DraftService draftService) {
        this.draftService = draftService;
    }

    /**
     * Возвращает экземпляр класса EventService
     *
     * @return EventService сервис предназначен для получения ленты соббытий документооборота
     * @see ru.kontur.extern_api.sdk.service.EventService
     */
    public EventService getEventService() {
        return eventService;
    }

    /**
     * Устанавливает экземпляр класса EventService
     *
     * @param eventService сервис предназначен для получения ленты соббытий документооборота
     * @see ru.kontur.extern_api.sdk.service.EventService
     */
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @return serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider
     */
    public UriProvider getServiceBaseUriProvider() {
        return serviceBaseUriProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @param serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider
     */
    public final void setServiceBaseUriProvider(UriProvider serviceBaseUriProvider) {
        this.serviceBaseUriProvider = serviceBaseUriProvider;
        this.accountService.serviceBaseUriProvider(serviceBaseUriProvider);
        this.certificateService.serviceBaseUriProvider(serviceBaseUriProvider);
        this.docflowService.serviceBaseUriProvider(serviceBaseUriProvider);
        this.draftService.serviceBaseUriProvider(serviceBaseUriProvider);
        this.eventService.serviceBaseUriProvider(serviceBaseUriProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @return AuthenticationProvider предназначен для получения токена аутентификации
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @param authenticationProvider предназначен для получения токена аутентификации
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
    public final void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        if (authenticationProvider != null) {
            authenticationProvider.addAuthenticationListener(this);
            this.authenticationProvider = new EngineAuthenticationProvider(authenticationProvider,env);
            this.accountService.authenticationProvider(this.authenticationProvider);
            this.certificateService.authenticationProvider(this.authenticationProvider);
            this.docflowService.authenticationProvider(this.authenticationProvider);
            this.draftService.authenticationProvider(this.authenticationProvider);
            this.eventService.authenticationProvider(this.authenticationProvider);
        }
        else if (this.authenticationProvider != null) {
            AuthenticationProvider originAuthenticationProvider = this.authenticationProvider.getOriginAuthenticationProvider();
            originAuthenticationProvider.removeAuthenticationListener(this);
            this.authenticationProvider = null;
            this.accountService.authenticationProvider(null);
            this.certificateService.authenticationProvider(null);
            this.docflowService.authenticationProvider(null);
            this.draftService.authenticationProvider(null);
            this.eventService.authenticationProvider(null);
        }
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @return AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
    public AccountProvider getAccountProvider() {
        return accountProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @param accountProvider AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
    public final void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
        this.accountService.accountProvider(accountProvider);
        this.certificateService.accountProvider(accountProvider);
        this.docflowService.accountProvider(accountProvider);
        this.draftService.accountProvider(accountProvider);
        this.eventService.accountProvider(accountProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @return ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.skbkontur.sdk.extern.providers.ApiKeyProvider
     */
    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @param apiKeyProvider ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.skbkontur.sdk.extern.providers.ApiKeyProvider
     */
    public final void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
        this.accountService.apiKeyProvider(apiKeyProvider);
        this.certificateService.apiKeyProvider(apiKeyProvider);
        this.docflowService.apiKeyProvider(apiKeyProvider);
        this.draftService.apiKeyProvider(apiKeyProvider);
        this.eventService.apiKeyProvider(apiKeyProvider);
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @return CryptoProvider предназначен выполнения криптографических операций
     * @throws SDKException необрабатываемое исключение генерится в том случае, если криптопровайдер отсутствует
     * @see ru.skbkontur.sdk.extern.providers.CryptoProvider
     */
    public CryptoProvider getCryptoProvider() throws SDKException {
        if (cryptoProvider == null) {
            throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
        }
        return cryptoProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @param cryptoProvider CryptoProvider предназначен выполнения криптографических операций
     * @return CryptoProvider
     * @see ru.skbkontur.sdk.extern.providers.CryptoProvider
     */
    public CryptoProvider setCryptoProvider(CryptoProvider cryptoProvider) {
        CryptoProvider current = this.cryptoProvider;
        this.cryptoProvider = cryptoProvider;
        accountService.cryptoProvider(cryptoProvider);
        certificateService.cryptoProvider(cryptoProvider);
        docflowService.cryptoProvider(cryptoProvider);
        draftService.cryptoProvider(cryptoProvider);
        eventService.cryptoProvider(cryptoProvider);
        return current;
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
     * @return BusinessDriver класс, предназначенный для выполненения крупных операций.
     * Например отправка документа выполняется с помощью следующих операций:
     * 1) создание черновика;
     * 2) подпись документа;
     * 3) отправка контента на сервер;
     * 4) проверка;
     * 5) запуск документооборота.
     */
    public BusinessDriver getBusinessDriver() {
        return businessDriver;
    }
}
