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

import ru.kontur.extern_api.sdk.model.PeriodIndicators;


/**
 * @author alexs
 */
class PeriodIndicatorsDto {

    public PeriodIndicators fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.PeriodIndicators dto) {

        if (dto == null) return null;

        PeriodIndicators i = new PeriodIndicators();

        i.setAvPu(dto.getAvPu());
        i.setDohod(dto.getDohod());
        i.setIschisl(dto.getIschisl());
        i.setNalBazaUbyt(dto.getNalBazaUbyt());
        i.setOktmo(dto.getOktmo());
        i.setRaschTorgSbor(new MerchantTaxDto().fromDto(dto.getRaschTorgSbor()));
        i.setRashod(dto.getRashod());
        i.setStavka(dto.getStavka());
        i.setUmenNal(dto.getUmenNal());

        return i;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.PeriodIndicators toDto(PeriodIndicators i) {

        if (i == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.PeriodIndicators dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.PeriodIndicators();

        dto.setAvPu(i.getAvPu());
        dto.setDohod(i.getDohod());
        dto.setIschisl(i.getIschisl());
        dto.setNalBazaUbyt(i.getNalBazaUbyt());
        dto.setOktmo(i.getOktmo());
        dto.setRaschTorgSbor(new MerchantTaxDto().toDto(i.getRaschTorgSbor()));
        dto.setRashod(i.getRashod());
        dto.setStavka(i.getStavka());
        dto.setUmenNal(i.getUmenNal());

        return dto;
    }
}
