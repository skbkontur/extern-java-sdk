@echo off

if "%1"=="/?" goto :help
if "%1"=="/h" goto :help
if "%1"=="-h" goto :help
if "%1"=="--help" goto :help

if "%1"=="" (
    set cert_root=.
) else (
    set cert_root=%1
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


if exist "%java_home%\jre\lib\security\cacerts" (
    set keystore=-keystore "%java_home%\jre\lib\security\cacerts"
) else (
    set keystore=-cacerts
)

%keytool_cmd% -delete -alias SKB_Kontur_Root_CA %keystore% -storepass changeit
%keytool_cmd% -delete -alias CA_SKB_Kontur %keystore% -storepass changeit

echo # installing SKB_Kontur_Root_CA (%cert_root%\SKB_Kontur_Root_CA.cer)...
%keytool_cmd% -importcert -alias SKB_Kontur_Root_CA -file %cert_root%\SKB_Kontur_Root_CA.cer %keystore% -storepass changeit -noprompt
echo # installing CA_SKB_Kontur (%cert_root%\CA_SKB_Kontur.cer)...
%keytool_cmd% -importcert -alias CA_SKB_Kontur -file %cert_root%\CA_SKB_Kontur.cer %keystore% -storepass changeit -noprompt

goto :eof

:help

echo Install or reinstall kontur ssl certs into java keystore
echo.
echo Usage: install_kontur_ssl cert_root [working dir by default]
