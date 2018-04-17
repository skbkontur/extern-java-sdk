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
public class DocumentDto {

    public ru.skbkontur.sdk.extern.model.Document fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Document dto) {

        if (dto == null) return null;

        ContentDto contentDto = new ContentDto();
        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();
        LinkDto linkDto = new LinkDto();
        SignatureDto signatureDto = new SignatureDto();
        ru.skbkontur.sdk.extern.model.Document document = new ru.skbkontur.sdk.extern.model.Document();
        document.setContent(contentDto.fromDto(dto.getContent()));
        document.setDescription(documentDescriptionDto.fromDto(dto.getDescription()));
        document.setId(dto.getId());
        if (dto.getLinks() != null) {
            document.setLinks(dto.getLinks().stream().map(l -> linkDto.fromDto(l)).collect(Collectors.toList()));
        }
        document.setSignatures(dto.getSignatures().stream().map(s -> signatureDto.fromDto(s)).collect(Collectors.toList()));

        return document;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Document toDto(ru.skbkontur.sdk.extern.model.Document document) {

        if (document == null) return null;

        ContentDto contentDto = new ContentDto();
        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();
        LinkDto linkDto = new LinkDto();
        SignatureDto signatureDto = new SignatureDto();
        ru.skbkontur.sdk.extern.service.transport.swagger.model.Document dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Document();
        dto.setContent(contentDto.toDto(document.getContent()));
        dto.setDescription(documentDescriptionDto.toDto(document.getDescription()));
        dto.setId(document.getId());
        if (document.getLinks() != null) {
            dto.setLinks(document.getLinks().stream().map(l -> linkDto.toDto(l)).collect(Collectors.toList()));
        }
        dto.setSignatures(document.getSignatures().stream().map(s -> signatureDto.toDto(s)).collect(Collectors.toList()));

        return dto;
    }
}
