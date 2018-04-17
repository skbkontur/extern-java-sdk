/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.Taxpayer;


/**
 * @author alexs
 */
public class TaxpayerDto {

    public Taxpayer fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Taxpayer dto) {

        if (dto == null) return null;

        Taxpayer taxpayer = new Taxpayer();

        RepresentativeDto representativeDto = new RepresentativeDto();

        taxpayer.setRepresentative(representativeDto.fromDto(dto.getRepresentative()));
        taxpayer.setTaxpayerChiefFio(dto.getTaxpayerChiefFio());
        taxpayer.setTaxpayerFullName(dto.getTaxpayerFullName());
        taxpayer.setTaxpayerOkved(dto.getTaxpayerOkved());
        taxpayer.setTaxpayerPhone(dto.getTaxpayerPhone());

        return taxpayer;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Taxpayer toDto(Taxpayer taxpayer) {

        if (taxpayer == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.Taxpayer dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Taxpayer();

        RepresentativeDto representativeDto = new RepresentativeDto();

        dto.setRepresentative(representativeDto.toDto(taxpayer.getRepresentative()));
        dto.setTaxpayerChiefFio(taxpayer.getTaxpayerChiefFio());
        dto.setTaxpayerFullName(taxpayer.getTaxpayerFullName());
        dto.setTaxpayerOkved(taxpayer.getTaxpayerOkved());
        dto.setTaxpayerPhone(taxpayer.getTaxpayerPhone());

        return dto;
    }
}
