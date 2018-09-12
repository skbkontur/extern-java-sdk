/*
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
 *
 */
package ru.kontur.extern_api.sdk.adaptor;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import javax.net.ssl.HttpsURLConnection;

import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.ServiceError.ErrorCode;
import ru.kontur.extern_api.sdk.ServiceException;
import ru.kontur.extern_api.sdk.model.*;
import ru.kontur.extern_api.sdk.provider.AccountProvider;
import ru.kontur.extern_api.sdk.provider.ApiKeyProvider;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.provider.UriProvider;

/**
 * {@code QueryContext} класс предоставляет контекст функционального интерфейса.
 * <p>Предназначен для:</p>
 * <p>- установки входных параметров для операций;</p>
 * <p>- установки результата операции;</p>
 * <p>- установки ошибки;</p>
 * <p>- передачи параметров операции от от одного контекста другому;</p>
 * <p>- установки правайдеров: аутентификации, адреса сервиса и апи-кей;</p>
 * <p>- выполнения операций в синхронном и асинхронном режиме. Для этого опреация должна
 * удовлетворять функциональному интерфейсу {@code Query<R>}</p>
 *
 * <pre>Перечисленные свойства контекста позволяют строить цепочку выполнения операций с
 * использованием парадигмы FORK &amp; JOIN.</pre>
 *
 * @param <R> тип возвращаемого результата операции
 * @author Aleksey Sukhorukov
 * @since 1.2
 */
public class QueryContext<R> implements Serializable {

    private static final Map<String, List<String>> EMPTY_MAP = new HashMap<>();

    private static final long serialVersionUID = -2919303896965835578L;

    /**
     * Токен аутентификации
     */
    public static final String SESSION_ID = "sessionId";
    /**
     * Префикс токена аутентификации
     */
    public static final String AUTH_PREFIX = "authPrefix";
    /**
     * Наименование сущности. Может быть использовано для логичесокой связи между параметрами.
     * Например, ENTITY_NAME может указывать на сущность, для которой контекст значение
     * идентификатора ENTITY_ID.
     */
    public static final String ENTITY_NAME = "entityName";
    /**
     * Идентификатор сущности
     */
    public static final String ENTITY_ID = "entityId";
    /**
     * Идентификатор черновика
     */
    public static final String DRAFT_ID = "draftId";
    /**
     * Объект черновик {@link Draft}
     */
    public static final String DRAFT = "draft";
    /**
     * Объект документ черновика {@link DraftDocument}
     */
    public static final String DRAFT_DOCUMENT = "draftDocument";
    /**
     * Идентификатор документа
     */
    public static final String DOCUMENT_ID = "documentId";
    /**
     * Объект документ
     */
    public static final String DOCUMENT = "document";
    /**
     * Список документов {@code List<Document>}
     */
    public static final String DOCUMENTS = "documents";
    /**
     * Идентификатор документооборота (ДО)
     */
    public static final String DOCFLOW_ID = "docflowId";
    /**
     * Объект {@link Docflow}
     */
    public static final String DOCFLOW = "docflow";
    /**
     * Список документооборотов {@code List<Docflow>}
     */
    public static final String DOCFLOWS = "docflows";
    /**
     * Тип документа
     */
    public static final String DOCUMENT_TYPE = "documentType";
    /**
     * Идентификатор ответа
     */
    public static final String REPLY_ID = "replyId";
    /**
     * Объект для отправки документа @see DocumentToSend
     */
    public static final String DOCUMENT_TO_SEND = "documentToSend";
    /**
     * Объект, содержащий IP адрес отправителя
     */
    public static final String SENDER_IP = "senderIP";
    /**
     * Список объектов для отправки документов ({@code List<DocumentToSend>})
     */
    public static final String DOCUMENT_TO_SENDS = "documentToSends";
    /**
     * Объект "Описание черновика" {@link DraftMeta}
     */
    public static final String DRAFT_META = "draftMeta";
    /**
     * Режим задержки (отсрочки)
     */
    public static final String DEFFERED = "deffered";
    /**
     * ускоренный режим
     */
    public static final String FORCE = "force";
    // public static final String CONTENT_BYTES = "contentBytes";
    /**
     * Произвольная строка
     */
    public static final String CONTENT_STRING = "contentString";
    /**
     * Имя файла
     */
    public static final String FILE_NAME = "fileName";
    /**
     * Объект "Содержимое документа" {@link DocumentContents}
     */
    public static final String DOCUMENT_CONTENTS = "documentContents";
    /**
     * Массив байт
     */
    public static final String CONTENT = "content";
    /**
     * Коллекция типа Map {@link Map}
     */
    public static final String MAP = "map";
    /**
     * Объект {@link ru.kontur.extern_api.sdk.model.PrepareResult}
     */
    public static final String PREPARE_RESULT = "prepareResult";
    /**
     * Объект {@link ru.kontur.extern_api.sdk.model.CheckResultData}
     */
    public static final String CHECK_RESULT_DATA = "checkResultData";
    /**
     * Объект "Дескриптор документа" {@link DocumentDescription}
     */
    public static final String DOCUMENT_DESCRIPTION = "documentDescription";
    /**
     * Идентификатор подписи
     */
    public static final String SIGNATURE_ID = "signatureId";
    /**
     * Объект "Подпис" {@link Signature}
     */
    public static final String SIGNATURE = "signature";
    /**
     * Список подписией ({@code List<Signature>})
     */
    public static final String SIGNATURES = "signatures";
    /**
     * Отпечаток сертификата - хеш SHA-1
     */
    public static final String THUMBPRINT = "thumbprint";
    /**
     * Список ссылок ({@code List<Link>}) {@link Link}
     */
    public static final String LINKS = "links";
    /**
     * Идентификатор учетной записи
     */
    public static final String ACCOUNT_ID = "accountId";
    /**
     * Объект "Учетная запись" {@link Account}
     */
    public static final String ACCOUNT = "account";
    /**
     * Список учетных записей ({@code List<Account>})
     */
    public static final String ACCOUNT_LIST = "accountList";
    /**
     * Объект для постраничного извлечения списка документооборотов {@link
     * ru.kontur.extern_api.sdk.model.DocflowPage}
     */
    public static final String DOCFLOW_PAGE = "docflowPage";
    /**
     * Объект для создания учетной записи для организации {@link CreateAccountRequest}
     */
    public static final String CREATE_ACCOUNT_REQUEST = "createAccountRequest";
    /**
     * Признак завершенности документооборота
     */
    public static final String FINISHED = "finished";
    /**
     * Признак документооборотов инициализируемые контролирующими органами
     */
    public static final String INCOMING = "incoming";
    /**
     * Смещение от начала списка для постраничного чтения
     */
    public static final String SKIP = "skip";
    /**
     * Максимальное количество считываемых записей для постраничного чтения
     */
    public static final String TAKE = "take";
    /**
     * ИНН+КПП
     */
    public static final String INN_KPP = "innKpp";
    /**
     * ИНН
     */
    public static final String INN = "inn";
    /**
     * КПП
     */
    public static final String KPP = "kpp";
    /**
     * Дата начала периода для обновления документооборотов
     */
    public static final String UPDATED_FROM = "updatedFrom";
    /**
     * Дата окончания периода для обновления документооборотов
     */
    public static final String UPDATED_TO = "updatedTo";
    /**
     * Дата начала периода создания документооборотов
     */
    public static final String CREATED_FROM = "createdFrom";
    /**
     * Дата окончания периода создания документооборотов
     */
    public static final String CREATED_TO = "createdTo";
    /**
     * Тип
     */
    public static final String TYPE = "type";
    /**
     * Объект сертификат {@link ru.kontur.extern_api.sdk.model.Certificate}
     */
    public static final String CERTIFICATE = "certificate";
    /**
     * Объект список сертификатов {@link CertificateList} для постраничного извлечения сертификтов
     */
    public static final String CERTIFICATE_LIST = "certificateList";
    /**
     * Объект содержащий информацию для создания УСН декларации на сервисе. Объект включает в себя
     * JSON с данными декларации. {@link UsnServiceContractInfo}
     */
    public static final String USN_SERVICE_CONTRACT_INFO = "usnServiceContractInfo";
    /**
     * Объект содержащий информацию для создания УСН декларации на сервисе. Объект включает в себя
     * объектную модель с данными декларации. {@link UsnServiceContractInfo}
     */
    public static final String USN_SERVICE_CONTRACT_INFO_V2 = "usnServiceContractInfoV2";
    /**
     * Идентификатор первой учетной записи, с которой необходимо произвести загрузку списока
     */
    public static final String FROM_ID = "fromId";
    /**
     * Содержит размер списка объектов
     */
    public static final String SIZE = "size";
    /**
     * Объект "Страница событий" {@link EventsPage}
     */
    public static final String EVENTS_PAGE = "eventsPage";
    /**
     * Признак отсутствия выходных данных для операции {@link Void}
     */
    public static final String NOTHING = "nothing";
    /**
     * Объект "Простой объект" {@link Object}
     */
    public static final String OBJECT = "object";
    /**
     * Версия
     */
    public static final String VERSION = "version";
    /**
     * Идетификатор организации
     */
    public static final String COMPANY_ID = "companyId";
    /**
     * Объект "Организация". Возвращается сервисом и содержит идентификатор организации. {@link
     * Company}
     */
    public static final String COMPANY = "company";
    /**
     * Объект "Организация". Передается сервису для создания новой организации. Не содержит
     * идентификатор. {@link CompanyGeneral}
     */
    public static final String COMPANY_GENERAL = "companyGeneral";
    /**
     * Имя
     */
    public static final String NAME = "name";
    /**
     * Объект "Страница списка организации". {@link ru.kontur.extern_api.sdk.model.CompanyBatch}
     */
    public static final String COMPANY_BATCH = "companyBatch";
    /**
     * Идентификатор запроса на облачную подпись
     */
    public static final String REQUEST_ID = "requestId";
    /**
     * SMS-код подтврждения
     */
    public static final String SMS_CODE = "code";
    /**
     * IP адрес отправителя. Требуется для ФНС.
     */
    public static final String USER_IP = "userIP";
    /**
     * Объект, содержащий информацию об ответном документе
     */
    public static final String REPLY_DOCUMENT = "replyDocument";

    /**
     * Объект, содержащий информацию об ответных документах
     */
    public static final String REPLY_DOCUMENTS = "replyDocuments";

    private final Map<String, Object> params;

    private String result;

    private ServiceError serviceError;

    private UriProvider serviceBaseUriProvider;

    private AuthenticationProvider authenticationProvider;

    private AccountProvider accountProvider;

    private ApiKeyProvider apiKeyProvider;

    /**
     * Крнструктор для создания контекста
     */
    public QueryContext() {
        this.params = new ConcurrentHashMap<>();
        this.serviceError = null;
        this.result = null;
    }

    /**
     * Конструктор для создания контекста
     *
     * @param entityName наименования сущности, для которой создается контекст
     */
    public QueryContext(String entityName) {
        this();
        this.setEntityName(entityName);
    }

    /**
     * Конструктор для создания контекста
     *
     * @param parent     контекст, из которого копируются параметры для создаваемого контекста
     * @param entityName наименования сущности, для которой создается контекст
     */
    public QueryContext(QueryContext<?> parent, String entityName) {
        this();
        this.params.putAll(parent.params);
        this.serviceError = parent.getServiceError();
        this.result = null;
        this.setEntityName(entityName);
        this.authenticationProvider = parent.authenticationProvider;
        this.accountProvider = parent.accountProvider;
        this.apiKeyProvider = parent.apiKeyProvider;
        this.serviceBaseUriProvider = parent.serviceBaseUriProvider;
    }

    /**
     * Метод устанавливает результат операции, который можно получить с помощью метода {@link
     * QueryContext#get()}
     *
     * @param result результат операции
     * @param key    наименование параметра
     * @return контекст
     */
    public QueryContext<R> setResult(R result, String key) {
        this.result = key;
        this.serviceError = null;
        return key.equals(NOTHING) ? this : set(key, result);
    }

    /**
     * Метод возвращает ошибку операции. Если метод вернул значение не null, то метод {@link
     * QueryContext#isFail()} вернет true, иначе метод {@link QueryContext#isSuccess()} ()} вернет
     * true.
     *
     * @return объект {@link ServiceError}
     */
    public ServiceError getServiceError() {
        return serviceError;
    }

    /**
     * Метод устанавливает сообщение об ошибке. Метод {@link QueryContext#getServiceError()} вернет
     * ошибку со значением кода {@link ErrorCode#business}
     *
     * @param message сообщение об ошибке
     * @return контекст
     */
    public QueryContext<R> setServiceError(String message) {
        return setServiceError(ErrorCode.business, message, 0, EMPTY_MAP, null, null);
    }

    /**
     * Метод устанавливает значение ошибки из переданного контекста. Если контекст содержит ошибку
     * авторизации (401), то значение токена авторизации (SID) будет удалено из контекста.
     *
     * @param queryContext контекст, ошибка которого будет сохранена
     * @return контекст
     */
    public QueryContext<R> setServiceError(QueryContext<?> queryContext) {
        this.result = null;
        this.serviceError = queryContext.serviceError;
        if (serviceError.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            set(SESSION_ID, null);
        }
        return this;
    }

    /**
     * Метод устанавливает ошибку {@link ServiceError} в контекст.
     *
     * @param serviceError ошибка
     * @return контекст
     */
    public QueryContext<R> setServiceError(ServiceError serviceError) {
        this.result = null;
        this.serviceError = serviceError;
        if (serviceError.getResponseCode() == HttpsURLConnection.HTTP_UNAUTHORIZED) {
            set(SESSION_ID, null);
        }
        return this;
    }

    /**
     * Метод устанавливает ошибку и дефолтное сообщение по переданному коду ошибки
     *
     * @param errorCode код ошибки {@link ErrorCode}
     * @return контекст
     */
    public QueryContext<R> setServiceError(ServiceError.ErrorCode errorCode) {
        return setServiceError(errorCode, errorCode.message(), 0, EMPTY_MAP, null, null);
    }

    /**
     * Метод устанавливает значение ошибки, которое вернул сервис, при этом ErrorCode примет
     * значение ErrorCode.server
     *
     * @param x ошибка сервера (4XX, 5XX)
     * @return контекст
     */
    public QueryContext<R> setServiceError(ApiException x) {
        return setServiceError(ErrorCode.server, x.getMessage(), x.getCode(),
                x.getResponseHeaders(), x.getResponseBody(), x.getCause());
    }

    /**
     * Метод устанавливает значение ошибки
     *
     * @param errorCode       код ошибки {@link ErrorCode}
     * @param message         сообщение об ошибки
     * @param code            HTTP код ошибки
     * @param responseHeaders коллекция HTTP заголовков (ключ, значения)
     * @param responseBody    ответ сервера
     * @param thrown          эксепшин, приведший к ошибке
     * @return контекст
     */
    public QueryContext<R> setServiceError(ServiceError.ErrorCode errorCode, String message,
                                           int code, Map<String, List<String>> responseHeaders, String responseBody,
                                           Throwable thrown) {
        return setServiceError(
                new ServiceError() {
                    @Override
                    public ErrorCode getErrorCode() {
                        return errorCode;
                    }

                    @Override
                    public int getResponseCode() {
                        return code;
                    }

                    @Override
                    public String getMessage() {
                        return message;
                    }

                    @Override
                    public Map<String, List<String>> getResponseHeaders() {
                        return responseHeaders;
                    }

                    @Override
                    public String getResponseBody() {
                        return responseBody;
                    }

                    @Override
                    public Throwable getCause() {
                        return thrown;
                    }

                    @Override
                    public String toString() {
                        return prettyErrorPrint(this);
                    }
                }
        );
    }

    /**
     * Метод устанавливает значение ошибки. При этом ErrorCode примет значение ErrorCode.business
     *
     * @param message сообщение с ошибкой
     * @param x       эксепшин, приведший к ошибке
     * @return контекст
     */
    public QueryContext<R> setServiceError(String message, Throwable x) {
        return setServiceError(ErrorCode.business, message, 0, EMPTY_MAP, null, x);
    }

    /**
     * Метод возвращает результат операции
     *
     * @return результат операции
     */
    public R get() {
        if (result == null) {
            return null;
        }
        return get(result);
    }

    /**
     * Метод возвращает true, если ошибка в контекст не была установлена, иначе - false
     *
     * @return true, если ошибка в контекст не была установлена, иначе - false
     */
    public boolean isSuccess() {
        return serviceError == null;
    }

    /**
     * Метод возвращает true, если ошибка в контекст была установлена, иначе - false
     *
     * @return true, если ошибка в контекст была установлена, иначе - false
     */
    public boolean isFail() {
        return serviceError != null;
    }


    /**
     * Возвращает функцию типа {@link UriProvider}. Данная функция возвращает адрес сервиса в
     * Интернет. Если значение не было установлено, то метод вернет функцию, которая вернет пустую
     * строку.
     *
     * @return функция, возвращающая адрес сервиса
     */
    public UriProvider getServiceBaseUriProvider() {
        return serviceBaseUriProvider == null ? () -> "" : serviceBaseUriProvider;
    }

    /**
     * Метод устанавливает функцию типа {@link UriProvider}, возвращающую адрес сервиса
     *
     * @param serviceBaseUriProvider функция, возвращающая адрес сервиса
     * @return контекст
     */
    public QueryContext<R> setServiceBaseUriProvider(@NotNull UriProvider serviceBaseUriProvider) {
        this.serviceBaseUriProvider = serviceBaseUriProvider;
        return this;
    }

    /**
     * Метод возвращает провайдер для аутентификации {@link AuthenticationProvider}
     *
     * @return провайдер для аутентификации {@link AuthenticationProvider}
     */
    public AuthenticationProvider getAuthenticationProvider() {
        return authenticationProvider;
    }

    /**
     * Метод устанавливает провайдер аутентификации {@link AuthenticationProvider} в контекст.
     * Существует несколько типов аутентификации: - аутентификация по логину и паролю; -
     * аутентификация по сертификату; - доверитеоьная аутентификация.
     *
     * @param authenticationProvider провайдер аутентификации
     * @return контекст
     */
    public QueryContext<R> setAuthenticationProvider(
            @NotNull AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
        return this;
    }

    /**
     * Метод возвращает провайдер идентификатора учетной записи {@link AccountProvider}
     *
     * @return провайдер идентификатора учетной записи
     */
    public AccountProvider getAccountProvider() {
        return accountProvider;
    }

    /**
     * Метод устанавливает провайдер идентификатора учетной записи
     *
     * @param accountProvider провайдер идентификатора учетной записи
     * @return контекст
     */
    public QueryContext<R> setAccountProvider(@NotNull AccountProvider accountProvider) {
        this.accountProvider = accountProvider;
        return this;
    }

    /**
     * Метод возвращает провайдер идентификатора внешнего сервиса {@link ApiKeyProvider}
     *
     * @return провайдер идентификатора внешнего сервиса
     */
    public ApiKeyProvider getApiKeyProvider() {
        return apiKeyProvider == null ? () -> "" : apiKeyProvider;
    }

    /**
     * Метод устанавливает правайдер идентификатора внешнего сервиса {@link ApiKeyProvider} в
     * контекст
     *
     * @param apiKeyProvider правайдер идентификатора внешнего сервиса
     * @return контекст
     */
    public QueryContext<R> setApiKeyProvider(@NotNull ApiKeyProvider apiKeyProvider) {
        this.apiKeyProvider = apiKeyProvider;
        return this;
    }

    /**
     * Метод возвращает идентификатор сессии (SID)
     *
     * @return идентификатор сессии (SID)
     */
    public String getSessionId() {
        return (String) params.get(SESSION_ID);
    }

    /**
     * Метод устанавливает идентификатор сессии (SID)
     *
     * @param sessionId идентификатор сессии
     * @return контекст
     */
    public QueryContext<R> setSessionId(String sessionId) {
        return set(SESSION_ID, sessionId);
    }

    /**
     * Метод устанавливает идентификатор сессии (SID), установленный в переданный контекст
     *
     * @param queryContext контекст, с установленным идентификатором сессии
     * @return контекст
     */
    public QueryContext<R> setSessionId(QueryContext<?> queryContext) {
        return set(SESSION_ID, UUID.fromString(queryContext.getSessionId()));
    }

    /**
     * Метод возвращает префикс идентификатора сессии (SID)
     *
     * @return префикс идентификатора сессии
     */
    public String getAuthPrefix() {
        return (String) params.get(AUTH_PREFIX);
    }

    /**
     * Метод устанавливает префикс идентификатора сессии (SID)
     *
     * @param authPrefix префикс идентификатора сессии
     * @return контекст
     */
    public QueryContext<R> setAuthPrefix(String authPrefix) {
        return set(AUTH_PREFIX, authPrefix);
    }

    /**
     * Метод возвращает имя сущности
     *
     * @return имя сущности
     */
    public String getEntityName() {
        return (String) params.get(ENTITY_NAME);
    }

    /**
     * Метод устанавливает имя сущности
     *
     * @param entityName имя сущности
     * @return контекст
     */
    public final QueryContext<R> setEntityName(String entityName) {
        return set(ENTITY_NAME, entityName);
    }

    /**
     * Метод возвращает идентификатор черновика
     *
     * @return идентификатор черновика
     */
    public UUID getDraftId() {
        return (UUID) params.get(DRAFT_ID);
    }

    /**
     * Метод устанавливает идентификатор черновика
     *
     * @param draftId идентификатор черновика
     * @return контекст
     */
    public QueryContext<R> setDraftId(String draftId) {
        return set(DRAFT_ID, UUID.fromString(draftId));
    }

    /**
     * Метод устанавливает идентификатор черновика
     *
     * @param draftId идентификатор черновика
     * @return контекст
     */
    public QueryContext<R> setDraftId(UUID draftId) {
        return set(DRAFT_ID, draftId);
    }

    /**
     * Метод возвращает объект черновик {@link Draft}
     *
     * @return объект черновик
     */
    public Draft getDraft() {
        return (Draft) params.get(DRAFT);
    }

    /**
     * Метод устанавливает объект черновик {@link Draft}
     *
     * @param draft объект черновик
     * @return контекст
     */
    public QueryContext<R> setDraft(Draft draft) {
        return set(DRAFT, draft);
    }

    /**
     * Метод возвращает объект документооборот {@link Docflow}
     *
     * @return объект документооборот
     */
    public Docflow getDocflow() {
        return (Docflow) params.get(DOCFLOW);
    }

    /**
     * Метод устанавливает объект документооборот {@link Docflow}
     *
     * @param docflow объект документооборот
     * @return контекст
     */
    public QueryContext<R> setDocflow(Docflow docflow) {
        return set(DOCFLOW, docflow);
    }

    /**
     * Метод устанавливает список документооборотов {@link Docflow}
     *
     * @param docflows {@code List<Docflow>} список документооборотов
     * @return контекст
     */
    public QueryContext<R> setDocflows(List<Docflow> docflows) {
        return set(DOCFLOWS, docflows);
    }

    /**
     * Метод возвращает список документооборотов {@link Docflow}
     *
     * @return {@code List<Docflow>} список документооборот
     */
    @SuppressWarnings("unchecked")
    public List<Docflow> getDocflows() {
        return (List<Docflow>) params.get(DOCFLOWS);
    }

    /**
     * Метод возвращает идентификатор документооборота
     *
     * @return идентификатор документооборота
     */
    public UUID getDocflowId() {
        UUID docflowId = (UUID) params.get(DOCFLOW_ID);
        if (docflowId == null) {
            Docflow docflow = this.getDocflow();
            if (docflow != null) {
                docflowId = docflow.getId();
            }
        }
        return docflowId;
    }

    /**
     * Метод устанавливает идентификатор документооборота
     *
     * @param docflowId идентификатор документооборота
     * @return контекст
     */
    public QueryContext<R> setDocflowId(UUID docflowId) {
        return set(DOCFLOW_ID, docflowId);
    }

    /**
     * Метод устанавливает идентификатор документооборота
     *
     * @param docflowId идентификатор документооборота
     * @return контекст
     */
    public QueryContext<R> setDocflowId(String docflowId) {
        return set(DOCFLOW_ID, UUID.fromString(docflowId));
    }

    /**
     * Метод возвращает идентификатор документооборота
     *
     * @return идентификатор документооборота
     */
    public UUID getDocumentId() {
        return (UUID) params.get(DOCUMENT_ID);
    }

    /**
     * Метод устанавливает идентификатор документа
     *
     * @param documentId идентификатор документа
     * @return контекст
     */
    public QueryContext<R> setDocumentId(UUID documentId) {
        return set(DOCUMENT_ID, documentId);
    }

    /**
     * Метод устанавливает идентификатор документа
     *
     * @param documentId идентификатор документа
     * @return контекст
     */
    public QueryContext<R> setDocumentId(String documentId) {
        return set(DOCUMENT_ID, UUID.fromString(documentId));
    }

    /**
     * Метод возвращает объект документ {@link Document}
     *
     * @return идентификатор документооборота
     */
    public Document getDocument() {
        return (Document) params.get(DOCUMENT);
    }

    /**
     * Метод устанавливает список документов {@link Document}
     *
     * @param documents список документов
     * @return контекст
     */
    public QueryContext<R> setDocuments(List<Document> documents) {
        return set(DOCUMENTS, documents);
    }

    /**
     * Метод устанавливает объект документ {@link Document}
     *
     * @param document объект документов
     * @return контекст
     */
    public QueryContext<R> setDocument(Document document) {
        return set(DOCUMENT, document);
    }

    /**
     * Метод возвращает список документов {@link Document}
     *
     * @return список документов
     */
    @SuppressWarnings("unchecked")
    public List<Document> getDocuments() {
        return (List<Document>) params.get(DOCUMENTS);
    }

    /**
     * Иетод возвращает дескриптор документа {@link DocumentDescription}
     *
     * @return дескриптор документа
     */
    public DocumentDescription getDocumentDescription() {
        return (DocumentDescription) params.get(DOCUMENT_DESCRIPTION);
    }

    /**
     * Метод устанавливает дескриптор документа {@link DocumentDescription}
     *
     * @param documentDescription дескриптор документа
     * @return контекст
     */
    public QueryContext<R> setDocumentDescription(DocumentDescription documentDescription) {
        return set(DOCUMENT_DESCRIPTION, documentDescription);
    }

    /**
     * Метод возвращает тип документа
     *
     * @return тип документа
     */
    public String getDocumentType() {
        return (String) params.get(DOCUMENT_TYPE);
    }

    /**
     * Метод устанавливает тип документа
     *
     * @param documentType тип документа
     * @return контекст
     */
    public QueryContext<R> setDocumentType(String documentType) {
        return set(DOCUMENT_TYPE, documentType);
    }

    /**
     * Метод возвращает идентификатор ответа
     *
     * @return идентификатор ответа
     */
    public UUID getReplyId() {
        return (UUID) params.get(REPLY_ID);
    }

    /**
     * Метод устанавливает идентификатор ответа
     *
     * @param replyId идентификатор ответа
     * @return контекст
     */
    public QueryContext<R> setReplyId(String replyId) {
        return set(REPLY_ID, UUID.fromString(replyId));
    }

    /**
     * Метод возвращает объект документ для отправки {@link DocumentToSend}
     *
     * @return объект документ для отправки
     */
    public DocumentToSend getDocumentToSend() {
        return (DocumentToSend) params.get(DOCUMENT_TO_SEND);
    }

    /**
     * Метод устанавливает документ для отправки {@link DocumentToSend}
     *
     * @param documentToSend документ для отправки
     * @return контекст
     */
    public QueryContext<R> setDocumentToSend(DocumentToSend documentToSend) {
        return set(DOCUMENT_TO_SEND, documentToSend);
    }

    /**
     * Метод возвращает объект, содержащий IP адрес отправителя {@link SenderIP}
     * @return IP адрес отправителя
     */
    public SenderIP getSenderIP() {
        return (SenderIP)params.get(SENDER_IP);
    }

    /**
     * Метод устанавливает объект, содержащий IP адрес отправителя {@link SenderIP}
     * @param senderIP объект, содержащий IP адрес отправителя
     * @return контекст
     */
    public QueryContext<R> setSenderIP(SenderIP senderIP) {
        return set(SENDER_IP, senderIP);
    }

    /**
     * Метод возвращает объект метаданные черновика {@link DraftMeta}
     *
     * @return метаданные черновика
     */
    public DraftMeta getDraftMeta() {
        return (DraftMeta) params.get(DRAFT_META);
    }

    /**
     * Метод устанавливает объект метаданные черновика {@link DraftMeta}
     *
     * @param draftMeta объект метаданные черновика
     * @return контекст
     */
    public QueryContext<R> setDraftMeta(DraftMeta draftMeta) {
        return set(DRAFT_META, draftMeta);
    }

    /**
     * Метод возвращает режим задержки (отсрочки)
     *
     * @return режим задержки (отсрочки)
     */
    public boolean getDeffered() {
        Object v = params.get(DEFFERED);
        return v == null || (boolean) v;
    }

    /**
     * Метод устанавливает режим задержки (отсрочки)
     *
     * @param deffered режим задержки (отсрочки)
     * @return контекст
     */
    public QueryContext<R> setDeffered(boolean deffered) {
        return set(DEFFERED, deffered);
    }

    /**
     * Метод возвращает признак ускоренного режима
     *
     * @return признак ускоренного режима
     */
    public boolean getForce() {
        Object v = params.get(FORCE);
        return v == null || (boolean) v;
    }

    /**
     * Метод устанавливает признак ускоренного режима
     *
     * @param force признак ускоренного режима
     * @return контекст
     */
    public QueryContext<R> setForce(boolean force) {
        return set(FORCE, force);
    }
/*
    public byte[] getContentBytes() {
        return (byte[]) params.get(CONTENT_BYTES);
    }

    public QueryContext<R> setContentBytes(byte[] documentContent) {
        return set(CONTENT_BYTES, documentContent);
    }
*/

    /**
     * Метод возвращает значение - строку
     *
     * @return значение - строку
     */
    public String getContentString() {
        return (String) params.get(CONTENT_STRING);
    }

    /**
     * Метод устанавливает значение - строку
     *
     * @param contentString значение - строка
     * @return контекст
     */
    public QueryContext<R> setContentString(String contentString) {
        return set(CONTENT_STRING, contentString);
    }

    /**
     * Метод возвращает имя файла
     *
     * @return имя файла
     */
    public String getFileName() {
        return (String) params.get(FILE_NAME);
    }

    /**
     * Метод устанавливает имя файла
     *
     * @param fileName имя файла
     * @return контекст
     */
    public QueryContext<R> setFileName(String fileName) {
        return set(FILE_NAME, fileName);
    }

    /**
     * Метод возвращает объект Контекст документа {@link DocumentContents}
     *
     * @return возвращает объект Контекст документа
     */
    public DocumentContents getDocumentContents() {
        return (DocumentContents) params.get(DOCUMENT_CONTENTS);
    }

    /**
     * Метол устанавливает объект Контекст документа
     *
     * @param documentContents объект Контекст документа
     * @return контекст
     */
    public QueryContext<R> setDocumentContents(DocumentContents documentContents) {
        return set(DOCUMENT_CONTENTS, documentContents);
    }

    /**
     * Метод возвращает массив байт
     *
     * @return массив байт
     */
    public byte[] getContent() {
        return (byte[]) params.get(CONTENT);
    }

    /**
     * Метод устанавливает массив байт
     *
     * @param content массив байт
     * @return контекст
     */
    public QueryContext<R> setContent(byte[] content) {
        return set(CONTENT, content);
    }

    /**
     * Метод возвращает ссылку
     *
     * @return массив байт
     */
    public String getContentUrl() {
        return (String) params.get(CONTENT);
    }

    /**
     * Метод устанавливает ссылку
     *
     * @param content ссылка
     * @return контекст
     */
    public QueryContext<R> setContentUrl(String content) {
        return set(CONTENT, content);
    }



    /**
     * Метод возвращает объект DraftDocument {@link DraftDocument}
     *
     * @return возвращает объект DraftDocument
     */
    public DraftDocument getDraftDocument() {
        return (DraftDocument) params.get(DRAFT_DOCUMENT);
    }

    /**
     * Метод возвращает идентификатор подписи
     *
     * @return идентификатор подписи
     */
    public UUID getSignatureId() {
        return (UUID) params.get(SIGNATURE_ID);
    }

    /**
     * Метод устанавливает идентификатор подписи
     *
     * @param signatureId идентификатор подписи
     * @return контекст
     */
    public QueryContext<R> setSignatureId(UUID signatureId) {
        return set(SIGNATURE_ID, signatureId);
    }

    /**
     * Метод устанавливает идентификатор подписи
     *
     * @param signatureId идентификатор подписи
     * @return контекст
     */
    public QueryContext<R> setSignatureId(String signatureId) {
        return set(SIGNATURE_ID, UUID.fromString(signatureId));
    }

    /**
     * Метод Возвращает отпечаток сертификата
     *
     * @return отпечаток сертификата
     */
    public String getThumbprint() {
        return (String) params.get(THUMBPRINT);
    }

    /**
     * Метод устанавливает отпечаток сертификата
     *
     * @param thumbprint отпечаток сертификата
     * @return контекст
     */
    public QueryContext<R> setThumbprint(String thumbprint) {
        return set(THUMBPRINT, thumbprint);
    }

    /**
     * Метод возвращает список ссылок {@link Link}
     *
     * @return список ссылок
     */
    @SuppressWarnings("unchecked")
    public List<Link> getLinks() {
        return (List<Link>) params.get(LINKS);
    }

    /**
     * Метод устанавливает список ссылок {@link Link}
     *
     * @param links список ссылок
     * @return контекст
     */
    public QueryContext<R> setLinks(List<Link> links) {
        return set(LINKS, links);
    }

    /**
     * Метод возвращает идентификатор учетной записи
     *
     * @return идентификатор учетной записи
     */
    public UUID getAccountId() {
        return (UUID) params.get(ACCOUNT_ID);
    }

    /**
     * Метод устанавливает идентификатор учетной записи
     *
     * @param accountId идентификатор учетной записи
     * @return контекст
     */
    public QueryContext<R> setAccountId(String accountId) {
        return set(ACCOUNT_ID, UUID.fromString(accountId));
    }

    /**
     * Метод устанавливает идентификатор учетной записи
     *
     * @param accountId идентификатор учетной записи
     * @return контекст
     */
    public QueryContext<R> setAccountId(UUID accountId) {
        return set(ACCOUNT_ID, accountId);
    }

    /**
     * Метод возвращает объект "Учетная запись" {@link Account}
     *
     * @return объект "Учетная запись"
     */
    public Account getAccount() {
        return (Account) params.get(ACCOUNT);
    }

    /**
     * Метод устанавливает объект "Учетная запись" {@link Account}
     *
     * @param account объект "Учетная запись"
     * @return контекст
     */
    public QueryContext<R> setAccount(Account account) {
        return set(ACCOUNT, account);
    }

    /**
     * Метод возвращает объект "Список учетных записей" {@link AccountList}. Включает в себя
     * свойства для постраничной загрузки.
     *
     * @return объект "Список учетных записей"
     */
    public AccountList getAccountList() {
        return (AccountList) params.get(ACCOUNT_LIST);
    }

    /**
     * Метод устанавливает объект "Список учетных записей" {@link AccountList}
     *
     * @param accountList объект "Список учетных записей"
     * @return контекст
     */
    public QueryContext<R> setAccountList(AccountList accountList) {
        return set(ACCOUNT_LIST, accountList);
    }

    /**
     * Метод возвращает объект "Данные новой учетной записи" {@link CreateAccountRequest}. Служит
     * для передачи запросу данных о новой учетной записи
     *
     * @return объект "Данные новой учетной записи"
     */
    public CreateAccountRequest getCreateAccountRequest() {
        return (CreateAccountRequest) params.get(CREATE_ACCOUNT_REQUEST);
    }

    /**
     * Метод устанавливает объект "Данные новой учетной записи" {@link CreateAccountRequest}. Служит
     * для передачи запросу данных о новой учетной записи
     *
     * @param createAccountRequest объект "Данные новой учетной записи"
     * @return контекст
     */
    public QueryContext<R> setCreateAccountRequest(CreateAccountRequest createAccountRequest) {
        return set(CREATE_ACCOUNT_REQUEST, createAccountRequest);
    }

    /**
     * Метод возвращает объект "Подпись" {@link Signature}
     *
     * @return объект "Подпись"
     */
    public Signature getSignature() {
        return (Signature) params.get(SIGNATURE);
    }

    /**
     * Метод устанавливает объект "Подпись" {@link Signature}
     *
     * @param signature объект "Подпись"
     * @return контекст
     */
    public QueryContext<R> setSignature(Signature signature) {
        return set(SIGNATURE, signature);
    }

    /**
     * Метод возвращает список подписей {@link Signature}
     *
     * @return список подписей
     */
    @SuppressWarnings("unchecked")
    public List<Signature> getSignatures() {
        return (List<Signature>) params.get(SIGNATURES);
    }

    /**
     * Метод устанавливает список подписей {@link Signature}
     *
     * @param signatures список подписей
     * @return контекст
     */
    public QueryContext<R> setSignatures(List<Signature> signatures) {
        return set(SIGNATURES, signatures);
    }

    /**
     * Метод возвращает список документов подготовленных к отправке {@link DocumentToSend}
     *
     * @return список документов подготовленных к отправке
     */
    @SuppressWarnings("unchecked")
    public List<DocumentToSend> getDocumentToSends() {
        return (List<DocumentToSend>) params.get(DOCUMENT_TO_SENDS);
    }

    /**
     * Метод устанавливает список документов подготовленных к отправке {@link DocumentToSend}
     *
     * @param replies список документов подготовленных к отправке
     * @return контекст
     */
    public QueryContext<R> setDocumentToSends(List<DocumentToSend> replies) {
        return set(DOCUMENT_TO_SENDS, replies);
    }

    /**
     * Метод возвращает признак завершенности документооборота (ДО). True - ДО завершен, false - не
     * завершен Используется в запросе для извлечения списка документооборотов.
     *
     * @return признак завершенности документооборота
     */
    public Boolean getFinished() {
        return (Boolean) params.get(FINISHED);
    }

    /**
     * Метод возвращает признак завершенности документооборота (ДО). True - ДО завершен, false - не
     * завершен Используется в запросе для извлечения списка документооборотов.
     *
     * @param finished признак завершенности документооборота
     * @return контекст
     */
    public QueryContext<R> setFinished(Boolean finished) {
        return set(FINISHED, finished);
    }

    /**
     * Метод возвращает признак документооборотов (ДО), инициализированные контролирующими органами.
     * True - требуются входящие ДО , false - требуются исходящие ДО Используется в запросе для
     * извлечения списка документооборотов.
     *
     * @return признак документооборотов (ДО), инициализированные контролирующими органами
     */
    public Boolean getIncoming() {
        return (Boolean) params.get(INCOMING);
    }

    /**
     * Метод устанавливает признак документооборотов (ДО), инициализированные контролирующими
     * органами. True - требуются входящие ДО , false - требуются исходящие ДО Используется в
     * запросе для извлечения списка документооборотов.
     *
     * @param incoming признак наличия входящих документов документооборота
     * @return контекст
     */
    public QueryContext<R> setIncoming(Boolean incoming) {
        return set(INCOMING, incoming);
    }

    /**
     * Метод возвращает порядковый номер первой записи от начала упорядоченного списка в
     * возвращаемой колекции. Необходим для организации постраничной загрузки.
     *
     * @return порядковый номер первой записи от начала списка в возвращаемой колекции
     */
    public Long getSkip() {
        return (Long) params.get(SKIP);
    }

    /**
     * Метод возвращает порядковый номер первой записи от начала упорядоченного списка в
     * возвращаемой колекции. Необходим для организации постраничной загрузки.
     *
     * @param skip порядковый номер первой записи от начала списка в возвращаемой колекции
     * @return контекст
     */
    public QueryContext<R> setSkip(Long skip) {
        return set(SKIP, skip);
    }

    /**
     * Метод возвращает максимальное количество записей в возвращаемой коллекции. Необходим для
     * организации постраничной загрузки.
     *
     * @return максимальное количество записей в возвращаемой коллекции
     */
    public Integer getTake() {
        return (Integer) params.get(TAKE);
    }

    /**
     * Метод устанавливает максимальное количество записей в возвращаемой коллекции. Необходим для
     * организации постраничной загрузки.
     *
     * @param take максимальное количество записей в возвращаемой коллекции
     * @return контекст
     */
    public QueryContext<R> setTake(Integer take) {
        return set(TAKE, take);
    }

    /**
     * Метод возвращает ИНН
     *
     * @return ИНН
     */
    public String getInn() {
        return (String) params.get(INN);
    }

    /**
     * Метод устанавливает ИНН
     *
     * @param inn ИНН
     * @return контекст
     */
    public QueryContext<R> setInn(String inn) {
        return set(INN, inn);
    }

    /**
     * Метод возвращает КПП
     *
     * @return КПП
     */
    public String getKpp() {
        return (String) params.get(KPP);
    }

    /**
     * Метод устанавливает КПП
     *
     * @param kpp КПП
     * @return контекст
     */
    public QueryContext<R> setKpp(String kpp) {
        return set(KPP, kpp);
    }

    /**
     * Метод возвращает ИНН+КПП
     *
     * @return ИНН+КПП
     */
    public String getInnKpp() {
        return (String) params.get(INN_KPP);
    }

    /**
     * Метод устанавливает ИНН+КПП
     *
     * @param innKpp ИНН+КПП
     * @return контекст
     */
    public QueryContext<R> setInnKpp(String innKpp) {
        return set(INN_KPP, innKpp);
    }

    /**
     * Метод возвращает дату начала периода для дат обновления документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @return дату начала периода для дат обновления документооборотов.
     */
    public Date getUpdatedFrom() {
        return (Date) params.get(UPDATED_FROM);
    }

    /**
     * Метод устанавливает дату начала периода для дат обновления документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @param updatedFrom дата начала периода для дат обновления документооборотов.
     * @return контекст
     */
    public QueryContext<R> setUpdatedFrom(Date updatedFrom) {
        return set(UPDATED_FROM, updatedFrom);
    }

    /**
     * Метод возвращает дату конца периода для дат обновления документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @return дату конца периода для дат обновления документооборотов.
     */
    public Date getUpdatedTo() {
        return (Date) params.get(UPDATED_TO);
    }

    /**
     * Метод устанавливает дату конца периода для дат обновления документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @param updatedTo дата начала периода для дат обновления документооборотов.
     * @return контекст
     */
    public QueryContext<R> setUpdatedTo(Date updatedTo) {
        return set(UPDATED_TO, updatedTo);
    }

    /**
     * Метод возвращает дату начала периода для дат создания документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @return дату начала периода для дат создания документооборотов.
     */
    public Date getCreatedFrom() {
        return (Date) params.get(CREATED_FROM);
    }

    /**
     * Метод устанавливает дату начала периода для дат создания документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @param createdFrom дата начала периода для дат создания документооборотов.
     * @return контекст
     */
    public QueryContext<R> setCreatedFrom(Date createdFrom) {
        return set(CREATED_FROM, createdFrom);
    }

    /**
     * Метод возвращает дату конца периода для дат создания документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @return дату конца периода для дат создания документооборотов.
     */
    public Date getCreatedTo() {
        return (Date) params.get(CREATED_TO);
    }

    /**
     * Метод устанавливает дату конца периода для дат создания документооборотов. Данный критерий
     * используется для формирования списка документооборотов
     *
     * @param createdTo дата начала периода для дат создания документооборотов.
     * @return контекст
     */
    public QueryContext<R> setCreatedTo(Date createdTo) {
        return set(CREATED_TO, createdTo);
    }

    /**
     * Метод возвращает тип (или типы, разделенные знаком запятая) документооборотов Данный критерий
     * используется для формирования списка документооборотов
     *
     * @return тип (или типы, разделенные знаком запятая) документооборотов
     */
    public String getType() {
        return (String) params.get(TYPE);
    }

    /**
     * Метод устанавливает тип (или типы, разделенные знаком запятая) документооборотов Данный
     * критерий используется для формирования списка документооборотов
     *
     * @param type тип (или типы, разделенные знаком запятая) документооборотов
     * @return контекст
     */
    public QueryContext<R> setType(String type) {
        return set(TYPE, type);
    }

    /**
     * Метод возвращает сертификат в кодировке BASE64 без тегов
     *
     * @return сертификат в кодировке BASE64 без тегов
     */
    public String getCertificate() {
        return (String) params.get(CERTIFICATE);
    }

    /**
     * Метод устанавливает сертификат в кодировке BASE64 без тегов
     *
     * @param certificate сертификат в кодировке BASE64 без тегов
     * @return контекст
     */
    public QueryContext<R> setCertificate(String certificate) {
        return set(CERTIFICATE, certificate);
    }

    /**
     * Метод возвращает объект {@link CertificateList}, предназначенный для постраничнего извлечения
     * сертификатов.
     *
     * @return объект {@link CertificateList}, предназначенный для постраничнего извлечения
     * сертификатов
     */
    public CertificateList getCertificateList() {
        return (CertificateList) params.get(CERTIFICATE_LIST);
    }

    /**
     * Метод устанавливает объект {@link CertificateList}, предназначенный для постраничнего
     * извлечения сертификатов.
     *
     * @param certificateList объект {@link CertificateList}, предназначенный для постраничнего
     *                        извлечения сертификатов
     * @return контекст
     */
    public QueryContext<R> setCertificateList(CertificateList certificateList) {
        return set(CERTIFICATE_LIST, certificateList);
    }

    /**
     * Метод возвращает объект {@link UsnServiceContractInfo}, содержащий информацию о УСН
     * декларации Предназначен для передачи данных на сервер, для создания УСН декларации. Объект
     * включает в себя JSON с данными УСН декларации.
     *
     * @return объект {@link UsnServiceContractInfo}
     */
    public UsnServiceContractInfo getUsnServiceContractInfo() {
        return (UsnServiceContractInfo) params.get(USN_SERVICE_CONTRACT_INFO);
    }

    /**
     * Метод устанавливает объект {@link UsnServiceContractInfo}, содержащий информацию о УСН
     * декларации Предназначен для передачи данных на сервер, для создания УСН декларации. Объект
     * включает в себя JSON с данными УСН декларации.
     *
     * @param usnServiceContractInfo объект, содержащий информацию о УСН декларации
     * @return контекст
     */
    public QueryContext<R> setUsnServiceContractInfo(
            UsnServiceContractInfo usnServiceContractInfo) {
        return set(USN_SERVICE_CONTRACT_INFO, usnServiceContractInfo);
    }

    /**
     * Метод возвращает идентификатор записи, после которой необходимо произвести загрузку страницы.
     * Для считывания первой страницы должно быть установлено значение "0".
     *
     * @return идентификатор записи
     */
    public String getFromId() {
        return (String) params.get(FROM_ID);
    }

    /**
     * Метод устанавливает идентификатор записи, после которой необходимо произвести загрузку
     * страницы. Для считывания первой страницы должно быть установлено значение "0".
     *
     * @param fromId идентификатор записи
     * @return контекст
     */
    public QueryContext<R> setFromId(String fromId) {
        return set(FROM_ID, fromId);
    }

    /**
     * Метод возвращает идентификатор организации. Используется в группе запросов для работы с
     * организациями {@code OrganizationService}.
     *
     * @return идентификатор организации
     */
    public UUID getCompanyId() {
        return (UUID) params.get(COMPANY_ID);
    }

    /**
     * Метод устанавливает идентификатор организации. Используется в группе запросов для работы с
     * организациями {@code OrganizationService}.
     *
     * @param companyId идентификатор организации
     * @return контекст
     */
    public QueryContext<R> setCompanyId(String companyId) {
        return set(COMPANY_ID, UUID.fromString(companyId));
    }

    /**
     * Метод устанавливает идентификатор организации. Используется в группе запросов для работы с
     * организациями {@code OrganizationService}
     *
     * @param companyId идентификатор организации
     * @return контекст
     */
    public QueryContext<R> setCompanyId(UUID companyId) {
        return set(COMPANY_ID, companyId);
    }

    /**
     * Метод возвращает объект "Организация" {@link Company}. Используется в группе запросов для
     * работы с организациями {@code OrganizationService}.
     *
     * @return объект "Организация"
     */
    public Company getCompany() {
        return (Company) params.get(COMPANY);
    }

    /**
     * Метод устанавливает объект "Организация" {@link Company}. Используется в группе запросов для
     * работы с организациями {@code OrganizationService}.
     *
     * @param company объект "Организация"
     * @return контекст
     */
    public QueryContext<R> setCompany(Company company) {
        return set(COMPANY, company);
    }

    /**
     * Метод возвращает объект "Новая организация" {@link CompanyGeneral}. Объект предназначен для
     * создания новой организации. В отличии от класса {@link Company}, в нем отсутствует
     * идентификатор организации. Используется в группе запросов для работы с организациями {@code
     * OrganizationService}.
     *
     * @return объект "Новая организация"
     */
    public CompanyGeneral getCompanyGeneral() {
        return (CompanyGeneral) params.get(COMPANY_GENERAL);
    }

    /**
     * Метод устанавливает объект "Новая организация" {@link CompanyGeneral}. Объект предназначен
     * для создания новой организации. В отличии от класса {@link Company}, в нем отсутствует
     * идентификатор организации. Используется в группе запросов для работы с организациями {@code
     * OrganizationService}.
     *
     * @param companyGeneral объект "Новая организация"
     * @return контекст
     */
    public QueryContext<R> setCompanyGeneral(CompanyGeneral companyGeneral) {
        return set(COMPANY_GENERAL, companyGeneral);
    }

    /**
     * Метод возвращает имя. Используется в группе запросов для работы с организациями {@code
     * OrganizationService} при изменении имени организации.
     *
     * @return имя
     */
    public String getName() {
        return (String) params.get(NAME);
    }

    /**
     * Метод устанавливает имя. Используется в группе запросов для работы с организациями {@code
     * OrganizationService} при изменении имени организации.
     *
     * @param name имя
     * @return контекст
     */
    public QueryContext<R> setName(String name) {
        return set(NAME, name);
    }

    /**
     * Метод возвращает размер. Используется для определения максимального размера страницы при
     * извлечения списка объектов.
     *
     * @return размер
     */
    public int getSize() {
        return (int) params.get(SIZE);
    }

    /**
     * Метод устанавливает размер. Используется для определения максимального размера страницы при
     * извлечения списка объектов.
     *
     * @param size размер
     * @return контекст
     */
    public QueryContext<R> setSize(int size) {
        return set(SIZE, size);
    }

    /**
     * Метод возвращает объект "Страница событий" {@link EventsPage}. Используется для постраничного
     * извлечения списка событий в группе запросов {@code EventService}.
     *
     * @return "Страница событий"
     */
    public EventsPage getEventsPage() {
        return (EventsPage) params.get(EVENTS_PAGE);
    }

    /**
     * Метод устанавливаето объект "Страница событий" {@link EventsPage}. Используется для
     * постраничного извлечения списка событий в группе запросов {@code EventService}.
     *
     * @param eventsPage "Страница событий"
     * @return контекст
     */
    public QueryContext<R> setEventsPage(EventsPage eventsPage) {
        return set(EVENTS_PAGE, eventsPage);
    }

    /**
     * Метод возвращает номер версии. Используется для указания версии при создании декларации в
     * группе запросов {@code DraftService}.
     *
     * @return номер версии
     */
    public int getVersion() {
        return (int) params.get(VERSION);
    }

    /**
     * Метод устанавливает номер версии. Используется для указания версии при создании декларации в
     * группе запросов {@code DraftService}.
     *
     * @param version номер версии
     * @return контекст
     */
    public QueryContext<R> setVersion(int version) {
        return set(VERSION, version);
    }

    /**
     * Метод возвращает идентификатор запроса
     * @return идентификатор запроса
     */
    public String getRequestId() {
        return (String) params.get(REQUEST_ID);
    }

    /**
     * Метод устанавливает идентификатор запроса
     * @param requestId идентификатор запроса
     * @return контекст
     */
    public QueryContext<R> setRequestId(String requestId) {
        return set(REQUEST_ID, requestId);
    }

    public String getSmsCode() {
        return (String) params.get(SMS_CODE);
    }

    public QueryContext<R> setSmsCode(String smsCode) {
        return set(SMS_CODE, smsCode);
    }

    /**
     * Метод возвращает IP адрес отправителя
     * @return IP адрес отправителя
     */
    public String getUserIP() {
        return (String) params.get(USER_IP);
    }

    /**
     * Метод устанавливает IP адрес отправителя
     * @param userIP IP адрес отправителя
     * @return контекст
     */
    public QueryContext<R> setUserIP(String userIP) {
        return set(USER_IP, userIP);
    }

    /**
     * Метод возвращает объект, содержащий информацию об ответном документе
     * @return объект, содержащий информацию об ответном документе
     */
    public ReplyDocument getReplyDocument() {
        return (ReplyDocument) params.get(REPLY_DOCUMENT);
    }

    /**
     * Метод устанавливает объект, содержащий информацию об ответном документе
     * @param replyDocument объект, содержащий информацию об ответном документе
     * @return контекст
     */
    public QueryContext<R> setReplyDocument(ReplyDocument replyDocument) {
        return set(REPLY_DOCUMENT, replyDocument);
    }

    /**
     * Метод возвращает список объектов, содержащий информацию об ответных документах
     * @return список объектов, содержащий информацию об ответных документах
     * @see ReplyDocument
     */
    @SuppressWarnings("unchecked")
    public List<ReplyDocument> getReplyDocuments() {
        return (List<ReplyDocument>) params.get(REPLY_DOCUMENTS);
    }

    /**
     * Метод устанавливает список объектов, содержащий информацию об ответных документах
     * @param replyDocuments список объектов, содержащий информацию об ответных документах
     * @return контекст
     * @see ReplyDocument
     */
    public QueryContext<R> setReplyDocuments(List<ReplyDocument> replyDocuments) {
        return set(REPLY_DOCUMENTS, replyDocuments);
    }

    /**
     * Метод устанавливает значение для параметра. Если переданное значение null, то параметр
     * удаляется.
     *
     * @param name имя параметра
     * @param val  значение параметра
     * @return контекст
     */
    public QueryContext<R> set(String name, Object val) {
        if (val != null) {
            params.put(name, val);
        } else {
            params.remove(name);
        }

        return this;
    }

    /**
     * Метод возвращает параметр с именем "name", приводя его к типу переменной.
     *
     * @param name имя параметра
     * @param <T>  задекларированный тип переменной
     * @return возвращает параметр с именем "name", приводя его к типу переменной
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String name) {
        return (T) params.get(name);
    }

    /**
     * Метод выполняет операцию типа {@link Query} в отдельном потоке. Операция принимает в качестве
     * параметра данный контекст.
     *
     * @param query операция
     * @return квитанция на результат операции
     */
    public CompletableFuture<QueryContext<R>> applyAsync(Query<R> query) {
        if (isFail()) {
            return CompletableFuture.completedFuture(this);
        }

        return CompletableFuture.supplyAsync(() -> query.apply(this));
    }

    /**
     * Метод выполняет операцию типа {@link Query} в текущем потоке. Операция принимает в качестве
     * параметра данный контекст.
     *
     * @param query операция
     * @return контекст с результатом операции
     */
    public QueryContext<R> apply(Query<R> query) {
        if (isFail()) {
            return this;
        }

        return query.apply(this);
    }

    /**
     * Метод кидает исключение, если контекст содержит ошибку
     */
    public void failure() {
        ServiceError e = getServiceError();
        if (e != null) {
            throw new ServiceException(e);
        }
    }

    /**
     * Метод в случае наличия ошибки кидает исключение. Предназначен для тестов.
     *
     * @return контекст
     */
    public QueryContext<R> ensureSuccess() {
        failure();
        return this;
    }

    public R getOrThrow() {
        return ensureSuccess().get();
    }

    private String prettyErrorPrint(ServiceError se) {
        final String EOL = "\r\n";

        ErrorCode errorCode = se.getErrorCode();
        String message = se.getMessage();
        int responseCode = se.getResponseCode();
        Map<String, List<String>> responseHeaders = se.getResponseHeaders();
        String responseBody = se.getResponseBody();

        StringBuilder errorMsg = new StringBuilder("Message error: ")
                .append(message == null ? errorCode.message() : message).append(EOL);
        if (responseCode != 0) {
            errorMsg.append("  Response code: ").append(responseCode).append(EOL);
            if (responseHeaders != null) {
                errorMsg.append("  Headers:").append(EOL);
                responseHeaders.keySet().forEach((k) -> {
                    List<String> values = responseHeaders.get(k);
                    if (values != null) {
                        StringBuilder headerLine = new StringBuilder("    ").append(k).append(": ");
                        values.forEach((v) -> {
                            headerLine.append(v).append("; ");
                        });
                        errorMsg.append(headerLine).append(EOL);
                    }
                });
            }
            if (responseBody != null) {
                String cleanText = responseBody.replaceAll("\n", " ").replaceAll("\r", "");
                errorMsg.append("  Response body: ").append(cleanText).append(EOL);
            }
        }
        return errorMsg.toString();
    }

    @Override
    public String toString() {
        return "context{" + result + "=" + get() + " with " + params.size() + "params}";
    }
}
