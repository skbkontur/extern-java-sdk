package ru.kontur.extern_api.sdk.it.utils;

import ru.kontur.extern_api.sdk.adaptor.QueryContext;

public class SessionIdAuth extends AuthenticationProviderAdaptor {

    private final String sessionId;

    public SessionIdAuth(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public QueryContext<String> sessionId() {
        return new QueryContext<String>().setResult(sessionId, QueryContext.SESSION_ID);
    }
}
