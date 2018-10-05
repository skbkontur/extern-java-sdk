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

import java.io.ByteArrayInputStream;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import ru.argosgrp.cryptoservice.utils.IOUtil;
import ru.kontur.extern_api.sdk.utils.UncheckedSupplier;

public class X509CertificateFactory {

    private static final String ALGORITHM = "SHA-1";
    private final CertificateFactory factory;
    private final MessageDigest digest;

    public X509CertificateFactory() throws CertificateException {
        factory = CertificateFactory.getInstance("X.509");
        digest = UncheckedSupplier.get(() -> MessageDigest.getInstance(ALGORITHM));
    }

    public CertificateWrapper create(byte[] certBytes) throws CertificateException {
        X509Certificate x509Certificate = (X509Certificate) factory
                .generateCertificate(new ByteArrayInputStream(certBytes));

        return new CertificateWrapper(x509Certificate, getThumbprint(x509Certificate.getEncoded()));
    }


    public String getThumbprint(byte[] cert) {
        digest.update(cert);
        String s = IOUtil.bytesToHex(digest.digest()).toLowerCase();
        digest.reset();
        return s;
    }

}
