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

    private static final long serialVersionUID = 1813799131945186727L;

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
