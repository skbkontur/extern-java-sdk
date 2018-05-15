/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import java.util.List;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT_LIST;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.LINKS;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.OBJECT;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.AccountDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.AccountListDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.ApiExceptionDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.CreateAccountRequestDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.dto.LinkDto;
import ru.kontur.extern_api.sdk.service.transport.adaptor.swagger.invoker.ApiClient;
import ru.kontur.extern_api.sdk.service.transport.swagger.invoker.ApiException;
import ru.kontur.extern_api.sdk.service.transport.swagger.api.AccountsApi;

/**
 *
 * @author alexs
 */
public class AccountsAdaptorImpl extends BaseAdaptor implements AccountsAdaptor {

    private final AccountsApi api;

    public AccountsAdaptorImpl() {
        this.api = new AccountsApi();
    }

    @Override
    public ApiClient getApiClient() {
        return (ApiClient) api.getApiClient();
    }

    @Override
    public void setApiClient(ApiClient apiClient) {
        api.setApiClient(apiClient);
    }

    @Override
    public QueryContext<List<Link>> acquireBaseUri(QueryContext<List<Link>> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            LinkDto linkDto = new LinkDto();

            return cxt.setResult(
                transport(cxt)
                    .rootIndex()
                    .stream()
                    .map(linkDto::fromDto)
                    .collect(Collectors.toList()),
                LINKS
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<AccountList> acquireAccounts(QueryContext<AccountList> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            AccountListDto accountListDto = new AccountListDto();

            return cxt.setResult(
                accountListDto
                    .fromDto(
                        transport(cxt)
                            .accountsGetAll()
                    ),
                ACCOUNT_LIST
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<Object> createAccount(QueryContext<Object> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();

            return cxt.setResult(
                transport(cxt).accountsCreate(
                    createAccountRequestDto
                        .toDto(cxt.getCreateAccountRequest())
                ),
                OBJECT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<Account> getAccount(QueryContext<Account> cxt) {
        try {
            if (cxt.isFail()) {
                return cxt;
            }

            AccountDto accountDto = new AccountDto();

            return cxt.setResult(
                accountDto
                    .fromDto(
                        transport(cxt).accountsGet(cxt.getAccountId())
                    ),
                 ACCOUNT
            );
        }
        catch (ApiException x) {
            return cxt.setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    private AccountsApi transport(QueryContext<?> cxt) {
        super.prepareTransport(cxt);
        return api;
    }
}
