/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 * @author Aleksey Sukhorukov
 */

package ru.kontur.extern_api.sdk.validator;

import ru.kontur.extern_api.sdk.adaptor.Query;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

import java.text.MessageFormat;

public class ParamExists<R> implements Query<R> {
    private Query<R> query;
    private String paramName;

    public ParamExists(String paramName, Query<R> query ) {
        this.query = query;
        this.paramName = paramName;
    }

    @Override
    public QueryContext<R> apply(QueryContext<?> cxt) {
        if (cxt.isFail()) {
            return new QueryContext<>(cxt, cxt.getEntityName());
        }

        Object paramObject = cxt.get(paramName);
        if (paramObject == null) {
            return new QueryContext<R>(cxt, cxt.getEntityName()).setServiceError(MessageFormat.format("There is no parameter {0} in the context.", paramName));
        }
        return query.apply(cxt);
    }
}
