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

import ru.kontur.extern_api.sdk.model.Representative;


/**
 * @author alexs
 */
class RepresentativeDto {

    public Representative fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.Representative dto) {

        if (dto == null) return null;

        Representative representative = new Representative();

        PassportInfoDto pasportInfoDto = new PassportInfoDto();

        representative.setPassport(pasportInfoDto.fromDto(dto.getPassport()));
        representative.setRepresentativeDocument(dto.getRepresentativeDocument());

        return representative;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Representative toDto(Representative representative) {

        if (representative == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Representative dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Representative();

        PassportInfoDto pasportInfoDto = new PassportInfoDto();

        dto.setPassport(pasportInfoDto.toDto(representative.getPassport()));
        dto.setRepresentativeDocument(representative.getRepresentativeDocument());

        return dto;
    }

}
