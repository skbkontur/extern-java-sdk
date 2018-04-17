/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

/**
 * @author AlexS
 */
public class DraftDocumentDto {

    public ru.skbkontur.sdk.extern.model.DraftDocument fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftDocument dto) {

        if (dto == null) return null;

        ru.skbkontur.sdk.extern.model.DraftDocument draftDocument = new ru.skbkontur.sdk.extern.model.DraftDocument();

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

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftDocument toDto(ru.skbkontur.sdk.extern.model.DraftDocument draftDocument) {

        if (draftDocument == null) return null;

        ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftDocument dto
                = new ru.skbkontur.sdk.extern.service.transport.swagger.model.DraftDocument();

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
