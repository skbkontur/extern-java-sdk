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
@SuppressWarnings("unused")
public class CreateAccountRequestDto {
	
	public CreateAccountRequest fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto dto) {

		if (dto == null) return null;

		CreateAccountRequest request = new CreateAccountRequest();
		
		request.setInn(dto.getInn());
		request.setKpp(dto.getKpp());
		request.setOrganizationName(dto.getOrganizationName());
		
		return request;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto toDto(CreateAccountRequest request) {

		if (request == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.CreateAccountRequestDto();
		
		dto.setInn(request.getInn());
		dto.setKpp(request.getKpp());
		dto.setOrganizationName(request.getOrganizationName());
		
		return dto;
	}
}
