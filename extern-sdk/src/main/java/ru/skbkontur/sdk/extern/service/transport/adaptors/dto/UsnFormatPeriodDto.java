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

import ru.skbkontur.sdk.extern.model.UsnFormatPeriod;
import ru.skbkontur.sdk.extern.model.UsnFormatPeriod.PeriodModifiersEnum;


/**
 * @author alexs
 */
public class UsnFormatPeriodDto {

    public UsnFormatPeriod fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod dto) {

        if (dto == null) return null;

        UsnFormatPeriod period = new UsnFormatPeriod();

        period.setPeriodModifiers(PeriodModifiersEnum.fromValue(dto.getPeriodModifiers().getValue()));
        period.setYear(dto.getYear());

        return period;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod toDto(UsnFormatPeriod period) {

        if (period == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod();

        dto.setPeriodModifiers(ru.skbkontur.sdk.extern.service.transport.swagger.model.UsnFormatPeriod.PeriodModifiersEnum.fromValue(period.getPeriodModifiers().getValue()));
        dto.setYear(period.getYear());

        return dto;
    }
}
