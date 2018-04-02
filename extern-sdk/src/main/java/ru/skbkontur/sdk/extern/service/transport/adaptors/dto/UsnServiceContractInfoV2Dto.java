/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.UsnServiceContractInfoV2;

/**
 *
 * @author alexs
 */
public class UsnServiceContractInfoV2Dto {
	
	public UsnServiceContractInfoV2 fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 dto) {
		
		if (dto == null) return null;
		
		UsnServiceContractInfoV2 data = new UsnServiceContractInfoV2();
		
		data.setAdditionalOrgInfo(new AdditionalClientInfoDto().fromDto(dto.getAdditionalOrgInfo()));
		data.setData(new UsnDataV2Dto().fromDto(dto.getData()));
		data.setPeriod(new UsnFormatPeriodDto().fromDto(dto.getPeriod()));
		
		return data;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 toDto(UsnServiceContractInfoV2 data) {
		
		if (data == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2();
		
		dto.setAdditionalOrgInfo(new AdditionalClientInfoDto().toDto(data.getAdditionalOrgInfo()));
		dto.setData(new UsnDataV2Dto().toDto(data.getData()));
		dto.setPeriod(new UsnFormatPeriodDto().toDto(data.getPeriod()));
		
		return dto;
	}
}
