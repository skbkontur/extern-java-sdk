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

package ru.kontur.extern_api.sdk.service.transport.adaptors;

import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.transport.adaptors.dto.AccountDto;
import ru.kontur.extern_api.sdk.service.transport.adaptors.dto.AccountListDto;
import ru.kontur.extern_api.sdk.service.transport.adaptors.dto.CreateAccountRequestDto;
import ru.kontur.extern_api.sdk.service.transport.adaptors.dto.LinkDto;
import ru.kontur.extern_api.sdk.service.transport.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.AccountsApi;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.ACCOUNT;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.ACCOUNT_LIST;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.LINKS;
import static ru.kontur.extern_api.sdk.service.transport.adaptors.QueryContext.OBJECT;


/**
 * @author AlexS
 */
public class AccountsAdaptor extends BaseAdaptor {

    private final AccountsApi api;

    public AccountsAdaptor(AccountsApi accountsApi) {
        this.api = accountsApi;
    }

    public AccountsAdaptor() {
        this(new AccountsApi());
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
        api.setApiClient(apiClient);
    }

    public QueryContext<List<Link>> acquireBaseUri(QueryContext<List<Link>> cxt) {
        try {
            if (cxt.isFail()) return cxt;

            LinkDto linkDto = new LinkDto();

            return cxt.setResult(
                    transport(cxt)
                            .rootIndex()
                            .stream()
                            .map(linkDto::fromDto)
                            .collect(Collectors.toList())
                    ,
                    LINKS
            );
        } catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    public QueryContext<AccountList> acquireAccounts(QueryContext<AccountList> cxt) {
        try {
            if (cxt.isFail()) return cxt;

            AccountListDto accountListDto = new AccountListDto();

            return cxt.setResult(
                    accountListDto
                            .fromDto(
                                    transport(cxt)
                                            .accountsGetAll()
                            )
                    ,
                    ACCOUNT_LIST
            );
        } catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    public QueryContext<Object> createAccount(QueryContext<Object> cxt) {
        try {
            if (cxt.isFail()) return cxt;

            CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();

            return cxt.setResult(
                    transport(cxt).accountsCreate(
                            createAccountRequestDto
                                    .toDto(cxt.getCreateAccountRequest())
                    )
                    ,
                    OBJECT
            );
        } catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    public QueryContext<Account> getAccount(QueryContext<Account> cxt) {
        try {
            if (cxt.isFail()) return cxt;

            AccountDto accountDto = new AccountDto();

            return cxt.setResult(
                    accountDto
                            .fromDto(
                                    transport(cxt).accountsGet(cxt.getAccountId())
                            )
                    ,
                    ACCOUNT
            );
        } catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private AccountsApi transport(QueryContext<?> cxt) {
        super.prepareTransport(cxt);
        return api;
    }
}
