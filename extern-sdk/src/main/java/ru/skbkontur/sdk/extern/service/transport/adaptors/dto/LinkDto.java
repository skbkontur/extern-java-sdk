/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.lang.reflect.Field;
import java.util.Map;

/**
 *
 * @author AlexS
 */
public class LinkDto {

	public ru.skbkontur.sdk.extern.model.Link fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Link dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.Link link = new ru.skbkontur.sdk.extern.model.Link();
		link.setHref(dto.getHref());
		link.setName(dto.getName());
		link.setProfile(dto.getProfile());
		link.setRel(dto.getRel());
		link.setTemplated(dto.getTemplated());
		link.setTitle(dto.getTitle());

		return link;
	}

	public ru.skbkontur.sdk.extern.model.Link fromDto(Map<String, Object> dto) {

		if (dto == null) return null;

		ru.skbkontur.sdk.extern.model.Link link = new ru.skbkontur.sdk.extern.model.Link();
		link.setHref((String) dto.get("href"));
		link.setName((String) dto.get("name"));
		link.setProfile((String) dto.get("profile"));
		link.setRel((String) dto.get("rel"));
		link.setTemplated((Boolean) dto.get("templated"));
		link.setTitle((String) dto.get("title"));

		return link;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Link toDto(ru.skbkontur.sdk.extern.model.Link link) {

		if (link == null) return null;

		ru.skbkontur.sdk.extern.service.transport.swagger.model.Link dto
			= new ru.skbkontur.sdk.extern.service.transport.swagger.model.Link();
		setPrivateFieldValue(dto, "href", link.getHref());
		setPrivateFieldValue(dto, "rel", link.getRel());

		return dto;
	}

	private void setPrivateFieldValue(Object obj, String fieldName, Object value) {
		try {
			Field field = obj.getClass().getDeclaredField(fieldName);
			field.setAccessible(true);
			field.set(obj, value);
		}
		catch (NoSuchFieldException | SecurityException | IllegalAccessException ignore) {
		}
	}
}
