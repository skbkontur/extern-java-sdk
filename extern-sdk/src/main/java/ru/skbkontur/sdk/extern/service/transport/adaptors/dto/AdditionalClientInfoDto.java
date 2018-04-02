/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.AdditionalClientInfo;

/**
 *
 * @author alexs
 */
public class AdditionalClientInfoDto {

	public AdditionalClientInfo fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.AdditionalClientInfo dto) {
		AdditionalClientInfo info = new AdditionalClientInfo();
		
		TaxpayerDto taxpayerDto = new TaxpayerDto();
		
		info.setSenderFullName(dto.getDocumentSender().getSenderFullName());
		info.setSignerType(AdditionalClientInfo.SignerTypeEnum.fromValue(dto.getSignerType().getValue()));
		info.setTaxpayer(taxpayerDto.fromDto(dto.getTaxpayer()));
		
		return info;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.AdditionalClientInfo toDto(AdditionalClientInfo info) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.AdditionalClientInfo dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.AdditionalClientInfo();
		
		TaxpayerDto taxpayerDto = new TaxpayerDto();
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentSender sender
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentSender();
		
		sender.setSenderFullName(info.getSenderFullName());
		
		dto.setDocumentSender(sender);
		dto.setSignerType(ru.skbkontur.sdk.extern.service.transport.swagger.model.AdditionalClientInfo.SignerTypeEnum.fromValue(info.getSignerType().getValue()));
		dto.setTaxpayer(taxpayerDto.toDto(info.getTaxpayer()));
		
		return dto;
	}
}
