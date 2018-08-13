/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.kontur.extern_api.sdk.service.transport.adaptor.swagger;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import ru.kontur.extern_api.sdk.model.Account;
import ru.kontur.extern_api.sdk.model.AccountList;
import ru.kontur.extern_api.sdk.model.Link;
import ru.kontur.extern_api.sdk.service.transport.adaptor.AccountsAdaptor;
import ru.kontur.extern_api.sdk.service.transport.adaptor.HttpClient;
import ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.ACCOUNT_LIST;
import static ru.kontur.extern_api.sdk.service.transport.adaptor.QueryContext.LINKS;
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
    public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            LinkDto linkDto = new LinkDto();

            return new QueryContext<List<Link>>(cxt, cxt.getEntityName()).setResult(
                transport(cxt)
                    .rootIndex()
                    .stream()
                    .map(linkDto::fromDto)
                    .collect(Collectors.toList()),
                LINKS
            );
        }
        catch (ApiException x) {
            return new QueryContext<List<Link>>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            AccountListDto accountListDto = new AccountListDto();

            return new QueryContext<AccountList>(cxt, cxt.getEntityName()).setResult(
                accountListDto
                    .fromDto(
                        transport(cxt)
                            .accountsGetAll()
                    ),
                ACCOUNT_LIST
            );
        }
        catch (ApiException x) {
            return new QueryContext<AccountList>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public QueryContext<Account> createAccount(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            CreateAccountRequestDto createAccountRequestDto = new CreateAccountRequestDto();

            return new QueryContext<Account>(cxt, cxt.getEntityName()).setResult(
                new AccountDto().fromDto(
                    (Map<String,Object>)transport(cxt).accountsCreate(
                        createAccountRequestDto
                            .toDto(cxt.getCreateAccountRequest())
                    )
                ),
                ACCOUNT
            );
        }
        catch (ApiException x) {
            return new QueryContext<Account>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @Override
    public QueryContext<Account> getAccount(QueryContext<?> cxt) {
        try {
            if (cxt.isFail()) {
                return new QueryContext<>(cxt, cxt.getEntityName());
            }

            AccountDto accountDto = new AccountDto();

            return new QueryContext<Account>(cxt, cxt.getEntityName()).setResult(
                accountDto
                    .fromDto(
                        transport(cxt).accountsGet(cxt.getAccountProvider().accountId())
                    ),
                 ACCOUNT
            );
        }
        catch (ApiException x) {
            return new QueryContext<Account>(cxt, cxt.getEntityName()).setServiceError(new ApiExceptionDto().fromDto(x));
        }
    }

    @SuppressWarnings("unchecked")
    private AccountsApi transport(QueryContext<?> cxt) {
        api.setApiClient((ApiClient)super.prepareTransport(cxt));
        return api;
    }
}
