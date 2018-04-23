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
class CheckErrorDto {

	public CheckErrorDto() {

	}

	public ru.skbkontur.sdk.extern.model.CheckError fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckError dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.CheckError checkError = new ru.skbkontur.sdk.extern.model.CheckError();
		checkError.setDescription(dto.getDescription());
		checkError.setId(dto.getId());
		checkError.setLevel(dto.getLevel());
		checkError.setTags(dto.getTags());
		checkError.setType(dto.getType());

		return checkError;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckError toDto(ru.skbkontur.sdk.extern.model.CheckError checkError) {

		if (checkError == null)	return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckError dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckError();
		dto.setDescription(checkError.getDescription());
		dto.setId(checkError.getId());
		dto.setLevel(checkError.getLevel());
		dto.setTags(checkError.getTags());
		dto.setType(checkError.getType());

		return dto;
	}

}
