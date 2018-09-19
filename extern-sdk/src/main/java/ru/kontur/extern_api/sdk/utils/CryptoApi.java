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

import com.argos.asn1.Asn1Exception;
import com.argos.cipher.asn1ext.EnvelopedData;
import com.argos.cipher.asn1ext.PKCS7;
import ru.argosgrp.cryptoservice.utils.IOUtil;

import java.security.MessageDigest;
import java.util.stream.Stream;

public class CryptoApi {

    public static String getThumbprint(byte[] cert) {
        MessageDigest md = UncheckedSupplier.get(() -> MessageDigest.getInstance("SHA-1"));
        md.update(cert);
        return IOUtil.bytesToHex(md.digest()).toLowerCase();
    }

    public static Stream<String> getSerialNumbers(byte[] encryptedData) throws Asn1Exception {
        EnvelopedData envelopedData = new PKCS7(encryptedData).getEnvelopedData();

        Stream.Builder<String> builder = Stream.builder();
        for (int i = 0; i < envelopedData.sizeRecipient(); i++) {
            builder.add(envelopedData.getRecipientInfo(i).getIssuerSerialNumber().toPrintableString());
        }
        return builder.build();
    }

}
