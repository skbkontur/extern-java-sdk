/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors.dto;

import java.util.stream.Collectors;
import ru.skbkontur.sdk.extern.model.AccountList;

/**
 *
 * @author AlexS
 */
public class AccountListDto {

	public AccountList fromDto(ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList dto) {
		AccountList accountList = new AccountList();
		
		if (dto.getAccounts() != null && !dto.getAccounts().isEmpty()) {
			AccountDto accountDto = new AccountDto();
			accountList.setAccounts(dto.getAccounts().stream().map(accountDto::fromDto).collect(Collectors.toList()));
		}
		accountList.setPageIndex(dto.getPageIndex());
		accountList.setPageSize(dto.getPageSize());
		accountList.setTotalCount(dto.getTotalCount());
		
		return accountList;
	}

	public ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList toDto(AccountList accountList) {
		ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList dto = new ru.skbkontur.sdk.extern.service.transport.swagger.model.AccountList();
		
		if (accountList.getAccounts() != null && !accountList.getAccounts().isEmpty()) {
			AccountDto accountDto = new AccountDto();
			dto.setAccounts(accountList.getAccounts().stream().map(accountDto::toDto).collect(Collectors.toList()));
		}
		dto.setPageIndex(accountList.getPageIndex());
		dto.setPageSize(accountList.getPageSize());
		dto.setTotalCount(accountList.getTotalCount());
		
		return dto;
	}
}
