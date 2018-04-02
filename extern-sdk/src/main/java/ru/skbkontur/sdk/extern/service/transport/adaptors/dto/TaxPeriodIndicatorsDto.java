/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.TaxPeriodIndicators;

/**
 *
 * @author alexs
 */
public class TaxPeriodIndicatorsDto {

	public TaxPeriodIndicators fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.TaxPeriodIndicators dto) {
		
		TaxPeriodIndicators i = new TaxPeriodIndicators();

		i.setAvPu(dto.getAvPu());
		i.setDohod(dto.getDohod());
		i.setIschisl(dto.getIschisl());
		i.setNalBazaUbyt(dto.getNalBazaUbyt());
		i.setNalPumin(dto.getNalPumin());
		i.setOktmo(dto.getOktmo());
		i.setRaschTorgSbor(new MerchantTaxDto().fromDto(dto.getRaschTorgSbor()));
		i.setRashod(dto.getRashod());
		i.setStavka(dto.getStavka());
		i.setUmenNal(dto.getUmenNal());
		
		return i;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.TaxPeriodIndicators toDto(TaxPeriodIndicators i) {
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.TaxPeriodIndicators dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.TaxPeriodIndicators();

		dto.setAvPu(i.getAvPu());
		dto.setDohod(i.getDohod());
		dto.setIschisl(i.getIschisl());
		dto.setNalBazaUbyt(i.getNalBazaUbyt());
		dto.setNalPumin(i.getNalPumin());
		dto.setOktmo(i.getOktmo());
		dto.setRaschTorgSbor(new MerchantTaxDto().toDto(i.getRaschTorgSbor()));
		dto.setRashod(i.getRashod());
		dto.setStavka(i.getStavka());
		dto.setUmenNal(i.getUmenNal());
		
		return dto;
	}
}
