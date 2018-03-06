/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.Map;
import java.util.UUID;

/**
 *
 * @author AlexS
 */
public class DocumentToSendDto {
	private static final java.util.Base64.Decoder base64Decoder = java.util.Base64.getDecoder();

	public DocumentToSendDto() {
	}
	
	public ru.skbkontur.sdk.extern.model.DocumentToSend fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend dto) {
		if (dto == null)
			return null;
		
		ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend = new ru.skbkontur.sdk.extern.model.DocumentToSend();
		
		documentToSend.setContent(dto.getContent());
		documentToSend.setFilename(dto.getFilename());
		documentToSend.setId(dto.getId());
		documentToSend.setSenderIp(dto.getSenderIp());
		documentToSend.setSignature(new SignatureToSendDto().fromDto(dto.getSignature()));
		
		return documentToSend;
	}

	public ru.skbkontur.sdk.extern.model.DocumentToSend fromDto(Map<String,Object> dto) {
		if (dto == null)
			return null;
		
		ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend = new ru.skbkontur.sdk.extern.model.DocumentToSend();
		
		String base64 = (String)dto.get("content");
		byte[] content = null;
		if (base64 != null) {
			try {
				content = base64Decoder.decode(base64);
			}
			catch (Exception x) {
			}
		}
			
		
		documentToSend.setContent(content);
		documentToSend.setFilename((String)dto.get("filename"));
		documentToSend.setId((String)dto.get("id"));
		documentToSend.setSenderIp(null);
		documentToSend.setSignature(new SignatureToSendDto().fromDto((Map)dto.get("signature")));
		
		return documentToSend;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend toDto(ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend();
		
		dto.setContent(documentToSend.getContent());
		dto.setFilename(documentToSend.getFilename());
		dto.setId(documentToSend.getId());
		dto.setSenderIp(documentToSend.getSenderIp());
		dto.setSignature(new SignatureToSendDto().toDto(documentToSend.getSignature()));
		
		return dto;
	}
}
