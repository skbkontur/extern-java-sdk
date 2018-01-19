# extern-java-sdk

SDK для работы с API Контур.Экстерна для платформы JVM
======================================================
Для сборки проекта: 
-------------------
	- без тестов: mvn clean package -Dmaven.test.skip=true
	- с тестами: mvn clean package

Основной проект: extern-sdk
---------------------------
	- файл с настройками: extern-sdk\src\main\resources\extern-sdk-config.json
{
	"billingAccountId":"4f2fee32-6b0a-4f36-bd3a-93bf5df76030",
	"apiKey":"d5f8c8b3-7716-4e75-99bd-67a910045fd5",
	"authPrefix":"auth.sid ",
	"login":"argos_testing@skbkontur.ru",
	"pass":"QWERTY123456",
	"authBaseUri":"http://api.testkontur.ru/auth/",

	"thumbprint":"fd3e438933387026ee46c03691f20743d7d34766"
}

где: thumbprint - отпечаток сертификата подписи, опционально, если отсутствует, то не будет формировать подпись
		
	- файл с тестовыми данными (DraftMeta[]): extern-sdk\src\test\resources\clientInfosTest.json
[
	{"recipient": {"ifns-code":"6653" },"organization":{"type":1,"inn":"6653000832","organization":{"kpp":"665325934"}}}
]

Проект с примерами использования SDK: extern-sdk-examples
---------------------------------------------------------

Проект с автогенеренным кодом swagger: extern-sdk-swagger
---------------------------------------------------------
Для генерации кода:
	- положить в каталог extern-sdk-swagger\src\main\resources\swagger.json - http://extern-api.testkontur.ru/swagger/docs/v1
	- из каталога extern-sdk-swagger необходимо запустить команду: mvn clean compile
	- сгенеренный код будет находится в: extern-sdk-swagger\target\generated-sources\swagger\src

