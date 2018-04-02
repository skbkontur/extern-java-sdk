/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.DocflowPageItem;

/**
 *
 * @author alexs
 */
public class DocflowPageItemDto {
	
	public DocflowPageItem fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPageItem dto) {

		if (dto == null) return null;
		
		DocflowPageItem docflowPageItem = new DocflowPageItem();
		
		LinkDto linkDto = new LinkDto();
		
		docflowPageItem.setId(dto.getId());
		docflowPageItem.setLastChangeDate(dto.getLastChangeDate());
		docflowPageItem.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
		docflowPageItem.setSendDate(dto.getSendDate());
		docflowPageItem.setStatus(dto.getStatus());
		docflowPageItem.setType(dto.getType());
		
		return docflowPageItem;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPageItem toDto(DocflowPageItem docflowPageItem) {

		if (docflowPageItem == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPageItem dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPageItem();
		
		LinkDto linkDto = new LinkDto();
		
		dto.setId(docflowPageItem.getId());
		dto.setLastChangeDate(docflowPageItem.getLastChangeDate());
		dto.setLinks(docflowPageItem.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
		dto.setSendDate(docflowPageItem.getSendDate());
		dto.setStatus(docflowPageItem.getStatus());
		dto.setType(docflowPageItem.getType());
		
		return dto;
	}
}
