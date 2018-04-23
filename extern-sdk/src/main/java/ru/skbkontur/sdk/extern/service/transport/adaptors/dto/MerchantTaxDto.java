/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.MerchantTax;

/**
 *
 * @author alexs
 */
@SuppressWarnings("unused")
class MerchantTaxDto {

	public MerchantTax fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax dto) {
		
		if (dto == null) return null;
		
		MerchantTax t = new MerchantTax();

		t.setDohod(dto.getDohod());
		t.setIschisl(dto.getIschisl());
		t.setTorgSborFact(dto.getTorgSborFact());
		t.setTorgSborUmen(dto.getTorgSborUmen());
		
		return t;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax toDto(MerchantTax t) {
		
		if (t == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.MerchantTax();

		dto.setDohod(t.getDohod());
		dto.setIschisl(t.getIschisl());
		dto.setTorgSborFact(t.getTorgSborFact());
		dto.setTorgSborUmen(t.getTorgSborUmen());
		
		return dto;
	}
}
