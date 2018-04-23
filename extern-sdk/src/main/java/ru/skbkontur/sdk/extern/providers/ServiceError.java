/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers;

/**
 *
 * @author AlexS
 */
public interface ServiceError {
	enum ErrorCode {
		server(1,"A server side error."), 
		auth(2,"An authentication error."), 
		unknownAuth(3,"Unknown authenticator."), 
		business(4,"Unkonow business error.");
	
		private final int value;
		private final String message;
		
		ErrorCode(int value, String message) {
			this.value = value;
			this.message = message;
		}
		
		public int value() {
			return value;
		}

		public String message() {
			return message;
		}
	}
	
	ErrorCode getErrorCode();
	
	int getResponseCode();
	
	String getMessage();
    
    @SuppressWarnings("unchecked")
    Throwable getCause();
}
