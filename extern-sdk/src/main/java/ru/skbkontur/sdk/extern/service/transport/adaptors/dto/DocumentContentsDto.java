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
@SuppressWarnings("unused")
public class DocumentContentsDto {

	public ru.skbkontur.sdk.extern.model.DocumentContents fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentContents dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.DocumentContents documentContents = new ru.skbkontur.sdk.extern.model.DocumentContents();

		documentContents.setBase64Content(dto.getBase64Content());

		DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

		documentContents.setDocumentDescription(documentDescriptionDto.fromDto(dto.getDescription()));

		documentContents.setSignature(dto.getSignature());

		return documentContents;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentContents toDto(ru.skbkontur.sdk.extern.model.DocumentContents documentContents) {

		if (documentContents == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentContents dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentContents();

		dto.setBase64Content(documentContents.getBase64Content());

		DocumentDescriptionDto documentDescriptionDto = new DocumentDescriptionDto();

		dto.setDescription(documentDescriptionDto.toDto(documentContents.getDocumentDescription()));

		dto.setSignature(documentContents.getSignature());

		return dto;
	}
}
