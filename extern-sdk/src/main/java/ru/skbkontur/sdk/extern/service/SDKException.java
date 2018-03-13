/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service;

import java.text.MessageFormat;

/**
 *
 * @author AlexS
 */
public class SDKException extends RuntimeException {
	public static final String UNKNOWN = "Неизвестная ошибка.";
	public static final String S_AUTHORIZATION_BY_LOGIN = "Ошибка авторизации.";
	public static final String S_SERVER_ERROR = "Ошибка сервера.";
	public static final String S_ENTITY_NOT_FOUND = "{0} ({1}) не найден.";
	public static final String C_CONFIG_NOT_FOUND = "Не найден файл конфигурации.";
	public static final String C_CONFIG_LOAD = "Ошибка загрузки файла конфигурации Контур SDK.";
	public static final String C_CRYPTO_ERROR = "Ошибка криптографии.";
	public static final String C_CRYPTO_ERROR_NO_CRYPTO_PROVIDER = "Не определен криптопровайдер.";
	public static final String C_CRYPTO_ERROR_INIT = "Ошибка инициализации криптографии.";
	public static final String C_CRYPTO_ERROR_KEY_NOT_FOUND = "Ключ подписи не найден для сертификата: {0}.";
	public static final String C_RESOURCE_NOT_FOUND = "Не найден ресурс: {0}.";

	public SDKException(String message, Throwable x) {
		super(message, x);
	}
	
	public SDKException(String message) {
		super(message);
	}
	
	public SDKException(String template, Object ... params) {
		super(MessageFormat.format(template, params));
	}
	
	public SDKException(String template, Throwable x, Object ... params) {
		super(MessageFormat.format(template, params), x);
	}
}
