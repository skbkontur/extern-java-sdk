/*
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package ru.kontur.extern_api.sdk.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import ru.kontur.extern_api.sdk.adaptor.ApiException;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;

public class QueryContextUtils {

    public static <T> QueryContext<T> join(CompletableFuture<QueryContext<T>> future) {
        try {
            return future.exceptionally(QueryContextUtils::completeCareful).get();
        } catch (InterruptedException | ExecutionException e) {
            return new QueryContext<T>().setServiceError(e.getMessage(), e);
        }
    }

    public static <T> QueryContext<T> join(
            QueryContext<?> parent,
            ApiResponse<T> response,
            String resultKey) {

        if (response.isSuccessful()) {
            return new QueryContext<T>(parent, resultKey).setResult(response.getData(), resultKey);
        }

        return new QueryContext<T>(parent, resultKey).setServiceError(response.asApiException());
    }


    public static <T> QueryContext<T> join(ApiResponse<T> response, String resultKey) {
        if (response.isSuccessful()) {
            return new QueryContext<>(resultKey, response.getData());
        }
        return new QueryContext<T>(resultKey).setServiceError(response.asApiException());
    }

    public static <T> Function<ApiResponse<T>, QueryContext<T>> contextAdaptor(String withKey) {
        return response -> join(response, withKey);
    }

    public static <T> QueryContext<T> completeCareful(Throwable t) {
        Throwable cause = t.getCause();
        if (t instanceof CompletionException && cause != null) {
            return QueryContext.error(cause);
        }
        if (cause instanceof ApiException) {
            return QueryContext.error(cause);
        }
        return QueryContext.error(t);
    }

}
