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

для получения accountId, apiKey, login, pass необходимо обратиться по адресу extern-api@skbkontur.ru
		
	- файл с тестовыми данными: extern-sdk\src\test\resources\clientInfosTest.json
[
	{
		"clientInfo":{"recipient": {"ifns-code":"7810" },"organization":{"type":1,"inn":"6653000832","organization":{"kpp":"665325934"}}},
		"docs":
			[
				"/docs/NO_SRCHIS_7810_7810_6653000832665325934_20180126_993f2341-2b37-45dc-8fd5-fca7c019d49a.xml"
			]
	}
]
	- подкаталог с тестовыми декларациями:extern-sdk\src\test\resources\docs

Проект с примерами использования SDK: extern-sdk-examples
---------------------------------------------------------

Проект с автогенеренным кодом swagger: extern-sdk-swagger
---------------------------------------------------------
Для генерации кода:
	- положить в каталог extern-sdk-swagger\src\main\resources\swagger.json - http://extern-api.testkontur.ru/swagger/docs/v1
	- из каталога extern-sdk-swagger необходимо запустить команду: mvn clean compile
	- сгенеренный код будет находится в: extern-sdk-swagger\target\generated-sources\swagger\src

