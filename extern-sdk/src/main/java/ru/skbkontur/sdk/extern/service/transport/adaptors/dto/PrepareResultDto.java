/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class PrepareResultDto {

	public ru.skbkontur.sdk.extern.model.PrepareResult fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.PrepareResult dto) {

		if (dto == null) return null;

		CheckResultDataDto checkResultDataDto = new CheckResultDataDto();
		LinkDto linkDto = new LinkDto();
		ru.skbkontur.sdk.extern.model.PrepareResult prepareResult = new ru.skbkontur.sdk.extern.model.PrepareResult();
		prepareResult.setCheckResult(checkResultDataDto.fromDto(dto.getCheckResult()));
		prepareResult.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
		prepareResult.setStatus(ru.skbkontur.sdk.extern.model.PrepareResult.Status.fromValue(dto.getStatus().getValue()));

		return prepareResult;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.PrepareResult toDto(ru.skbkontur.sdk.extern.model.PrepareResult prepareResult) {

		if (prepareResult == null) return null;

		CheckResultDataDto checkResultDataDto = new CheckResultDataDto();
		LinkDto linkDto = new LinkDto();
		ru.skbkontur.sdk.extern.service.transport.swagger.model.PrepareResult dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.PrepareResult();
		dto.setCheckResult(checkResultDataDto.toDto(prepareResult.getCheckResult()));
		dto.setLinks(prepareResult.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
		dto.setStatus(ru.skbkontur.sdk.extern.service.transport.swagger.model.PrepareResult.StatusEnum.fromValue(prepareResult.getStatus().getValue()));

		return dto;
	}
}
