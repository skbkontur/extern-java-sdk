/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class DocumentToSendDto {

    private static final java.util.Base64.Decoder BASE64DECODER = java.util.Base64.getDecoder();

    public ru.skbkontur.sdk.extern.model.DocumentToSend fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend dto) {

        if (dto == null) {
            return null;
        }

        ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend = new ru.skbkontur.sdk.extern.model.DocumentToSend();

        documentToSend.setContent(dto.getContent());
        documentToSend.setFilename(dto.getFilename());
        documentToSend.setId(dto.getId());
        documentToSend.setSenderIp(dto.getSenderIp());
        documentToSend.setSignature(new SignatureToSendDto().fromDto(dto.getSignature()));
        if (dto.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            documentToSend.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        }

        return documentToSend;
    }

    @SuppressWarnings("unchecked")
    public ru.skbkontur.sdk.extern.model.DocumentToSend fromDto(Map<String, Object> dto) {
        if (dto == null) {
            return null;
        }

        ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend = new ru.skbkontur.sdk.extern.model.DocumentToSend();

        String base64 = (String) dto.get("content");
        byte[] content = null;
        if (base64 != null) {
            try {
                content = BASE64DECODER.decode(base64);
            }
            catch (Exception x) {
                content = null;
            }
        }

        documentToSend.setContent(content);
        documentToSend.setFilename((String) dto.get("filename"));
        documentToSend.setId((String) dto.get("id"));
        documentToSend.setSenderIp(null);
        documentToSend.setSignature(new SignatureToSendDto().fromDto((Map<String, Object>) dto.get("signature")));
        if (dto.get("links") != null) {
            LinkDto linkDto = new LinkDto();
            List<Map<String, Object>> links = (List<Map<String, Object>>) dto.get("links");
            documentToSend.setLinks(links.stream().map(linkDto::fromDto).collect(Collectors.toList()));
        }

        return documentToSend;
    }

    public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend toDto(ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend) {

        if (documentToSend == null) {
            return null;
        }

        ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend();

        dto.setContent(documentToSend.getContent());
        dto.setFilename(documentToSend.getFilename());
        dto.setId(documentToSend.getId());
        dto.setSenderIp(documentToSend.getSenderIp());
        dto.setSignature(new SignatureToSendDto().toDto(documentToSend.getSignature()));
        if (documentToSend.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            dto.setLinks(documentToSend.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        }

        return dto;
    }
}
