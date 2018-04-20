/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import ru.skbkontur.sdk.extern.service.SDKException;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import static ru.skbkontur.sdk.extern.Messages.C_CONFIG_LOAD;
import static ru.skbkontur.sdk.extern.Messages.C_CONFIG_NOT_FOUND;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER;
import static ru.skbkontur.sdk.extern.Messages.UNKNOWN;
import ru.skbkontur.sdk.extern.event.AuthenticationEvent;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.providers.AccountProvider;
import ru.skbkontur.sdk.extern.providers.ApiKeyProvider;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.providers.auth.AuthenticationProviderByPass;
import ru.skbkontur.sdk.extern.service.AccountService;
import ru.skbkontur.sdk.extern.service.BusinessDriver;
import ru.skbkontur.sdk.extern.service.CertificateService;
import ru.skbkontur.sdk.extern.service.DocflowService;
import ru.skbkontur.sdk.extern.service.DraftService;
import ru.skbkontur.sdk.extern.service.EventService;
import ru.skbkontur.sdk.extern.service.impl.AccountServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.BaseService;
import ru.skbkontur.sdk.extern.service.impl.CertificateServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DocflowServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.DraftServiceImpl;
import ru.skbkontur.sdk.extern.service.impl.EventServiceImpl;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.CertificatesAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DocflowsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.DraftsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.EventsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.SESSION_ID;

/**
 * {@code ExternEngine} класс предоставляет инструмент для работы с API Контур Экстерна
 * 
 * @since 1.1
 * 
 * @author Сухоруков А., St.Petersburg 20/04/2018
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
		this.env = new Environment();
		this.env.configuration = new Configuration();
		this.businessDriver = new BusinessDriver(this);
		configure();
	}

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     * @param configuration содержит конфигурационные параметры для инициализации нового ExternEngine объекта
     * @see ru.skbkontur.sdk.extern.Configuration 
     */
	public ExternEngine(Configuration configuration) {
		env = new Environment();
		env.configuration = configuration;
		configure();
	}

    /**
     * Возвращает окружение среды выполненения
     * @return параметры среды выполнения
     * @see Environment
     */
	public Environment getEnvironment() {
		return env;
	}

    /**
     * Возвращает параметры, использующиеся для конфигурации среды выполнения
     * @return Configuration параметры среды выполненя
     * @see Configuration
     */
	public Configuration getConfiguration() {
		return env.configuration;
	}

    /**
     * Инициализирует новый объект, представляющий сервисы для работы с API Контур Экстерн
     * @param configPath содержит путь к файлу, содержащий конфигурационные параметры
     * @see ru.skbkontur.sdk.extern.Configuration 
     * @throws SDKException непроверяемое исключение может возникнуть при загрузки данных из файла
     * @see ru.skbkontur.sdk.extern.service.SDKException
     */
	public ExternEngine(String configPath) throws SDKException {
		// loads config data from the resourse file: extern-sdk-config.json
		this(loadConfiguration(configPath));
		configure();
	}

    /**
     * Возвращает экземпляр класса AccountService
     * @return AccountService сервис предназначен для работы с учетными записями
     * @see ru.skbkontur.sdk.extern.service.AccountService
     */
	public AccountService getAccountService() {
		return accountService;
	}

    /**
     * Устанавливает экземпляр класса AccountService
     * @param accountService сервис предназначен для работы с учетными записями
     * @see ru.skbkontur.sdk.extern.service.AccountService
     */
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

    /**
     * Возвращает экземпляр класса DraftService
     * @return DraftService сервис предназначен для работы с черновиками
     * @see ru.skbkontur.sdk.extern.service.DraftService
     */
	public DraftService getDraftService() {
		return draftService;
	}

    /**
     * Устанавливает экземпляр класса DraftService
     * @param draftService сервис предназначен для работы с черновиками
     * @see ru.skbkontur.sdk.extern.service.DraftService
     */
	public void setDraftService(DraftService draftService) {
		this.draftService = draftService;
	}

    /**
     * Возвращает экземпляр класса DocflowService
     * @return DocflowService сервис предназначен для работы с документооборотами
     * @see ru.skbkontur.sdk.extern.service.DocflowService
     */
	public DocflowService getDocflowService() {
		return docflowService;
	}

    /**
     * Устанавливает экземпляр класса DocflowService
     * @param docflowService сервис предназначен для работы с документооборотами
     * @see ru.skbkontur.sdk.extern.service.DocflowService
     */
	public void setDocflowService(DocflowService docflowService) {
		this.docflowService = docflowService;
	}

    /**
     * Возвращает экземпляр класса CertificateService
     * @return CertificateService сервис предназначен для работы с сертификатами пользователей
     * @see ru.skbkontur.sdk.extern.service.CertificateService
     */
	public CertificateService getCertificateService() {
		return certificateService;
	}

    /**
     * Устанавливает экземпляр класса CertificateService
     * @param certificateService сервис предназначен для работы с сертификатами пользователей
     * @see ru.skbkontur.sdk.extern.service.CertificateService
     */
	public void setCertificateService(CertificateService certificateService) {
		this.certificateService = certificateService;
	}

    /**
     * Возвращает экземпляр класса EventService
     * @return EventService сервис предназначен для получения ленты соббытий документооборота
     * @see ru.skbkontur.sdk.extern.service.EventService
     */
	public EventService getEventService() {
		return eventService;
	}
    
    /**
     * Устанавливает экземпляр класса EventService
     * @param eventService сервис предназначен для получения ленты соббытий документооборота
     * @see ru.skbkontur.sdk.extern.service.EventService
     */
	public void setEventService(EventService eventService) {
		this.eventService = eventService;
	}
    
    /**
     * Возвращает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     * @return serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider
     */
	public ServiceBaseUriProvider getServiceBaseUriProvider() {
		return serviceBaseUriProvider;
	}

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ServiceBaseUriProvider
     * @param serviceBaseUriProvider предназначен получения адреса сетевого сервиса Контур Экстерн
     * @see ru.skbkontur.sdk.extern.providers.ServiceBaseUriProvider
     */
	public void setServiceBaseUriProvider(ServiceBaseUriProvider serviceBaseUriProvider) {
		this.serviceBaseUriProvider = serviceBaseUriProvider;
	}

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AuthenticationProvider
     * @return AuthenticationProvider предназначен для получения токена аутентификации
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
	public AuthenticationProvider getAuthenticationProvider() {
		return authenticationProvider.getOriginAuthenticationProvider();
	}

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AuthenticationProvider
     * @param authenticationProvider предназначен для получения токена аутентификации
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
	public void setAuthenticationProvider(AuthenticationProvider authenticationProvider) {
		if (authenticationProvider != null) {
			authenticationProvider.addAuthenticationListener(this);
			this.authenticationProvider = new EngineAuthenticationProvider(authenticationProvider);
		}
		else if (this.authenticationProvider != null) {
			AuthenticationProvider originAuthenticationProvider = this.authenticationProvider.getOriginAuthenticationProvider();
			originAuthenticationProvider.removeAuthenticationListener(this);
		}
	}

    /**
     * Возвращает экземпляр класса, реализующий интерфейс AccountProvider
     * @return AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
	public AccountProvider getAccountProvider() {
		return accountProvider;
	}

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс AccountProvider
     * @param accountProvider AccountProvider предназначен для получения учетной записи пользователя
     * @see ru.skbkontur.sdk.extern.providers.AuthenticationProvider
     */
	public void setAccountProvider(AccountProvider accountProvider) {
		this.accountProvider = accountProvider;
	}

    /**
     * Возвращает экземпляр класса, реализующий интерфейс ApiKeyProvider
     * @return ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.skbkontur.sdk.extern.providers.ApiKeyProvider
     */
	public ApiKeyProvider getApiKeyProvider() {
		return apiKeyProvider;
	}

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс ApiKeyProvider
     * @param apiKeyProvider ApiKeyProvider предназначен для получения идентификатора внешнего сервиса
     * @see ru.skbkontur.sdk.extern.providers.ApiKeyProvider
     */
	public void setApiKeyProvider(ApiKeyProvider apiKeyProvider) {
		this.apiKeyProvider = apiKeyProvider;
	}

    /**
     * Возвращает экземпляр класса, реализующий интерфейс CryptoProvider
     * @return CryptoProvider предназначен выполнения криптографических операций
     * @see ru.skbkontur.sdk.extern.providers.CryptoProvider
     * @throws SDKException необрабатываемое исключение генерится в том случае, если криптопровайдер отсутствует
     */
	public CryptoProvider getCryptoProvider() throws SDKException {
		if (cryptoProvider == null) {
			throw new SDKException(Messages.get(C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER));
		}
		return cryptoProvider;
	}

    /**
     * Устанавливает экземпляр класса, реализующий интерфейс CryptoProvider
     * @param cryptoProvider CryptoProvider предназначен выполнения криптографических операций
     * @return CryptoProvider
     * @see ru.skbkontur.sdk.extern.providers.CryptoProvider
     */
	public CryptoProvider setCryptoProvider(CryptoProvider cryptoProvider) {
		CryptoProvider current = this.cryptoProvider;
		this.cryptoProvider = cryptoProvider;
		return current;
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
     * @param authEvent событие, которое передается в данный метод
     * @see ru.skbkontur.sdk.extern.event.AuthenticationListener
     */
	@Override
	public synchronized void authenticate(AuthenticationEvent authEvent) {
		env.accessToken = authEvent.getAuthCxt().isSuccess() ? authEvent.getAuthCxt().get() : null;
	}

	class EngineAuthenticationProvider implements AuthenticationProvider {

		private final AuthenticationProvider authenticationProvider;

		private EngineAuthenticationProvider(AuthenticationProvider authenticationProvider) {
			this.authenticationProvider = authenticationProvider;
		}

		public AuthenticationProvider getOriginAuthenticationProvider() {
			return authenticationProvider;
		}

		@Override
		public QueryContext<String> sessionId() {
			if (env.accessToken == null) {
				return authenticationProvider.sessionId();
			}
			else {
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
