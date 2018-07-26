/*
 * The MIT License
 *
 * Copyright 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ru.kontur.extern_api.sdk.provider.auth;

import ru.kontur.extern_api.sdk.Environment;
import ru.kontur.extern_api.sdk.ServiceError;
import ru.kontur.extern_api.sdk.event.AuthenticationListener;
import ru.kontur.extern_api.sdk.provider.AuthenticationProvider;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.SESSION_ID;

/**
 *
 * @author Aleksey Sukhorukov
 */
public class EngineAuthenticationProvider implements AuthenticationProvider {

    private final AuthenticationProvider authenticationProvider;
    private final Environment env;

    public EngineAuthenticationProvider(AuthenticationProvider authenticationProvider, Environment env) {
        this.authenticationProvider = authenticationProvider;
        this.env = env;
    }

    public AuthenticationProvider getOriginAuthenticationProvider() {
        return authenticationProvider;
    }

    @Override
    public QueryContext<String> sessionId() {
        if (env.accessToken == null) {
            return authenticationProvider.sessionId();
        }
        else {
            return new QueryContext<String>().setResult(env.accessToken, SESSION_ID);
        }
    }

    @Override
    public String authPrefix() {
        return authenticationProvider.authPrefix();
    }

    @Override
    public AuthenticationProvider httpClient(HttpClient httpClient) {
        return authenticationProvider.httpClient(httpClient);
    }
    
    @Override
    public void addAuthenticationListener(AuthenticationListener authListener) {
        authenticationProvider.addAuthenticationListener(authListener);
    }

    @Override
    public void removeAuthenticationListener(AuthenticationListener authListener) {
        authenticationProvider.removeAuthenticationListener(authListener);
    }

    @Override
    public void raiseUnauthenticated(ServiceError x) {
        authenticationProvider.raiseUnauthenticated(x);
    }
}
 