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

import java.util.ArrayList;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.CertificateList;

import java.util.stream.Collectors;


/**
 * @author alexs
 */
public class CertificateListDto {
	
	public CertificateList fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateList dto) {

		if (dto == null) return null;
		
		CertificateList certs = new CertificateList();
		
		CertificateDto certificateDto = new CertificateDto();
		
		certs.setCertificates(
            dto.getCertificates() == null ?
                new ArrayList<>() :
                dto.getCertificates().stream().map(certificateDto::fromDto).collect(Collectors.toList())
        );
		certs.setSkip(dto.getSkip() == null ? 0 : dto.getSkip());
		certs.setTake(dto.getTake());
		certs.setTotalCount(dto.getTotalCount());
		
		return certs;
	}

	public ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateList toDto(CertificateList certs) {

		if (certs == null) return null;
		
		ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateList dto 
			= new ru.kontur.extern_api.sdk.service.transport.swagger.model.CertificateList();
		
		CertificateDto certificateDto = new CertificateDto();
		
		dto.setCertificates(certs.getCertificates().stream().map(certificateDto::toDto).collect(Collectors.toList()));
		dto.setSkip(certs.getSkip() == null ? 0 : certs.getSkip());
		dto.setTake(certs.getTake());
		dto.setTotalCount(certs.getTotalCount());
		
		return dto;
	}
}
