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
	public static final String CONFIG_LOAD = "Ошибка загрузки файла конфигурации Контур SDK.";
	public static final String AUTHORIZATION_BY_LOGIN = "Ошибка авторизации.";
	public static final String SERVER_ERROR = "Ошибка сервера.";
	public static final String CRYPTO_ERROR = "Ошибка криптографии.";
	public static final String ENTITY_NOT_FOUND = "{0} ({1}) не найден.";

	public ExternSDKException(String message, Throwable x) {
		super(message, x);
	}
	
	public ExternSDKException(String message) {
		super(message);
	}
	
	public ExternSDKException(String template, Object ... params) {
		super(MessageFormat.format(template, params));
	}
}
