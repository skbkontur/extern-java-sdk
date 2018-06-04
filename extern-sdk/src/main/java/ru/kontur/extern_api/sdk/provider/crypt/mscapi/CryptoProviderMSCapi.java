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

import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.Messages;
import ru.kontur.extern_api.sdk.provider.CryptoProvider;
import ru.kontur.extern_api.sdk.service.SDKException;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_INIT;
import static ru.kontur.extern_api.sdk.Messages.C_CRYPTO_ERROR_KEY_NOT_FOUND;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.CONTENT;


/**
 * @author AlexS
 */
public class CryptoProviderMSCapi implements CryptoProvider {

    private final CryptoService cryptoService;
    private final Map<String, Key> cacheSignKey;

    public CryptoProviderMSCapi() throws SDKException {
        try {
            cryptoService = new MSCapi(true);
            cacheSignKey = new ConcurrentHashMap<>();
        } catch (CryptoException x) {
            throw new SDKException(Messages.get(C_CRYPTO_ERROR_INIT), x);
        }
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> signAsync(String thumbprint, byte[] content) {
        return CompletableFuture.supplyAsync(() -> sign(new QueryContext<byte[]>().setThumbprint(thumbprint).setContent(content)));
    }

    @Override
    public QueryContext<byte[]> sign(QueryContext<byte[]> cxt) {
        try {
            String thumbprint = cxt.getThumbprint();

            Key key = getKeyByThumbprint(thumbprint);
            if (key == null) {
                return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint));
            }

            PKCS7 p7p = new PKCS7(cryptoService);

            byte[] content = cxt.getContent();

            return new QueryContext<byte[]>().setResult(p7p.sign(key, null, content, false), CONTENT);
        } catch (CryptoException x) {
            return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR, x.getMessage()));
        }
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> getSignerCertificateAsync(String thumbprint) {
        return CompletableFuture.supplyAsync(() -> getSignerCertificate(new QueryContext<byte[]>().setThumbprint(thumbprint)));
    }

    @Override
    public QueryContext<byte[]> getSignerCertificate(QueryContext<byte[]> cxt) {
        try {
            String thumbprint = cxt.getThumbprint();

            Key key = getKeyByThumbprint(thumbprint);
            if (key == null) {
                return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint));
            }

            return new QueryContext<byte[]>().setResult(key.getX509ctx(), CONTENT);
        } catch (CryptoException x) {
            return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR, x.getMessage()));
        }
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
        return CompletableFuture.supplyAsync(() -> decrypt(new QueryContext<byte[]>().setThumbprint(thumbprint).setContent(content)));
    }

    @Override
    public QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt) {
        try {
            String thumbprint = cxt.getThumbprint();

            Key key = getKeyByThumbprint(thumbprint);
            if (key == null) {
                return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint));
            }

            PKCS7 p7p = new PKCS7(cryptoService);

            byte[] content = cxt.getContent();

            return new QueryContext<byte[]>().setResult(p7p.decrypt(key, null, content), CONTENT);
        } catch (CryptoException x) {
            return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR, x.getMessage()));
        }
    }

    public void removeKey(String thumbprint) {
        cacheSignKey.remove(thumbprint);
    }

    public void removeAllKeys() {
        cacheSignKey.clear();
    }

    private Key getKeyByThumbprint(String thumbprint) throws CryptoException {
        Key key = cacheSignKey.get(thumbprint);
        if (key == null) {
            Key[] keys = cryptoService.getKeys();
            if (keys != null && keys.length > 0) {
                byte[] t = IOUtil.hexToBytes(thumbprint);
                key = Stream.of(keys).filter(k -> Arrays.equals(k.getThumbprint(), t)).findAny().orElse(null);
                if (key != null)
                    cacheSignKey.put(thumbprint, key);
            }
        }
        return key;
    }
}
