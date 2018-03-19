/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.auth;

import java.util.ArrayList;
import java.util.List;
import ru.skbkontur.sdk.extern.event.AuthenticationEvent;
import ru.skbkontur.sdk.extern.event.AuthenticationListener;
import ru.skbkontur.sdk.extern.providers.AuthenticationProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author alexs
 */
public abstract class AuthenticationProviderAbstract implements AuthenticationProvider {
	
	private final List<AuthenticationListener> authListeners;

	protected AuthenticationProviderAbstract() {
		this.authListeners = new ArrayList<>();
	}
	
	@Override
	public void addAuthenticationListener(AuthenticationListener authenticationListener) {
		synchronized (authListeners) {
			authListeners.add(authenticationListener);
		}
	}
	
	@Override
	public void removeAuthenticationListener(AuthenticationListener authenticationListener) {
		synchronized (authListeners) {
			authListeners.add(authenticationListener);
		}
	}
	
	private List<AuthenticationListener> getAuthenticationListener() {
		List<AuthenticationListener> cloned = new ArrayList<>();
		synchronized (authListeners) {
			authListeners.forEach(a->cloned.add(a));
		}
		return cloned;
	}
	
	@Override
	public void raiseUnauthenticated(ServiceError x) {
		QueryContext<String> authCxt = new QueryContext();
		authCxt.setServiceError(x);
		fireAuthenticationEvent(authCxt);
	}
	
	protected void fireAuthenticationEvent(QueryContext<String> authCxt) {
		List<AuthenticationListener> clonedAuthListeners = getAuthenticationListener();
		if (clonedAuthListeners != null) {
			clonedAuthListeners.stream().forEach(l->l.authenticate(new AuthenticationEvent(AuthenticationProviderAbstract.this, authCxt)));
		}
	}
}
