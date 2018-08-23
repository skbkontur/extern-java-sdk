package ru.kontur.extern_api.sdk.service.transport.adaptor;

import java.util.List;

public interface QueryForComplex<R>{

    QueryContext<R> apply(QueryContext<?> context);
    QueryContext<List<R>> applyAtAll(QueryContext<?> context);
}
