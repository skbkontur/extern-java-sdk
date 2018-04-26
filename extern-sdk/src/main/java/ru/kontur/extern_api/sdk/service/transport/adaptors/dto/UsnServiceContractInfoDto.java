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

package ru.kontur.extern_api.sdk.service.transport.adaptors.dto;

import ru.kontur.extern_api.sdk.model.UsnServiceContractInfo;


/**
 * @author alexs
 */
public class UsnServiceContractInfoDto {

    public UsnServiceContractInfo fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoObject dto) {

        if (dto == null) return null;

        UsnServiceContractInfo info = new UsnServiceContractInfo();

        AdditionalClientInfoDto additionalOrgInfoDto = new AdditionalClientInfoDto();
        UsnFormatPeriodDto usnFormatPeriodDto = new UsnFormatPeriodDto();

        info.setAdditionalOrgInfo(additionalOrgInfoDto.fromDto(dto.getAdditionalOrgInfo()));
        info.setData(dto.getData());
        info.setPeriod(usnFormatPeriodDto.fromDto(dto.getPeriod()));

        return info;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoObject toDto(UsnServiceContractInfo info) {

        if (info == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoObject dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoObject();

        AdditionalClientInfoDto additionalOrgInfoDto = new AdditionalClientInfoDto();
        UsnFormatPeriodDto usnFormatPeriodDto = new UsnFormatPeriodDto();

        dto.setAdditionalOrgInfo(additionalOrgInfoDto.toDto(info.getAdditionalOrgInfo()));
        dto.setData(info.getData());
        dto.setPeriod(usnFormatPeriodDto.toDto(info.getPeriod()));

        return dto;
    }
}
