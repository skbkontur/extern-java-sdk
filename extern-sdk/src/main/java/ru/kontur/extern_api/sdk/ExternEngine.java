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

import static ru.kontur.extern_api.sdk.utils.YAStringUtils.isNullOrEmpty;

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.event.AuthenticationEvent;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.LoginAndPasswordProvider;
import ru.kontur.extern_api.sdk.provider.ProviderHolderParent;
import ru.kontur.extern_api.sdk.provider.auth.AuthenticationProviderByPass;
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
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;

/**
 * @author Aleksey Sukhorukov
 */
public class ExternEngine implements AuthenticationListener, ProviderHolderParent<ServicesFactory> {

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
     * @param config содержит конфигурационные параметры для инициализации нового ExternEngine
     * объекта
     * @param servicesFactory ServicesFactory предоставляет проинициализированные сервисы,
     * предоставляющие высокоуровневый доступ к Extern API
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
                            config::getAuthBaseUri,
                            LoginAndPasswordProvider.form(config.getLogin(), config.getPass()),
                            config
                    ).httpClient(getHttpClient())
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
        // loads config data from the resource file: extern-sdk-config.json
        this(loadConfiguration(configPath), new DefaultServicesFactory());
    }

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     *
     * @param configuration содержит конфигурационные параметры для инициализации нового
     * ExternEngine объекта
     * @throws SDKException непроверяемое исключение может возникнуть при загрузки данных из файла
     * @see ru.kontur.extern_api.sdk.Configuration
     * @see ru.kontur.extern_api.sdk.service.SDKException
     */
    public ExternEngine(@NotNull Configuration configuration) throws SDKException {
        // loads config data from the resource file: extern-sdk-config.json
        this(configuration, new DefaultServicesFactory());
    }

    private static Configuration loadConfiguration(String path) {
        return UncheckedSupplier
                .get(() -> Configuration.load(ExternEngine.class.getResource(path)));
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

    @Override
    public ServicesFactory getChildProviderHolder() {
        return servicesFactory;
    }

    @Override
    public void authenticate(AuthenticationEvent authEvent) {
        env.accessToken = authEvent.getAuthCxt().isSuccess() ? authEvent.getAuthCxt().get() : null;
    }

    /**
     * Возвращает экземпляр класса BusinessDriver.
     *
     * @return BusinessDriver класс, предназначенный для выполненения крупных операций. Например
     * отправка документа выполняется с помощью следующих операций: 1) создание черновика; 2)
     * подпись документа; 3) отправка контента на сервер; 4) проверка; 5) запуск документооборота.
     */
    public BusinessDriver getBusinessDriver() {
        return businessDriver;
    }

    public HttpClient getHttpClient() {
        return servicesFactory.getHttpClient();
    }
}
