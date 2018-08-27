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
package ru.kontur.extern_api.sdk.service.transport.adaptor.httpclient;

import java.util.function.Supplier;

import ru.kontur.extern_api.sdk.service.transport.adaptor.Adaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.GsonProvider;

/**
 *
 * @author alexs
 */
class BaseAdaptor implements Adaptor {
    Supplier<HttpClient> httpClientSupplier;
    
    HttpClient configureTransport(QueryContext<?> cxt) {
        HttpClient httpClient = httpClientSupplier.get();
        httpClient
            .setServiceBaseUri(cxt.getServiceBaseUriProvider().getUri())
            .acceptAccessToken(cxt.getAuthPrefix(),cxt.getSessionId())
            .acceptApiKey(cxt.getApiKeyProvider().getApiKey())
            .setGson(GsonProvider.getGson());

        return httpClient;
    }

    @Override
    public HttpClient getHttpClient() {
        if (httpClientSupplier != null) {
            return httpClientSupplier.get();
        }
        else {
            return null;
        }
    }

    @Override
    public void setHttpClient(Supplier<HttpClient> httpClient) {
        httpClientSupplier = httpClient;
    }
}
