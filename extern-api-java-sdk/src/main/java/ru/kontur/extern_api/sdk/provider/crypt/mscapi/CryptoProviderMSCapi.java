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

package ru.kontur.extern_api.sdk.provider.crypt.mscapi;

import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_INIT;
import static ru.kontur.extern_api.sdk.adaptor.QueryContext.CONTENT;

import java.security.cert.CertificateException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.adaptor.QueryContext;
import ru.kontur.extern_api.sdk.crypt.CryptoApi;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.service.SDKException;

public class CryptoProviderMSCapi implements CryptoProvider {

    private final CryptoApi cryptoApi;
    private final CryptoService cryptoService;
    private static HashMap<String, Key> keysCache = new HashMap<>();

    private static CryptoApi cryptoApiSingletonInstance;

    public CryptoProviderMSCapi() throws SDKException {
        try {
            cryptoApi = cryptoApiSingletonInstance != null
                ? cryptoApiSingletonInstance
                : new CryptoApi();
            cryptoApiSingletonInstance = cryptoApi;

            cryptoService = cryptoApi.getCryptoService();

        } catch (CertificateException x) {
            throw new SDKException(Messages.get(C_CRYPTO_ERROR_INIT), x);
        }
    }

    public CryptoProviderMSCapi withKeysInCache(Key[] initialPrivateKeys) {
        addKeysToCache(initialPrivateKeys);
        return this;
    }

    public static void addKeysToCache(Key[] initialPrivateKeys) {
        for (Key key : initialPrivateKeys) {
            String thumbprint = IOUtil.bytesToHex(key.getThumbprint());
            keysCache.put(thumbprint.toLowerCase(), key);
        }
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content) {
        return CompletableFuture.supplyAsync(() -> sign(new QueryContext<byte[]>()
                                                                .setThumbprint(thumbprint)
                                                                .setContent(content)
        ));
    }

    @Override
    public QueryContext<byte[]> sign(QueryContext<byte[]> cxt) {
        return new QueryContext<byte[]>().setResultAware(CONTENT, () -> {
            Key key = getKeyByThumbprint(cxt.getThumbprint());
            byte[] content = cxt.getContent();

            return new PKCS7KeAPi(cryptoService).sign(key, null, content, false);
        });
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint) {
        return CompletableFuture.supplyAsync(() -> getSignerCertificate(new QueryContext<byte[]>()
                                                                                .setThumbprint(thumbprint)));
    }

    @Override
    public QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> cxt) {
        return new QueryContext<byte[]>().setResultAware(CONTENT, () ->
                getKeyByThumbprint(cxt.getThumbprint()).getX509ctx()
        );
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
        return CompletableFuture.supplyAsync(
                () ->
                        decrypt(new QueryContext<byte[]>()
                                        .setThumbprint(thumbprint)
                                        .setContent(content)
                        )
        );
    }

    @Override
    public QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt) {
        return new QueryContext<byte[]>().setResultAware(CONTENT, () -> {
            Key key = getKeyByThumbprint(cxt.getThumbprint());
            byte[] content = cxt.getContent();

            return decrypt(key, content);
        });
    }

    private byte[] decrypt(Key key, byte[] content) throws CryptoException, InterruptedException {
        PKCS7KeAPi pkcs7 = new PKCS7KeAPi(cryptoService);
        byte[] decrypted = pkcs7.decrypt(key, null, content);
        return decrypted;
    }

    @NotNull
    private Key getKeyByThumbprint(@NotNull String thumbprint) throws CryptoException {
        thumbprint = thumbprint.toLowerCase();
        if (keysCache.containsKey(thumbprint)) {
            System.out.printf("Certificate %s found in local cache. \n", thumbprint);
            return keysCache.get(thumbprint);
        }

        List<Key> installedKeys = cryptoApi.getInstalledKeysCache(false);
        Key key = getKeyFromCacheByThumbprint(thumbprint, installedKeys);
        if (key != null)
            return key;

        System.out.printf("Certificate %s not found in local cache. Will refresh cache \n", thumbprint);
        installedKeys = cryptoApi.getInstalledKeysCache(true);
        key = getKeyFromCacheByThumbprint(thumbprint, installedKeys);
        if (key != null)
            return key;

        throw new CryptoException("Cannot find locally installed certificate with thumbprint " + thumbprint);
    }

    @Nullable
    private static Key getKeyFromCacheByThumbprint(@NotNull String thumbprint, List<Key> installedKeys) {
        for (Key key : installedKeys) {
            if (thumbprint.compareToIgnoreCase(IOUtil.bytesToHex(key.getThumbprint())) == 0) {
                keysCache.put(thumbprint, key);
                return key;
            }
        }
        return null;
    }
}