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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.kontur.extern_api.sdk.crypt.X509CertificateFactory;
import ru.kontur.extern_api.sdk.utils.TestConfig;

class CryptcpIT {

    private static String thumbprint;
    private static CryptcpApi cryptcpApi;
    private static CryptcpCryptoprovider cryptcpProvider;

    @BeforeAll
    static void setUp() {
        thumbprint = TestConfig.LoadConfigFromEnvironment().getThumbprint();
        cryptcpApi = new CryptcpApi("cryptcp.x64.exe");
        cryptcpProvider = new CryptcpCryptoprovider("cryptcp.x64.exe");
    }

    @Test
    void apiEncryptDecryptTest() throws Exception {

        String message = "Hello, World!";
        byte[] c = message.getBytes();

        byte[] encrypt = cryptcpApi.encrypt(thumbprint, c);
        byte[] decrypt = cryptcpApi.decrypt(thumbprint, encrypt);

        Assertions.assertEquals(message, new String(decrypt));
    }

    @Test
    void apiSignVerifyTest() throws Exception {
        String message = "Hello, World!";
        byte[] c = message.getBytes();

        byte[] signature = cryptcpApi.sign(thumbprint, c);
        byte[] verified = cryptcpApi.verify(thumbprint, signature);

        Assertions.assertEquals(message, new String(verified));
    }

    @Test
    void apiGetCertificateTest() throws Exception {
        byte[] certificate = cryptcpApi.getCertificate(thumbprint);
        new X509CertificateFactory().create(certificate).getCert().checkValidity();
    }


    @Test
    void providerEncryptDecryptTest() throws Exception {

        String message = "Hello, World!";
        byte[] c = message.getBytes();

        byte[] encrypt = cryptcpApi.encrypt(thumbprint, c);
        byte[] decrypt = cryptcpProvider.decryptAsync(thumbprint, encrypt).join().getOrThrow();

        Assertions.assertEquals(message, new String(decrypt));
    }

    @Test
    void providerSignVerifyTest() throws Exception {
        String message = "Hello, World!";
        byte[] c = message.getBytes();

        byte[] signature = cryptcpProvider.signAsync(thumbprint, c).join().getOrThrow();
        byte[] verified = cryptcpApi.verify(thumbprint, signature);

        Assertions.assertEquals(message, new String(verified));
    }

    @Test
    void providerGetCertificateTest() throws Exception {
        byte[] certificate = cryptcpProvider.getSignerCertificateAsync(thumbprint).join().getOrThrow();
        new X509CertificateFactory().create(certificate).getCert().checkValidity();
    }
}
