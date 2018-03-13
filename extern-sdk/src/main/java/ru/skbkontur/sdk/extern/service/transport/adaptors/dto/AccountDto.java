/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.Account;

/**
 *
 * @author AlexS
 */
public class AccountDto {
	
	public Account fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.Account dto) {
		Account account = new Account();
		
		account.setId(dto.getId());
		account.setInn(dto.getInn());
		account.setKpp(dto.getKpp());
		if (dto.getLinks() != null && !dto.getLinks().isEmpty()) {
			LinkDto linkDto = new LinkDto();
			account.setLinks(
				dto.getLinks()
					.stream()
						.map(linkDto::fromDto)
						.collect(Collectors.toList())
			);
		}
		account.setOrganizationName(dto.getOrganizationName());
		
		return account;
	}
	
	public ru.skbkontur.sdk.extern.service.transport.swagger.model.Account toDto(Account account) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.Account dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.Account();
		
		dto.setId(account.getId());
		dto.setInn(account.getInn());
		dto.setKpp(account.getKpp());
		if (account.getLinks() != null && !account.getLinks().isEmpty()) {
			LinkDto linkDto = new LinkDto();
			dto.setLinks(
				account.getLinks()
					.stream()
						.map(linkDto::toDto)
						.collect(Collectors.toList())
			);
		}
		dto.setOrganizationName(account.getOrganizationName());
		
		return dto;
	}
}
