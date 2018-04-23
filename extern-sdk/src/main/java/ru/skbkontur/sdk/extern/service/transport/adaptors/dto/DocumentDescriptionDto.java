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
public class DocumentDescriptionDto {

	public ru.skbkontur.sdk.extern.model.DocumentDescription fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.DocumentDescription documentDescription = new ru.skbkontur.sdk.extern.model.DocumentDescription();
		documentDescription.setContentType(dto.getContentType());
		documentDescription.setFilename(dto.getFilename());
		documentDescription.setType(dto.getType());

		return documentDescription;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription toDto(ru.skbkontur.sdk.extern.model.DocumentDescription documentDescription) {

		if (documentDescription == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription();
		dto.setContentType(documentDescription.getContentType());
		dto.setFilename(documentDescription.getFilename());
		dto.setType(documentDescription.getType());

		return dto;
	}
}
