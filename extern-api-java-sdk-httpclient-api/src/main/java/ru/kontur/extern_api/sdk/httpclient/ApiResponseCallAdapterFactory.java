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

package ru.kontur.extern_api.sdk.httpclient;

import com.google.gson.Gson;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.concurrent.CompletableFuture;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;

/**
 * Inspired by {@code retrofit2.adapter.java8.Java8CallAdapterFactory}.
 * Augmented with {@link ApiResponse} result and annotated converters.
 */
public final class ApiResponseCallAdapterFactory extends CallAdapter.Factory {

    public static ApiResponseCallAdapterFactory create(Gson gson, ResponseConverter converter) {
        return new ApiResponseCallAdapterFactory(gson, converter);
    }

    private final Gson gson;
    private final ResponseConverter converter;

    private ApiResponseCallAdapterFactory(Gson gson, ResponseConverter converter) {
        this.gson = gson;
        this.converter = converter;
    }

    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (getRawType(returnType) != CompletableFuture.class) {
            return null;
        }
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalStateException("CompletableFuture return type must be parameterized"
                    + " as CompletableFuture<Foo> or CompletableFuture<? extends Foo>");
        }
        Type innerType = getParameterUpperBound(0, (ParameterizedType) returnType);

        if (getRawType(innerType) != ApiResponse.class) {
            // Generic type is not Response<T>. Use it for body-only adapter.
            return new BodyCallAdapter<>(innerType, gson, converter);
        }

        // Generic type is Response<T>. Extract T and create the Response version of the adapter.
        if (!(innerType instanceof ParameterizedType)) {
            throw new IllegalStateException("Response must be parameterized"
                    + " as Response<Foo> or Response<? extends Foo>");
        }
        Type responseType = getParameterUpperBound(0, (ParameterizedType) innerType);
        return new ResponseCallAdapter<>(responseType, gson, converter);
    }

    private static final class BodyCallAdapter<R> implements CallAdapter<R, CompletableFuture<R>> {

        private final Type responseType;
        private final Gson gson;
        private final ResponseConverter converter;

        BodyCallAdapter(Type responseType, Gson gson, ResponseConverter converter) {
            this.responseType = responseType;
            this.gson = gson;
            this.converter = converter;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public CompletableFuture<R> adapt(final Call<R> call) {
            final CompletableFuture<R> future = new CompletableFuture<R>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    if (mayInterruptIfRunning) {
                        call.cancel();
                    }
                    return super.cancel(mayInterruptIfRunning);
                }
            };

            call.enqueue(new Callback<R>() {
                @Override
                public void onResponse(Call<R> call, Response<R> response) {
                    if (response.isSuccessful()) {
                        future.complete(response.body());
                    } else {
                        future.completeExceptionally(
                                converter.toApiResponse(gson, response).asApiException()
                        );
                    }
                }

                @Override
                public void onFailure(Call<R> call, Throwable t) {
                    future.completeExceptionally(t);
                }
            });

            return future;
        }
    }

    private static final class ResponseCallAdapter<R>
            implements CallAdapter<R, CompletableFuture<ApiResponse<R>>> {

        private final Type responseType;
        private final Gson gson;
        private final ResponseConverter converter;

        ResponseCallAdapter(Type responseType, Gson gson, ResponseConverter converter) {
            this.responseType = responseType;
            this.gson = gson;
            this.converter = converter;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        public CompletableFuture<ApiResponse<R>> adapt(final Call<R> call) {
            final CompletableFuture<Response<R>> future = new CompletableFuture<Response<R>>() {
                @Override
                public boolean cancel(boolean mayInterruptIfRunning) {
                    if (mayInterruptIfRunning) {
                        call.cancel();
                    }
                    return super.cancel(mayInterruptIfRunning);
                }
            };

            call.enqueue(new Callback<R>() {
                @Override
                public void onResponse(Call<R> call, Response<R> response) {
                    future.complete(response);
                }

                @Override
                public void onFailure(Call<R> call, Throwable t) {
                    future.completeExceptionally(t);
                }
            });

            return future
                    .thenApply(response -> converter.toApiResponse(gson, response))
                    .exceptionally(ApiResponse::error);
        }
    }
}
