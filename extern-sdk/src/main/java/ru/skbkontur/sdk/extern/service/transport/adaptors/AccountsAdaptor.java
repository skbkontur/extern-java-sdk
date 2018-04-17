/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.transport.adaptors;

import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.providers.ServiceError;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.AccountDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.AccountListDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.CreateAccountRequestDto;
import ru.skbkontur.sdk.extern.service.transport.adaptors.dto.LinkDto;
import ru.skbkontur.sdk.extern.service.transport.invoker.ApiClient;
import ru.skbkontur.sdk.extern.service.transport.swagger.api.AccountsApi;
import ru.skbkontur.sdk.extern.service.transport.swagger.invoker.ApiException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.ACCOUNT;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.ACCOUNT_LIST;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.LINKS;
import static ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext.OBJECT;


/**
 * @author AlexS
 */
public class AccountsAdaptor extends BaseAdaptor {

    private AccountsApi api;

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
            return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
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
            return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
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
            return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
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
            return cxt.setServiceError(new ServiceErrorImpl(ServiceError.ErrorCode.server, x.getMessage(), x.getCode(), x.getResponseHeaders(), x.getResponseBody()));
        }
    }

    private AccountsApi transport(QueryContext<?> cxt) {
        super.prepareTransport(cxt);
        return api;
    }
}
