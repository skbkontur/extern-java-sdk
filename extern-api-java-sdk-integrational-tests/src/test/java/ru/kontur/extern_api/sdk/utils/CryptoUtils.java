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

import java.util.Base64;
import org.jetbrains.annotations.NotNull;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;

public class CryptoUtils {

    private final CryptoProvider cryptoProvider;

    private CryptoUtils(CryptoProvider cryptoProvider) {
        this.cryptoProvider = cryptoProvider;
    }

    public byte[] sign(String certThumbprint, byte[] bytes) {
        return cryptoProvider
                .sign(new QueryContext<byte[]>().setThumbprint(certThumbprint).setContent(bytes))
                .getOrThrow();
    }

    public byte[] decrypt(String certThumbprint, byte[] bytes) {
        return cryptoProvider
                .decrypt(new QueryContext<byte[]>().setThumbprint(certThumbprint).setContent(bytes))
                .getOrThrow();
    }

    public byte[] signBase64(String certThumbprint, String b64data) {
        return sign(certThumbprint, Base64.getDecoder().decode(b64data));
    }

    public String loadX509(String thumbprint) {
        byte[] certContent = loadCertContent(thumbprint);
        return Base64.getEncoder().encodeToString(certContent);
    }

    public byte[] loadCertContent(String thumbprint) {
        return cryptoProvider
                .getSignerCertificateAsync(thumbprint)
                .thenApply(QueryContext::ensureSuccess)
                .thenApply(QueryContext::getContent)
                .join();
    }

    @NotNull
    public static CryptoUtils with(@NotNull CryptoProvider cryptoProvider) {
        if (!isAdmin()) {
            throw new SecurityException("Admin rights required for using crypto operations");
        }

        return new CryptoUtils(cryptoProvider);
    }

    private static boolean isAdmin() {
        // tired to write it
        return true;
    }
}
