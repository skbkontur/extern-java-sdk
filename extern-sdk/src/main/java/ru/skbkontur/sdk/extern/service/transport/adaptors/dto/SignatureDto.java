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

import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class SignatureDto {

    public SignatureDto() {
    }

    public ru.skbkontur.sdk.extern.model.Signature fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature dto) {

        if (dto == null) return null;

        ru.skbkontur.sdk.extern.model.Signature signature
                = new ru.skbkontur.sdk.extern.model.Signature();

        LinkDto linkDto = new LinkDto();

        signature.setContentLink(linkDto.fromDto(dto.getContentLink()));
        signature.setId(dto.getId());
        signature.setLinks(dto.getLinks().stream().map(l -> linkDto.fromDto(l)).collect(Collectors.toList()));

        return signature;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature toDto(ru.skbkontur.sdk.extern.model.Signature signature) {

        if (signature == null) return null;

        LinkDto linkDto = new LinkDto();
        ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature();
        dto.setContentLink(linkDto.toDto(signature.getContentLink()));
        dto.setId(signature.getId());
        if (signature.getLinks() != null) {
            dto.setLinks(signature.getLinks().stream().map(l -> linkDto.toDto(l)).collect(Collectors.toList()));
        }

        return dto;
    }
}
