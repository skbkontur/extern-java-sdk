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
public class DraftDto {

	public DraftDto() {
	}

	public ru.skbkontur.sdk.extern.model.Draft fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft dto) {
		ru.skbkontur.sdk.extern.model.Draft draft = null;
		if (dto != null) {
			draft = new ru.skbkontur.sdk.extern.model.Draft();
			draft.setId(dto.getId());
			draft.setStatus(dto.getStatus().getValue());
		}
		return draft;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft toDto(ru.skbkontur.sdk.extern.model.Draft draft) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft dto = null;
		if (draft != null) {
			dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft();
			dto.setId(draft.getId());
			ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft.StatusEnum status
				= ru.skbkontur.sdk.extern.service.transport.swagger.model.Draft.StatusEnum.fromValue(draft.getStatus().getValue());
			dto.setStatus(status);
		}
		return dto;
	}
}
