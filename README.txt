# extern-java-sdk

SDK для работы с API Контур.Экстерна для платформы JVM
======================================================
Для сборки проекта: 
-------------------
	- без тестов: mvn clean package -Dmaven.test.skip=true
	- с тестами: mvn clean package

Основной проект: extern-sdk
---------------------------
	- файл с настройками: extern-sdk\src\test\resources\extern-sdk-config.json
{
	"accountId":"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
	"apiKey":"XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX",
	"authPrefix":"auth.sid ",
	"login":"XXXXXXXXXXXXX",
	"pass":"XXXXXXXXXXXX",
	"authBaseUri":"http://api.testkontur.ru/auth/",

	"thumbprint":"XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
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

