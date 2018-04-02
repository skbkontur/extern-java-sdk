/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author AlexS
 */
public class CheckResultDataDto {

	public ru.skbkontur.sdk.extern.model.CheckResultData fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckResultData dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.CheckResultData checkResultData
			= new ru.skbkontur.sdk.extern.model.CheckResultData();

		CheckErrorDto checkErrorDto = new CheckErrorDto();

		if (dto.getCommonErrors() != null) {
			checkResultData.setCommonErrors(dto.getCommonErrors().stream().map(e -> checkErrorDto.fromDto(e)).collect(Collectors.toList()));
		}

		if (dto.getDocumentsErrors() != null) {
			checkResultData.setDocumentsErrors(
				dto.getDocumentsErrors().entrySet().stream().collect(
					Collectors.toMap(Map.Entry::getKey, l -> l.getValue().stream().map(e -> checkErrorDto.fromDto(e)).collect(Collectors.toList()))
				)
			);
		}
		return checkResultData;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckResultData toDto(ru.skbkontur.sdk.extern.model.CheckResultData checkResultData) {

		if (checkResultData == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckResultData dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.CheckResultData();

		CheckErrorDto checkErrorDto = new CheckErrorDto();

		if (checkResultData.getCommonErrors() != null) {
			dto.setCommonErrors(checkResultData.getCommonErrors().stream().map(e -> checkErrorDto.toDto(e)).collect(Collectors.toList()));
		}

		if (checkResultData.getDocumentsErrors() != null) {
			dto.setDocumentsErrors(
				checkResultData.getDocumentsErrors().entrySet().stream().collect(
					Collectors.toMap(Map.Entry::getKey, l -> l.getValue().stream().map(e -> checkErrorDto.toDto(e)).collect(Collectors.toList()))
				)
			);
		}

		return dto;
	}
}
