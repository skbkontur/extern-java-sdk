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
class ContentDto {

	public ru.skbkontur.sdk.extern.model.Content fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Content dto) {

		if (dto == null) return null;

		LinkDto linkDto = new LinkDto();
		ru.skbkontur.sdk.extern.model.Content content = new ru.skbkontur.sdk.extern.model.Content();
		content.setDecrypted(linkDto.fromDto(dto.getDecrypted()));
		content.setEncrypted(linkDto.fromDto(dto.getEncrypted()));

		return content;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Content toDto(ru.skbkontur.sdk.extern.model.Content content) {

		if (content == null) return null;

		LinkDto linkDto = new LinkDto();
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Content dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.Content();
		dto.setDecrypted(linkDto.toDto(content.getDecrypted()));
		dto.setEncrypted(linkDto.toDto(content.getEncrypted()));

		return dto;
	}

}
