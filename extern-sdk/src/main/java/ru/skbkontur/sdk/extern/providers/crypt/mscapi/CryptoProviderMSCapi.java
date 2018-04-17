/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.providers.crypt.mscapi;

import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.Messages;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.service.SDKException;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_INIT;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_KEY_NOT_FOUND;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.CONTENT;


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
        return CompletableFuture
                .supplyAsync(
                        () ->
                        {
                            return sign(
                                    new QueryContext<byte[]>()
                                            .setThumbprint(thumbprint)
                                            .setContent(content)
                            );
                        }
                );
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
        return CompletableFuture
                .supplyAsync(
                        () ->
                        {
                            return getSignerCertificate(
                                    new QueryContext<byte[]>()
                                            .setThumbprint(thumbprint)
                            );
                        }
                );
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
        return CompletableFuture
                .supplyAsync(
                        () ->
                        {
                            return decrypt(
                                    new QueryContext<byte[]>()
                                            .setThumbprint(thumbprint)
                                            .setContent(content)
                            );
                        }
                );
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
