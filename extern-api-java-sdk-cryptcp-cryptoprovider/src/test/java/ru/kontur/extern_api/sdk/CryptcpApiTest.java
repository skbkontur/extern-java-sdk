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
import org.junit.jupiter.api.Test;


class CryptcpApiTest {

    private static final String THUMBPRINT = "fd3e438933387026ee46c03691f20743d7d34766";

    @Test
    void encryptDecryptTest() throws Exception {
        String thumbprint = THUMBPRINT;

        String message = "Hello, World!";
        byte[] c = message.getBytes();

        CryptcpApi cryptcpApi = new CryptcpApi("cryptcp.x64.exe");

        byte[] encrypt = cryptcpApi.encrypt(thumbprint, c);
        byte[] decrypt = cryptcpApi.decrypt(thumbprint, encrypt);

        Assertions.assertEquals(message, new String(decrypt));
    }

    @Test
    void signVerifyTest() throws Exception {
        String thumbprint = THUMBPRINT;

        String message = "Hello, World!";
        byte[] c = message.getBytes();

        CryptcpApi cryptcpApi = new CryptcpApi("cryptcp.x64.exe");

        byte[] signature = cryptcpApi.sign(thumbprint, c);
        byte[] verified = cryptcpApi.verify(thumbprint, signature);

        Assertions.assertEquals(message, new String(verified));
    }
}
