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

import ru.skbkontur.sdk.extern.model.UsnDataV2;


/**
 * @author alexs
 */
class UsnDataV2Dto {


    public UsnDataV2 fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 dto) {

        if (dto == null) return null;

        UsnDataV2 v2 = new UsnDataV2();

        PeriodIndicatorsDto periodIndicatorsDto = new PeriodIndicatorsDto();

        v2.setIschislMin(dto.getIschislMin());
        v2.setNomKorr(dto.getNomKorr());
        v2.setPoMestu(dto.getPoMestu());
        v2.setPrizNp(dto.getPrizNp());
        v2.setUbytPred(dto.getUbytPred());
        v2.setZaKv(periodIndicatorsDto.fromDto(dto.getZaKv()));
        v2.setZaNalPer(new TaxPeriodIndicatorsDto().fromDto(dto.getZaNalPer()));
        v2.setZaPg(periodIndicatorsDto.fromDto(dto.getZaPg()));
        v2.setZa9m(periodIndicatorsDto.fromDto(dto.getZa9m()));

        return v2;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 toDto(UsnDataV2 v2) {

        if (v2 == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2 dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnDataV2();

        PeriodIndicatorsDto periodIndicatorsDto = new PeriodIndicatorsDto();

        dto.setIschislMin(v2.getIschislMin());
        dto.setNomKorr(v2.getNomKorr());
        dto.setPoMestu(v2.getPoMestu());
        dto.setPrizNp(dto.getPrizNp());
        dto.setUbytPred(v2.getUbytPred());
        dto.setZa9m(periodIndicatorsDto.toDto(v2.getZa9m()));
        dto.setZaKv(periodIndicatorsDto.toDto(v2.getZaKv()));
        dto.setZaNalPer(new TaxPeriodIndicatorsDto().toDto(v2.getZaNalPer()));
        dto.setZaPg(periodIndicatorsDto.toDto(v2.getZaPg()));

        return dto;
    }
}
