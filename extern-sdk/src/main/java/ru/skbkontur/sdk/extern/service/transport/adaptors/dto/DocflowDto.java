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
public class DocflowDto {

	public ru.skbkontur.sdk.extern.model.Docflow fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.Docflow docflow = new ru.skbkontur.sdk.extern.model.Docflow();

		docflow.setDescription(new ru.skbkontur.sdk.extern.model.DocflowDescription());

		if (dto.getDocuments() != null) {
			DocumentDto documentDto = new DocumentDto();
			docflow.setDocuments(dto.getDocuments().stream().map(documentDto::fromDto).collect(Collectors.toList()));
		}

		docflow.setId(dto.getId());

		docflow.setLastChangeDate(dto.getLastChangeDate());

		if (dto.getLinks() != null) {
			LinkDto linkDto = new LinkDto();
			docflow.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
		}

		docflow.setSendDate(dto.getSendDate());

		docflow.setStatus(dto.getStatus());

		docflow.setType(dto.getType());

		return docflow;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow toDto(ru.skbkontur.sdk.extern.model.Docflow docflow) {

		if (docflow == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.Docflow();

		dto.setDescription(new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowDescription());

		if (docflow.getDocuments() != null) {
			DocumentDto documentDto = new DocumentDto();
			dto.setDocuments(docflow.getDocuments().stream().map(documentDto::toDto).collect(Collectors.toList()));
		}

		dto.setId(docflow.getId());

		dto.setLastChangeDate(docflow.getLastChangeDate());

		if (docflow.getLinks() != null) {
			LinkDto linkDto = new LinkDto();
			dto.setLinks(docflow.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
		}

		dto.setSendDate(docflow.getSendDate());

		dto.setStatus(docflow.getStatus());

		dto.setType(docflow.getType());

		return dto;
	}
}
