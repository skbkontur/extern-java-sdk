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

import ru.kontur.extern_api.sdk.model.Account;

import java.util.ArrayList;
import java.util.stream.Collectors;


/**
 * @author AlexS
 */
public class AccountDto {

    public Account fromDto(ru.kontur.extern_api.sdk.service.transport.swagger.model.Account dto) {

        if (dto == null) return null;

        Account account = new Account();

        account.setId(dto.getId());
        account.setInn(dto.getInn());
        account.setKpp(dto.getKpp());
        account.setLinks(new ArrayList<>());
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

    public ru.kontur.extern_api.sdk.service.transport.swagger.model.Account toDto(Account account) {

        if (account == null) return null;

        ru.kontur.extern_api.sdk.service.transport.swagger.model.Account dto = new ru.kontur.extern_api.sdk.service.transport.swagger.model.Account();

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
