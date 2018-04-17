/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
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
