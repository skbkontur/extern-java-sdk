/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.PeriodIndicators;

/**
 *
 * @author alexs
 */
public class PeriodIndicatorsDto {

	public PeriodIndicators fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.PeriodIndicators dto) {
	
		PeriodIndicators i = new PeriodIndicators();
		
		i.setAvPu(dto.getAvPu());
		i.setDohod(dto.getDohod());
		i.setIschisl(dto.getIschisl());
		i.setNalBazaUbyt(dto.getNalBazaUbyt());
		i.setOktmo(dto.getOktmo());
		i.setRaschTorgSbor(new MerchantTaxDto().fromDto(dto.getRaschTorgSbor()));
		i.setRashod(dto.getRashod());
		i.setStavka(dto.getStavka());
		i.setUmenNal(dto.getUmenNal());
		
		return i;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.PeriodIndicators toDto(PeriodIndicators i) {
	
		ru.skbkontur.sdk.extern.service.transport.swagger.model.PeriodIndicators dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.PeriodIndicators();
		
		dto.setAvPu(i.getAvPu());
		dto.setDohod(i.getDohod());
		dto.setIschisl(i.getIschisl());
		dto.setNalBazaUbyt(i.getNalBazaUbyt());
		dto.setOktmo(i.getOktmo());
		dto.setRaschTorgSbor(new MerchantTaxDto().toDto(i.getRaschTorgSbor()));
		dto.setRashod(i.getRashod());
		dto.setStavka(i.getStavka());
		dto.setUmenNal(i.getUmenNal());
		
		return dto;
	}
}
