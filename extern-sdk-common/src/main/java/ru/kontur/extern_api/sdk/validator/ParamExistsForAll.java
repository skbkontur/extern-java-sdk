package ru.kontur.extern_api.sdk.validator;

import java.text.MessageFormat;
import java.util.List;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryForComplex;

public class ParamExistsForAll<R> implements QueryForComplex<R> {

    private QueryForComplex<R> queryForAll;
    private String paramName;

    public ParamExistsForAll(String paramName, QueryForComplex<R> query) {
        this.queryForAll = query;
        this.paramName = paramName;
    }

    @Override
    public QueryContext<R> apply(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        Object paramObject = cxt.get(paramName);
        if (paramObject == null) {
            String format = MessageFormat
                    .format("There is no parameter {0} in the context.", paramName);
            return new QueryContext<R>(cxt, cxt.getEntityName()).setServiceError(format);
        }
        return queryForAll.apply(cxt);
    }

    @Override
    public QueryContext<List<R>> applyAtAll(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        Object paramObject = cxt.get(paramName);
        if (paramObject == null) {
            String format = MessageFormat
                    .format("There is no parameter {0} in the context.", paramName);

            return new QueryContext<List<R>>(cxt, cxt.getEntityName()).setServiceError(format);
        }
        return queryForAll.applyAtAll(cxt);
    }
}
