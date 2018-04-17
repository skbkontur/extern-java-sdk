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

package ru.skbkontur.sdk.extern.providers.crypt.rsa;

import com.argos.asn1.Asn1Exception;
import com.argos.cipher.asn1ext.X509certificate;
import ru.argosgrp.cryptoservice.CryptoException;
import ru.argosgrp.cryptoservice.pkcs7.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.skbkontur.sdk.extern.Messages;
import ru.skbkontur.sdk.extern.providers.CryptoProvider;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;

import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR;
import static ru.skbkontur.sdk.extern.Messages.C_CRYPTO_ERROR_KEY_NOT_FOUND;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.CONTENT;


/**
 * @author alexs
 */
public class CryptoProviderRSA implements CryptoProvider {

    private final Map<String, String> DIGEST_ALGORITHMS = new HashMap<String, String>() {
        private static final long serialVersionUID = 1L;

        {
            put("1.2.840.113549.1.1.5", "SHA1");
            put("1.3.14.3.2.29", "SHA1");
            put("1.2.840.113549.1.1.11", "SHA-256");
            put("1.2.840.113549.1.1.12", "SHA-384");
            put("1.2.840.113549.1.1.13", "SHA-512");
        }
    };

    private final String keyPass;
    private final String keyStorePass;
    private final Map<String, KeyPair> cacheSignKey;

    private KeyStore keyStore;
    private Supplier<String> keyStoreProvider;

    public CryptoProviderRSA(String keyStorePass, String keyPass) {
        // default key store source: the jre key store
        keyStoreProvider = () -> System.getProperty("java.home") + File.separator + "lib" + File.separator + "security" + File.separator + "cacerts";
        // cache for keys
        this.cacheSignKey = new ConcurrentHashMap<>();
        //
        this.keyStorePass = keyStorePass;
        this.keyPass = keyPass;
    }

    public Supplier<String> getKeyStoreProvider() {
        return keyStoreProvider;
    }

    public void setKeyStoreProvider(Supplier<String> keyStoreProvider) {
        this.keyStore = null;
        cacheSignKey.clear();
        this.keyStoreProvider = keyStoreProvider;
    }

    public CryptoProviderRSA keyStoreProvider(Supplier<String> keyStoreProvider) {
        this.keyStore = null;
        cacheSignKey.clear();
        this.keyStoreProvider = keyStoreProvider;
        return this;
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

            KeyPair key = getKeyByThumbprint(thumbprint);
            if (key == null) {
                return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint));
            }

            byte[] content = cxt.getContent();

            byte[] signature = sign(key, content);

            return new QueryContext<byte[]>().setResult(signature, CONTENT);
        } catch (Asn1Exception | GeneralSecurityException | CryptoException | IOException x) {
            return new QueryContext<byte[]>().setServiceError(Messages.get(C_CRYPTO_ERROR, x.getMessage()), x);
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

            KeyPair key = getKeyByThumbprint(thumbprint);
            if (key == null) {
                return new QueryContext<byte[]>().setServiceError(MessageFormat.format(C_CRYPTO_ERROR_KEY_NOT_FOUND, thumbprint));
            }

            return new QueryContext<byte[]>().setContent(key.getX509() == null ? null : key.getX509().getEncoded());
        } catch (Asn1Exception | GeneralSecurityException | IOException x) {
            return new QueryContext<byte[]>().setServiceError(ServiceError.ErrorCode.business, MessageFormat.format(C_CRYPTO_ERROR, x.getMessage()), 0, null, null);
        }
    }

    @Override
    public CompletableFuture<QueryContext<byte[]>> decryptAsync(String thumbprint, byte[] content) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public QueryContext<byte[]> decrypt(QueryContext<byte[]> cxt) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private byte[] sign(KeyPair key, byte[] content) throws GeneralSecurityException, Asn1Exception, CryptoException {
        // OID для SHA & RSA
        String sha_rsa_oid = PKCS7.getCertificateAlgorithmOID(key.x509.getEncoded());
        // алгоритм хеширования контента
        String digestAlgorithm = DIGEST_ALGORITHMS.get(sha_rsa_oid);
        // хеш контента
        byte[] digest = this.getDigest(content, digestAlgorithm);
        // массив байт подписываемых атрибутов
        byte[] authenticatedAttributes = PKCS7.createRSAAuthenticatedAttributes(digest);
        // вычисление подписи
        // получаем экземпляр механизма подписи
        Signature signer = Signature.getInstance(sha_rsa_oid);
        // инициализируем механизм подписи секретным ключом
        signer.initSign(key.getPrivateKey());
        // вычисляем подпись
        signer.update(authenticatedAttributes);
        // извлекаем подпись
        byte[] signature = signer.sign();
        // создаем сообщение PKCS#7
        return PKCS7.createSignCMS(key.getX509().getEncoded(), signature, authenticatedAttributes, null);
    }

    private byte[] sign_(KeyPair key, byte[] content) throws GeneralSecurityException, Asn1Exception, CryptoException {
        // OID для SHA & RSA
        String sha_rsa_oid = PKCS7.getCertificateAlgorithmOID(key.x509.getEncoded());
        // получаем экземпляр механизма подписи
        Signature signer = Signature.getInstance(sha_rsa_oid);
        // инициализируем механизм подписи секретным ключом
        signer.initSign(key.getPrivateKey());
        // вычисляем подпись
        signer.update(content);
        // извлекаем подпись
        byte[] signature = signer.sign();
        // создаем сообщение PKCS#7
        return PKCS7.createSignCMS(key.getX509(), signature, null);
    }

    private KeyStore getKeyStore() throws IOException, GeneralSecurityException {
        if (keyStore == null) {
            // acquires an java key store instance
            this.keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            // initialize the key store
            try (FileInputStream is = new FileInputStream(keyStoreProvider.get())) {
                this.keyStore.load(is, keyStorePass == null ? new char[0] : keyStorePass.toCharArray());
            }
        }
        return keyStore;
    }

    private KeyPair getKeyByThumbprint(String thumbprint) throws GeneralSecurityException, Asn1Exception, IOException {
        KeyPair key = cacheSignKey.get(thumbprint);
        if (key == null) {
            Enumeration<String> aliases = getKeyStore().aliases();
            if (aliases != null) {
                byte[] goal = IOUtil.hexToBytes(thumbprint);

                while (key == null && aliases.hasMoreElements()) {
                    String alias = aliases.nextElement();
                    X509Certificate x509 = (X509Certificate) keyStore.getCertificate(alias);
                    byte[] theThumbprint = getThumbprint(x509);
                    if (Arrays.equals(goal, theThumbprint)) {
                        PrivateKey privateKey = (PrivateKey) keyStore.getKey(alias, keyPass == null ? new char[0] : keyPass.toCharArray());
                        if (privateKey != null) {
                            key = new KeyPair();
                            key.setPrivateKey(privateKey);
                            key.setX509(x509);
                            key.setSigningAlgorithm(extractSigningAlgorithm(x509));
                            cacheSignKey.put(thumbprint, key);
                        }
                    }
                }
            }
        }
        return key;
    }

    private byte[] getThumbprint(X509Certificate x509) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-1");
            return digest.digest(x509.getEncoded());
        } catch (GeneralSecurityException unexpected) {
            return "unknown thumbprint".getBytes();
        }
    }

    private String extractSigningAlgorithm(X509Certificate x509Cert) throws CertificateEncodingException, Asn1Exception {
        return PKCS7.getEncryptOID(new X509certificate(x509Cert.getEncoded()));
    }

    private byte[] getDigest(byte[] data, String digestAlgorithm) throws GeneralSecurityException {
        MessageDigest md = MessageDigest.getInstance(digestAlgorithm);
        md.update(data);
        return md.digest();
    }

    class KeyPair {

        private PrivateKey privateKey;

        private X509Certificate x509;

        private String signingAlgorithm;

        public PrivateKey getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(PrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        public X509Certificate getX509() {
            return x509;
        }

        public void setX509(X509Certificate x509) {
            this.x509 = x509;
        }

        public String getSigningAlgorithm() {
            return signingAlgorithm;
        }

        public void setSigningAlgorithm(String signingAlgorithm) {
            this.signingAlgorithm = signingAlgorithm;
        }
    }
}
