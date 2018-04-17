/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.event;

import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.EventObject;


/**
 * @author alexs
 */
public class AuthenticationEvent extends EventObject {

    private final QueryContext<String> authCxt;

    public AuthenticationEvent(Object source, QueryContext<String> authCxt) {
        super(source);
        this.authCxt = authCxt;
    }

    public QueryContext<String> getAuthCxt() {
        return authCxt;
    }
}
