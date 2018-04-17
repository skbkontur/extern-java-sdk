/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.UsnServiceContractInfo;


/**
 * @author alexs
 */
public class UsnServiceContractInfoDto {

    public UsnServiceContractInfo fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoObject dto) {

        if (dto == null) return null;

        UsnServiceContractInfo info = new UsnServiceContractInfo();

        AdditionalClientInfoDto additionalOrgInfoDto = new AdditionalClientInfoDto();
        UsnFormatPeriodDto usnFormatPeriodDto = new UsnFormatPeriodDto();

        info.setAdditionalOrgInfo(additionalOrgInfoDto.fromDto(dto.getAdditionalOrgInfo()));
        info.setData(dto.getData());
        info.setPeriod(usnFormatPeriodDto.fromDto(dto.getPeriod()));

        return info;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoObject toDto(UsnServiceContractInfo info) {

        if (info == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoObject dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnServiceContractInfoObject();

        AdditionalClientInfoDto additionalOrgInfoDto = new AdditionalClientInfoDto();
        UsnFormatPeriodDto usnFormatPeriodDto = new UsnFormatPeriodDto();

        dto.setAdditionalOrgInfo(additionalOrgInfoDto.toDto(info.getAdditionalOrgInfo()));
        dto.setData(info.getData());
        dto.setPeriod(usnFormatPeriodDto.toDto(info.getPeriod()));

        return dto;
    }
}
