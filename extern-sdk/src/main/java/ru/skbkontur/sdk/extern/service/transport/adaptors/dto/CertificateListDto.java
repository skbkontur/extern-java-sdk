/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.CertificateList;

/**
 *
 * @author alexs
 */
public class CertificateListDto {
	
	public CertificateList fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList dto) {
		CertificateList certs = new CertificateList();
		
		CertificateDto certificateDto = new CertificateDto();
		
		certs.setCertificates(dto.getCertificates().stream().map(certificateDto::fromDto).collect(Collectors.toList()));
		certs.setPageIndex(dto.getPageIndex());
		certs.setPageSize(dto.getPageSize());
		certs.setTotalCount(dto.getTotalCount());
		
		return certs;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.CertificateList toDto(CertificateList certs) {
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
