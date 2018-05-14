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

/**
 * @author AlexS
 */
public class DraftDocumentDto {

    public ru.kontur.extern_api.sdk.model.DraftDocument fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftDocument dto) {

        if (dto == null) return null;

        ru.kontur.extern_api.sdk.model.DraftDocument draftDocument = new ru.kontur.extern_api.sdk.model.DraftDocument();

        LinkDto linkDto = new LinkDto();

        if (dto.getDecryptedContentLink() != null) {
            draftDocument.setDecryptedContentLink(linkDto.fromDto(dto.getDecryptedContentLink()));
        }

        if (dto.getEncryptedContentLink() != null) {
            draftDocument.setEncryptedContentLink(linkDto.fromDto(dto.getEncryptedContentLink()));
        }

        draftDocument.setId(dto.getId());

        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

        draftDocument.setDocumentDescription(documentDescriptionDto.fromDto(dto.getDescription()));

        draftDocument.setSignatureContentLink(linkDto.fromDto(dto.getSignatureContentLink()));

        return draftDocument;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftDocument toDto(ru.kontur.extern_api.sdk.model.DraftDocument draftDocument) {

        if (draftDocument == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftDocument dto
                = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DraftDocument();

        LinkDto linkDto = new LinkDto();

        if (draftDocument.getDecryptedContentLink() != null) {
            dto.setDecryptedContentLink(linkDto.toDto(draftDocument.getDecryptedContentLink()));
        }

        if (draftDocument.getEncryptedContentLink() != null) {
            dto.setEncryptedContentLink(linkDto.toDto(draftDocument.getEncryptedContentLink()));
        }

        dto.setId(draftDocument.getId());

        DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

        dto.setDescription(documentDescriptionDto.toDto(draftDocument.getDocumentDescription()));

        dto.setSignatureContentLink(linkDto.toDto(draftDocument.getSignatureContentLink()));

        return dto;
    }
}
