package ru.skbkontur.sdk.extern.certificates;

import ru.skbkontur.sdk.extern.model.Certificate;
import ru.skbkontur.sdk.extern.model.CertificateList;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;

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
