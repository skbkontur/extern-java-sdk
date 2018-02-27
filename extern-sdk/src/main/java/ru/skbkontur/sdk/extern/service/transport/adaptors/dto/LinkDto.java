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
public class LinkDto {
	
	public LinkDto() {
	}
	
	public ru.skbkontur.sdk.extern.model.Link fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Link dto) {
		ru.skbkontur.sdk.extern.model.Link link = null;
		if (dto != null) {
			link = new ru.skbkontur.sdk.extern.model.Link();
			link.setHref(dto.getHref());
			link.setName(dto.getName());
			link.setProfile(dto.getProfile());
			link.setRel(dto.getRel());
			link.setTemplated(dto.getTemplated());
			link.setTitle(dto.getTitle());
		}
		return link;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Link toDto(ru.skbkontur.sdk.extern.model.Link link) {
		throw new UnsupportedOperationException();
	}
}
