/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.DocflowPage;

/**
 *
 * @author alexs
 */
public class DocflowPageDto {
	
	public DocflowPage fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage dto) {
		DocflowPage docflowPage = new DocflowPage();
		
		DocflowPageItemDto docflowPageItemDto = new DocflowPageItemDto();
		
		docflowPage.setDocflowsPageItem(dto.getDocflowsPageItem().stream().map(docflowPageItemDto::fromDto).collect(Collectors.toList()));
		docflowPage.setSkip(dto.getSkip());
		docflowPage.setTake(dto.getTake());
		docflowPage.setTotalCount(dto.getTotalCount());
		
		return docflowPage;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage toDto(DocflowPage docflowPage) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage dto 
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.DocflowPage();
		
		DocflowPageItemDto docflowPageItemDto = new DocflowPageItemDto();
		
		dto.setDocflowsPageItem(docflowPage.getDocflowsPageItem().stream().map(docflowPageItemDto::toDto).collect(Collectors.toList()));
		dto.setSkip(docflowPage.getSkip());
		dto.setTake(docflowPage.getTake());
		dto.setTotalCount(docflowPage.getTotalCount());
		
		return dto;
	}
}
