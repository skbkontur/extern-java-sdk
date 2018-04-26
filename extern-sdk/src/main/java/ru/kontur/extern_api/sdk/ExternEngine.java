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

package ru.kontur.extern_api.sdk;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import ru.kontur.extern_api.sdk.event.AuthenticationEvent;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.providers.AccountProvider;
import ru.kontur.extern_api.sdk.providers.ApiKeyProvider;
import ru.kontur.extern_api.sdk.providers.AuthenticationProvider;
import ru.kontur.extern_api.sdk.providers.CryptoProvider;
import ru.kontur.extern_api.sdk.providers.ServiceBaseUriProvider;
import ru.kontur.extern_api.sdk.providers.ServiceError;
import ru.kontur.extern_api.sdk.providers.auth.AuthenticationProviderByPass;
import ru.kontur.extern_api.sdk.service.AccountService;
import ru.kontur.extern_api.sdk.service.BusinessDriver;
import ru.kontur.extern_api.sdk.service.CertificateService;
import ru.kontur.extern_api.sdk.service.DocflowService;
import ru.kontur.extern_api.sdk.service.DraftService;
import ru.kontur.extern_api.sdk.service.EventService;
import ru.kontur.extern_api.sdk.service.SDKException;
import ru.kontur.extern_api.sdk.service.impl.AccountServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.BaseService;
import ru.kontur.extern_api.sdk.service.impl.CertificateServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.DocflowServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.DraftServiceImpl;
import ru.kontur.extern_api.sdk.service.impl.EventServiceImpl;
import ru.kontur.extern_api.sdk.service.transport.adaptors.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.CertificatesAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.DraftsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.EventsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_LOAD;
import static ru.kontur.extern_api.sdk.Messages.C_CONFIG_NOT_FOUND;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import static ru.kontur.extern_api.sdk.Messages.UNKNOWN;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.SESSION_ID;


/**
 * {@code ExternEngine} класс предоставляет инструмент для работы с API Контур Экстерна
 *
 * @author Сухоруков А., St.Petersburg 20/04/2018
 * @since 1.1
 */
public class ExternEngine implements AuthenticationListener {

    private static final Gson GSON = new Gson();

    private final Environment env;

    private AccountService accountService;

    private DraftService draftService;

    private DocflowService docflowService;

    private CertificateService certificateService;

    private EventService eventService;

    private ServiceBaseUriProvider serviceBaseUriProvider;

    private EngineAuthenticationProvider authenticationProvider;

    private AccountProvider accountProvider;

    private ApiKeyProvider apiKeyProvider;

    private CryptoProvider cryptoProvider;

    private BusinessDriver businessDriver;

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     */
    public ExternEngine() {
        this(new Configuration());
    }

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param configuration содержит конфигурационные параметры для инициализации нового ExternEngine объекта
     * @see ru.kontur.extern_api.sdk.Configuration
     */
    public ExternEngine(Configuration configuration) {
        this.businessDriver = new BusinessDriver(this);
        this.env = new Environment();
        this.env.configuration = configuration;
        configure();
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
        } catch (IOException x) {
            throw new SDKException(Messages.get(C_CONFIG_LOAD), x);
        } catch (SDKException x) {
            throw x;
        } catch (Throwable x) {
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
     * Возвращает экземпляр класса DocflowService
     *
     * @return DocflowService сервис предназначен для работы с документооборотами
     * @see ru.kontur.extern_api.sdk.service.DocflowService
     */
    public DocflowService getDocflowService() {
        return docflowService;
    }

    /**
     * Устанавливает экземпляр класса DocflowService
     *
     * @param docflowService сервис предназначен для работы с документооборотами
     * @see ru.kontur.extern_api.sdk.service.DocflowService
     */
    public void setDocflowService(DocflowService docflowService) {
        this.docflowService = docflowService;
    }

    /**
     * Возвращает экземпляр класса CertificateService
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
     * @see ru.kontur.extern_api.sdk.providers.ServiceBaseUriProvider
     */
    public ServiceBaseUriProvider getServiceBaseUriProvider() {
        return serviceBaseUriProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     *
     * @param serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.kontur.extern_api.sdk.providers.ServiceBaseUriProvider
     */
    public void setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
        this.serviceBaseUriProvider = serviceBaseUriProvider;
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @return AuthenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.providers.AuthenticationProvider
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider.getOriginAuthenticationProvider();
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AuthenticationProvider
     *
     * @param authenticationProvider предназначен для получения токена аутентификации
     * @see ru.kontur.extern_api.sdk.providers.AuthenticationProvider
     */
    public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
        if (authenticationProvider != null) {
            authenticationProvider.addAuthenticationListener(this);
            this.authenticationProvider = new EngineAuthenticationProvider(authenticationProvider);
        } else if (this.authenticationProvider != null) {
            AuthenticationProvider originAuthenticationProvider = this.authenticationProvider.getOriginAuthenticationProvider();
            originAuthenticationProvider.removeAuthenticationListener(this);
        }
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @return AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.providers.AuthenticationProvider
     */
    public AccountProvider getAccountProvider() {
        return accountProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AccountProvider
     *
     * @param accountProvider AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.kontur.extern_api.sdk.providers.AuthenticationProvider
     */
    public void setAccountProvider(AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @return ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.providers.ApiKeyProvider
     */
    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider;
    }

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ApiKeyProvider
     *
     * @param apiKeyProvider ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.kontur.extern_api.sdk.providers.ApiKeyProvider
     */
    public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
    }

    /**
     * Возвращает экземпляр класса, реализующий интерфейс CryptoProvider
     *
     * @return CryptoProvider предназначен выполнения криптографических операций
     * @throws SDKException необрабатываемое исключение генерится в том случае, если криптопровайдер отсутствует
     * @see ru.kontur.extern_api.sdk.providers.CryptoProvider
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
     * @see ru.kontur.extern_api.sdk.providers.CryptoProvider
     */
    public CryptoProvider setCryptoProvider(CryptoProvider cryptoProvider) {
        CryptoProvider current = this.cryptoProvider;
        this.cryptoProvider = cryptoProvider;
        return current;
    }

    /**
     * Метод конфигурирует экземпляр класса ExternEngine и должен быть вызван после установки всех провайдеров или загрузки конфигурации
     */
    public void configureServices() {
        AccountServiceImpl accSrv = new AccountServiceImpl();
        configureService(accSrv);
        accSrv.setApi(new AccountsAdaptor());
        this.accountService = accSrv;

        DraftServiceImpl draftSrv = new DraftServiceImpl();
        configureService(draftSrv);
        draftSrv.setApi(new DraftsAdaptor());
        this.draftService = draftSrv;

        DocflowServiceImpl docflowSrv = new DocflowServiceImpl();
        configureService(docflowSrv);
        docflowSrv.setApi(new DocflowsAdaptor());
        this.docflowService = docflowSrv;

        CertificateServiceImpl certificateSrv = new CertificateServiceImpl();
        configureService(certificateSrv);
        certificateSrv.setApi(new CertificatesAdaptor());
        this.certificateService = certificateSrv;

        EventServiceImpl eventSrv = new EventServiceImpl();
        configureService(eventSrv);
        eventSrv.setApi(new EventsAdaptor());
        this.eventService = eventSrv;
    }

    private void configureService(BaseService<?> baseService) {
        baseService.setServiceBaseUriProvider(this.serviceBaseUriProvider);
        baseService.setAuthenticationProvider(this.authenticationProvider);
        baseService.setAccountProvider(this.accountProvider);
        baseService.setApiKeyProvider(this.apiKeyProvider);
        baseService.setCryptoProvider(this.cryptoProvider);
    }

    /**
     * Метод, реализующий интерфейс AuthenticationListener, предназначенный для обоаботки события аутентификации пользователя.
     * В случае если пользователь аутентифицировался, то токен аутентификации (ТА) сохраняется в окружении. В противном случае устанавливается null.
     *
     * @param authEvent событие, которое передается в данный метод
     * @see ru.kontur.extern_api.sdk.event.AuthenticationListener
     */
    @Override
    public synchronized void authenticate(AuthenticationEvent authEvent) {
        env.accessToken = authEvent.getAuthCxt().isSuccess() ? authEvent.getAuthCxt().get() : null;
    }

    private void configure() {
        Configuration c = env.configuration;
        if (c != null) {

            if (c.accountId() != null) {
                setAccountProvider(c);
            }

            if (c.getApiKey() != null) {
                setApiKeyProvider(c);
            }

            if (c.getServiceBaseUri() != null) {
                setServiceBaseUriProvider(c);
            }

            if (c.getUri() != null && c.getLogin() != null && c.getPass() != null && c.getApiKey() != null) {
                setAuthenticationProvider(new AuthenticationProviderByPass(c, c, c));
            }
        }
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


    class EngineAuthenticationProvider implements AuthenticationProvider {

        private final AuthenticationProvider authenticationProvider;

        private EngineAuthenticationProvider(AuthenticationProvider authenticationProvider) {
            this.authenticationProvider = authenticationProvider;
        }

        private AuthenticationProvider getOriginAuthenticationProvider() {
            return authenticationProvider;
        }

        @Override
        public QueryContext<String> sessionId() {
            if (env.accessToken == null) {
                return authenticationProvider.sessionId();
            } else {
                return new QueryContext<String>().setResult(env.accessToken, SESSION_ID);
            }
        }

        @Override
        public String authPrefix() {
            return authenticationProvider.authPrefix();
        }

        @Override
        public void addAuthenticationListener(AuthenticationListener authListener) {
            authenticationProvider.addAuthenticationListener(authListener);
        }

        @Override
        public void removeAuthenticationListener(AuthenticationListener authListener) {
            authenticationProvider.removeAuthenticationListener(authListener);
        }

        @Override
        public void raiseUnauthenticated(ServiceError x) {
            authenticationProvider.raiseUnauthenticated(x);
        }
    }
}
