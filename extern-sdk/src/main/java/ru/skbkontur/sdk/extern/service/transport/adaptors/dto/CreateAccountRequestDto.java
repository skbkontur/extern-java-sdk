/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.CreateAccountRequest;

/**
 *
 * @author AlexS
 */
public class CreateAccountRequestDto {
	
	public CreateAccountRequest fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto dto) {
		CreateAccountRequest request = new CreateAccountRequest();
		
		request.setAccountCertificate(new AccountCertificateDto().fromDto(dto.getAccountCertificate()));
		request.setInn(dto.getInn());
		request.setKpp(dto.getKpp());
		request.setOrganizationName(dto.getOrganizationName());
		
		return request;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto toDto(CreateAccountRequest request) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto();
		
		dto.setAccountCertificate(new AccountCertificateDto().toDto(request.getAccountCertificate()));
		dto.setInn(request.getInn());
		dto.setKpp(request.getKpp());
		dto.setOrganizationName(request.getOrganizationName());
		
		return dto;
	}
}
