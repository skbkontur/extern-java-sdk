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
public class DocflowDto {

    public ru.skbkontur.sdk.extern.model.Docflow fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow dto) {

        if (dto == null) return null;

        ru.skbkontur.sdk.extern.model.Docflow docflow = new ru.skbkontur.sdk.extern.model.Docflow();

        docflow.setDescription(new ru.skbkontur.sdk.extern.model.DocflowDescription());

        if (dto.getDocuments() != null) {
            DocumentDto documentDto = new DocumentDto();
            docflow.setDocuments(dto.getDocuments().stream().map(documentDto::fromDto).collect(Collectors.toList()));
        }

        docflow.setId(dto.getId());

        docflow.setLastChangeDate(dto.getLastChangeDate());

        if (dto.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            docflow.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        }

        docflow.setSendDate(dto.getSendDate());

        docflow.setStatus(dto.getStatus());

        docflow.setType(dto.getType());

        return docflow;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow toDto(ru.skbkontur.sdk.extern.model.Docflow docflow) {

        if (docflow == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow();

        dto.setDescription(new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowDescription());

        if (docflow.getDocuments() != null) {
            DocumentDto documentDto = new DocumentDto();
            dto.setDocuments(docflow.getDocuments().stream().map(documentDto::toDto).collect(Collectors.toList()));
        }

        dto.setId(docflow.getId());

        dto.setLastChangeDate(docflow.getLastChangeDate());

        if (docflow.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            dto.setLinks(docflow.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        }

        dto.setSendDate(docflow.getSendDate());

        dto.setStatus(docflow.getStatus());

        dto.setType(docflow.getType());

        return dto;
    }
}
