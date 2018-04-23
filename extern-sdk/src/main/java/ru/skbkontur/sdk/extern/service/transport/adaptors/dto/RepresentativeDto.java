/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.Representative;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
class RepresentativeDto {

	public Representative fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Representative dto) {
		
		if (dto == null) return null;
		
		Representative representative = new Representative();
		
		PassportInfoDto pasportInfoDto = new PassportInfoDto();
		
		representative.setPassport(pasportInfoDto.fromDto(dto.getPassport()));
		representative.setRepresentativeDocument(dto.getRepresentativeDocument());
		
		return representative;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Representative toDto(Representative representative) {
		
		if (representative == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Representative dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.Representative();
		
		PassportInfoDto pasportInfoDto = new PassportInfoDto();
		
		dto.setPassport(pasportInfoDto.toDto(representative.getPassport()));
		dto.setRepresentativeDocument(representative.getRepresentativeDocument());
		
		return dto;
	}
	
}
