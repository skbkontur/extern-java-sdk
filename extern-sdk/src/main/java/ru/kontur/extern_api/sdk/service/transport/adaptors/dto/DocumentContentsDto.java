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

/**
 * @author AlexS
 */
public class DocumentContentsDto {

    public ru.kontur.extern_api.sdk.model.DocumentContents fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentContents dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.DocumentContents documentContents = new ru.kontur.extern_api.sdk.model.DocumentContents();

        documentContents.setBase64Content(dto.getBase64Content());

        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

        documentContents.setDocumentDescription(documentDescriptionDto.fromDto(dto.getDescription()));

        documentContents.setSignature(dto.getSignature());

        return documentContents;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentContents toDto(ru.kontur.extern_api.sdk.model.DocumentContents documentContents) {

        if (documentContents == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentContents dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentContents();

        dto.setBase64Content(documentContents.getBase64Content());

        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

        dto.setDescription(documentDescriptionDto.toDto(documentContents.getDocumentDescription()));

        dto.setSignature(documentContents.getSignature());

        return dto;
    }
}
