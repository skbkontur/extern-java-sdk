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

import java.util.concurrent.TimeUnit;
import okhttp3.logging.HttpLoggingInterceptor.Level;
import ru.kontur.extern_api.sdk.adaptor.AdaptorBundle;
import ru.kontur.extern_api.sdk.adaptor.DocflowsAdaptor;
import ru.kontur.extern_api.sdk.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.httpclient.retrofit.KonturConfiguredClient;
import ru.kontur.extern_api.sdk.provider.ProviderHolder;

public class HttpClientBundle implements AdaptorBundle {

    private final ProviderHolder providerHolder;
    private final KonturConfiguredClient konturConfiguredClient;

    public HttpClientBundle(ProviderHolder providerHolder) {
        this.providerHolder = providerHolder;
        this.konturConfiguredClient = new KonturConfiguredClient(Level.BODY)
                .setConnectTimeout(60, TimeUnit.SECONDS)
                .setReadTimeout(60, TimeUnit.SECONDS);
    }

    @Override
    public DocflowsAdaptor getDocflowsAdaptor() {
        DocflowsAdaptorImpl docflowsAdaptor = new DocflowsAdaptorImpl();
        docflowsAdaptor.setHttpClient(this::getHttpClientAdaptor);
        return docflowsAdaptor;
    }

    @Override
    public HttpClient getHttpClientAdaptor() {
        return new KonturHttpClient(konturConfiguredClient)
                .setUserAgentProvider(providerHolder.getUserAgentProvider());
    }
}
