/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.UsnDataV2;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
class UsnDataV2Dto {


	public UsnDataV2 fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 dto) {
		
		if (dto == null) return null;
		
		UsnDataV2 v2 = new UsnDataV2();
		
		PeriodIndicatorsDto periodIndicatorsDto = new PeriodIndicatorsDto();
		
		v2.setIschislMin(dto.getIschislMin());
		v2.setNomKorr(dto.getNomKorr());
		v2.setPoMestu(dto.getPoMestu());
		v2.setPrizNp(dto.getPrizNp());
		v2.setUbytPred(dto.getUbytPred());
		v2.setZaKv(periodIndicatorsDto.fromDto(dto.getZaKv()));
		v2.setZaNalPer(new TaxPeriodIndicatorsDto().fromDto(dto.getZaNalPer()));
		v2.setZaPg(periodIndicatorsDto.fromDto(dto.getZaPg()));
		v2.setZa9m(periodIndicatorsDto.fromDto(dto.getZa9m()));
		
		return v2;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 toDto(UsnDataV2 v2) {
		
		if (v2 == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2();
		
		PeriodIndicatorsDto periodIndicatorsDto = new PeriodIndicatorsDto();
		
		dto.setIschislMin(v2.getIschislMin());
		dto.setNomKorr(v2.getNomKorr());
		dto.setPoMestu(v2.getPoMestu());
		dto.setPrizNp(dto.getPrizNp());
		dto.setUbytPred(v2.getUbytPred());
		dto.setZa9m(periodIndicatorsDto.toDto(v2.getZa9m()));
		dto.setZaKv(periodIndicatorsDto.toDto(v2.getZaKv()));
		dto.setZaNalPer(new TaxPeriodIndicatorsDto().toDto(v2.getZaNalPer()));
		dto.setZaPg(periodIndicatorsDto.toDto(v2.getZaPg()));
		
		return dto;
	}
}
