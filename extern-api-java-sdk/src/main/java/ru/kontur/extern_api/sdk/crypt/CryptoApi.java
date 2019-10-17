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

package ru.kontur.extern_api.sdk.crypt;

import static ru.kontur.extern_api.sdk.crypt.CryptoApiException.catchCryptoException;

import com.argos.asn1.Asn1Exception;
import com.argos.cipher.asn1ext.EnvelopedData;
import com.argos.cipher.asn1ext.PKCS7;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Stream;
import ru.argosgrp.cryptoservice.CryptoService;
import ru.argosgrp.cryptoservice.Key;
import ru.argosgrp.cryptoservice.mscapi.MSCapi;

public class CryptoApi {

    private static final Logger log = Logger.getLogger(CryptoApi.class.getSimpleName());

    // Probably bad design, but significantly improves performance
    private static List<Key> keyCache = null;

    private CryptoService cryptoService;

    private final X509CertificateFactory certificateFactory;


    public CryptoApi() throws CertificateException {
        CryptoService cryptoService = null;
        try {
            cryptoService = new MSCapi(true, CryptoService.GOST_2012_256_ALG_ID);
        } catch (Exception e) {
            log.warning(e.getMessage());
        }
        this.cryptoService = cryptoService;
        certificateFactory = new X509CertificateFactory();
    }

    public String getThumbprint(byte[] cert) {
        return certificateFactory.getThumbprint(cert);
    }

    public String getThumbprint(String base64) {
        return certificateFactory.getThumbprint(Base64.getDecoder().decode(base64));
    }

    public static Stream<String> getSerialNumbers(byte[] encryptedData) throws Asn1Exception {
        EnvelopedData envelopedData = new PKCS7(encryptedData).getEnvelopedData();

        Stream.Builder<String> builder = Stream.builder();
        for (int i = 0; i < envelopedData.sizeRecipient(); i++) {
            builder.add(envelopedData.getRecipientInfo(i).getIssuerSerialNumber().toPrintableString());
        }
        return builder.build();
    }

    private static ArrayList<CertificateWrapper> localCertsCache;

    public synchronized List<CertificateWrapper> getCertificatesInstalledLocally()
            throws CertificateException {

        if (localCertsCache == null) {
            localCertsCache = new ArrayList<>();
            List<Key> installedKeys = getInstalledKeys(false);
            for (Key key : installedKeys) {
                localCertsCache.add(certificateFactory.create(key.getX509ctx()));
            }
        }

        return localCertsCache;
    }

    public CertificateWrapper asX509Wrapper(Key key) throws CertificateException {
        return certificateFactory.create(key.getX509ctx());
    }

    public CryptoService getCryptoService() {
        if (cryptoService == null) {
            throw new IllegalStateException("crypto service unavailable");
        }
        return cryptoService;
    }

    public List<Key> getInstalledKeys(boolean refreshCache) {
        if (keyCache == null || refreshCache || true) {
            log.info("Installed keys loading...");
            CryptoService cryptoService = getCryptoService();
            CryptoExceptionThrows<Key[]> keysResult = cryptoService::getKeys;
            Key[] keys = catchCryptoException(keysResult);
            keyCache = Arrays.asList(keys);
        }
        log.info("Found " + keyCache.size() + " installed keys");

        return keyCache;
    }

}
