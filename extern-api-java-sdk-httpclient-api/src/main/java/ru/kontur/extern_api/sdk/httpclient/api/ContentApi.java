package ru.kontur.extern_api.sdk.httpclient.api;
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

import okhttp3.Headers;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;
import retrofit2.http.Streaming;
import ru.kontur.extern_api.sdk.GsonProvider;
import ru.kontur.extern_api.sdk.adaptor.ApiResponse;
import ru.kontur.extern_api.sdk.httpclient.ApiResponseConverter;
import ru.kontur.extern_api.sdk.httpclient.JsonSerialization;
import ru.kontur.extern_api.sdk.httpclient.LibapiResponseConverter;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@JsonSerialization(GsonProvider.LIBAPI)
@ApiResponseConverter(LibapiResponseConverter.class)
public interface ContentApi {

    @GET("v1/{accountId}/contents/{contentId}")
    CompletableFuture<ResponseBody> downloadContent(
            @Path("accountId") UUID accountId,
            @Path("contentId") UUID contentId
    );

    @GET("v1/{accountId}/contents/{contentId}")
    CompletableFuture<ResponseBody> downloadContentByPart(
            @Path("accountId") UUID accountId,
            @Path("contentId") UUID contentId,
            @Header("Range") String range
    );

    @Streaming
    @GET("v1/{accountId}/contents/{contentId}")
    CompletableFuture<ResponseBody> downloadContentByStream(
            @Path("accountId") UUID accountId,
            @Path("contentId") UUID contentId,
            @Header("Range") String range
    );
}
