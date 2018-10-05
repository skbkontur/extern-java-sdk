@echo off

if "%1"=="/?" goto :help
if "%1"=="/h" goto :help
if "%1"=="-h" goto :help
if "%1"=="--help" goto :help

if "%1"=="" (
    goto :help
) else (
    set rsa_file=%1
)

net session >nul 2>&1
IF ERRORLEVEL 2 (
    echo Error: Admin rights required.
    exit /B 2
)

IF NOT DEFINED JAVA_HOME (
    echo Error: JAVA_HOME variable should be defined
    exit /B 1
)

set keytool_cmd="%java_home%"\bin\keytool
%keytool_cmd% >nul 2>&1

IF ERRORLEVEL 3 (
    echo Error: JAVA_HOME variable should point to jdk
    exit /B 3
)

rem TODO: install all certs from resources

if exist "%java_home%\jre\lib\security\cacerts" (
    set keystore=-keystore "%java_home%\jre\lib\security\cacerts"
    set destkeystore=-destkeystore "%java_home%\jre\lib\security\cacerts"
) else (
    set keystore=-cacerts
    set destkeystore=-destkeystore "%java_home%\lib\security\cacerts"
)

%keytool_cmd% -delete -alias kontur_auth_trusted_key %keystore% -storepass changeit
%keytool_cmd% -importkeystore -srckeystore %rsa_file% -srcstoretype pkcs12 -srcalias te-e493b4d1-a1f5-4d33-b3db-4faeae150fe5 -srcstorepass 123 -destalias kontur_auth_trusted_key %destkeystore% -deststoretype JKS -deststorepass changeit
%keytool_cmd% -list %keystore% -storepass changeit -alias kontur_auth_trusted_key

echo Installed.

goto :eof

:help
echo Install or reinstall rsa cert from *.pfx keysore into java keystore
echo.
echo Usage: install_rsa_key path/to/cert.pfx
