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
public class DocumentDescriptionDto {

	public DocumentDescriptionDto() {
	}

	public ru.skbkontur.sdk.extern.model.DocumentDescription fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription dto) {
		ru.skbkontur.sdk.extern.model.DocumentDescription documentDescription = null;
		if (dto != null) {
			documentDescription = new ru.skbkontur.sdk.extern.model.DocumentDescription();
			documentDescription.setCompressionType(dto.getCompressionType());
			documentDescription.setContentType(dto.getContentType());
			documentDescription.setFilename(dto.getFilename());
			documentDescription.setType(dto.getType());
		}
		return documentDescription;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription toDto(ru.skbkontur.sdk.extern.model.DocumentDescription documentDescription) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription dto = null;
		if (documentDescription != null) {
			dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocumentDescription();
			dto.setCompressionType(documentDescription.getCompressionType());
			dto.setContentType(documentDescription.getContentType());
			dto.setFilename(documentDescription.getFilename());
			dto.setType(documentDescription.getType());
		}
		return dto;
	}
}
