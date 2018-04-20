/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.event;

import java.util.EventObject;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author alexs
 */
public class AuthenticationEvent extends EventObject {

    private static final long serialVersionUID = -161388553183748994L;
    
	private final QueryContext<String> authCxt;
	
	public AuthenticationEvent(Object source, QueryContext<String> authCxt) {
		super(source);
		this.authCxt = authCxt;
	}
	
	public QueryContext<String> getAuthCxt() {
		return authCxt;
	}
}
