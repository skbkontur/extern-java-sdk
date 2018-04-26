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

package ru.kontur.extern_api.sdk.certificates;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

import ru.kontur.extern_api.sdk.model.Certificate;
import ru.kontur.extern_api.sdk.model.CertificateList;

/**
 * @author Mikhail Pavlenko
 */

class CertificatesValidator {

    static void validateCertificateList(CertificateList certificateList) {
        assertNotNull("CertificateList must not be null!", certificateList);
        assertEquals("TotalCount is wrong!", 0, certificateList.getTotalCount().intValue());
        assertEquals("PageIndex is wrong!", 0, certificateList.getPageIndex().intValue());
        assertEquals("PageSize is wrong!", 0, certificateList.getPageSize().intValue());
    }

    static void validateCertificate(Certificate certificate) {
        assertNotNull("Certificate must not be null!", certificate);
        assertEquals("Fio is wrong! ", "string", certificate.getFio());
        assertEquals("Inn is wrong! ", "string", certificate.getInn());
        assertEquals("Kpp is wrong! ", "string", certificate.getKpp());
        assertTrue("IsValid is wrong! ", certificate.getIsValid());
        assertTrue("IsCloud is wrong! ", certificate.getIsCloud());
        assertTrue("IsQualified is wrong! ", certificate.getIsQualified());
        assertEquals("Content is wrong! ", "string", certificate.getContent());
    }
}
