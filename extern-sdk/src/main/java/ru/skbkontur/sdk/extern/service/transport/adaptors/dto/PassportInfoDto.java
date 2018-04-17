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

import ru.skbkontur.sdk.extern.model.PassportInfo;


/**
 * @author alexs
 */
class PassportInfoDto {

    public PassportInfo fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo dto) {

        if (dto == null) return null;

        PassportInfo passportInfo = new PassportInfo();

        passportInfo.setCode(dto.getCode());
        passportInfo.setIssuedBy(dto.getIssuedBy());
        passportInfo.setIssuedDate(dto.getIssuedDate());
        passportInfo.setSeriesNumber(dto.getSeriesNumber());

        return passportInfo;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo toDto(PassportInfo passportInfo) {

        if (passportInfo == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.PassportInfo();

        dto.setCode(passportInfo.getCode());
        dto.setIssuedBy(passportInfo.getIssuedBy());
        dto.setIssuedDate(passportInfo.getIssuedDate());
        dto.setSeriesNumber(passportInfo.getSeriesNumber());

        return dto;
    }
}