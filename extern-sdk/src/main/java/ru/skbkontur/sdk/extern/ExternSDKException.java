/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern;

import java.text.MessageFormat;

/**
 *
 * @author AlexS
 */
public class ExternSDKException extends Exception {
	public static final String UNKNOWN = "Неизвестная ошибка.";
	public static final String S_AUTHORIZATION_BY_LOGIN = "Ошибка авторизации.";
	public static final String S_SERVER_ERROR = "Ошибка сервера.";
	public static final String S_ENTITY_NOT_FOUND = "{0} ({1}) не найден.";
	public static final String C_CONFIG_LOAD = "Ошибка загрузки файла конфигурации Контур SDK.";
	public static final String C_CRYPTO_ERROR = "Ошибка криптографии.";
	public static final String C_RESOURCE_NOT_FOUND = "Не найден ресурс: {0}.";

	public ExternSDKException(String message, Throwable x) {
		super(message, x);
	}
	
	public ExternSDKException(String message) {
		super(message);
	}
	
	public ExternSDKException(String template, Object ... params) {
		super(MessageFormat.format(template, params));
	}
	
	public ExternSDKException(String template, Throwable x, Object ... params) {
		super(MessageFormat.format(template, params), x);
	}
}
