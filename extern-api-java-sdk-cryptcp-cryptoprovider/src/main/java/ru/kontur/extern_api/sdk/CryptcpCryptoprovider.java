/*
 * Copyright (c) 2019 SKB Kontur
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

package ru.kontur.extern_api.sdk;

import java.util.concurrent.CompletableFuture;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.utils.ThrowableSupplier;


public class CryptcpCryptoprovider implements CryptoProvider {

    private CryptcpApi cryptcpApi;

    /**
     * Creates cryptoprovider that uses <a href='https://www.cryptopro.ru/products/other/cryptcp'>cryptocp</a>
     * to run basic crypto operations.
     *
     * Use {@link CryptcpCryptoprovider#CryptcpCryptoprovider(String, String)} if error output corrupted
     *
     * @param executable the name of the executable (cryptocp.exe or cryptocp.x64.exe)
     *         or full path to executable (C:\\users\\user\\desktop\\cryptcp...)
     *         if one cannot be found via PATH variable
     */
    public CryptcpCryptoprovider(String executable) {
        this(executable, "CP866");
    }

    /**
     * Creates cryptoprovider that uses <a href='https://www.cryptopro.ru/products/other/cryptcp'>cryptocp</a>
     * to run basic crypto operations.
     *
     * @param executable the name of the executable (cryptocp.exe or cryptocp.x64.exe)
     *         or full path to executable (C:\\users\\user\\desktop\\cryptcp...)
     *         if one cannot be found via PATH variable
     * @param outputEncoding charset name used to decode cryptcp output.
     *         A.k.a. the name of a supported {@link java.nio.charset.Charset charset}
     */
    public CryptcpCryptoprovider(String executable, String outputEncoding) {
        this.cryptcpApi = new CryptcpApi(executable, outputEncoding);
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content) {
        return launch(() -> cryptcpApi.sign(thumbprint, content));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint) {
        return  launch(() -> cryptcpApi.getCertificate(thumbprint));
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
        return  launch(() -> cryptcpApi.decrypt(thumbprint, content));
    }

    private CompletableFuture<QueryContext<byte[]>> launch(
            ThrowableSupplier<byte[], Exception> contentSupplier
    ) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return new QueryContext<>(QueryContext.CONTENT, contentSupplier.get());
            } catch (Exception e) {
                return QueryContext.error(e);
            }
        });
    }

    @Override
    public QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> cxt) {
        return getSignerCertificateAsync(cxt.require(QueryContext.THUMBPRINT)).join();
    }

    @Override
    public QueryContext<byte[]> sign(QueryContext<byte[]> cxt) {
        return signAsync(cxt.require(QueryContext.THUMBPRINT), cxt.require(QueryContext.CONTENT)).join();
    }

    @Override
    public QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt) {
        return decryptAsync(cxt.require(QueryContext.THUMBPRINT), cxt.require(QueryContext.CONTENT)).join();
    }
}
