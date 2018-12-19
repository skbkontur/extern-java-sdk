/*
 * MIT License
 *
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
 */

package ru.kontur.extern_api.sdk.service;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.model.CertificateList;
import ru.kontur.extern_api.sdk.model.OrgFilter;


/**
 * Предназначен для получения списка сертификатов
 *
 * @author Aleksey Sukhorukov
 * @see QueryContext
 */
public interface CertificateService {

    /**
     * <p>GET /v1/{accountId}/certificates</p>
     * Асинхронный метод возвращает список сертификатов
     *
     * @return список сертификатов
     * @see CertificateList
     */
    CompletableFuture<QueryContext<CertificateList>> getCertificateListAsync(int skip, int take);

    /**
     * <p>GET /v1/{accountId}/certificates</p>
     * Асинхронный метод возвращает список сертификатов
     *
     * @return список сертификатов
     * @see CertificateList
     * @deprecated use {@link CertificateService#getCertificateListAsync(int, int)} instead
     */
    @Deprecated
    CompletableFuture<QueryContext<CertificateList>> getCertificateListAsync();

    /**
     * <p>GET /v1/{accountId}/certificates</p>
     * Синхронный метод возвращает список сертификатов
     *
     * @param cxt контекст
     * @return список сертификатов
     * @see CertificateList
     * @deprecated use {@link CertificateService#getCertificateListAsync()}
     */
    @Deprecated
    QueryContext<CertificateList> getCertificateList(QueryContext<?> cxt);
}
