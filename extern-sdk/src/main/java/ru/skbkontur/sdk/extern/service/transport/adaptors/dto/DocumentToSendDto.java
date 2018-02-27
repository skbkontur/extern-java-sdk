/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

/**
 *
 * @author AlexS
 */
public class DocumentToSendDto {

	public DocumentToSendDto() {
	}
	
	public ru.skbkontur.sdk.extern.model.DocumentToSend fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentToSend dto) {
		ru.skbkontur.sdk.extern.model.DocumentToSend documentToSend = new ru.skbkontur.sdk.extern.model.DocumentToSend();
		
		documentToSend.setContent(dto.getContent());
		documentToSend.setFilename(dto.getFilename());
		documentToSend.setId(dto.getId());
		documentToSend.setSenderIp(dto.getSenderIp());
		documentToSend.setSignature(new SignatureToSendDto().fromDto(dto.getSignature()));
		
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
