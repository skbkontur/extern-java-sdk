@echo off

set NAME=c:\ava-sdk

git clone https://git.skbkontur.ru/ke-api/extern-java-sdk-secrets %NAME% %*

cd %NAME%

rem git clone https://github.com/skbkontur/extern-java-sdk extern-java-sdk-tmp %*

rem xcopy /H /E /Q extern-java-sdk-tmp extern-sdk
rem rmdir /S /Q extern-java-sdk-tmp

rem cd extern-sdk

rem mvn compile