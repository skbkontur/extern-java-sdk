/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers;

import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public interface AuthenticationProvider {

	QueryContext<String> sessionId();

	String authPrefix();
	
	void addAuthenticationListener(AuthenticationListener authListener);

	void removeAuthenticationListener(AuthenticationListener authListener);
	
	void raiseUnauthenticated(ServiceError x);
}
