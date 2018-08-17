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

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class DocumentToSendDto {

    private static final java.util.Base64.Decoder BASE64DECODER = java.util.Base64.getDecoder();

    public ru.kontur.extern_api.sdk.model.DocumentToSend fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend dto) {

        if (dto == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.model.DocumentToSend documentToSend = new ru.kontur.extern_api.sdk.model.DocumentToSend();

        documentToSend.setContent(dto.getContent());
        documentToSend.setFilename(dto.getFilename());
        documentToSend.setId(dto.getId());
        documentToSend.setSignature(new SignatureToSendDto().fromDto(dto.getSignature()));
        if (dto.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            documentToSend.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
        }

        return documentToSend;
    }

		@SuppressWarnings("unchecked")
    public ru.kontur.extern_api.sdk.model.DocumentToSend fromDto(Map<String, Object> dto) {
        if (dto == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.model.DocumentToSend documentToSend = new ru.kontur.extern_api.sdk.model.DocumentToSend();

        String base64 = (String) dto.get("content");
        byte[] content = null;
        if (base64 != null) {
            try {
                content = BASE64DECODER.decode(base64);
            } catch (Exception x) {
                content = null;
            }
        }

        documentToSend.setContent(content);
        documentToSend.setFilename((String) dto.get("filename"));
        documentToSend.setId((String) dto.get("id"));
        documentToSend.setSignature(new SignatureToSendDto().fromDto((Map<String, Object>) dto.get("signature")));
        if (dto.get("links") != null) {
            LinkDto linkDto = new LinkDto();
            List<Map<String, Object>> links = (List<Map<String, Object>>) dto.get("links");
            documentToSend.setLinks(links.stream().map(linkDto::fromDto).collect(Collectors.toList()));
        }

        return documentToSend;
    }

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend toDto(ru.kontur.extern_api.sdk.model.DocumentToSend documentToSend) {

        if (documentToSend == null) {
            return null;
        }

        ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.DocumentToSend();

        dto.setContent(documentToSend.getContent());
        dto.setFilename(documentToSend.getFilename());
        dto.setId(documentToSend.getId());
        dto.setSignature(new SignatureToSendDto().toDto(documentToSend.getSignature()));
        if (documentToSend.getLinks() != null) {
            LinkDto linkDto = new LinkDto();
            dto.setLinks(documentToSend.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
        }

        return dto;
    }
}
