/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.PassportInfo;

/**
 *
 * @author alexs
 */
class PassportInfoDto {
	
	public PassportInfo fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo dto) {
		
		if (dto == null) return null;
		
		PassportInfo passportInfo = new PassportInfo();
		
		passportInfo.setCode(dto.getCode());
		passportInfo.setIssuedBy(dto.getIssuedBy());
		passportInfo.setIssuedDate(dto.getIssuedDate());
		passportInfo.setSeriesNumber(dto.getSeriesNumber());
			
		return passportInfo;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo toDto(PassportInfo passportInfo) {
		
		if (passportInfo == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo();
		
		dto.setCode(passportInfo.getCode());
		dto.setIssuedBy(passportInfo.getIssuedBy());
		dto.setIssuedDate(passportInfo.getIssuedDate());
		dto.setSeriesNumber(passportInfo.getSeriesNumber());
			
		return dto;
	}
}
