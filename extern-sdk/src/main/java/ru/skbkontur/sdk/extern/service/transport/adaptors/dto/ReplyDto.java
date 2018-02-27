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
public class ReplyDto {
	
	public ReplyDto() {
	}
	
	public ru.skbkontur.sdk.extern.model.Reply fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Reply dto) {
		ru.skbkontur.sdk.extern.model.Reply reply = new ru.skbkontur.sdk.extern.model.Reply();
		
		LinkDto linkDto = new LinkDto();
		
		reply.setDocument(new DocumentToSendDto().fromDto(dto.getDocument()));
		reply.setLinks(dto.getLinks().stream().map(linkDto::fromDto).collect(Collectors.toList()));
		
		return reply;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Reply toDto(ru.skbkontur.sdk.extern.model.Reply reply) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Reply dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Reply();
		
		LinkDto linkDto = new LinkDto();
		
		dto.setDocument(new DocumentToSendDto().toDto(reply.getDocument()));
		dto.setLinks(reply.getLinks().stream().map(linkDto::toDto).collect(Collectors.toList()));
		
		return dto;
	}
}
