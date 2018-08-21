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

package ru.kontur.extern_api.sdk.provider;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

/**
 * Интерфейс предоставляет криптографические операции: подписать, расшифровать.
 * @author Aleksey Sukhorukov
 */
public interface CryptoProvider {

    /**
     * Асинхронный метод для вычисления отсоединной подписи для переданного массива байт
     * @param thumbprint отпечаток сертификата ключа подписи
     * @param content массив байт, для которого производится вычисление подписи
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит вычисленныю подпись, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content);

    /**
     * Синхронный метод для вычисления отсоединной подписи для переданного массива байт
     * @param cxt контекст, должен содержать:
     * <ul>
     *      <li>отпечаток сертификата, установленный с помощью метода {@link QueryContext#setCertificate(String)};</li>
     *      <li>контент, для которого необходимо вычислить подпись, установленный с помощью метода {@link QueryContext#setContent(byte[])}.</li>
     * </ul>
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит вычисленныю подпись, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    QueryContext<byte[]> sign(QueryContext<byte[]> cxt);

    /**
     * Асинхронный метод для получения сертификата по отпечатку в формате X.509
     * @param thumbprint отпечаток сертификата
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит сертификат, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint);

    /**
     * Ссинхронный метод для получения сертификата по отпечатку в формате X.509
     * @param cxt контекст, должен содержать отпечаток сертификата, установленный с помощью метода {@link QueryContext#setThumbprint(String)}
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит сертификат, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> cxt);

    /**
     * Асинхронный метод для расшифрования зашифрованного в формате PKCS#7 контента
     * @param thumbprint отпечаток сертификата закрытого ключа
     * @param content массив байт зашифрованного в формате PKCS#7 контента
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит массив байт для расшифрованного контента, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content);

    /**
     * Синхронный метод для расшифрования зашифрованного в формате PKCS#7 контента
     * @param cxt контекст, должен содержать:
     * <ul>
     *      <li>отпечаток сертификата, установленный с помощью метода {@link QueryContext#setCertificate(String)};</li>
     *      <li>зашифрованный контент, который необходимо расшифровать, установленный с помощью метода {@link QueryContext#setContent(byte[])}.</li>
     * </ul>
     * @return контекст. Если метод {@link QueryContext#isFail()} вернет false, то контекст содержит массив байт для расшифрованного контента, иначе метод {@link QueryContext#getServiceError()} вернет ошибку.
     */
    QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt);
}
