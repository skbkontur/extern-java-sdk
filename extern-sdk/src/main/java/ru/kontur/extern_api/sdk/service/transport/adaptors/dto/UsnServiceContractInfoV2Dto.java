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

import ru.kontur.extern_api.sdk.model.UsnServiceContractInfoV2;


/**
 * @author alexs
 */
public class UsnServiceContractInfoV2Dto {

    public UsnServiceContractInfoV2 fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 dto) {

        if (dto == null) return null;

        UsnServiceContractInfoV2 data = new UsnServiceContractInfoV2();

        data.setAdditionalOrgInfo(new AdditionalClientInfoDto().fromDto(dto.getAdditionalOrgInfo()));
        data.setData(new UsnDataV2Dto().fromDto(dto.getData()));
        data.setPeriod(new UsnFormatPeriodDto().fromDto(dto.getPeriod()));

        return data;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 toDto(UsnServiceContractInfoV2 data) {

        if (data == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2 dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.UsnServiceContractInfoUsnDataV2();

        dto.setAdditionalOrgInfo(new AdditionalClientInfoDto().toDto(data.getAdditionalOrgInfo()));
        dto.setData(new UsnDataV2Dto().toDto(data.getData()));
        dto.setPeriod(new UsnFormatPeriodDto().toDto(data.getPeriod()));

        return dto;
    }
}
