# SDK для работы с API Контур.Экстерна

![Maven Central](https://img.shields.io/maven-central/v/ru.kontur.extern-api/extern-api-java-sdk.svg)
![oss.sonatype](https://img.shields.io/nexus/s/https/oss.sonatype.org/ru.kontur.extern-api/extern-api-java-sdk.svg)

Предназначен для интеграции внешних систем с [API Контур.Экстерна](https://github.com/skbkontur/extern-api-docs). 
Библиотека предоставляет классы и методы для работы с сервисами СКБ Контур.

## Подключение SDK к проекту

### Указать библиотеку как зависимость из [maven.central](https://mvnrepository.com/artifact/ru.kontur.extern-api/extern-api-java-sdk):

```xml
<dependency>
    <groupId>ru.kontur.extern-api</groupId>
    <artifactId>extern-api-java-sdk</artifactId>
    <version>${extern-api-java-sdk.version}</version>
</dependency>
```

#### [Использовать SNAPSHOT версии sdk](https://stackoverflow.com/questions/7715321/how-to-download-snapshot-version-from-maven-snapshot-repository)

Для использования SNAPSHOT версий sdk нужно разрешить использование **sonatype snapshot repository**.
Для этого добавьте в ` ~/.m2/settings.xml`
```xml
<profiles>
  <profile>
     <id>allow-snapshots</id>
        <activation><activeByDefault>true</activeByDefault></activation>
     <repositories>
       <repository>
         <id>snapshots-repo</id>
         <url>https://oss.sonatype.org/content/repositories/snapshots</url>
         <releases><enabled>false</enabled></releases>
         <snapshots><enabled>true</enabled></snapshots>
       </repository>
     </repositories>
   </profile>
</profiles>
```

Snapshot версии полезно использовать для активной разработки, оперативного исправления багов
и получения самых свежих обновлений. 

### Версионирование

Информацию самых новых версиях sdk можно найти [здесь](https://github.com/skbkontur/extern-java-sdk/releases) 

![Maven Central](https://img.shields.io/maven-central/v/ru.kontur.extern-api/extern-api-java-sdk.svg)
![oss.sonatype](https://img.shields.io/nexus/s/https/oss.sonatype.org/ru.kontur.extern-api/extern-api-java-sdk.svg)

> указывать без префикса `v`

Название тега соответствует существующей версии sdk в maven.central или версии snapshot'а в
репозитории oss.sonatype.org

### Компиляция из исходного кода:

1. Склонировать репозиторий SDK:
    ```cmd
    git clone git@github.com:skbkontur/extern-java-sdk.git
    ```
    
1. Собрать и установить проект в локальный maven репозиторий
 (пропуская шаг интеграционных тестов: о них будет ниже):
    ```cmd
    mvn clean install -DskipITs
    ```
    
1. Добавить в `pom.xml` проекта артефакт `extern-api-java-sdk`:
    ```xml
    <dependency>
        <groupId>ru.kontur.extern-api</groupId>
        <artifactId>extern-api-java-sdk</artifactId>
        <version>${extern-api-java-sdk.version}</version>
    </dependency>
    ```

## Конфигурирование
Для того чтобы начать работу с SDK, необходимо создать и сконфигурировать `ExternEngine`.
Для этого стоит воспользоваться `ExternEngineBuilder`.
Например, для базового создания движка с аутентификацией по сертификату код будет выглядеть так:

```java
ExternEngine ee = ExternEngineBuilder
        .createExternEngine("http://extern-api.testkontur.ru")
        .apiKey("ваш api-key")
        .buildAuthentication("http://api.testkontur.ru", authBuilder -> authBuilder
                .certificateAuthentication(/* place certificate content here */)
        )
        .doNotUseCryptoProvider()
        .doNotSetupAccount()
        .build()
```

Больше информации можно найти в документации к коду или на [wiki проекта](https://github.com/skbkontur/extern-java-sdk/wiki/Get-started-%5Bsince-1.6%5D).

### Аутентификация

1. **Api-Key** – идентификатор, который выдается *сервису*, от которого отправляются запросы к API СКБ Контура.

2. **AuthenticationProvider** – провайдер токена аутентификации *пользователя*
    (см. [auth.sid](https://docs-ke.readthedocs.io/ru/latest/auth/index.html)). Чтобы создать провайдеры для
    стандартных методов аутентификации можно воспользоваться `AuthenticationProviderBuilder`.

### Account:

- **AccountId** — предоставляет идентификатор аккаунта, который передается при отправки данных на сервис.
    Данный идентификатор связан с лицевым счетом в системе СКБ Контур. UUID.
    (см. ExternEngine#setAccountId)
    
### CryptoProvider
    
  - Реализация для работы с [MSCapi](https://msdn.microsoft.com/en-us/library/windows/desktop/aa380256.aspx) - класс `CryptoProviderMSCap`;
    Предназначена для вычисления электронной подписи и расшифрования зашифрованных документов, с использованием алгоритмов ГОСТ.
    Для расшифрования и вычисления электронной подписи требуется доступный ключевой носитель, содержащий сертификат открытого ключа.
  - Реализация для RSA-криптографии `CryptoProviderRSA`.
    Предназначена для подписания идентификационных данных для доверительной аутентификации.
     Конструктор принимает на вход пароль для ключевого хранилища **JAVA** (**JKS**) и пароль для секретного ключа. 
     Если в качестве паролей передать значение **null**, то это будет означать его отсутствие.



## Архитектура
В SDK реализован класс **ExternEngine**, обеспечивающий доступ к операциям Контур Экстерн. Все операции разделены на следующие группы:

- сервис для работы с учетными записями конечных пользователей (**AccoutService**);
- сервис для получения информации о сертификатах конечных пользователей (**CertificateService**);
- сервис для работы со списком организаций (**OrganizationService**);
- сервис для работы с черновиками (**DraftService**);
- сервис для работы с документооборотами (**DocflowService**);
- сервис для работы с лентой событий (**EventService**).

Для получения доступа к сервису необходимо у объекта ExternEngine вызвать соответствующий метод 
типа **get<имя сервиса>**. Например для того чтобы получить доступ к операциям сервиса для работы 
с учетными записями необходимо использовать метод **getAccoutService()**, который вернет экземпляр класса, 
реализующий интерфейс **AccoutService**. 


#### QueryContext

Warning! Всё что здесь написано имеет место, но не является рекомендуемым сценарием использования SDK
так как является местом потенциальных ошибок отсутствия параметров в контексте.

##### Актуальная информация

* Методы использующие `QueryContext` в качестве входного параметра помечены `@Deprecated` и
будут удалены в следующих версиях

* `QueryContext` возвращаемый из метода предназначен только для
    * получения результата метода в случае успеха `.get()`
    * получения/пробрасывания сервисной ошибки `ApiException`: `.isFail()`/`.getServiceError()`/`.getOrThrow()`
  
##### Deprecated
  
> Класс **QueryContext** содержит коллекцию типа **Map**, для сохранения входных и выходных параметров. 
Для каждого используемого параметра существует свой сеттер, 
например, для установки передаваемого контента есть метод **setContent**. 

> Как уже упоминалось выше, метод **isFail** возвращает признак операции, завершившейся с ошибкой. 
В этом случае метод **getServiceError** вернет ошибку. Если операции выполнилась успешно, 
то метод **isSuccess** вернет истину, а метод **get** результат операции, 
который указан в качестве параметра класса **QueryContext**. 
Более подробное описание смотри в **javadoc**.
