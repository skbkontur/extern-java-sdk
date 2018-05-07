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

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class SignatureDto {

    public SignatureDto() {
    }

    public ru.kontur.extern_api.sdk.model.Signature fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.Signature dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.Signature signature
                = new ru.kontur.extern_api.sdk.model.Signature();

        LinkDto linkDto = new LinkDto();

        signature.setContentLink(linkDto.fromDto(dto.getContentLink()));
        signature.setId(dto.getId());
        if (dto.getLinks() != null && !dto.getLinks().isEmpty()) {
            signature.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        } else {
            signature.setLinks(new ArrayList<>());
        }

        return signature;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Signature toDto(ru.kontur.extern_api.sdk.model.Signature signature) {

        if (signature == null) return null;

        LinkDto linkDto = new LinkDto();
        ru.kontur.extern_api.sdk.service.transport.swagger.model.Signature dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Signature();
        dto.setContentLink(linkDto.toDto(signature.getContentLink()));
        dto.setId(signature.getId());
        if (signature.getLinks() != null) {
            dto.setLinks(signature.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        }

        return dto;
    }
}
