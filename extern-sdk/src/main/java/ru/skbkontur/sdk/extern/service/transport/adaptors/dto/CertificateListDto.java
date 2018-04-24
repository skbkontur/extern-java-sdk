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

package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.CertificateList;

import java.util.stream.Collectors;


/**
 * @author alexs
 */
public class CertificateListDto {

    public CertificateList fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList dto) {

        if (dto == null) return null;

        CertificateList certs = new CertificateList();

        CertificateDto certificateDto = new CertificateDto();

        certs.setCertificates(dto.getCertificates().stream().map(certificateDto::fromDto).collect(Collectors.toList()));
        certs.setPageIndex(dto.getPageIndex());
        certs.setPageSize(dto.getPageSize());
        certs.setTotalCount(dto.getTotalCount());

        return certs;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList toDto(CertificateList certs) {

        if (certs == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList();

        CertificateDto certificateDto = new CertificateDto();

        dto.setCertificates(certs.getCertificates().stream().map(certificateDto::toDto).collect(Collectors.toList()));
        dto.setPageIndex(certs.getPageIndex());
        dto.setPageSize(certs.getPageSize());
        dto.setTotalCount(certs.getTotalCount());

        return dto;
    }
}
