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

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import ru.kontur.extern_api.sdk.model.Certificate;


/**
 * @author alexs
 */
class CertificateDto {

    public Certificate fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateDto dto) {

        if (dto == null) return null;

        Certificate cert = new Certificate();

        cert.setContent(dto.getContent());
        cert.setFio(dto.getFio());
        cert.setInn(dto.getInn());
        cert.setIsCloud(dto.getIsCloud());
        cert.setIsQualified(dto.getIsQualified());
        cert.setIsValid(dto.getIsValid());
        cert.setKpp(dto.getKpp());

        return cert;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateDto toDto(Certificate cert) {

        if (cert == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateDto dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateDto();

        dto.setContent(cert.getContent());
        dto.setFio(cert.getFio());
        dto.setInn(cert.getInn());
        dto.setIsCloud(cert.getIsCloud());
        dto.setIsQualified(cert.getIsQualified());
        dto.setIsValid(cert.getIsValid());
        dto.setKpp(cert.getKpp());

        return dto;
    }
}
