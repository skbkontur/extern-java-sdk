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
public class SignatureDto {

	public SignatureDto() {
	}

	public ru.skbkontur.sdk.extern.model.Signature fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature signatureDto) {
		ru.skbkontur.sdk.extern.model.Signature signature = null;
		if (signatureDto != null) {
			signature = new ru.skbkontur.sdk.extern.model.Signature();

			LinkDto linkDto = new LinkDto();

			signature.setContentLink(linkDto.fromDto(signatureDto.getContentLink()));
			signature.setId(signatureDto.getId());
			signature.setLinks(signatureDto.getLinks().stream().map(l -> linkDto.fromDto(l)).collect(Collectors.toList()));
		}
		return signature;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature toDto(ru.skbkontur.sdk.extern.model.Signature signature) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature dto = null;
		if (signature != null) {
			LinkDto linkDto = new LinkDto();
			dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Signature();
			dto.setContentLink(linkDto.toDto(signature.getContentLink()));
			dto.setId(signature.getId());
			if (signature.getLinks() != null) {
				dto.setLinks(signature.getLinks().stream().map(l -> linkDto.toDto(l)).collect(Collectors.toList()));
			}
		}
		return dto;
	}
}
