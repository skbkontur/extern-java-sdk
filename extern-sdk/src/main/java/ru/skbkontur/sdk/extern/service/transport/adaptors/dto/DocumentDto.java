/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
