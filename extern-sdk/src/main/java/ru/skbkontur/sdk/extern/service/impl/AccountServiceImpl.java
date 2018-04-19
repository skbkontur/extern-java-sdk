/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ru.skbkontur.sdk.extern.service.impl;

import ru.skbkontur.sdk.extern.model.Account;
import ru.skbkontur.sdk.extern.model.AccountList;
import ru.skbkontur.sdk.extern.model.CreateAccountRequest;
import ru.skbkontur.sdk.extern.model.Link;
import ru.skbkontur.sdk.extern.service.AccountService;
import ru.skbkontur.sdk.extern.service.transport.adaptors.AccountsAdaptor;
import ru.skbkontur.sdk.extern.service.transport.adaptors.QueryContext;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 *
 * @author AlexS
 */
public class AccountServiceImpl extends BaseService<AccountsAdaptor> implements AccountService {
	private static final String EN_ACC = "account";
	
	@Override
	public CompletableFuture<QueryContext<List<Link>>> acquireBaseUriAsync() {
		QueryContext<List<Link>> cxt = createQueryContext(EN_ACC);
		return cxt.applyAsync(api::acquireBaseUri);
	}

	@Override
	public QueryContext<List<Link>> acquireBaseUri(QueryContext<?> parent) {
		QueryContext<List<Link>> cxt = createQueryContext(parent, EN_ACC);
		return cxt.apply(api::acquireBaseUri);
	}

	@Override
	public CompletableFuture<QueryContext<AccountList>> acquireAccountsAsync() {
		QueryContext<AccountList> cxt = createQueryContext(EN_ACC);
		return cxt.applyAsync(api::acquireAccounts);
	}

	@Override
	public QueryContext<AccountList> acquireAccounts(QueryContext<?> parent) {
		QueryContext<AccountList> cxt = createQueryContext(parent, EN_ACC);
		return cxt.apply(api::acquireAccounts);
	}

	@Override
	public CompletableFuture<QueryContext<Object>> createAccountAsync(CreateAccountRequest createAccountRequest) {
		QueryContext<Object> cxt = createQueryContext(EN_ACC);
		return cxt
			.setCreateAccountRequest(createAccountRequest)
			.applyAsync(api::createAccount);
	}

	@Override
	public QueryContext<Object> createAccount(QueryContext<?> parent) {
		QueryContext<Object> cxt = createQueryContext(parent, EN_ACC);
		return cxt.apply(api::createAccount);
	}

	@Override
	public CompletableFuture<QueryContext<Account>> getAccountAsync(String accountId) {
		QueryContext<Account> cxt = createQueryContext(EN_ACC);
		return cxt
			.setAccountId(accountId)
			.applyAsync(api::getAccount);
	}

	@Override
	public QueryContext<Account> getAccount(QueryContext<?> parent) {
		QueryContext<Account> cxt = createQueryContext(parent, EN_ACC);
		return cxt.apply(api::getAccount);
	}
}
