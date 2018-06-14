# SDK для работы с API Контур.Экстерна для платформы JVM

[TOC]

## 1. Назначение
Предназначен для легкой интеграции внешних систем с [API Контур.Экстерна](https://github.com/skbkontur/extern-api-docs), разработанных для платформы **JVM**. Для этого библиотека предоставляет класс *ExternEngine*, с помощью которого производится передача данных на внешние сервисы СКБ Контур. Класс *ExternEngine* включает в себя следующие сервисы:

- *AccountService* – сервис для работы с учетными записями пользователей:
    - получить учетную запись;
    - создать новую учетную запись;
- *DraftService* – сервис для работы с черновиками;
- *DocflowService* – сервис для работы с документооборотами;
- *CertificateService* – сервис для работы с сертификатами.

## 2. Подключение SDK к проекту
Для того чтобы воспользоваться SDK нужно выполнить следующие шаги:
- склонировать командой git репозиторий SDK:

`git clone git@github.com:skbkontur/extern-java-sdk.git`

- cобрать с помощью maven SDK проект:

`mvn clean install`

- добавить в maven-проект (pom.xml) артефакт extern-api-java-sdk:
```xml
<dependency>
    <groupId>ru.kontur.extern-api</groupId>
    <artifactId>extern-api-java-sdk</artifactId>
    <version>${extern-api-java-sdk.version}</version>
</dependency>
```

## 3. Конфигурирование
Для того чтобы начать работу с **SDK**, необходимо создать и сконфигурировать объект *ExternEngine*. Для конфигурации необходимо создать и передать с помощью соответствующего сеттера следующие провайдеры:
- **ServiceBaseUriProvider** – предоставляет адрес сервиса в Интернет. Провайдер представляет из себя объект, имплементирующий метод getServiceBaseUri интерфейса ServiceBaseUriProvider, возвращающий URI сервиса. В простейшем случае вы можете передать лямбда-выражение типа: ()->”https://...”;
- **AccountProvider** — предоставляет идентификатор аккаунта, который передается при отправки данных на сервис. Данный идентификатор связан с лицевым счетом в системе СКБ Контур. Провайдер представляет из себя объект, имплементирующий метод *accountId* интерфейса *AccountProvider*. В простейшем случае вы можете передать лямбда-выражение типа:     ()->UUID.fromString("XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX");
- <a name="apiKey">**ApiKeyProvider**</a> – предоставляет идентификатор, который выдается сервису, от которого отправляются запросы к API СКБ Контура. Провайдер представляет из себя объект, имплементирующий метод getApiKey интерфейса ApiKeyProvider. Также может из себя представлять лямбда-выражение типа:     ()->"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX";
- **CryptoProvider** – предоставляет криптографический провайдер. В SDK есть три реализации:
    - реализация для работы с [MSCapi](https://msdn.microsoft.com/en-us/library/windows/desktop/aa380256.aspx) - класс *CryptoProviderMSCap*
    - реализация для работы с облачными сертификатами Удостоверяющего Центра СКБ Контур - класс *CloudCryptoProvider*;
    - реализация для RSA-криптографии *CryptoProviderRSA*.

     Первая (MSCAPI) и вторая (облачная) реализации криптографии предназначены для вычисления электронной подписи и расшифрования зашифрованных документов, с использованием алгоритмов ГОСТ. Третья (RSA) предназначена для подписания идентификационных данных для доверительной аутентификации, с использованием алгоритмов RSA;

- **AuthenticationProvider** — предоставляет аутентификатор. Каждый запрос, отправляемый к сервисам СКБ Контур, должен сопровождаться идентификатором аутентификационной сессии. Аутентифицироваться можно:
   * по логину и паролю, для этого в SDK есть класс AuthenticationProviderByPass, реализующий интерфейс *AuthenticationProvider*;
   * с помощью механизмов доверительной аутентификации, для этого в SDK реализован класс *TrustedAuthentication*, так-же реализующий интерфейс *AuthenticationProvider*;
   * с помощью сертификата личного ключа, для этого в SDK предназначен класс *CertificateAuthenticationProvider*, реализующий интерфейс *AuthenticationProvider*.

### 3.1. <a name="cryptoProvider">Конфигурирование криптопровайдеров</a>

#### 3.1.1. <a name="mscapi">MSCAPI криптопровайдер</a>
##### Назначение
Предназначен для выполнения криптографических операций на компьютере клиента с помощью сертифицированного криптопровайдера с использованием **ГОСТ**-алгоритмов, например **КриптоПро**. Для расшифрования и вычисления электронной подписи требуется доступный ключевой носитель, содержащий сертификат открытого ключа.
##### Создание и конфигурирование
Для создания объекта достаточно вызвать конструктор класса *CryptoProviderMSCapi*. Дальнейшее конфигурирование не требуется.

#### 3.1.2. <a name="cloudcapi">Облачный криптопровайдер</a>
##### Назначение
Предназначен для выполнения криптографических операций на сервере с использованием **ГОСТ**-алгоритмов. Клиент делегирует выполнение криптографических операций специальному сервису.

Криптографические операции: вычисление подписи и расшифрование выполняются в два этапа:

- 1 этап – это запрос на выполнение операции;
- 2 этап – подтверждение операции СМС–кодом.

На первом этапе сервис принимает запрос и возвращает идентификатор запроса. На втором этапе сервис ожидает идентификатор запроса, который был отдан, и СМС-код.
##### Создание и конфигурация
Для работы с облачной криптографией необходимо создать объект *CloudCryptoProvider* с помощью конструктора, передав в него, адрес  облачного сервиса. Для полученного объекта необходимо установить:
   * *AuthenticationProvider* – провайдер для получения токена аутентификации. Назначение аутентификации и типы были описаны выше.
   * *ApiKeyProvider* – провайдер для получения идентификатора сервиса. [Описание см. выше](#apiKey).
   * *CertificateProvider* – провайдер предназначен для получения сертификата отправителя в DER-кодировке по его отпечатку из внутреннего хранилища внешней системы. Данный провайдер должен имплементировать интерфейс типа: *CertificateProvider*, реализующий метод  `QueryContext<byte[]> getCertificate(String thumbprint)`, где thumbprint - отпечаток сертификата. Метод возвращает контекст типа: QueryContext<byte[]>. Контекст должен содержащий сертификат или ошибку, в зависимости от возвращаемого результата метода isFail. Если метод isFail возвращает значение false, то метод getResult вернет массив байт сертификата, иначе метод getServiceError вернет ошибку;
   * *ApproveCodeProvider* – провайдер предназначен для получения СМС-кода, который отправляется облачным сервисом на зарегистрированный номер телефона отправителя. Данный провайдер должен имплементировать интерфейс типа: `Function<String, String>`, где параметром является идентификатор запроса, а возвращаемым значением – СМС-код.

#### 3.1.3. RSA криптопровайдер
##### Назначение
Предназначен для вычисления электронной подписи с использованием **RSA**-алгоритмов. Данный криптопровайдер может быть использован для доверительной аутентификации.
##### Создание и конфигурация
Для работы с **RSA** подписью необходимо создать объект типа *CryptoProviderRSA* с помощью конструктора. Конструктор принимает на вход пароль для ключевого хранилища **JAVA** (**JKS**) и пароль для секретного ключа. Если в качестве паролей передать значение **null**, то это будет означать его отсутствие.  Для полученного объекта можно установить провайдер *KeyStoreProvider* типа **`Supplier<String>`**, который возвращает путь к **JKS**. По умолчанию криптопровайдер использует **java runtime JKS**: `System.getProperty("java.home")+File.separator+"lib"+File.separator+"security"+File.separator+"cacerts"`.

### 3.2. Конфигурирование аутентификаторов

#### 3.2.1.  Аутентификация по логину и паролю
Для аутентификации по логину и паролю необходимо создать объект класса AuthenticationProviderByPass с помощью конструктора. Конструктор принимает четыре параметра:
- *authBaseUriProvider* : *UriProvider* – URI провайдер, возвращает адрес сервиса аутентификации. В простейшем случае можно использовать лямбда-выражение: `()->"https://...”`;
- *loginAndPasswordProvider* : *LoginAndPasswordProvider* – объект, реализующий соответствующий интерфейс, возвращающий логин и пароль пользователя из внешней системы;
- *apiKeyProvider* : *ApiKeyProvider*. [Описание см. выше](#apiKey).

#### 3.2.2.  Доверительная аутентификация
Для доверительной аутентификации необходимо создать объект класса *TrustedAuthentication* с помощью конструктора.  Для полученного объекта с помощью соответствующих сеттеров необходимо установить следующие провайдеры:
- *ApiKeyProvider* – провайдер для получения идентификатора организации. [Описание см. выше.](#apiKey)
- *authBaseUriProvider* : *UriProvider* – **URI** провайдер, возвращает адрес сервиса аутентификации. В простейшем случае можно использовать лямбда-выражение: `()->"https://...”`;
- *CryptoProvider* – предоставляет криптографический провайдер. Можно установить либо **MSCAPI** провайдер, либо **RSA** криптопровайдер. Для того чтобы можно доверительно аутентифицироваться, необходимо зарегистрировать сертификат в сервисе доверительной аутентификации. Для этого необходимо обратиться в СКБ Контур.
- *SignatureKeyProvider* – предоставляет отпечаток сертификата ключа, с помощью которого будут подписываться идентификационный данные конечного пользователя (отправителя). В простейшем случае можно использовать лямбда-выражение: `()->”XXXXXXXXXXXXXXXXXXXX”`;
- *ServiceUserIdProvider* – предоставляет идентификатор конечного пользователя во внешней системе. В простейшем случае можно использовать лямбда-выражение: `()->”XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX”`;
- *CredentialProvider* – предоставляет структуру данных содержащий наименования идентификатора (СНИЛС, номер телефона) и его значение. В простейшем случае можно использовать лямбда-выражение:` ()->new Credential(”phone”,”03”)`.

#### 3.2.3.  Аутентификация по сертификату
Аутентификация по сертификату проходит по следующей схеме:
* клиент выполняет запрос на сервис аутентификации. При этом он пересылает сертификат закрытого ключа;
* сервис, получив запрос, выполняет проверку регистрации полученного от клиента сертификата;
* в случае успешной проверки сервис возвращает зашифрованный контент на переданном сертификате;
* клиент расшифровывает зашифрованный контент своим личным ключом, полученный от сервиса аутентификации, и отправляет на сервис аутентификации;
* сервис аутентификации проверяет результат, и в случае успеха возвращает клиенту токен аутентификации для доступа к сервисам.

[Подробнее можно ознакомиться здесь.](https://github.com/skbkontur/extern-api-docs/blob/master/%D0%90%D1%83%D1%82%D0%B5%D0%BD%D1%82%D0%B8%D1%84%D0%B8%D0%BA%D0%B0%D1%86%D0%B8%D1%8F.md#1)
   
Для аутентификации по сертификату необходимо создать провайдер типа *CertificateAuthenticationProvider* и установить его в экземпляр класса *ExternEngine*. Чтобы создать провайдер необходимо выполнить следующие шаги:
* вызвать статический метод *usingCertificate* класса *CertificateAuthenticationProvider* и передать ему следующие параметры:
  * *certificateProvider*: *CertificateProvider* -  объект, реализующий метод `QueryContext<byte[]> getCertificate(String thumbprint)`. Метод должен принимать параметр отпечаток сертификата, а возвращать контекст с сертификатом в DER-кодировке или ошибку, в зависимости от возвращаемого результата метода `isFail`. Метод `isFail` вернет значение `true`, если в контекст будет установлена ошибка с помощью одного из методов `setServiceError`;
  * *skipCertValidation*: *boolean* - необязательный параметр, задающий режим отмены валидации сертификата. Если параметр не задан, то сертификат будет проходить процедуру валидации.

  Данный метод вернет билдер *CertificateAuthenticationProviderBuilder*;

* в полученный билдер с помощью соответствующих методов необходимо установить следующие провайдеры:
  * *ApiKeyProvider* - провайдер для получения идентификатора сервиса. [Описание см. выше](#apiKey);
  * *CryptoProvider* - реализацию криптографического провайдера. Аутентификация по сертификату использует только одну криптографическую операцию - расшифрование. В связи с этим, объект, реализующий интерфейс *CryptoProvider*, должен реализовать только метод расшифрования `QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt)`. В реализациях приняты следующие соглашения. Передаваемый контекст должен содержать отпечаток сертификата, установленный с помощью метода `setThumbprint`, и зашифрованный  контент в формате PKCS#7, установленный с помощью метода `setContent`. Метод должен возвращать контекст, содержащий массив байт расшифрованного контента, установленный с помощью метода `setResult(<decrypted content>,CONTENT)`. Если аутентификация по сертификату используется в сервисе, то реализация `CryptoProvider` должна быть выполнена в режиме прокси, т.е. для расшифрования зашифрованный контент необходимо переслать конечному пользователю. Для "толстого" клиента можно использовать реализацию [`CryptoProviderMSCapi`](#mscapi) или [`CloudCryptoProvider`](#cloudcapi);
  * *ServiceBaseUriProvider* - провайдер, возвращающий адрес сервиса аутентификации в Интернет. В простейшем случае вы можете передать лямбда-выражение типа: `()->”https://..."`;
  * *SignatureKeyProvider* - провайдер, предоставляющий отпечаток сертификата личного ключа. В простейшем случае можно использовать лямда-выражение: ()->"XXXXXXXXXXXXXXXXXXXX".
