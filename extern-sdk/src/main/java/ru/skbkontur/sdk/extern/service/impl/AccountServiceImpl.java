/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.service.AccountService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

/**
 *
 * @author AlexS
 */
public class AccountServiceImpl extends BaseService implements AccountService {
	private static final String EN_ACC = "account";
	
	private AccountAdaptor accountApi;
	
	public void setAccountApi(AccountAdaptor accountApi) {
		this.accountApi = accountApi;
	}
	
	@Override
	public CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync() {
		return createQueryContext(EN_ACC)
			.applyAsync(accountApi::acquireBaseUri);
	}

	@Override
	public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> cxt) {
		return createQueryContext(cxt, EN_ACC)
			.apply(accountApi::acquireBaseUri);
	}

	@Override
	public CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync() {
		return createQueryContext(EN_ACC)
			.applyAsync(accountApi::acquireAccounts);
	}

	@Override
	public QueryContext<AccountList> acquireAccounts(QueryContext<?> cxt) {
		return createQueryContext(cxt, EN_ACC)
			.apply(accountApi::acquireAccounts);
	}

	@Override
	public CompletableFuture<QueryContext<Object>> createrAccountAsync(CreateAccountRequest createAccountRequest) {
		return createQueryContext(EN_ACC)
			.setCreateAccountRequest(createAccountRequest)
			.applyAsync(accountApi::acquireAccounts);
	}

	@Override
	public QueryContext<Object> createrAccount(QueryContext<?> cxt) {
		return createQueryContext(cxt, EN_ACC)
			.apply(accountApi::acquireAccounts);
	}

	@Override
	public CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId) {
		return createQueryContext(EN_ACC)
			.setAccountId(accountId)
			.applyAsync(accountApi::acquireAccounts);
	}

	@Override
	public QueryContext<Account> getAccount(QueryContext<?> cxt) {
		return createQueryContext(cxt, EN_ACC)
			.apply(accountApi::acquireAccounts);
	}
}
