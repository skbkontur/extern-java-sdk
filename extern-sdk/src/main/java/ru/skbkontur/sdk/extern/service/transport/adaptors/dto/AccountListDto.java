/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import ru.skbkontur.sdk.extern.model.AccountList;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 *
 * @author AlexS
 */
@SuppressWarnings("unused")
public class AccountListDto {

	public AccountList fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList dto) {
		
		if (dto == null) return null;
		
		AccountList accountList = new AccountList();
		accountList.setAccounts(new ArrayList<>());
		if (dto.getAccounts() != null && !dto.getAccounts().isEmpty()) {
			AccountDto accountDto = new AccountDto();
			accountList.setAccounts(dto.getAccounts().stream().map(accountDto::fromDto).collect(Collectors.toList()));
		}
		accountList.setSkip(dto.getSkip());
		accountList.setTake(dto.getTake());
		accountList.setTotalCount(dto.getTotalCount());
		
		return accountList;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList toDto(AccountList accountList) {
		
		if (accountList == null) return null;
		
		ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList();
		
		if (accountList.getAccounts() != null && !accountList.getAccounts().isEmpty()) {
			AccountDto accountDto = new AccountDto();
			dto.setAccounts(accountList.getAccounts().stream().map(accountDto::toDto).collect(Collectors.toList()));
		}
		dto.setSkip(accountList.getSkip());
		dto.setTake(accountList.getTake());
		dto.setTotalCount(accountList.getTotalCount());
		
		return dto;
	}
}
