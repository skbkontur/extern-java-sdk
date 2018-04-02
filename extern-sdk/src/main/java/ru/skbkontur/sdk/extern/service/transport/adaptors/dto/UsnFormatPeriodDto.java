/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.UsnFormatPeriod;
import ru.skbkontur.sdk.extern.model.UsnFormatPeriod.PeriodModifiersEnum;

/**
 *
 * @author alexs
 */
public class UsnFormatPeriodDto {
	
	public UsnFormatPeriod fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod dto) {
		UsnFormatPeriod period = new UsnFormatPeriod();
		
		period.setPeriodModifiers(PeriodModifiersEnum.fromValue(dto.getPeriodModifiers().getValue()));
		period.setYear(dto.getYear());
		
		return period;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod toDto(UsnFormatPeriod period) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod();
		
		dto.setPeriodModifiers(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod.PeriodModifiersEnum.fromValue(period.getPeriodModifiers().getValue()));
		dto.setYear(period.getYear());
		
		return dto;
	}
}
