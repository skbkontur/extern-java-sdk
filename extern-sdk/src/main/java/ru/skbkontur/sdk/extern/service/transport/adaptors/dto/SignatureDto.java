/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * @author AlexS
 */
@SuppressWarnings("unused")
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
        if (dto.getLinks() != null && !dto.getLinks().isEmpty()) {
            signature.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        } else {
            signature.setLinks(new ArrayList<>());
        }

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
            dto.setLinks(signature.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        }

        return dto;
    }
}
