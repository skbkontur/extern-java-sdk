/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.Map;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
class SignatureToSendDto {

	public SignatureToSendDto() {
	}
	
	public ru.skbkontur.sdk.extern.model.SignatureToSend fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend dto) {
		
		if (dto == null) return null;
		
		ru.skbkontur.sdk.extern.model.SignatureToSend signatureToSend = new ru.skbkontur.sdk.extern.model.SignatureToSend();
		
		signatureToSend.setContentData(dto.getContentData());
		signatureToSend.setId(dto.getId());
		
		return signatureToSend;
	}

	public ru.skbkontur.sdk.extern.model.SignatureToSend fromDto(Map<String,Object> dto) {
		
		if (dto == null) return null;
		
		ru.skbkontur.sdk.extern.model.SignatureToSend signatureToSend = new ru.skbkontur.sdk.extern.model.SignatureToSend();
		
		signatureToSend.setContentData((byte[])dto.get("content-data"));
		signatureToSend.setId((String)dto.get("id"));
		
		return signatureToSend;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend toDto(ru.skbkontur.sdk.extern.model.SignatureToSend signatureToSend) {
		
		if (signatureToSend == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.SignatureToSend();
		
		dto.setContentData(signatureToSend.getContentData());
		dto.setId(signatureToSend.getId());
		
		return dto;
	}
}
