/*
 * MIT License
 *
 * Copyright (c) 2018 SKB Kontur
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto;

import ru.kontur.extern_api.sdk.model.AccountList;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class AccountListDto {

    public AccountList fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountList dto) {

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

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountList toDto(AccountList accountList) {

        if (accountList == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountList dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.AccountList();

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
